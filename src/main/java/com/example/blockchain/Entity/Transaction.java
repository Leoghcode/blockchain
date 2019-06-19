package com.example.blockchain.Entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.example.blockchain.service.KeyUtil;

import java.util.List;
import java.util.Random;

public class Transaction {
    @JSONField(ordinal = 1)
    private String from;    // from address
    @JSONField(ordinal = 2)
    private String to;      // to address
    @JSONField(ordinal = 3)
    private List<Item> item;    // item hash
    @JSONField(ordinal = 4)
    private String type;
    @JSONField(ordinal = 5)
    private boolean multiSign;
    @JSONField(ordinal = 6)
    private String hash;
    @JSONField(ordinal = 7)
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
