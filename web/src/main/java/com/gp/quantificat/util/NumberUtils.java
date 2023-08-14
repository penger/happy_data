package com.gp.quantificat.util;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

@Component
public class NumberUtils {
    public static Integer toInteger(String str){
        double value = Double.parseDouble(str);
        BigDecimal bigDecimal = BigDecimal.valueOf(value);
        bigDecimal.setScale(2, RoundingMode.DOWN);
        return bigDecimal.intValue();
    }

    public static Double toDouble(String str){
        double value = Double.parseDouble(str);
        return formatDouble(value);
    }

    public static Double formatDouble(Double num){
        DecimalFormat format = new DecimalFormat("#.00");
        String newStr = format.format(num);
        return Double.parseDouble(newStr);
    }

    public static Double bigDecimal2FormatDouble(BigDecimal num){
        DecimalFormat format = new DecimalFormat("#.000");
        String newStr = format.format(num);
        return Double.parseDouble(newStr);
    }


    public static String to10Thousand(String str){
        Integer value = toInteger(str);
        return value/10000+"万";
    }

    public static String wave(String a,String b){
        Double percent = Utils.getPercent(a, b);
        return percent.toString();
    }

    public static Boolean compare(String a ,String b){
        double aa = Double.parseDouble(a);
        double bb = Double.parseDouble(b);
        return aa> bb;
    }


    public static void main(String[] args) {
        System.out.println(toDouble("232342.232432"));
    }
}
