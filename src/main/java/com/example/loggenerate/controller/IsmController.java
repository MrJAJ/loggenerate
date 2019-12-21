package com.example.loggenerate.controller;

import com.example.loggenerate.service.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.*;


@RestController
@EnableAsync
public class IsmController {


    private static final Logger logger = LoggerFactory
            .getLogger(IsmController.class);


    @Autowired
    private LogService logService;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index() {
        logService.logCollecct();
        System.out.println("index is running...");
        return "welcome";
    }


}




