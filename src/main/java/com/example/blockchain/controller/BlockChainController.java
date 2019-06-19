package com.example.blockchain.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.blockchain.Entity.Block;
import com.example.blockchain.Entity.Transaction;
import com.example.blockchain.service.BlockChainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("blockchain")
public class BlockChainController {
    @Autowired
    BlockChainService bcService;

    @RequestMapping(value = "get", method = RequestMethod.GET)
    public List<Block> getBlockChain() {
        return bcService.getBlockChain();
    }

    @RequestMapping(value = "attack", method = RequestMethod.POST)
    public List<Block> setBlockChain(@RequestBody Map<String, Object> request) {
        List<Block> blockchain2 = JSON.parseArray(JSONArray.toJSONString(request.get("blockchain")), Block.class);
        bcService.setBlockChain((ArrayList<Block>) blockchain2);
        return bcService.getBlockChain();
    }

    @RequestMapping("/transaction/add")
    public void addTransaction() {

    }

    @RequestMapping(value = "consensus", method = RequestMethod.POST)
    public HttpStatus consensus(@RequestBody Map<String, Object> request) {
        List<Block> blockchain2 = JSON.parseArray(JSONArray.toJSONString(request.get("blockchain")), Block.class);
        System.out.println("in consensus");
        System.out.println(request.get("blockchain").toString());
        System.out.println(JSONObject.toJSONString(blockchain2.get(0)));
        if(bcService.consensus(blockchain2)) {
            return HttpStatus.OK;
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    @RequestMapping("/save")
    public boolean saveBlockChain() {
        return bcService.saveBC();
    }

    @RequestMapping(value = "trace", method = RequestMethod.POST)
    public String trace(@RequestBody Map<String, String> hash) {
        String key = "hash";
        if (hash.containsKey(key))
            return JSON.toJSONString(BlockChainService.trace(hash.get(key)), SerializerFeature.DisableCircularReferenceDetect);
        return null;
    }

    @RequestMapping(value = "dynamic/newtran", method = RequestMethod.POST)
    public String newTransaction(@RequestBody Map<String, Object> request) {
        Transaction transaction = JSON.parseObject(JSON.toJSONString(request.get("transaction")), Transaction.class);
//        String public_key = JSON.parseObject(JSON.toJSONString(request.get("public_key")), String.class);
        String public_key = (String)request.get("public_key");

        System.out.println("get new tran");
        System.out.println(JSON.toJSONString(request.get("transaction")));
        System.out.println(transaction.isMultiSign());

//        return "received";
        return bcService.receiveCandidateTransaction(transaction, public_key);
    }

    @RequestMapping(value = "dynamic/compute", method = RequestMethod.POST)
    public String computeNewHash(@RequestBody Map<String, Object> request) {
        long timestamp = (long) request.get("timestamp");
        String public_key = (String)request.get("public_key");
        if(!bcService.checkProposer(public_key)) return "null";
        return bcService.computeDBlockWithCandidateTransaction(timestamp);
    }

    @RequestMapping(value = "dynamic/consensus", method = RequestMethod.POST)
    public String consensusDynamicBlock(@RequestBody Map<String, Object> request) {
        Block dynamicBlock = JSON.parseObject(JSON.toJSONString(request.get("dynamicBlock")), Block.class);
        String public_key = (String)request.get("public_key");
        if(!bcService.checkProposer(public_key)) return "null";
        return bcService.consensusDynamicBlock(dynamicBlock);
    }

    @RequestMapping(value = "dynamic/back", method = RequestMethod.POST)
    public String rollBackDynamicBlock(@RequestBody Map<String, Object> request) {
        String public_key = (String)request.get("public_key");
        if(!bcService.checkProposer(public_key)) return "null";
        return bcService.rollBackDynamicBlock();
    }

    @RequestMapping(value = "dynamic/get", method = RequestMethod.GET)
    public Block getBackDynamicBlock() {
        return bcService.getDynamicBlock();
    }
}
