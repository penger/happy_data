package com.gp.quantificat.domain.coin.response;

import lombok.Data;

import java.util.List;

@Data
public class GetPersonalWealthResponse {
    //资产更新时间
    private String uTime;
    //资产总量 保留两位有效数字
    private Double wealth;
    //资产用于显示
    private String wealthStr;

    private List<CoinItem> coinItemList;
}


