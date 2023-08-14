package com.gp.quantificat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * @Author Administrator
 * @Create 2023-08-13 10:29
*/

@Controller
public class PageController {


    @GetMapping("/index")
    public String toIndex() {
        return "/index";
    }


    @GetMapping("/toLogin")
    public String page() {
        return "/page/login";
    }

    @RequestMapping("/to404")
    public String to404() {
        //总用户数
        return "page/404";
    }

    @RequestMapping("/toDashboard")
    public String toDashboard() {
        //总用户数
        return "page/dashboard";
    }


    @RequestMapping("/toMarket")
    public String toMarket() {
        //总用户数
        return "page/market";
    }
}

