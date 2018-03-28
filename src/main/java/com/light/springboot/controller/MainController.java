package com.light.springboot.controller;

import Base.Graph;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;
import util.JSONUtil;

@RestController
//@RequestMapping("/api")

public class MainController {

    @GetMapping("/helloworld")
    public String helloworld() {
        return "main";
    }

    @GetMapping("/")
    public String root() {
        return "main";
    }

//    @GetMapping("/component")
//    @ResponseBody
//    public String getComponentInfo() {
//        JSONObject componets = JSONUtil.jsonRead("src/main/resources/component.json");
//        return componets.toString();
//    }

    @PostMapping("/run")
    @ResponseBody
    public String run(@RequestParam("nodes") String nodes,
                      @RequestParam("links") String links
                      ) throws Exception{

        System.out.print(nodes);
        System.out.print(links);
        JSONObject componets = new JSONObject(nodes);
        JSONObject edges = new JSONObject(links);

        Graph graph = new Graph(componets, edges);
        graph.run();
        return "success";
    }
}
