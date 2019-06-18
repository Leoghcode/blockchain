package com.example.blockchain.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.blockchain.Entity.Block;
import com.example.blockchain.Entity.Transaction;
import com.example.blockchain.service.BlockChainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
    public String trace(@RequestBody String hash) {
        return JSON.toJSONString(BlockChainService.trace(hash));
    }
}
