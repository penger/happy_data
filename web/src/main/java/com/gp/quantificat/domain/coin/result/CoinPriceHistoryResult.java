package com.gp.quantificat.domain.coin.result;

import com.gp.quantificat.util.Utils;
import com.gp.quantificat.domain.CoinHistoryDetail;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CoinPriceHistoryResult {
    //账户信息更新时间
    String ts;
    //open
    String o;
    //highest
    String h;
    //lowest
    String l;
    //close
    String c;
    //vol
    String vol;
    //volccy
    String volCcy;
    //完成状态
    String confirm;

    public CoinHistoryDetail getCoinHistoryDetail(String name){
        CoinHistoryDetail detail = new CoinHistoryDetail();
        detail.setTime(Utils.covertTsToDate(ts));
        detail.setOpen(BigDecimal.valueOf(Double.parseDouble(o)));
        detail.setHigh(BigDecimal.valueOf(Double.parseDouble(h)));
        detail.setLow(BigDecimal.valueOf(Double.parseDouble(l)));
        detail.setClose(BigDecimal.valueOf(Double.parseDouble(c)));
        detail.setVol(BigDecimal.valueOf(Double.parseDouble(volCcy)));
        detail.setMaxPrecent(Utils.getPercent(h,l));
        detail.setMaxDiePrecent(Utils.getPercent(l,o));
        detail.setMaxZhangPrecent(Utils.getPercent(h,o));
        detail.setPrecent(Utils.getPercent(c,o));
        detail.setName(name);
        return detail;

    }


}
