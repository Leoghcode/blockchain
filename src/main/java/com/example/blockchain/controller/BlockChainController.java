package com.example.blockchain.controller;

import com.example.blockchain.Entity.Block;
import com.example.blockchain.service.BlockChainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BlockChainController {
    @Autowired
    BlockChainService blockChain;

    @RequestMapping("/blockchain")
    public List<Block> getBlockChain() {
        return blockChain.getBlockChain();
    }

    @RequestMapping("/transaction/add")
    public void addTransaction() {

    }
}
