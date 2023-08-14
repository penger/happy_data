package com.gp.quantificat.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.gp.quantificat.domain.Coin;
import com.gp.quantificat.domain.LayVO;
import com.gp.quantificat.domain.coin.response.DayInsight;
import com.gp.quantificat.service.CoinHistoryDetailService;
import com.gp.quantificat.service.CoinService;
import com.gp.quantificat.util.DateUtil;
import com.gp.quantificat.domain.CoinHistoryDetail;
import com.gp.quantificat.domain.Shenglv;
import com.gp.quantificat.domain.coin.requset.CoinPriceHistoryRequest;
import com.gp.quantificat.domain.coin.response.MarketResultResponse;
import com.gp.quantificat.domain.coin.result.MarketAllResult;
import com.gp.quantificat.service.QuantificatService;
import com.gp.quantificat.service.impl.TongjiServiceImpl;
import com.gp.quantificat.util.NumberUtils;
import com.gp.quantificat.util.Utils;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.jws.WebParam;
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
public class MarketController {

    @Autowired
    QuantificatService quantificatService;

    @Autowired
    TongjiServiceImpl tongjiService;

    @Autowired
    CoinService  coinService;

    @Autowired
    CoinHistoryDetailService coinHistoryDetailService;


    @GetMapping("/wx")
    @ResponseBody
    public String weixin(){
        Map<String, String> currentTotalBalance = quantificatService.getCurrentTotalBalance();
        return currentTotalBalance.get("wx");
    }

    //全量写入mysql 三年时间
    @GetMapping("/insert")
    @ResponseBody
    public String insertCoinPrice2Mysql(){
        List<String> favoriteCoins = coinService.getFavriteCoin();
        Map<String, Integer> coinPriceHistoryMap = quantificatService.getCoinPriceHistory(favoriteCoins,3000);
        return JSON.toJSONString(coinPriceHistoryMap);
    }


    //查看市场行情
    @GetMapping("/market")
    @ResponseBody
    public LayVO market(){
        //更新今天最新的数据
//        quantificatService.updateTodayData();
        List<MarketResultResponse> newList = new ArrayList<>();
        List<MarketAllResult> list = quantificatService.getMarketAll();
        Map<String, String> messageMap = coinHistoryDetailService.selectLuckiestAndUnluckiestCoins(10);
        for (MarketAllResult marketAllResult : list) {
            MarketResultResponse response = new MarketResultResponse();
            BeanUtils.copyProperties(marketAllResult,response);
            String coin = response.getInstId();
            response.setMessage(messageMap.get(coin));
            response.setWave(""+NumberUtils.wave(response.getLast(),response.getSodUtc8()));
            response.setLast(""+NumberUtils.toDouble(response.getLast()));
            response.setSodUtc8(""+NumberUtils.toDouble(response.getSodUtc8()));
            response.setHigh24h(""+NumberUtils.toDouble(response.getHigh24h()));
            response.setLow24h(""+NumberUtils.toDouble(response.getLow24h()));
            response.setVolCcy24hFormat(""+NumberUtils.to10Thousand(response.getVolCcy24h()));
            newList.add(response);
        }
        Collections.sort(newList);
        return new LayVO(0,"",newList.size(),newList);
    }


    @GetMapping("/detail/{coinName}")
    @ResponseBody
    public ModelAndView detail(@PathVariable("coinName") String coinName){
        CoinPriceHistoryRequest request = new CoinPriceHistoryRequest();
        request.setBar("1D");
        request.setInstId(coinName);
        request.setLimit("300");
        List<CoinHistoryDetail> list = quantificatService.getCoinHistory(request);
        ModelAndView mv = new ModelAndView("detail");
        mv.addObject("details",list);
        mv.addObject("data",Lists.reverse(list));
        mv.addObject("msg",coinName+"实时查询okex接口行情数据");
        return mv;
    }


    @GetMapping("/shenglv/{coinName}/{days}")
    @ResponseBody
    public String shenglv(@PathVariable("coinName") String coinName,@PathVariable("days") Integer days){
        String end = DateUtil.getLastNDays(0);
        String start = DateUtil.getLastNDays(30);
        Map<String, List<Shenglv>> slMap = tongjiService.calcShenglv(start, end, coinName);
        return JSON.toJSONString(slMap);
    }

    @GetMapping("/shenglv")
    public ModelAndView getShenglvPage(){
        List<String> favoriteCoins = coinService.getFavriteCoin();
        ModelAndView modelAndView = new ModelAndView("shenglv", "coins", favoriteCoins);
        String end = DateUtil.getLastNDays(0);
        String start = DateUtil.getLastNDays(30);
        Map<String, List<Shenglv>> shenglv = tongjiService.calcShenglv(start, end, "nestusdt");
        modelAndView.addObject("zhang",shenglv.get("zhang"));
        modelAndView.addObject("reality",shenglv.get("reality"));
        modelAndView.addObject("die",shenglv.get("die"));
        return modelAndView;
    }


    //涨跌幅排行
    @GetMapping("/z")
    public ModelAndView getZDFPH(){
        String zdfph = quantificatService.getZDFPH();
        ModelAndView m = new ModelAndView("zdfph");
        m.addObject("msg",zdfph);
        return m;
    }

    //推荐价格设置
    @GetMapping("/r")
    public ModelAndView getRecommandPrice(){
        String recommend = quantificatService.recommandPrice();
        ModelAndView m = new ModelAndView("recommend");
        m.addObject("msg",recommend);
        return m;
    }


}
