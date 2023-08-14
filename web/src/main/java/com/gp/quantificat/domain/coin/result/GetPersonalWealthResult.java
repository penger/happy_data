package com.gp.quantificat.domain.coin.result;

import com.gp.quantificat.domain.coin.response.CoinItem;
import com.gp.quantificat.domain.coin.response.GetPersonalWealthResponse;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Data
public class GetPersonalWealthResult {
    //账户信息更新时间
    String uTime;
    //美金层面权益
    String totalEq;
    //美金层面权益
    String isoEq;
    //占用保证金
    String imr;

    List<PersonalWealthInnnerItem> details;


    public GetPersonalWealthResponse toGetPersonalWealthResponse(){
        GetPersonalWealthResponse response = new GetPersonalWealthResponse();
        Instant instant = Instant.ofEpochMilli(Long.parseLong(uTime));
        String dataTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).format(DateTimeFormatter.ISO_DATE_TIME);
        response.setUTime(dataTime);
        response.setWealth(Double.parseDouble(totalEq));
        List<CoinItem> list = new ArrayList<>();
        for (PersonalWealthInnnerItem detail : details) {
            if(Double.parseDouble(detail.getCashBal())>5){
                CoinItem coinItem = new CoinItem();
                coinItem.setCoinName(detail.getCcy());
                coinItem.setEarn(Double.parseDouble(detail.getUpl()));
                coinItem.setTotal(Double.parseDouble(detail.getCashBal()));
                coinItem.setUsed(Double.parseDouble(detail.getFrozenBal()));
                coinItem.setAvailable(Double.valueOf(detail.getAvailBal()));
                coinItem.setMultiple(Double.valueOf(detail.getNotionalLever()));
                list.add(coinItem);
            }
        }
        response.setCoinItemList(list);
        return response;
    }

}
