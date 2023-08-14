package com.gp.quantificat.service;

import com.gp.quantificat.domain.Coin;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


/**
 * 针对表【coin】的数据库操作Service
* @author gongpeng
* @date 2023-02-08 10:44:22
*/
public interface CoinService extends IService<Coin> {
    List<String> getChosenCoin();
    List<String> getFavriteCoin();

    List<String> getAllCoin();
}
