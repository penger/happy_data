package com.gp.quantificat.domain.coin.response;

import lombok.Data;

@Data
public class CoinItem {
    private String coinName;
    private Double total;
    private Double available;
    private Double used;
    private Double earn;
    private Double multiple;
    private Double realCost;
}
