package com.example.blockchain.service;

import java.io.*;
import java.util.*;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.example.blockchain.Entity.*;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@Service
public class BlockChainService implements ApplicationListener<WebServerInitializedEvent>  {
    @Autowired
    private NodeService nodeService;
    @Autowired
    private KeyService keyService;
    private RestTemplate restTemplate = new RestTemplate();

    private static int blockSize = 3;
    private static ArrayList<Transaction> transactions = new ArrayList<>();
    private String filePath = "blockchain.json";
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

    }

    private ArrayList<Block> loadBlockChain() {
        try {
            File file = new File(filePath);
            List<Block> c = JSON.parseArray(IOUtils.toString(new FileInputStream(file), "utf8"), Block.class);
            if (c.size() != 0)
                // c = initiateBlockChain();
                return (ArrayList<Block>) c;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
        List<Block> initBC = initiateBlockChain();
        writeToFile(filePath, JSON.toJSONString(initBC, true));
        return (ArrayList<Block>) initBC;
    }

    private boolean writeToFile(String path, String text) {
        File file = new File(path);
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(file));
            bw.write(text);
            bw.flush();
            bw.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    private boolean saveBlockChain() {
        System.out.println(filePath);
        return writeToFile(filePath, JSON.toJSONString(chain, true));
    }

    private static List<Block> initiateBlockChain() {
        List<Block> chain = new ArrayList<>();
        // 验证
        String address = "node1";
        transactions.add(new Transaction(address, address, "initial", null, false));
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
        saveBlockChain();
    }

    public void addTransaction(Transaction transaction) {
//        if(!keyService.isValidator()) {
//            return;
//        }
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
            // 清空交易缓冲区
            transactions = new ArrayList<>();
        }
    }

    public List<Block> getBlockChain() {
        return chain;
    }
    public boolean saveBC() {
        return saveBlockChain();
    }
    public static void main(String[] args) {
        BlockChainService bcService = new BlockChainService();
        System.out.println(bcService.saveBlockChain());
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
            saveBlockChain();
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

    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        int port = event.getWebServer().getPort();
        filePath = port + filePath;
        chain = loadBlockChain();
    }

    public static Map<String, Object> trace(String hash) {
        List<Transaction> found = new ArrayList<>();
        for (Block block : chain) {
            if (block.getPreviousHash().equals("-1"))
                continue;
            for (int index = 0; index < block.getTransactions().size(); index++) {
                Transaction t = JSON.parseObject(JSON.toJSONString(block.getTransactions().get(index)), Transaction.class);
                for (Item i : t.getItem()) {
                    if (i.getHash().equals(hash)) {
                        found.add(t);
                        break;
                    }
                }
            }
        }
        for (Transaction t : transactions) {
            for (Item i : t.getItem()) {
                if (i.getHash().equals(hash)) {
                    found.add(t);
                    break;
                }
            }
        }
        List<Node> nodes = new ArrayList<>();
        NodeService nodeService = new NodeService();
        KeyService keyService = new KeyService();
        String name = keyService.getNodename();
        String host = keyService.getHost();
        int port = keyService.getPort();
        String lastKey = "";
        for (Transaction t : found) {
            Optional<Node> foundFrom = nodeService.getNodeByPublicKey(t.getFrom());
            if(foundFrom.isPresent() && !foundFrom.get().getKey().equals(lastKey)) {
                nodes.add(foundFrom.get());
                lastKey = nodes.get(nodes.size() - 1).getKey();
            }
            if (keyService.getPublic_key().equals(t.getFrom())
                    && !keyService.getPublic_key().equals(lastKey)) {
                nodes.add(new Node(host, port, name, keyService.getPublic_key()));
                lastKey = nodes.get(nodes.size() - 1).getKey();
            }
            Optional<Node> foundTo = nodeService.getNodeByPublicKey(t.getTo());
            if(foundTo.isPresent() && !foundTo.get().getKey().equals(lastKey)) {
                nodes.add(foundTo.get());
                lastKey = nodes.get(nodes.size() - 1).getKey();
            }
            // foundTo.ifPresent(nodes::add);
            if (keyService.getPublic_key().equals(t.getTo())
                    && !keyService.getPublic_key().equals(lastKey)) {
                nodes.add(new Node(host, port, name, keyService.getPublic_key()));
                lastKey = nodes.get(nodes.size() - 1).getKey();
            }
        }
        Map<String, Object> ret = new HashMap<>();
        ret.put("transactions", found);
        ret.put("nodes", nodes);
        return ret;
    }
}



