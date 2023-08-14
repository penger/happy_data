package com.gp.quantificat.controller;

import com.gp.quantificat.domain.CoinHistoryDetail;
import com.gp.quantificat.domain.PrivateWealth;
import com.gp.quantificat.domain.coin.requset.CoinPriceHistoryRequest;
import com.gp.quantificat.service.CoinService;
import com.gp.quantificat.service.CoinTriggerService;
import com.gp.quantificat.service.MyTriggerService;
import com.gp.quantificat.service.QuantificatService;
import com.gp.quantificat.service.impl.QuantificatServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 定时访问地址，获取当天的数据 （币的数据，查询的
 * 根据列表更新，前一天的数据
 *
 * ，将数据提供给数据库，并且会触发告警
 */

@CrossOrigin
@Controller
@RequestMapping
public class SchedulerController {

    @Autowired
    QuantificatService  quantificatService;

    @Autowired
    CoinService coinService;


    @Autowired
    MyTriggerService myTriggerService;

    @Autowired
    CoinTriggerService coinTriggerService;

    //手动执行，更新多天前的数据
    @RequestMapping("/supply/{coin}/{begin}/{size}")
    @ResponseBody
    public String insertCoinPrice2Mysql2(@PathVariable("coin") String coin, @PathVariable("begin") Integer begin
    ,@PathVariable("size") Integer size ){
        quantificatService.getExactlyCoinPriceAndInsertHistory(coin,begin,size);
       return null;
    }


    //每日凌晨更新5天的数据
//    @Scheduled(cron = "0 10 0 * * ?")
    @RequestMapping("/supply/{days}")
    @ResponseBody
    public String insertCoinPrice2MysqlByDay(@PathVariable("days") Integer days){
        List<String> allCoins = coinService.getAllCoin();
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (String coin : allCoins) {
            executorService.execute(
                    () -> quantificatService.getExactlyCoinPriceAndInsertHistory(coin,days,days)
            );
        }
        executorService.shutdown();
        while(!executorService.isTerminated()){
            try {
                Thread.sleep(1000);
                System.out.print(".");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    //每日凌晨执行过去4天的数据
//    @Scheduled(cron = "0 11 0 * * ?")
    public void insertCoinPriceHistoryDaily(){
        insertCoinPrice2MysqlByDay(4);
    }

//    @Scheduled(cron = "0 3 0 * * ?")
    @GetMapping("/trigger_reset")
    public void resetAllTrigger(){
        myTriggerService.resetMyTrigger();
    }



    //每日凌晨重置告警
//    @Scheduled(cron = "0 0 0 * * ?")
    public void resetMyTrigger(){
        quantificatService.resetMyTrigger();
    }

    //    每日凌晨1分记录最新的价格
//    @Scheduled(cron = "0 1 0 * * ? ")
    @GetMapping("/updatePrivateWealth")
    public void updatePrivateWealth(){
        quantificatService.insertTargetPrivateWealth();
    }

//    @Scheduled(cron = "0 0/3 * * * *")
    @GetMapping("/updateOwn")
    public void updatePrivateWealthToday(){
        quantificatService.insertTargetPrivateWealth();
    }


    // 每5分钟执行一次获取最新的price
//    @Scheduled(cron = "0 0/5 * * * *")
    @RequestMapping("/updateALL")
    public void updateTodayData(){
        quantificatService.updateTodayData();
    }



    public static void main(String[] args) {
        QuantificatService service = new QuantificatServiceImpl();
        List<CoinHistoryDetail> list = service.getCoinHistory(new CoinPriceHistoryRequest("BTC-USDT", "1D", "", "", "" + 300));
        System.out.println(list);
    }


}
