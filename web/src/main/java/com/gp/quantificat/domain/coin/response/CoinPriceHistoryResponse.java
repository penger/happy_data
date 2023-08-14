package com.gp.quantificat.domain.coin.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CoinPriceHistoryResponse {
    String name;
    //账户信息更新时间
    String ts;
    //open
    BigDecimal o;
    //highest
    BigDecimal h;
    //lowest
    BigDecimal l;
    //close
    BigDecimal c;
    //vol
    BigDecimal vol;
    //volccy
    BigDecimal volCcy;
    //完成状态
    Boolean confirm;

    //涨跌幅
    private Double precent;
    //日最大涨跌幅
    private Double maxPrecent;
    //每日最大跌幅
    private Double maxDownPrecent;
    //每日最大涨幅
    private Double maxUpPrecent;

}
