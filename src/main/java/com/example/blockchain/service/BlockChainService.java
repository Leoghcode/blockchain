package com.example.blockchain.service;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.blockchain.Entity.*;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

@Service
public class BlockChainService {
    @Autowired
    private NodeService nodeService;
    private RestTemplate restTemplate = new RestTemplate();

    private static int blockSize = 2;
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

    public BlockChainService() {
        chain = (ArrayList<Block>) initiateBlockChain();
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
            System.out.println("trigger in add block");
            List<Node> nodeList = nodeService.getNodeArrayList();
            for(Node node: nodeList) {
                String url = "http://" + node.getHost() + ":" + node.getPort() + "/blockchain/consensus";
                Map<String, Object> request = new HashMap<>();
                request.put("blockchain", chain);
                System.out.println(request);
                System.out.println(JSON.toJSONString(request));
                Object res = restTemplate.postForObject(url, request, HttpStatus.class);
                System.out.println(res);
            }
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

    public Boolean consensus(List<Block> blockchain2) {
        if(!validateBlockchain(blockchain2)) return false;
        if(blockchain2.size() > chain.size()) {
            System.out.println("in service consensus");
            System.out.println(blockchain2.toString());
            chain = (ArrayList<Block>) blockchain2;
            System.out.println(chain.toString());
            return true;
        }
        return false;
    }

    private Boolean validateBlockchain(List<Block> blockchain) {
        String previousHash = blockchain.get(0).getPreviousHash();
        for(Block block: blockchain) {
            if(!previousHash.equals(block.getPreviousHash())) return false;
            previousHash = block.getHash();
        }
        return true;
    }
}



