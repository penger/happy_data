package com.gp.quantificat.domain.coin.requset;

import com.gp.quantificat.domain.coin.AbstractRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubAccountRequest extends AbstractRequest {

    //币种，多个用半角逗号分割，如BTC | BTC,ETH
    private String ccy;

    @Override
    public void setUrl() {
//        this.url="api/v5/account/balance";
        this.url="api/v5/account/account-position-risk";
//        this.url = "api/v5/account/positions?instId=BTC-USDT&instType=MARGIN";
    }

    public static void main(String[] args) throws IOException {
        SubAccountRequest request = new SubAccountRequest();
        String json = request.execute();
        System.out.println(json);
    }

}
