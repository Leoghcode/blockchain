package com.example.blockchain.Entity;

import com.example.blockchain.service.KeyUtil;

import java.util.Map;

public class Client {
    private String address;
    private String privateKey;

    public Client() {
        Map<String, String> hashMap = KeyUtil.getSHAKeys();
        address = hashMap.get("address");
        privateKey = hashMap.get("privateKey");
    }

    public String getAddress() {
        return address;
    }

    public String getPrivateKey() {
        return privateKey;
    }
}
