package com.gp.quantificat.service;

import com.gp.quantificat.domain.MyTrigger;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author gongpeng
* @description 针对表【my_trigger】的数据库操作Service
* @createDate 2023-02-08 10:44:37
*/
public interface MyTriggerService extends IService<MyTrigger> {

    void resetMyTrigger();

    String trigger(Integer earn);
}
