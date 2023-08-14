package com.gp.quantificat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gp.quantificat.domain.CoinHistoryDetail;
import com.gp.quantificat.domain.coin.LuckyCoin;

import java.util.List;
import java.util.Map;

/**
* @author gongpeng
* @description 针对表【coin_history_detail】的数据库操作Service
* @createDate 2023-02-08 10:41:46
*/
public interface CoinHistoryDetailService extends IService<CoinHistoryDetail> {

    List<CoinHistoryDetail> queryCoinHistoryDetailByNameOrderBYTime(List<String> list, String date);

    List<CoinHistoryDetail> getAllByTime(String date);

    void deleteAllByTime(String date);

    void deleteCoinHistoryDetailByNameAndTimeExactly(String name, String time);

    void deleteCoinHistoryDetailByNameAndTimeAfter(String name,String time);


    //select * from coin_history_detail where name = ?1 order by time desc limit ?2
    List<CoinHistoryDetail> getAllByNameAndTimeLimit(String name , Integer size);


    public Map<String,List<CoinHistoryDetail>> selectAllByNameListAndTimeList(List<String> nameList, List<String> dateList);

    public List<LuckyCoin> selectAllLuckyCoins(List<String> name, Integer day);


    public Map<String, String> selectLuckiestAndUnluckiestCoins(Integer day);

}
