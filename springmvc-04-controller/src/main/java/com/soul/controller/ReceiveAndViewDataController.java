package com.soul.controller;

import com.soul.pojo.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ReceiveAndViewDataController {

    @GetMapping("/receiveData")
    public String receiveData(@RequestParam("username") String name, Model model) {
        model.addAttribute("msg", name);
        return "hello";
    }

    @GetMapping("/receiveObject")
    public String receiveObject(User user) {
        System.out.println(user);
        return "hello";
    }
}
