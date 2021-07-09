package com.soul.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/helloController")
public class HelloController {

    @RequestMapping("/hello")
    public String sayHello(Model model) {
        model.addAttribute("msg", "Hello, Springmvc-annotation!");
        return "hello";
    }

    @RequestMapping("/hello2")
    public String sayHello2(Model model) {
        model.addAttribute("msg", "Hello");
        return "hello";
    }


}
