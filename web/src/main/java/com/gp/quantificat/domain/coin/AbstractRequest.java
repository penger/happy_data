package com.gp.quantificat.domain.coin;

import com.gp.quantificat.util.Utils;
import com.sun.javafx.PlatformUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.security.InvalidKeyException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractRequest {

    protected String url;

    public abstract void setUrl();

    public String execute() {
        setUrl();
        //打印请求url地址
        printInfo();
        Request.Builder builder = new Request.Builder();
        Map<String, String> headers = new HashMap<String, String>();
        builder.addHeader("OK-ACCESS-KEY", "d13a7da0-ca24-4fa1-af1c-720f0e6ee3a0");
//        String currentTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.000'Z'").format(new Date());
        String currentTime = Instant.now().toString();
        builder.addHeader("OK-ACCESS-TIMESTAMP", currentTime);
        builder.addHeader("OK-ACCESS-PASSPHRASE", "9ijn)OKM");

        String secretKey = "1FE71B9DC19D5BD3F5F845234ED82BF6";
        String sign = null;
        try {
            sign = Utils.sign(currentTime, "GET", "/api/v5/account/balance", "", "", secretKey);
        } catch (InvalidKeyException | CloneNotSupportedException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        builder.addHeader("OK-ACCESS-SIGN", sign);
        Request request = builder.url("http://www.okx.com/"+url).build();
        OkHttpClient client = new OkHttpClient();
        if(PlatformUtil.isWindows()) {
            client = new OkHttpClient.Builder().proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 10080))).build();
        }
        String content = "";
        try {
            Response response = client.newCall(request).execute();
            content = response.body().string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return content;
    }

    void printInfo(){
        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)+" --- "+url);
    }

}
