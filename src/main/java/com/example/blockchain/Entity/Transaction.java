package com.example.blockchain.Entity;

public class Transaction {
    String from;    // from address
    String to;      // to address
    String item;    // item hash

    public Transaction(String from, String to, String item) {
        this.from = from;
        this.to = to;
        this.item = item;
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

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }
}
