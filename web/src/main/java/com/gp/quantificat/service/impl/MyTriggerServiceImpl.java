package com.gp.quantificat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gp.quantificat.domain.MyTrigger;
import com.gp.quantificat.service.MyTriggerService;
import com.gp.quantificat.mapper.MyTriggerMapper;
import com.gp.quantificat.util.WeixinUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.rmi.dgc.Lease;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
* @author gongpeng
* @description 针对表【my_trigger】的数据库操作Service实现
* @createDate 2023-02-08 10:44:37
*/
@Service
public class MyTriggerServiceImpl extends ServiceImpl<MyTriggerMapper, MyTrigger>
    implements MyTriggerService{

    @Override
    public void resetMyTrigger() {
        //全部设定为没有穿越
        baseMapper.updateTypeByEarn1();
        baseMapper.updateTypeByEarn2();
    }

    @Override
    public String trigger(Integer earn) {
        //获取当前盈亏
        List<MyTrigger> myTriggers = baseMapper.selectAll();
        Collections.sort(myTriggers, Comparator.comparing(MyTrigger::getEarn));
        StringBuffer sb = new StringBuffer();
        for (MyTrigger item : myTriggers) {
            sb.append(processTrigger(item,earn));
        }
        String msg = sb.toString();
        WeixinUtils.sendMessage(msg);
        return msg;
    }

    private String processTrigger(MyTrigger item, Integer earn) {
        BigDecimal level = item.getEarn();
        String type = item.getType();

        if(type.equals("up") && earn > level.intValue()){
            item.setType("down");
            baseMapper.updateById(item);
            if(earn > 0 ) {
                return "涨 穿越 "+level.intValue()+"当前为："+earn;
            }else{
                return "跌 穿越 "+level.intValue()+"当前为："+earn;
            }
        }

        if(type.equals("down") && earn < level.intValue()){
            item.setType("up");
            baseMapper.updateById(item);
            if(earn > 0 ) {
                return "涨 穿越 "+level.intValue()+"当前为："+earn;
            }else{
                return "跌 穿越 "+level.intValue()+"当前为："+earn;
            }
        }
        return "";
    }


    public static void main(String[] args) {
        MyTrigger t0 = new MyTrigger(null,BigDecimal.valueOf(300L),"",true,"up");
        MyTrigger t1 = new MyTrigger(null,BigDecimal.valueOf(200L),"",true,"up");
        MyTrigger t2 = new MyTrigger(null,BigDecimal.valueOf(100L),"",true,"up");
        MyTrigger t3 = new MyTrigger(null,BigDecimal.valueOf(-100L),"",true,"down");
        MyTrigger t4 = new MyTrigger(null,BigDecimal.valueOf(-200L),"",true,"down");
        MyTrigger t5 = new MyTrigger(null,BigDecimal.valueOf(-300L),"",true,"down");

        List<MyTrigger> list = Arrays.asList(t0, t1, t2, t3, t4, t5);

        MyTriggerServiceImpl service = new MyTriggerServiceImpl();
        for (MyTrigger trigger : list) {
            System.out.println(service.processTrigger(trigger, 150));
        }

        for (MyTrigger trigger : list) {
            System.out.println(service.processTrigger(trigger, 150));
        }

        for (MyTrigger trigger : list) {
            System.out.println(service.processTrigger(trigger, 250));
        }

        for (MyTrigger trigger : list) {
            System.out.println(service.processTrigger(trigger, 150));
        }

        for (MyTrigger trigger : list) {
            System.out.println(service.processTrigger(trigger, -150));
        }

    }



}









