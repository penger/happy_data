package com.gp.quantificat.domain.coin.response;

import lombok.Data;

@Data
public class DayInsight {
    private Integer downCnt;
    private Integer upCnt;
    private Double avgDown;
    private Double avgUp;
    private Integer sumCnt;
}
