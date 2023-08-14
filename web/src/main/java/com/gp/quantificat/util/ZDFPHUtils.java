package com.gp.quantificat.util;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author gongpeng
 * @date 2021/6/28 11:25
 */
public class ZDFPHUtils {
    public static String zdfph(Map<String, List<BigDecimal>> map,List<String> coinList,List<String> dateList){
        StringBuffer wholeLine = new StringBuffer();
        Map<String,List<Integer>> newMap = new HashMap<>();
        Set<Map.Entry<String, List<BigDecimal>>> entries =
                map.entrySet();
        for (Map.Entry<String, List<BigDecimal>> entry : entries) {
            String key = entry.getKey();
            List<BigDecimal> valueList = entry.getValue();
            LinkedList<Integer> vs = new LinkedList<>();
            BigDecimal base = valueList.get(0);
            vs.add(100);
            for (int i = 1; i < valueList.size(); i++) {
                BigDecimal v = valueList.get(i);
                vs.add(getPrecent(v,base));
            }
            newMap.put(key,vs);
        }

        String header ="<tr><td>名称</td>";
        for (String s : dateList) {
            header += "<td>"+s+"</td>";
        }
        wholeLine.append(header).append("</tr>");
        for (String coin : coinList) {
            String line ="<tr><td  rowspan='2'>"+coin+"</td>";
            List<BigDecimal> bigDecimals = map.get(coin);
            for (BigDecimal bigDecimal : bigDecimals) {
                line += "<td>"+bigDecimal.doubleValue()+"</td>";
            }
            line+="</tr>";
            List<Integer> doubles = newMap.get(coin);
            for (Integer aDouble : doubles) {
                line += "<td><font color='green'>"+aDouble+"</font></td>";
            }
            line +="</tr>";
            wholeLine.append(line);
        }
        return "<table border = '1px' >"+ wholeLine.toString() +"</table>";
    }

    // a*100/b
    private static Integer getPrecent(BigDecimal a, BigDecimal b){
        BigDecimal result = a.multiply(BigDecimal.valueOf(100L)).divide(b,2,BigDecimal.ROUND_HALF_DOWN);
        return result.intValue();
    }
}
