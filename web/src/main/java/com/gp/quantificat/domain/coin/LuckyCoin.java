package com.gp.quantificat.domain.coin;

import com.google.common.collect.Lists;
import com.gp.quantificat.util.Utils;
import lombok.Data;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.Arrays;
import java.util.List;

@Data
public class LuckyCoin {
    private String name;
    private List<Double> precentList;
    private Integer upCnt;
    private Integer downCnt;
    private Integer lastCnt;
    private String lastStatus;
    private Double maxUpPrecent;
    private Double maxDownPrecent;
    private Double truePrecent;
    private Double open;
    private Double close;
    private String message;

    //从老到新的数据
    public void translate(){
        maxDownPrecent= (double) 0;
        maxUpPrecent = (double) 0;
        upCnt = 0 ;
        downCnt =0;
        for (Double item : precentList) {
            if(item>0){
                upCnt++;
                if(item>maxUpPrecent){
                    maxUpPrecent = item;
                }
            }else{
                downCnt++;
                if(item<maxDownPrecent){
                    maxDownPrecent = item;
                }
            }
        }
        List<Double> newList = Lists.reverse(precentList);
        Double last = newList.get(0);
        if(last>0){
            lastStatus="up";
        }else{
            lastStatus="down";
        }
        int count = 0 ;
        for (Double newItem : newList) {
            if(lastStatus.equals("up")){
                if(newItem>0){
                    count ++;
                }else{
                    break;
                }
            }else{
                if(newItem<0){
                    count++;
                }else{
                    break;
                }
            }
        }
        lastCnt = count;
        truePrecent = Utils.getPercent(close,open);

        int days = precentList.size();

        message = "在过去"+ days +"天里"+upCnt+"涨"+downCnt+"跌，最近"+lastCnt+"连"+(lastStatus.equals("up")?"涨":"跌")+":最大涨幅"+maxUpPrecent+"最大跌幅"+maxDownPrecent+"相对"+days+"天前涨幅为"+truePrecent;
    }


    public static void main(String[] args) {
        LuckyCoin luckyCoin = new LuckyCoin();
        luckyCoin.setName("k");
        luckyCoin.setPrecentList(Arrays.asList(Double.valueOf(1),Double.valueOf(1),Double.valueOf(-3)));
        luckyCoin.translate();
        System.out.println(luckyCoin);
    }


}
