package com.soul.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/encoding")
public class EncodingController {
    @PostMapping("/t1")
    public String sendChinese(Model model, String name){
        model.addAttribute("msg",name); //获取表单提交的值
        return "hello"; //跳转到test页面显示输入的值
    }
}
