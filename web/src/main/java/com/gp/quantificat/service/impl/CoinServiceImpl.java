package com.gp.quantificat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gp.quantificat.domain.Coin;
import com.gp.quantificat.service.CoinService;
import com.gp.quantificat.mapper.CoinMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
* @author gongpeng
* @description 针对表【coin】的数据库操作Service实现
* @createDate 2023-02-08 10:44:22
*/
@Service
public class CoinServiceImpl extends ServiceImpl<CoinMapper, Coin>
    implements CoinService{


    @Override
    public List<String> getChosenCoin() {
        List<Coin> coins = baseMapper.selectAllByIsChosenOrderByChosenRankDesc(true);
        return coins.stream().map(x->x.getCoinName()).collect(Collectors.toList());
    }

    @Override
    public List<String> getFavriteCoin() {
        List<Coin> coins = baseMapper.selectAllByIsFavoriteOrderByFavoriteRank(true);
        return coins.stream().map(x->x.getCoinName()).collect(Collectors.toList());
    }

    @Override
    public List<String> getAllCoin() {
        List<Coin> coins = baseMapper.selectAll();
        return coins.stream().map(x->x.getCoinName()).collect(Collectors.toList());
    }
}




