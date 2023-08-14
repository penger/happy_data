package com.gp.quantificat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gp.quantificat.domain.Coin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author gongpeng
* @description 针对表【coin】的数据库操作Mapper
* @createDate 2023-02-08 10:44:22
* @Entity com.gp.quantificat.domain.Coin
*/
@Mapper
public interface CoinMapper extends BaseMapper<Coin> {

    List<Coin> selectAllByIsChosenOrderByChosenRankDesc(@Param("isChosen") Boolean isChosen);

    List<Coin> selectAllByIsFavoriteOrderByFavoriteRank(@Param("isFavorite") Boolean isFavorite);

    List<Coin> selectAll();
}




