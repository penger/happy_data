package com.gp.quantificat.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author gongpeng
 * @date 2021/5/8 14:19
 */
public class DateUtil {
    public static String getLastNDays(Integer days){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,0-days);
        calendar.isWeekDateSupported();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(calendar.getTime());
    }

    public static List<String> getLastNDayList(Integer days){
        List<String> list = new ArrayList<>();
        for(int i = days-1 ; i>=0 ;i--){
            String tempDay = getLastNDays(i);
            list.add(tempDay);
        }
        return list;
    }




    public static void main(String[] args) {
//        System.out.println(DateUtil.getLastNDays(0));
        System.out.println(DateUtil.getLastNDayList(7));

    }
}
