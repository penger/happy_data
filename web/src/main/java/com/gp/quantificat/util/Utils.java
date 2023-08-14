package com.gp.quantificat.util;

import com.gp.quantificat.domain.CoinHistoryDetail;
import com.gp.quantificat.domain.coin.response.DayInsight;
import com.gp.quantificat.domain.coin.response.MarketResultResponse;
import org.apache.commons.lang.StringUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.management.RuntimeErrorException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

public class Utils {
    /**
     * Signing a Message.<br/>
     * <p>
     * using: Hmac SHA256 + base64
     *
     * @param timestamp   the number of seconds since Unix Epoch in UTC. Decimal values are allowed.
     *                    eg: 2018-03-08T10:59:25.789Z
     * @param method      eg: POST
     * @param requestPath eg: /orders
     * @param queryString eg: before=2&limit=30
     * @param body        json string, eg: {"product_id":"BTC-USD-0309","order_id":"377454671037440"}
     * @param secretKey   user's secret key eg: E65791902180E9EF4510DB6A77F6EBAE
     * @return signed string   eg: TO6uwdqz+31SIPkd4I+9NiZGmVH74dXi+Fd5X0EzzSQ=
     * @throws CloneNotSupportedException
     * @throws InvalidKeyException
     * @throws UnsupportedEncodingException
     */
    public static String sign(String timestamp, String method, String requestPath,
                              String queryString, String body, String secretKey)
            throws CloneNotSupportedException, InvalidKeyException, UnsupportedEncodingException {
        if (StringUtils.isEmpty(secretKey) || StringUtils.isEmpty(method)) {
            return "secretKey、method 不可为空";
        }

        String preHash = preHash(timestamp, method, requestPath, queryString, body);
        byte[] secretKeyBytes = secretKey.getBytes("UTF-8");
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKeyBytes, "HmacSHA256");
        Mac mac = (Mac) MAC.clone();
        mac.init(secretKeySpec);
        return Base64.getEncoder().encodeToString(mac.doFinal(preHash.getBytes("UTF-8")));
    }

    /**
     * the prehash string = timestamp + method + requestPath + body .<br/>
     *
     * @param timestamp   the number of seconds since Unix Epoch in UTC. Decimal values are allowed.
     *                    eg: 2018-03-08T10:59:25.789Z
     * @param method      eg: POST
     * @param requestPath eg: /orders
     * @param queryString eg: before=2&limit=30
     * @param body        json string, eg: {"product_id":"BTC-USD-0309","order_id":"377454671037440"}
     * @return prehash string eg: 2018-03-08T10:59:25.789ZPOST/orders?before=2&limit=30{"product_id":"BTC-USD-0309",
     * "order_id":"377454671037440"}
     */
    public static String preHash(String timestamp, String method, String requestPath,
                                 String queryString, String body) {
        StringBuilder preHash = new StringBuilder();
        preHash.append(timestamp);
        preHash.append(method.toUpperCase());
        preHash.append(requestPath);
        //get方法
        if (StringUtils.isNotEmpty(queryString)) {
            //在queryString前面拼接上？
            preHash.append("?").append(queryString);
            //改动了
            //preHash.append(queryString);
        }
//        post方法
        if (StringUtils.isNotEmpty(body)) {
            preHash.append(body);
        }
        return preHash.toString();
    }

    public static Mac MAC;

    static {
        try {
            MAC = Mac.getInstance("HmacSHA256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeErrorException(new Error("Can't get Mac's instance."));
        }
    }


    public static String convertTsToDateTime(String ts){
        Instant instant = Instant.ofEpochMilli(Long.parseLong(ts));
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).format(DateTimeFormatter.ISO_DATE_TIME);
    }

    public static String covertTsToDate(String ts){
        Instant instant = Instant.ofEpochMilli(Long.parseLong(ts));
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).format(DateTimeFormatter.ISO_DATE);
    }


    // return (a-b)/b
    public static Double getPercent(Double a, Double b){
        BigDecimal x = BigDecimal.valueOf(a);
        BigDecimal y = BigDecimal.valueOf(b);
        BigDecimal temp = x.subtract(y);
        BigDecimal result = temp.multiply(BigDecimal.valueOf(100L)).divide(y,2,BigDecimal.ROUND_HALF_DOWN);
        return result.doubleValue();
    }


    public static Double getPercent(String a, String b){
        return getPercent(Double.valueOf(a),Double.valueOf(b));
    }

    public static DayInsight getInsight(List<MarketResultResponse> list){
        DayInsight dayInsight = new DayInsight();
        dayInsight.setSumCnt(list.size());
        List<MarketResultResponse> uplist = list.stream().filter(x-> Double.parseDouble(x.getSodUtc8()) < Double.parseDouble(x.getLast())).collect(Collectors.toList());
        List<MarketResultResponse> downlist = list.stream().filter(x-> Double.parseDouble(x.getSodUtc8()) > Double.parseDouble(x.getLast())).collect(Collectors.toList());
        dayInsight.setDownCnt(downlist.size());
        dayInsight.setUpCnt(uplist.size());
        return dayInsight;
    }

}
