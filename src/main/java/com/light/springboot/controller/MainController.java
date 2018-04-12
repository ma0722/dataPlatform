package com.light.springboot.controller;

import Base.Graph;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;
import util.JSONUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Properties;

@RestController
public class MainController {

    @GetMapping("/")
    public String root() {
        return "test";
    }


    @PostMapping("/run")
    @ResponseBody
    public String run(@RequestParam("nodes") String nodes,
                      @RequestParam("links") String links
                      ) throws Exception{
        System.out.println("组件信息：" + nodes);
        System.out.println("组件协作关系：" + links);

        JSONUtil.jsonWrite("src/main/resources/tmp/nodes.json", nodes);
        System.out.println("write nodes success");
        JSONUtil.jsonWrite("src/main/resources/tmp/links.json", links);
        System.out.println("write links success");

        runByCommands();

        return "success";
    }

    public void runByCommands() {
        try {
            System.out.println("package job......");
            String command = "mvn package";
            Process ps = Runtime.getRuntime().exec(command);

            BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
            ps.waitFor();
        }
        catch (Exception e) {
            System.out.println("package job failed!");
            e.printStackTrace();
        }

        try {
            System.out.println("submit job......");
            Properties properties = new Properties();
            properties.load(MainController.class.getResourceAsStream("/command.properties"));
            String submitShellPath = properties.getProperty("submitShellPath");;
            String packagePath = " target/data-platform-1.0-SNAPSHOT-jar-with-dependencies.jar";
            String command = String.format("%s %s", submitShellPath, packagePath);
            System.out.println(command);
            Process ps = Runtime.getRuntime().exec(command);

            BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
            ps.waitFor();

        }
        catch (Exception e) {
            System.out.println("submit job failed!");
            e.printStackTrace();
        }
    }

    public void runByShell() {

        try {
            String rootPath = System.getProperty("user.dir");
            String shpath = rootPath + "/submit_job.sh";
            Process ps = Runtime.getRuntime().exec(shpath);
            System.out.println("submitting job......");
            ps.waitFor();

            BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            String result = sb.toString();
            System.out.println(result);
        }
        catch (Exception e) {
            System.out.println("job run failed!");
            e.printStackTrace();
        }
        System.out.println("job run success!");
    }

}
