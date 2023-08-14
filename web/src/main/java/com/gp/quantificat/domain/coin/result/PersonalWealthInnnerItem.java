package com.gp.quantificat.domain.coin.result;

import lombok.Data;

@Data
public class PersonalWealthInnnerItem {
    private String spotInUseAmt;
    //占用金额
    private String frozenBal;
    private String uplLiab;
    private String twap;
    //币种余额
    private String cashBal;
    //币种杠杆倍数
    private String notionalLever;
    private String stgyEq;
    private String fixedBal;
    private String interest;
    //币种名称
    private String ccy;
    private String isoUpl;
    private String availEq;
    //美金层面币种折算权益
    private String disEq;
    private String isoLiab;
    //美金层面币种总权益
    private String eq;
    private String isoEq;
    private String liab;
    //挂单冻结数量
    private String ordFrozen;
    //可用余额
    private String availBal;
    //未实现盈亏
    private String upl;
    private String crossLiab;
    private String maxLoan;
    //币种权益美金价值
    private String eqUsd;
    //时间
    private String uTime;
    //保证金率
    private String mgnRatio;


    @Override
    public String toString() {
        return "PersonalWealthInnnerItem{" +
                "占用金额='" + frozenBal + '\'' +
                ", 币种余额='" + cashBal + '\'' +
                ", 币种杠杆倍数='" + notionalLever + '\'' +
                ", 币种名称='" + ccy + '\'' +
                ", 美金层面币种折算权益='" + disEq + '\'' +
                ", 美金层面币种总权益='" + eq + '\'' +
                ", 挂单冻结数量='" + ordFrozen + '\'' +
                ", 可用余额='" + availBal + '\'' +
                ", 未实现盈亏='" + upl + '\'' +
                ", 币种权益美金价值='" + eqUsd + '\'' +
                ", 时间='" + uTime + '\'' +
                ", 保证金率='" + mgnRatio + '\'' +
                '}';
    }
}

//