package com.hton.api.security;

import com.hton.api.WebMvcConfig;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = WebMvcConfig.SECURITY_PATH)
public class SecurityController {

    @GetMapping(produces = "application/json")
    public ResponseEntity<String> get() {
        return new ResponseEntity<>("Hello world", HttpStatus.OK);
    }
}
