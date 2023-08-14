package com.gp.quantificat.controller;

import com.gp.quantificat.domain.PrivateWealth;
import com.gp.quantificat.domain.coin.response.GetPersonalWealthResponse;
import com.gp.quantificat.service.CoinService;
import com.gp.quantificat.service.CoinTriggerService;
import com.gp.quantificat.service.MyTriggerService;
import com.gp.quantificat.service.QuantificatService;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

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
@RequestMapping
public class MeController {

    @Autowired
    QuantificatService okexService;



    @Autowired
    CoinService coinService;

    @Autowired
    QuantificatService quantificatService;

    @RequestMapping("/me")
    @ResponseBody
    public ModelAndView personalWealth(){
        //各个账户下的金额
        GetPersonalWealthResponse personalWealth = okexService.getPersonalWealth();
        double cny = personalWealth.getWealth();
        List<PrivateWealth> list = okexService.getListOfPrivateWealth();
        ModelAndView mv = new ModelAndView("me");
        mv.addObject("data",personalWealth);
        Collections.reverse(list);
        mv.addObject("list", list);
        mv.addObject("cny", BigDecimal.valueOf(cny).intValue());
        mv.addObject("msg","查询个人钱包下的金额");
        return mv;
    }


}
