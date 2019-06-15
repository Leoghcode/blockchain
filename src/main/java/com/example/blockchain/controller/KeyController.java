package com.example.blockchain.controller;

import com.example.blockchain.service.KeyService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("key")
public class KeyController {
    private KeyService keyService = new KeyService();

    @RequestMapping(value = "get", method = RequestMethod.GET)
    public String getKey() {
        return keyService.getPublic_key();
    }

}
