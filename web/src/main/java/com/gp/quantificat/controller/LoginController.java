package com.gp.quantificat.controller;

import com.gp.quantificat.domain.PrivateWealth;
import com.gp.quantificat.domain.User;
import com.gp.quantificat.domain.coin.response.GetPersonalWealthResponse;
import com.gp.quantificat.service.CoinService;
import com.gp.quantificat.service.QuantificatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
 * @ClassName AController
 * @Description
 * @Author gongpeng
 * @Date 2020/5/12 15:26
 * @Version 1.0
 */
@Controller
@Slf4j
public class LoginController {

    @PostMapping("/doLogin")
    @ResponseBody
    public String submit(String username, String password, HttpSession session) {
        System.out.println("username is : "+ username +" password is : "+ password);
        if(username.equals(username)){
            System.out.println("设置session");
            User user = new User();
            user.setUserName(username);
            user.setPassword(password);
            session.setAttribute("user",user);
            return "success";
        }else{
            return "fail";
        }

    }
}
