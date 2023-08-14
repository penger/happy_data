package com.gp.quantificat.domain.coin.requset;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.gp.quantificat.domain.CoinHistoryDetail;
import com.gp.quantificat.domain.coin.AbstractRequest;
import com.gp.quantificat.domain.coin.result.CoinPriceHistoryResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

// https://www.okx.com/docs-v5/en/#rest-api-market-data-get-candlesticks

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoinPriceHistoryRequest extends AbstractRequest {

    // GET /api/v5/market/candles?instId=BTC-USD-190927-5000-C
    private String instId;
    //bar size 默认是 1m Hong Kong time opening price k-line：[6H/12H/1D/2D/3D/1W/1M/3M]
    private String bar;
    //ts
    private String after;
    //ts
    private String before;
    //最大300 ，默认100
    private String limit;

    @Override
    public void setUrl() {
        this.url = "api/v5/market/candles?";
        if(!StringUtils.isEmpty(instId)){
            this.url += "&instId="+instId;
        }
        if(!StringUtils.isEmpty(bar)){
            this.url += "&bar="+bar;
        }
        if(!StringUtils.isEmpty(after)){
            long val = Long.parseLong(after);
            this.url += "&after="+val;
        }
        if(!StringUtils.isEmpty(before)){
            long val = Long.parseLong(before);
            this.url += "&before="+val;
        }
        if(!StringUtils.isEmpty(limit)){
            this.url += "&limit="+limit;
        }
    }


    public static void main(String[] args) {

        int begin = 400;
        int between =100;

        LocalDateTime start = LocalDateTime.now().minusDays(begin);
        System.out.println(start.format(DateTimeFormatter.ISO_DATE_TIME));
        String e = ""+ Timestamp.valueOf(start).getTime();

        LocalDateTime end = LocalDateTime.now().minusDays(begin-between);
        System.out.println(end.format(DateTimeFormatter.ISO_DATE_TIME));
        long s = end.toInstant(ZoneOffset.UTC).toEpochMilli();



        CoinPriceHistoryRequest request = new CoinPriceHistoryRequest("BTC-USDT", "1D", ""+s , "" + e, "100");
        String content = request.execute();




        List<CoinHistoryDetail> list = new ArrayList<>();
        JSONArray array = JSON.parseObject(content).getJSONArray("data");
        for (Object o : array) {
            JSONArray innerArray = JSON.parseArray(o.toString());
            CoinPriceHistoryResult item = new CoinPriceHistoryResult();
            item.setTs(innerArray.get(0).toString());
            item.setO(innerArray.get(1).toString());
            item.setH(innerArray.get(2).toString());
            item.setL(innerArray.get(3).toString());
            item.setC(innerArray.get(4).toString());
            item.setVol(innerArray.get(5).toString());
            item.setVolCcy(innerArray.get(6).toString());
            item.setConfirm(innerArray.get(8).toString());
            CoinHistoryDetail detailItem = item.getCoinHistoryDetail("");
            list.add(detailItem);
        }

       list.stream().map(x->x.getTime()).forEach(System.out::println);


    }

}
