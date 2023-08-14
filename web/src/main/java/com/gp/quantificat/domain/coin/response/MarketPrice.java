package com.gp.quantificat.domain.coin.response;

import lombok.Data;

@Data
public class MarketPrice {
    //币名
    private String coinName;
    //价格
    private Double price;
    //24最高
    private Double Highest24;
    //24最低
    private Double Lowest24;
    //成交额
    private Double dealWealth;
    //伦敦开盘价格
    private Double londonOpen;
    //北京开盘价格
    private Double beiJinOpen;

    //波动
//    private Double
    //
}
