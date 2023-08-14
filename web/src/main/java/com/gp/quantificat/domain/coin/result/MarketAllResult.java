package com.gp.quantificat.domain.coin.result;

import com.gp.quantificat.util.Utils;
import com.gp.quantificat.domain.CoinHistoryDetail;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MarketAllResult {
    //类型
    private String instType;
    //id
    private String instId;
    //最新成交价格
    private String last;
    //最新成交数量
    private String lastSz;
    //买一价
    private String askPx;
    //买家挂单数量
    private String askSz;
    //卖一价
    private String bidPx;
    //卖家挂单数量
    private String bidSz;
    //24小时开盘价
    private String open24h;
    //24小时最高
    private String high24h;
    //24小时最低
    private String low24h;
    //24小时成交量
    private String volCcy24h;
    private String vol24h;
    //utc0 开盘
    private String sodUtc0;
    //utc8 开盘
    private String sodUtc8;
    //时间
    private String ts;



    public CoinHistoryDetail getCoinHistoryDetail(){
        CoinHistoryDetail detail = new CoinHistoryDetail();
        detail.setTime(ts.split("T")[0]);
        detail.setOpen(BigDecimal.valueOf(Double.parseDouble(sodUtc8)));
        detail.setHigh(BigDecimal.valueOf(Double.parseDouble(high24h)));
        detail.setLow(BigDecimal.valueOf(Double.parseDouble(low24h)));
        detail.setClose(BigDecimal.valueOf(Double.parseDouble(last)));
        long volusdt24 = Double.valueOf(volCcy24h).longValue();
        detail.setVol(BigDecimal.valueOf(volusdt24));
        detail.setMaxPrecent(Utils.getPercent(high24h,low24h));
        detail.setMaxDiePrecent(Utils.getPercent(low24h,sodUtc8));
        detail.setMaxZhangPrecent(Utils.getPercent(high24h,sodUtc8));
        detail.setPrecent(Utils.getPercent(last,sodUtc8));
        detail.setName(instId);
        return detail;

    }

}
