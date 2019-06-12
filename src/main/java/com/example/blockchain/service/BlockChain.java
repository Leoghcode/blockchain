package com.example.blockchain.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.blockchain.Entity.Block;
import com.example.blockchain.Entity.Transaction;
import org.springframework.stereotype.Service;

@Service
public class BlockChain {
    private ArrayList<Block> chain;
    private ArrayList<Transaction> transactions;
    public BlockChain() {
        chain = new ArrayList<Block>();
        transactions = new ArrayList<Transaction>();
        makeInitialBlock();
    }
    public void makeInitialBlock() {
        String address = "node111";
        transactions.add(new Transaction(address, address, "initial block"));
        int index = 0;
        long timestamp = new Date().getTime();
        String previousHash = "0";
        Block initialBlock = new Block(index, timestamp, transactions, previousHash);
        chain.add(initialBlock);
        // 清空交易缓冲区
        transactions = new ArrayList<>();
    }

    public void addBlock() {
        Block lastBlock = chain.get(chain.size() - 1);
        int index = lastBlock.getIndex() + 1;
        long timestamp = new Date().getTime();
        String previousHash = lastBlock.getHash();
        Block newBlock = new Block(index, timestamp, transactions, previousHash);
        chain.add(newBlock);
        // 清空交易缓冲区
        transactions = new ArrayList<>();
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        if(transactions.size() == 10) {
            addBlock();
        }
    }

    public List<Block> getBlockChain() {
        return chain;
    }

    public static void main(String[] args) {
        BlockChain blockChain = new BlockChain();
        System.out.println(blockChain.getBlockChain());
//        testJSON();
    }

    public static void testJSON() {

    }

}



