package com.gp.quantificat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gp.quantificat.domain.CoinTrigger;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author gongpeng
* @description 针对表【coin_trigger】的数据库操作Mapper
* @createDate 2023-02-08 10:44:31
* @Entity com.gp.quantificat.domain.CoinTrigger
*/
@Mapper
public interface CoinTriggerMapper extends BaseMapper<CoinTrigger> {

    List<CoinTrigger> selectAll();

}




