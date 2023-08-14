package com.gp.quantificat.controller;

import com.gp.quantificat.domain.coin.LuckyCoin;
import com.gp.quantificat.service.CoinService;
import com.gp.quantificat.util.DateUtil;
import com.gp.quantificat.domain.CoinHistoryDetail;
import com.gp.quantificat.domain.PrivateWealth;
import com.gp.quantificat.service.CoinHistoryDetailService;
import com.gp.quantificat.service.QuantificatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

/**
 * @ClassName AController
 * @Description
 * @Author gongpeng
 * @Date 2020/5/12 15:26
 * @Version 1.0
 */
@Controller
@RequestMapping
public class DashBoardController {

    @Autowired
    QuantificatService quantificatService;

    @Autowired
    CoinHistoryDetailService coinHistoryDetailService;

    @Autowired
    CoinService coinService;

    //查看市场行情
//    @GetMapping("/dashboard")
//    @ResponseBody
//    public ModelAndView index(){
//        Integer day = 7;
//        //当前的资产
//        PrivateWealth privateWealth = quantificatService.getCurrentPrivateWealth();
//        //汇率
//        Double exchangeRate = quantificatService.getExchangeRate();
//        //标记选中的cion
//        List<String> chosenCoin = coinService.getChosenCoin();
//        List<CoinHistoryDetail> details = coinHistoryDetailService.queryCoinHistoryDetailByNameOrderBYTime(chosenCoin, DateUtil.getLastNDays(day));
//        List<String> dateList = DateUtil.getLastNDayList(day);
//        // coinname date1.. date2 date3
//        Map<String,List<CoinHistoryDetail>> resultMap = new HashMap<>();
//        for (String coin : chosenCoin) {
//            List<CoinHistoryDetail> innerList = new LinkedList();
//            for (CoinHistoryDetail detail : details) {
//                if(detail.getName().equals(coin)){
//                    innerList.add(detail);
//                }
//            }
//            resultMap.put(coin,innerList);
//        }
//        ModelAndView mv = new ModelAndView("dashboard");
//        mv.addObject("dates",dateList);
//        mv.addObject("map",resultMap);
//        mv.addObject("exchangeRate",exchangeRate);
//        mv.addObject("msg","汇率为："+exchangeRate+" 当前账户余额："+privateWealth.getClose().intValue()+" 盈亏为："+privateWealth.getEarn().intValue());
//        return mv;
//    }

    @GetMapping("/dashboard")
    @ResponseBody
    public Map<String,List<CoinHistoryDetail>> index(){
        Integer day = 7;
        //汇率
        Double exchangeRate = quantificatService.getExchangeRate();
        //标记选中的cion
        List<String> chosenCoin = coinService.getChosenCoin();
        List<CoinHistoryDetail> details = coinHistoryDetailService.queryCoinHistoryDetailByNameOrderBYTime(chosenCoin, DateUtil.getLastNDays(day));
        List<String> dateList = DateUtil.getLastNDayList(day);
        // coinname date1.. date2 date3
        Map<String,List<CoinHistoryDetail>> resultMap = new HashMap<>();
        for (String coin : chosenCoin) {
            List<CoinHistoryDetail> innerList = new LinkedList();
            for (CoinHistoryDetail detail : details) {
                if(detail.getName().equals(coin)){
                    innerList.add(detail);
                }
            }
            resultMap.put(coin,innerList);
        }
        return resultMap;
    }




    @GetMapping("/rank")
    public ModelAndView rank(){
        ModelAndView mv = new ModelAndView("rank");
        List<String> dateLables = Arrays.asList(
                "半年前",
                "三月前",
                "两月前",
                "一月前",
                "两周前",
                "一周前",
                "今天"
        );
        List<String> dateList = Arrays.asList(
                DateUtil.getLastNDays(180),DateUtil.getLastNDays(90),DateUtil.getLastNDays(60)
                ,DateUtil.getLastNDays(30),DateUtil.getLastNDays(15),DateUtil.getLastNDays(7)
                ,DateUtil.getLastNDays(0)
        );

        List<String> chosenCoin = coinService.getChosenCoin();
        Map<String, List<CoinHistoryDetail>> map = coinHistoryDetailService.selectAllByNameListAndTimeList(chosenCoin, dateList);
        mv.addObject("dates",dateLables);
        mv.addObject("map",map);
        return mv;
    }


    //过去半个月的涨幅，涨跌分布 最近胜率
    @GetMapping("/lucky")
    public ModelAndView lucky(){
        ModelAndView mv = new ModelAndView("lucky");
        List<String> chosenCoin = coinService.getChosenCoin();
        List<LuckyCoin> luckyCoins = coinHistoryDetailService.selectAllLuckyCoins(chosenCoin, 15);
        mv.addObject("datas",luckyCoins);
        return mv;
    }


    @GetMapping("/luckyunlucky")
    public void luckyAndUnlucky(){
        Map<String,String> map = coinHistoryDetailService.selectLuckiestAndUnluckiestCoins(10);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            System.out.println(entry.getKey() +" -----------> "+ entry.getValue());
        }
    }



}
