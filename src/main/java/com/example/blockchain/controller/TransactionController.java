package com.example.blockchain.controller;

import com.alibaba.fastjson.JSON;
import com.example.blockchain.Entity.*;
import com.example.blockchain.service.BlockChainService;
import com.example.blockchain.service.KeyService;
import com.example.blockchain.service.KeyUtil;
import com.example.blockchain.service.NodeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@RestController
@RequestMapping("transaction")
public class TransactionController {
    private BlockChainService bcService = new BlockChainService();
    private NodeService nodeService = new NodeService();
    private KeyService keyService = new KeyService();
    private RestTemplate restTemplate = new RestTemplate();

    @RequestMapping(value = "getTransactions", method = RequestMethod.GET)
    public String getTransactions() {
        return JSON.toJSONString(bcService.getTransactions());
    }

    @RequestMapping(value = "getBlocks", method = RequestMethod.GET)
    public String getBlocks() {
        return JSON.toJSONString(bcService.getBlockChain());
    }

    @RequestMapping(value = "validateRequest", method = RequestMethod.POST)
    public HttpStatus validate(@RequestBody String str) {
        Request request = JSON.parseObject(str, Request.class);
        boolean validation_result = KeyUtil.verify(request.getFrom(), request.getFrom_message(), request.getFrom_signature())
                & KeyUtil.verify(request.getTo(), request.getTo_message(), request.getTo_signature());
        if (validation_result) {
            bcService.addTransaction(request.getTransaction());
            return HttpStatus.OK;
        } else {
            return HttpStatus.UNAUTHORIZED;
        }
    }

    @RequestMapping(value = "acceptRequest/{index}", method = RequestMethod.GET)
    public HttpStatus acceptRequest(@PathVariable("index") int index) {
        if (index > bcService.getRequests().size() - 1)
            return HttpStatus.BAD_REQUEST;
        try {
            bcService.getRequests().get(index).setStatus(1);
            Request request = bcService.getRequests().get(index);

            Optional<Node> validator = nodeService.getNodeByName("validator");
            if (validator.isPresent()) {
                Node node = validator.get();
                String url = "http://" + node.getHost() + ":" + node.getPort() + "/transaction/validateRequest";
                return restTemplate.postForObject(url, JSON.toJSONString(request), HttpStatus.class);
            } else
                return HttpStatus.NOT_FOUND;
        } catch (Exception e) {
            e.printStackTrace();
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    @RequestMapping(value = "cancelRequest/{index}", method = RequestMethod.GET)
    public HttpStatus cancelRequest(@PathVariable("index") int index) {
        if (index > bcService.getRequests().size() - 1)
            return HttpStatus.BAD_REQUEST;
        bcService.getRequests().remove(index);
        return HttpStatus.OK;
    }

    @RequestMapping(value = "getRequests", method = RequestMethod.GET)
    public String getRequests() {
        return JSON.toJSONString(bcService.getRequests());
    }

    @RequestMapping(value = "send", method = RequestMethod.POST)
    public HttpStatus send(@RequestBody Map<String, Object> request) {
        List<Item> items = JSON.parseArray(JSON.toJSONString(request.get("items")), Item.class);
        String to = request.get("to").toString();
        String type = request.get("type").toString();
        boolean multiSign = false;
        if (request.containsKey("multiSign"))
            multiSign = (Boolean) request.get("multiSign");
        Integer value = null;
        if (request.containsKey("value"))
            value = Integer.valueOf(request.get("value").toString());

        // 查找节点
        Optional<Node> toNode = nodeService.getNodeByName(to);
        if (toNode.isPresent()) {
            Node node = toNode.get();
            Transaction transaction;
            if (value != null)
                transaction = new Transaction(keyService.getPublic_key(), node.getKey(), type,
                    JSON.toJSONString(items), multiSign, value);
            else
                transaction = new Transaction(keyService.getPublic_key(), node.getKey(), type,
                        JSON.toJSONString(items), multiSign);
            String message = KeyUtil.getSHA256Str(JSON.toJSONString(transaction) + System.currentTimeMillis());
            String signature = KeyUtil.signMessage(keyService.getPrivate_key(), message);

            String url = "http://" + node.getHost() + ":" + node.getPort() + "/transaction/receive";
            Map<String, Object> sendRequest = new HashMap<>();
            sendRequest.put("transaction", JSON.toJSONString(transaction));
            sendRequest.put("public_key", keyService.getPublic_key());
            sendRequest.put("message", message);
            sendRequest.put("signature", signature);
            // 发送请求
            HttpStatus status = restTemplate.postForObject(url, sendRequest, HttpStatus.class);
            if (status.equals(HttpStatus.OK))
                return HttpStatus.OK;
            else
                return HttpStatus.INTERNAL_SERVER_ERROR;
        } else {
            return HttpStatus.BAD_REQUEST;
        }
    }

    @RequestMapping(value = "receive", method = RequestMethod.POST)
    public HttpStatus receive(@RequestBody Map<String, Object> request) {
        Transaction transaction = JSON.parseObject(request.get("transaction").toString(), Transaction.class);
        String public_key = request.get("public_key").toString(),
                message = request.get("message").toString(),
                signature = request.get("signature").toString();
        if (KeyUtil.verify(public_key, message, signature)) {
            Optional<Node> node = nodeService.getNodeByPublicKey(public_key);
            if (node.isPresent()) {
                String to_message = KeyUtil.getSHA256Str(JSON.toJSONString(transaction) + System.currentTimeMillis()),
                        to_signature = KeyUtil.signMessage(keyService.getPrivate_key(), to_message);
                bcService.getRequests().add(new Request(public_key, message, signature,
                        keyService.getPublic_key(), to_message, to_signature, transaction));
                return HttpStatus.OK;
            } else
                return HttpStatus.UNAUTHORIZED;
        } else
            return HttpStatus.UNAUTHORIZED;
    }

    @RequestMapping(value = "getItems", method = RequestMethod.GET)
    public String getItems() {
        return JSON.toJSONString(BlockChainService.getItems());
    }

    @RequestMapping(value = "addItems", method = RequestMethod.POST)
    public HttpStatus addItems(@RequestBody List<Item> items) {
        try {
            BlockChainService.addItem(items);
            return HttpStatus.OK;
        } catch (Exception e) {
            e.printStackTrace();
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    @Deprecated
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public HttpStatus addTransaction(@RequestBody List<Transaction> transactions) {
        try {
            for (Transaction t : transactions)
                bcService.addTransaction(t);
            return HttpStatus.OK;
        } catch (Exception e) {
            e.printStackTrace();
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}
