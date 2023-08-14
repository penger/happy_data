package com.gp.quantificat.domain.coin.response;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

@Data
public class MarketResultResponse implements Comparable<MarketResultResponse>{
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

    private String volCcy24hFormat;

    private String vol24h;
    //utc0 开盘
    private String sodUtc0;
    //utc8 开盘
    private String sodUtc8;
    //时间
    private String ts;
    //是否为选择的
    private Boolean isChoosen;

    private String message;

    private String wave;


    //成交量排序，根据
    @Override
    public int compareTo(@NotNull MarketResultResponse o) {
        BigDecimal a = BigDecimal.valueOf(Double.parseDouble(volCcy24h));
        BigDecimal b = BigDecimal.valueOf(Double.parseDouble(o.getVolCcy24h()));
        return b.compareTo(a);
    }
}
