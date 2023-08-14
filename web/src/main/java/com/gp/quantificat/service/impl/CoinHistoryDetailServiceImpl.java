package com.gp.quantificat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gp.quantificat.domain.CoinHistoryDetail;
import com.gp.quantificat.domain.coin.LuckyCoin;
import com.gp.quantificat.mapper.CoinHistoryDetailMapper;
import com.gp.quantificat.service.CoinHistoryDetailService;
import com.gp.quantificat.util.DateUtil;
import com.gp.quantificat.util.NumberUtils;
import com.gp.quantificat.util.Utils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
* @author gongpeng
* @description 针对表【coin_history_detail】的数据库操作Service实现
* @createDate 2023-02-08 10:41:46
*/
@Service
public class CoinHistoryDetailServiceImpl extends ServiceImpl<CoinHistoryDetailMapper, CoinHistoryDetail>
    implements CoinHistoryDetailService{

    @Override
    public List<CoinHistoryDetail> queryCoinHistoryDetailByNameOrderBYTime(List<String> list, String date) {
        return baseMapper.selectAllByNameInAndTimeAfterOrderByTime(list,date);
    }

    @Override
    public List<CoinHistoryDetail> getAllByTime( String date) {
        return baseMapper.selectAllByTime(date);
    }

    @Override
    public void deleteAllByTime(String date) {
        baseMapper.deleteByTime(date);
    }

    @Override
    public void deleteCoinHistoryDetailByNameAndTimeExactly(String name, String time) {
        baseMapper.deleteByTimeAndName(time,name);
    }

    @Override
    public void deleteCoinHistoryDetailByNameAndTimeAfter(String name,String time){
        baseMapper.deleteByNameAndTimeAfter(name,time);
    }

    @Override
    public List<CoinHistoryDetail> getAllByNameAndTimeLimit(String name, Integer size) {
        return baseMapper.selectAllByNameOrderByTimeDescLimit(name,size);
    }

    @Override
    public Map<String,List<CoinHistoryDetail>> selectAllByNameListAndTimeList(List<String> nameList,List<String> dateList){
        List<CoinHistoryDetail> coinHistoryDetails = baseMapper.selectAllByNameInAndTimeIn(nameList, dateList);
        Map<String, List<CoinHistoryDetail>> map = getSortListMap(nameList, dateList,coinHistoryDetails,true);
        return map;
    }

    @Override
    public List<LuckyCoin> selectAllLuckyCoins(List<String> nameList, Integer day) {
        List<LuckyCoin> outerList = new ArrayList<>();
        String days = DateUtil.getLastNDays(day);
        List<String> dateList = DateUtil.getLastNDayList(day);
        List<CoinHistoryDetail> coinHistoryDetails = baseMapper.selectAllByNameInAndTimeAfter(nameList, days);
        Map<String, List<CoinHistoryDetail>> map = getSortListMap(nameList,dateList, coinHistoryDetails,false);
        for (Map.Entry<String, List<CoinHistoryDetail>> item : map.entrySet()) {
            LuckyCoin luckyCoin = new LuckyCoin();
            luckyCoin.setName(item.getKey());
            List<CoinHistoryDetail> list = item.getValue();
            luckyCoin.setOpen(list.get(0).getClose().doubleValue());
            luckyCoin.setClose(list.get(list.size()-1).getClose().doubleValue());
            LinkedList<Double> precentList = new LinkedList<>();
            for (CoinHistoryDetail detail : list) {
                precentList.add(detail.getPrecent());
            }
            luckyCoin.setPrecentList(precentList);
            luckyCoin.translate();
            outerList.add(luckyCoin);
        }
        return outerList;
    }

    @Override
    public Map<String, String> selectLuckiestAndUnluckiestCoins(Integer day) {
        Map<String,String > map = new HashMap<>();
        List resultList =  new ArrayList();
        String days = DateUtil.getLastNDays(day);
        List<CoinHistoryDetail> list = baseMapper.selectLuckiestAndUnluckiestCoins(days);
        CoinHistoryDetail detail = new CoinHistoryDetail();
        detail.setName("for_test");
        detail.setPrecent(11.0);
        //用于冲抵掉最后的map
        list.add(detail);
        String tempName = "";
        String precentStr="";
        for (CoinHistoryDetail item : list) {
            if (tempName.equals(item.getName())){
                precentStr+=","+item.getPrecent();
            }else{
                if(!tempName.equals("")){
                    map.put(tempName,getMaxFirstLatterLength(precentStr));
                }
                tempName = item.getName();
                precentStr = ""+item.getPrecent();
            }
           //00
        }
        return map;
    }


    private String getMaxFirstLatterLength(String s ){
        String[] split = s.split(",");
        Boolean isHappy = Double.parseDouble(split[0])>0;
        Double precent = Double.valueOf(100);
        int count = 0 ;
        for (String item : split) {
            double v = Double.parseDouble(item);
            if(isHappy){
                if (v > 0){
                    count ++;
                    precent = precent * (100+ v)/100;
                }else{
                    break;
                }
            }else{
                if(v < 0 ){
                    count ++;
                    precent = precent * (100+ v)/100;
                }else{
                    break;
                }
            }
        }
        return ""+count + (isHappy?"连涨":"连跌")+" " + NumberUtils.formatDouble(precent - 100);
    }



    private Map<String,List<CoinHistoryDetail>> getSortListMap(List<String> nameList,List<String> dateList, List<CoinHistoryDetail> coinHistoryDetails,Boolean changePrecent){
        Map<String,List<CoinHistoryDetail>> map = new HashMap<>();
        for (String name : nameList) {
            String tempName = name;
            List<CoinHistoryDetail> list  = new ArrayList<>();
            map.put(tempName,list);
            for (String date : dateList) {
                for (CoinHistoryDetail tempDetail : coinHistoryDetails) {
                    if(tempDetail.getName().equals(tempName) && tempDetail.getTime().equals(date)){
                        list.add(tempDetail);
                        break;
                    }
                }
            }
            if(changePrecent) {
                List<CoinHistoryDetail> newList = getPrecentList(list);
                map.put(tempName,newList);
            }
        }
        return  map;
    }




    private List<CoinHistoryDetail> getPrecentList(List<CoinHistoryDetail> list ){
        //第一个设置为100 ,后边基于当前的算
        BigDecimal start = list.get(0).getClose();
        for (CoinHistoryDetail item : list) {
            BigDecimal close = item.getClose();
            BigDecimal real = start.add(close);
            Double percent = Utils.getPercent(real.doubleValue(),start.doubleValue());
            item.setPrecent(percent);
        }
        return list;
    }

}




