package com.potatosantaa.server.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller
@CrossOrigin
public class UIController {

    @RequestMapping(value = { "/", "/task", "/dashboard", "/limited" })
    public String index() {
        return "index.html";
    }
}
