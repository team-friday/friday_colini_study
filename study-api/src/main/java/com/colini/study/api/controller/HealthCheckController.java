package com.colini.study.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @RequestMapping("/healthy")
    public String healthCheck(){
        return "healthy";
    }
}
