package com.example.blockchain.Entity;

public class Request {
    private String from;
    private String fromName;
    private String from_message;
    private String from_signature;
    private String to;
    private String toName;
    private String to_message;
    private String to_signature;
    private Transaction transaction;
    // 0 等待确认或拒绝， 1 已确认 待返回.
    private int status;

    public Request() {}

    public Request(String from, String fromName, String from_message, String from_signature,
                   String to, String toName, String to_message, String to_signature, Transaction transaction) {
        this.from = from;
        this.fromName = fromName;
        this.from_message = from_message;
        this.from_signature = from_signature;
        this.to = to;
        this.toName = toName;
        this.to_message = to_message;
        this.to_signature = to_signature;
        this.transaction = transaction;
        this.status = 0;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getFrom_message() {
        return from_message;
    }

    public void setFrom_message(String from_message) {
        this.from_message = from_message;
    }

    public String getFrom_signature() {
        return from_signature;
    }

    public void setFrom_signature(String from_signature) {
        this.from_signature = from_signature;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTo_message() {
        return to_message;
    }

    public void setTo_message(String to_message) {
        this.to_message = to_message;
    }

    public String getTo_signature() {
        return to_signature;
    }

    public void setTo_signature(String to_signature) {
        this.to_signature = to_signature;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }
}
