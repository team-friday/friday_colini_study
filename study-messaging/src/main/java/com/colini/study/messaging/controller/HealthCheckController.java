package com.colini.study.messaging.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @RequestMapping("healthy")
    public String HealthCheck(){
        return "healthy";
    }
}
