package com.example.blockchain.service;

import com.example.blockchain.Entity.CAEntity;
import com.example.blockchain.Entity.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;


@Service
public class KeyService implements ApplicationListener<WebServerInitializedEvent> {
    private static String localAddress;
    private static String public_key;
    private static String private_key;
    private RestTemplate restTemplate = new RestTemplate();

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


    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        int port = event.getWebServer().getPort();
        localAddress = getUrl(port);
        getKeysFromCA(localAddress);
        System.out.println("get keys from ca");
        System.out.println("public key:" + public_key);
        System.out.println("private key: " + private_key);
    }

    private String getUrl(int port) {
        InetAddress address = null;
        try {
            address = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
//        String url =  "http://"+address.getHostAddress() +":"+this.serverPort;
        String url =  "localhost:"+port;
//        System.out.println(url);
        return url;
    }

    private void getKeysFromCA(String addr) {
        String CA_URL = "http://localhost:8080/CA/register";
        Map<String, String> request = new HashMap<>();
        request.put("address", addr);
        CAEntity res = restTemplate.postForObject(CA_URL, request, CAEntity.class);
        System.out.println(res);

        public_key = res.getPublic_key();
        private_key = res.getPrivate_key();
    }
}
