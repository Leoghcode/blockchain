package com.example.blockchain.controller;

import com.example.blockchain.service.KeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("key")
public class KeyController {
    @Autowired
    private KeyService keyService;

    @RequestMapping(value = "get", method = RequestMethod.GET)
    public String getKey() {
        return keyService.getPublic_key();
    }

    @RequestMapping(value = "isInspector", method = RequestMethod.GET)
    public Boolean isInspector() {
        return keyService.isInspector();
    }

    @RequestMapping(value = "isValidator", method = RequestMethod.GET)
    public Boolean isValidator() {
        return keyService.isValidator();
    }

}
