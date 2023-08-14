package com.gp.quantificat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gp.quantificat.domain.CoinHistoryDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
* @author gongpeng
* @description 针对表【coin_history_detail】的数据库操作Mapper
* @createDate 2023-02-08 10:41:46
* @Entity com.gp.quantificat.domain.CoinHistoryDetail
*/
@Mapper
public interface CoinHistoryDetailMapper extends BaseMapper<CoinHistoryDetail> {

    List<CoinHistoryDetail> selectAllByTime(@Param("time") String time);


    List<CoinHistoryDetail> selectAllByNameInAndTimeAfterOrderByTime(@Param("nameList") Collection<String> nameList, @Param("time") String time);


    int deleteByTimeAndName(@Param("time") String time, @Param("name") String name);


    int deleteByNameAndTimeAfter(@Param("name") String name, @Param("time") String time);


    //@Query(value = "select * from coin_history_detail where name = ?1 order by time desc limit ?2 " ,nativeQuery = true)
    List<CoinHistoryDetail> selectAllByNameOrderByTimeDescLimit(@Param("name") String name, @Param("size")Integer size);


    int deleteByTime(@Param("time") String time);


    List<CoinHistoryDetail> selectAllByNameInAndTimeIn(@Param("nameList") Collection<String> nameList, @Param("timeList") Collection<String> timeList);

    List<CoinHistoryDetail> selectAllByNameInAndTimeAfter(@Param("nameList") Collection<String> nameList, @Param("time") String time);


    List<CoinHistoryDetail> selectLuckiestAndUnluckiestCoins(@Param("time") String time);





}




