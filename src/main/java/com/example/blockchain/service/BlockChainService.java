package com.example.blockchain.service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.blockchain.Entity.Block;
import com.example.blockchain.Entity.Item;
import com.example.blockchain.Entity.Request;
import com.example.blockchain.Entity.Transaction;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

@Service
public class BlockChainService {
    private static int blockSize = 32;
    private static ArrayList<Transaction> transactions = new ArrayList<>();
    private static String filePath = ResourceUtils.CLASSPATH_URL_PREFIX + "BlockChain.json";
    //    private static ArrayList<Block> chain = loadBlockChain();
    private static ArrayList<Block> chain = new ArrayList<>();
    private static ArrayList<Item> items = new ArrayList<>();
    private ArrayList<Request> requests = new ArrayList<>();

    public static List<Item> getItems() {
        return items;
    }

    public static void addItem(List<Item> i) {
        items.addAll(i);
    }

    private static ArrayList<Block> loadBlockChain() {
        try {
            File file = ResourceUtils.getFile(filePath);
            InputStream is = new FileInputStream(file);
            List<Block> c = JSON.parseArray(IOUtils.toString(is, "utf8"), Block.class);
            if (c.size() == 0)
                c = initiateBlockChain();
            is.close();
            return (ArrayList<Block>) c;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static boolean saveBlockChain() {
        try {
            File file = ResourceUtils.getFile(filePath);
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write(JSON.toJSONString(chain));
            bw.flush();
            bw.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static List<Block> initiateBlockChain() {
        List<Block> chain = new ArrayList<>();
        // 验证
        String address = "node1";
        transactions.add(new Transaction(address, address, "initial", "initial block", false));
        String previousHash = "-1";
        Block initialBlock = new Block(0, transactions, previousHash);
        chain.add(initialBlock);
        // 清空交易缓冲区
        transactions = new ArrayList<>();
        return chain;
    }

    public ArrayList<Request> getRequests() {
        return requests;
    }

    public void setRequests(ArrayList<Request> requests) {
        this.requests = requests;
    }

    public void addBlock() {
        // 验证是否为validator
        if (chain.size() == 0)
            return;
        Block lastBlock = chain.get(chain.size() - 1);
        int index = lastBlock.getIndex() + 1;
        String previousHash = lastBlock.getHash();
        Block newBlock = new Block(index, transactions, previousHash);
        chain.add(newBlock);
        // 清空交易缓冲区
        transactions = new ArrayList<>();
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        if (transactions.size() == blockSize) {
            addBlock();
        }
    }

    public List<Block> getBlockChain() {
        return chain;
    }

    public static void main(String[] args) {
        BlockChainService bcService = new BlockChainService();
        System.out.println(bcService.getBlockChain());
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

}



