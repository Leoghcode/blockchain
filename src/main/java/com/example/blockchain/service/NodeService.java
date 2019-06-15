package com.example.blockchain.service;

import com.alibaba.fastjson.JSON;
import com.example.blockchain.Entity.Node;
import org.springframework.util.ResourceUtils;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NodeService {
    private static final String filePath = ResourceUtils.CLASSPATH_URL_PREFIX + "hosts";
//    private static List<Node> nodeList = loadProperties();
    private static List<Node> nodeList = new ArrayList<>();

    private static List<Node> loadProperties() {
        synchronized (filePath) {
            List<Node> nodeList = new ArrayList<>();
            try {
                File file = ResourceUtils.getFile(filePath);
                BufferedReader bf = new BufferedReader(new FileReader(file));
                String line;
                while ((line = bf.readLine()) != null) {
                    String[] splits = line.split(" ");
                    if (splits.length == 4) {
                        nodeList.add(new Node(splits[0], Integer.valueOf(splits[1]), splits[2], splits[3]));
                    }
                }
                bf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return nodeList;
        }
    }

    private static void saveProperties() {
        synchronized (filePath) {
            try {
                File file = ResourceUtils.getFile(filePath);
                BufferedWriter bw = new BufferedWriter(new FileWriter(file));
                for (Node node : nodeList) {
                    bw.write(node.getHost() + " " + node.getPort() + " " + node.getName());
                    bw.newLine();
                }
                bw.flush();
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean addNode(String address, int port, String name, String key) {
        for (Node node : nodeList) {
            if (node.getHost().equals(address))
                if (node.getPort() == port)
                    return false;
            if (node.getName().equals(name))
                return false;
        }
        nodeList.add(new Node(address, port, name, key));
//        saveProperties();
        return true;
    }

    public Optional<Node> getNodeByName(String name) {
        return nodeList.stream()
                .filter(n -> n.getName().equals(name))
                .findAny();
    }

    public Optional<Node> getNodeByPublicKey(String public_key) {
        return nodeList.stream()
                .filter(n -> n.getKey().equals(public_key))
                .findAny();
    }

    public String[] getNodeNames() {
        return nodeList.stream().map(Node::getName).toArray(String[]::new);
    }

    public String getNodeList () {
        return JSON.toJSONString(nodeList);
    }
}
