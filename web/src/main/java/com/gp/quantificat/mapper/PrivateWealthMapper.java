package com.gp.quantificat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gp.quantificat.domain.PrivateWealth;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author gongpeng
* @description 针对表【private_wealth】的数据库操作Mapper
* @createDate 2023-02-08 10:44:42
* @Entity com.gp.quantificat.domain.PrivateWealth
*/
@Mapper
public interface PrivateWealthMapper extends BaseMapper<PrivateWealth> {

    PrivateWealth selectOneByDate(@Param("date") String date);

    List<PrivateWealth> selectAllOrderByDateDesc();

    List<PrivateWealth> selectAllOrderByDateDescLimit10();

}




