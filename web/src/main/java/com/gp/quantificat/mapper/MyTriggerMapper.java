package com.gp.quantificat.mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gp.quantificat.domain.MyTrigger;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author gongpeng
* @description 针对表【my_trigger】的数据库操作Mapper
* @createDate 2023-02-08 10:44:37
* @Entity com.gp.quantificat.domain.MyTrigger
*/
@Mapper
public interface MyTriggerMapper extends BaseMapper<MyTrigger> {
    List<MyTrigger> selectAll();

    int updateTypeByEarn1();
    int updateTypeByEarn2();

    int updateStatus(@Param("status") Boolean status);

//    @Modifying
//    @Query(value = "update my_trigger set type='up' where earn >0 " ,nativeQuery = true)
//    void updateMyTrigger1();
//
//    @Modifying
//    @Query(value = "update my_trigger set type='down' where earn <0 " ,nativeQuery = true)
//    void updateMyTrigger2();
}




