package com.example.blockchain.service;

import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Map;


@Service
public class KeyService {
    private static String public_key;
    private static String private_key;
    static {
        Map<String, String> map = KeyUtil.getSHAKeys();
        public_key = map.get("public_key");
        private_key = map.get("private_key");
    }


    public String getPublic_key() {
        return public_key;
    }

    public void setPublic_key(String public_key) {
        this.public_key = public_key;
    }

    public String getPrivate_key() {
        return private_key;
    }

    public void setPrivate_key(String private_key) {
        this.private_key = private_key;
    }
}
