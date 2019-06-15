package com.example.blockchain.Entity;

public class Node {
    private String host;
    private int port;
    private String name;
    private String key;

    public Node() {}

    public Node(String host, int port, String name, String key) {
        this.host = host;
        this.port = port;
        this.name = name;
        this.key = key;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
