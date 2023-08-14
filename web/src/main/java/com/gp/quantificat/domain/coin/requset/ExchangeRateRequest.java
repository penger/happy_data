package com.gp.quantificat.domain.coin.requset;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.gp.quantificat.domain.coin.AbstractRequest;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;

// 获取汇率人民币兑换美元
// https://www.okx.com/docs-v5/zh/#rest-api-market-data-get-exchange-rate

@Data
@NoArgsConstructor
public class ExchangeRateRequest extends AbstractRequest {

    @Override
    public void setUrl() {
        this.url = "api/v5/market/exchange-rate";
    }

    public static void main(String[] args) throws IOException {
        String content = new ExchangeRateRequest().execute();
        JSONArray array = JSON.parseObject(content).getJSONArray("data");
        System.out.println(JSON.parseObject(array.get(0).toString()).getDouble("usdCny"));
    }


}
