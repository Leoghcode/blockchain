package com.example.blockchain.Entity;

import com.alibaba.fastjson.JSON;
import com.example.blockchain.service.KeyUtil;
import java.util.List;

public class Block {
    private int index;
    private String hash;
    private String previousHash;
    private long timestamp;
    private String lastTransaction;
    private List transactions;

    public Block(int index, List transactions, String previousHash) {
        this.index = index;
        this.timestamp = System.currentTimeMillis();
        this.transactions = transactions;
        this.previousHash = previousHash;
        this.hash = hash();
    }

    private String hash() {
        String transStr = JSON.toJSONString(transactions);
        String text = "" + index + previousHash + timestamp + lastTransaction + transStr;
        return KeyUtil.getSHA256Str(text);
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public void setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public List getTransactions() {
        return transactions;
    }

    public void setTransactions(List transactions) {
        this.transactions = transactions;
    }

    public String getLastTransaction() {
        return lastTransaction;
    }

    public void setLastTransaction(String lastTransaction) {
        this.lastTransaction = lastTransaction;
    }
}
