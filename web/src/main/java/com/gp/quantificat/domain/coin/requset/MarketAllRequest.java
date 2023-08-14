package com.gp.quantificat.domain.coin.requset;

import com.gp.quantificat.domain.coin.AbstractRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MarketAllRequest extends AbstractRequest {

    //币种，多个用半角逗号分割，如 BTC-USD-SWAP
    private String instType;
    private String instId;

    @Override
    public void setUrl() {
        if(StringUtils.isEmpty(instId)) {
            this.url = "api/v5/market/tickers?instType="+instType;
        }else{
            this.url = "api/v5/market/tickers?instType="+instType+"&instId="+instId;
        }
    }

    public static void main(String[] args) throws IOException {
        MarketAllRequest request = new MarketAllRequest();
        request.setInstType("SPOT");
        String json = request.execute();
        System.out.println(json);
    }

}
