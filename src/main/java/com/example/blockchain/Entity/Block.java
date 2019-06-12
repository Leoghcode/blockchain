package com.example.blockchain.Entity;

import com.alibaba.fastjson.JSON;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class Block {
    private int index;
    private long timestamp;
    private String previousHash;
    private String hash;
    private List transactions;

    public Block(int index, long timestamp, List transactions, String previousHash) {
        this.index = index;
        this.timestamp = timestamp;
        this.transactions = transactions;
        this.previousHash = previousHash;
        this.hash = hash();
    }

    private String hash() {
        String transStr = JSON.toJSONString(transactions);
        String text = "" + index + timestamp + transStr + previousHash;
        return getSHA256Str(text);
    }

    private String getSHA256Str(String text) {
        String encodeText = "";
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(text.getBytes("UTF-8"));
            encodeText = byte2Hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodeText;
    }

    private String byte2Hex(byte[] bytes) {
        StringBuffer stringBuffer = new StringBuffer();
        String tmp = null;
        for (byte aByte : bytes) {
            tmp = Integer.toHexString(aByte & 0xFF);
            if (tmp.length() == 1) {
                stringBuffer.append("0");
            }
            stringBuffer.append(tmp);
        }
        return stringBuffer.toString();
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
}
