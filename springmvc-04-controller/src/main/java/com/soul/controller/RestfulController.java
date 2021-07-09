package com.soul.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class RestfulController {
    //    Restful风格
    @GetMapping("/add/{a}/{b}") // GetMapping直接定义请求类型为GET
//    @RequestMapping("/add/{a}/{b}")
//    @RequestMapping(value = "/add/{a}/{b}", method = RequestMethod.GET)   // 可以用method来定义http请求类型
    public String add(Model model, @PathVariable int a, @PathVariable int b) {
        int res = a + b;
        model.addAttribute("msg", "result is: " + res);
        return "hello";
    }
}
