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
public class GetPersonalWealthRequest extends AbstractRequest {

    //币种，多个用半角逗号分割，如BTC | BTC,ETH
    private String ccy;

    @Override
    public void setUrl() {
        if(StringUtils.isEmpty(ccy)) {
            this.url = "api/v5/account/balance";
        }else{
            this.url= "api/v5/account/balance"+"?"+ccy;
        }
    }

    public static void main(String[] args) throws IOException {
        GetPersonalWealthRequest request = new GetPersonalWealthRequest();
        String json = request.execute();
        System.out.println(json);
    }

}
