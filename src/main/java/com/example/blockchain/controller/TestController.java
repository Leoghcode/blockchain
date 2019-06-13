package com.example.blockchain.controller;

import com.alibaba.fastjson.JSON;
import com.example.blockchain.service.NodeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    @RequestMapping("/test")
    public String test () {
        NodeService ns = new NodeService();
        return JSON.toJSONString(ns.getNodeByName("node1"));
    }
}
