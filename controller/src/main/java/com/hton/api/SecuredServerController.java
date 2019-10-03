package com.hton.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class SecuredServerController {
    @RequestMapping("/secured")
    public String secured() {
        System.out.println("Inside secured()");
        return "Hello user !!! : " + new Date();
    }
}
