package com.gp.quantificat.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author gongpeng
 * @date 2021/4/29 10:36
 */

@Data
public class CoinPrice {
    //名称
    private String coinName;
    //当前价格历史价格
    private BigDecimal currentPrice;
    //价格列表
    private List<BigDecimal> priceList;
    //日期列表
    private List<Date> dateList;
    //成交量
    private List<BigDecimal> dealCount;
}
