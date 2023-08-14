package com.gp.quantificat.service.impl;

import com.gp.quantificat.domain.CoinHistoryDetail;
import com.gp.quantificat.domain.Shenglv;
import com.gp.quantificat.service.CoinHistoryDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.*;

/**
 * @author gongpeng
 * @date 2021/5/8 10:53
 */
@Service
public class TongjiServiceImpl {

    @Autowired
    CoinHistoryDetailService coinHistoryDetailService;

    /**
     * 获取一段时间的跌幅分布
     * @param startTime
     * @param endTime
     */
    public Map<String,List<Shenglv>> calcShenglv(String startTime ,String endTime,String coinName){
        List<CoinHistoryDetail> coinHistoryDetails =
                coinHistoryDetailService.queryCoinHistoryDetailByNameOrderBYTime(Arrays.asList(coinName),endTime);
        List<CoinHistoryDetail> coinListByTime = getCoinListByTime(startTime, endTime, coinHistoryDetails);
        //分别计算这段时间的最大涨幅，最大跌幅，涨幅胜率
        //1.跌幅胜率
        System.out.println("计算跌幅");
        Map<Integer, Integer> dieMap= new TreeMap<>();
        Map<Integer, Integer> zhangMap= new TreeMap<>();
        Map<Integer, Integer> trueMap= new TreeMap<>();
        int length = coinListByTime.size();
        for (CoinHistoryDetail coinHistoryDetail : coinListByTime) {
            Integer maxDiePrecent = coinHistoryDetail.getMaxDiePrecent().intValue();
            Integer maxZhangPrecent = coinHistoryDetail.getMaxZhangPrecent().intValue();
            Integer precent = coinHistoryDetail.getPrecent().intValue();

            if(dieMap.containsKey(maxDiePrecent)){
                dieMap.put(maxDiePrecent,dieMap.get(maxDiePrecent)+1);
            }else{
                dieMap.put(maxDiePrecent,1);
            }

            if(zhangMap.containsKey(maxZhangPrecent)){
                zhangMap.put(maxZhangPrecent,zhangMap.get(maxZhangPrecent)+1);
            }else{
                zhangMap.put(maxZhangPrecent,1);
            }

            if(trueMap.containsKey(precent)){
                trueMap.put(precent,trueMap.get(precent)+1);
            }else{
                trueMap.put(precent,1);
            }
        }

        List<Shenglv> dieshenglv = getShenglv(dieMap, length);
        List<Shenglv> zhangshenglv = getShenglv(zhangMap, length);
        List<Shenglv> trueshenglv = getShenglv(trueMap, length);

        Map<String,List<Shenglv>> resultMap = new HashMap();
        resultMap.put("die",dieshenglv);
        resultMap.put("zhang",zhangshenglv);
        resultMap.put("reality",trueshenglv);

        return resultMap;


    }

    private List<Shenglv> getShenglv(Map<Integer, Integer> map, int length) {
        //双循环
        List<Shenglv> list =new LinkedList<Shenglv>();
        Set<Map.Entry<Integer, Integer>> entries = map.entrySet();
        Set<Map.Entry<Integer, Integer>> innerentries = map.entrySet();
        for (Map.Entry<Integer, Integer> entry : entries) {
            Integer key = entry.getKey();
            int sum = 0;
            if(key<0) {
                for (Map.Entry<Integer, Integer> innerentry : innerentries) {
                    Integer innerKey = innerentry.getKey();
                    Integer innerValue = innerentry.getValue();
                    if (innerKey <= key) {
                        sum += innerValue;
                    }
                }
            }else if(key >0){
                for (Map.Entry<Integer, Integer> innerentry : innerentries) {
                    Integer innerKey = innerentry.getKey();
                    Integer innerValue = innerentry.getValue();
                    if(innerKey>=key){
                        sum+=innerValue;
                    }
                }
            }else{
                sum =map.get(0);
            }
            Shenglv shenglv = new Shenglv();
            shenglv.setCoinPrecent(key);
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            shenglv.setVictory((float)sum*100/length);
            shenglv.setDayCount(entry.getValue());
            list.add(shenglv);
        }
        return list;
    }


    private List<CoinHistoryDetail> getCoinListByTime(String start,String end ,List<CoinHistoryDetail> coinHistoryDetailList){
        List<CoinHistoryDetail> newList = new LinkedList<>();
        for (CoinHistoryDetail coinHistoryDetail : coinHistoryDetailList) {
            String time = coinHistoryDetail.getTime();
            if(parseTime(time)>parseTime(start) && parseTime(time)<parseTime(end)){
                newList.add(coinHistoryDetail);
            }
        }
        return newList;
    }

    private Integer parseTime(String time ){
        return Integer.parseInt(time.replace("-",""));
    }

}
