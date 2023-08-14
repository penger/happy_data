package com.gp.quantificat.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.io.IOException;

/**
 * @author gongpeng
 * @date 2021/6/24 15:38
 */
public class WeixinUtils {
    private static final String userToken = "c3a1rvdbil63dsn8b1u0";

    public static void sendMessage(String message) {

        if(message == null || message.isEmpty()){
            return;
        }
        System.out.println("发送消息为："+message);
        Request request = new Request.Builder()
                .url("https://api.letserver.run/message/info?token=" + userToken + "&msg=" + message)
                .build();
        OkHttpClient client = new OkHttpClient();
        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        WeixinUtils.sendMessage("hllll");
    }

}
