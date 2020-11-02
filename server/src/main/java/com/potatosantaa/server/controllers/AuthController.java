package com.potatosantaa.server.controllers;

import com.potatosantaa.server.profiles.AuthenticationBean;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/auth")
public class AuthController {
    
    @GetMapping(path = "/basicauth")
    public AuthenticationBean AuthBean() {
        return new AuthenticationBean("You're authenticated");
    }
}
