package com.example.blockchain.controller;

import com.alibaba.fastjson.JSON;
import com.example.blockchain.Entity.CAEntity;
import com.example.blockchain.Entity.Node;
import com.example.blockchain.service.NodeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("node")
public class NodeController {
    private NodeService nodeService = new NodeService();

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public HttpStatus addNode(@RequestBody List<Node> nodes) {
        try {
            for (Node n : nodes)
                nodeService.addNode(n.getHost(), n.getPort(), n.getName(), n.getKey());
            return HttpStatus.OK;
        } catch (Exception e) {
            e.printStackTrace();
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    @RequestMapping(value = "get", method = RequestMethod.GET)
    public String getNodeNames() {
        return JSON.toJSONString(nodeService.getNodeNames());
    }

    @RequestMapping(value = "addFromCA", method = RequestMethod.GET)
    public List<Node> addNodesFromCA() {
        return nodeService.addNodesFromCA();
    }

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public List<Node> getList() {
        return nodeService.getNodeArrayList();
    }
}
