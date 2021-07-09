package com.soul.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soul.pojo.User;
import com.soul.utils.JsonUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class JsonUserController {

    @RequestMapping("/json1")
    @ResponseBody // 使用这个标签就不会走视图解析器，会直接返回一个字符串
    public String json1() {

        // jackson, ObjectMapper
        ObjectMapper mapper = new ObjectMapper();

        User user = new User("Jack", 30);
        String str = null;
        try {
            str = mapper.writeValueAsString(user);
        } catch (Exception e) {
            System.out.println("error");
            e.printStackTrace();
        }
        System.out.println(str);
        return str;
    }

    @RequestMapping("/json2")
    @ResponseBody // 使用这个标签就不会走视图解析器，会直接返回一个字符串
    public String json2() {

        User user = new User("David", 3);
        return JsonUtils.getJson(user);
    }
}
