package com.gp.quantificat.service;

import com.gp.quantificat.domain.coin.requset.CoinPriceHistoryRequest;
import com.gp.quantificat.domain.coin.response.GetPersonalWealthResponse;
import com.gp.quantificat.domain.coin.result.MarketAllResult;
import com.gp.quantificat.domain.*;

import java.util.List;
import java.util.Map;

public interface QuantificatService {

    PrivateWealth getCurrentPrivateWealth() ;
    List<PrivateWealth> getListOfPrivateWealth() ;

    PrivateWealth getTodayWealth();

    //对总价和list 做汇率转换
    GetPersonalWealthResponse getPersonalWealth();

    List<MarketAllResult> getMarketAll() ;

    Double getExchangeRate() ;

    List<CoinHistoryDetail> getCoinHistory(CoinPriceHistoryRequest coinPriceHistoryRequest) ;

    //获取当前的对应币种的价格,获取的是最后一次交易的价格
    List<CoinPrice> getCoinPrice(List<String> coinNames) ;

    void getExactlyCoinPriceAndInsertHistory(String coinName, Integer begin, Integer size) ;

    //获取历史
    Map<String, Integer> getCoinPriceHistory(List<String> coinNames, int days) ;

    //触发告警（发送数据到 短信）
    void triggerCoin(List<CoinHistoryDetail> coinHistoryDetailList, List<CoinTrigger> triggers, List<String> messageList) ;

    //获取当天的涨跌幅
    Map<String, List<CoinHistoryDetail>> getTodayPrecent(List<String> coinNames, Integer days) ;

    //凌晨写入  前天和当前比较 写入昨天
    //白天写入  昨天和当前比较 写入今天
    void insertTargetPrivateWealth() ;

    //获取当前总金额 获取之前的金额
    Map<String, String> getCurrentTotalBalance() ;

    //获取当前总金额 获取之前的金额
    void getCurrentTriggerMessage() ;

    void insertCoins(List<String> favorite_coin) ;

    //重置 自定义告警
    void resetMyTrigger();
    //涨跌幅排行
    String getZDFPH() ;

    String recommandPrice() ;

    String test(int s, int p, int l) ;

    //更新当天的数据
    void updateTodayData();

}
