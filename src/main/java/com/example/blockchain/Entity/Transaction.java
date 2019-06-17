package com.example.blockchain.Entity;

import com.example.blockchain.service.KeyUtil;

import java.util.List;
import java.util.Random;

public class Transaction {
    private String from;    // from address
    private String to;      // to address
    private List<Item> item;    // item hash
    private String type;
    private boolean multiSign;
    private String hash;
    private int value;

    public Transaction() {
        this("", "", "", null, false);
    }

    public Transaction(String from, String to, String type, List<Item> item, boolean multiSign) {
        this(from, to, type, item, multiSign, 0);
    }

    public Transaction(String from, String to, String type, List<Item> item, boolean multiSign, int value) {
        this.from = from;
        this.to = to;
        this.type = type;
        this.item = item;
        this.multiSign = multiSign;
        this.hash = KeyUtil.getSHA256Str(from + to + type + item + System.currentTimeMillis() + new Random());
        this.value = value;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public List<Item> getItem() {
        return item;
    }

    public void setItem(List<Item> item) {
        this.item = item;
    }

    public boolean isMultiSign() {
        return multiSign;
    }

    public void setMultiSign(boolean multiSign) {
        this.multiSign = multiSign;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
