package com.soul.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ForwardRedirectController {

    @GetMapping("/forward")
    public String forwardTest() {
        return "hello";
    }

    @GetMapping("/redirect")
    public String redirectTest() {
        return "redirect:/index.jsp";
    }
}
