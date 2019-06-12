package com.example.blockchain.Entity;

public class Node extends Client{
    private String host;
    private int port;
    public Node(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }
}
