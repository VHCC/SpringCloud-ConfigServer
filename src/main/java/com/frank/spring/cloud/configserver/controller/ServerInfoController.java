package com.frank.spring.cloud.configserver.controller;

import com.frank.spring.cloud.configserver.utils.RequestHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @Author IChen.Chu
 * @Date 2023/03/15
 */

@RestController
@CrossOrigin
@Slf4j
@RequestMapping(value = "api/server/info")
public class ServerInfoController {

    InputStream in = getClass().getClassLoader().getResourceAsStream("version.properties");

    Properties props = new Properties();
    Map<String, String> versionResults = new HashMap<>();
    static String version;

    public ServerInfoController() throws IOException {
        props.load(in);
        version = (String) props.get("version");
        versionResults.put("version", version);
    }

    @Autowired
    private DiscoveryClient client;


    @RequestMapping(value = "/version", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> getServerVersion() {
        return RequestHelper.formatSuccessResult(versionResults);
    }

}
