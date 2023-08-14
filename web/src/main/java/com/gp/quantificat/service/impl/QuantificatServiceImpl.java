package com.gp.quantificat.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gp.quantificat.util.QuantificatUtil;
import com.gp.quantificat.domain.*;
import com.gp.quantificat.domain.coin.requset.CoinPriceHistoryRequest;
import com.gp.quantificat.domain.coin.requset.ExchangeRateRequest;
import com.gp.quantificat.domain.coin.requset.GetPersonalWealthRequest;
import com.gp.quantificat.domain.coin.requset.MarketAllRequest;
import com.gp.quantificat.domain.coin.response.CoinItem;
import com.gp.quantificat.domain.coin.response.GetPersonalWealthResponse;
import com.gp.quantificat.domain.coin.result.CoinPriceHistoryResult;
import com.gp.quantificat.domain.coin.result.GetPersonalWealthResult;
import com.gp.quantificat.domain.coin.result.MarketAllResult;
import com.gp.quantificat.util.DateUtil;
import com.gp.quantificat.util.Utils;
import com.gp.quantificat.util.WeixinUtils;
import com.gp.quantificat.util.ZDFPHUtils;
import com.gp.quantificat.mapper.*;
import com.gp.quantificat.service.CoinHistoryDetailService;
import com.gp.quantificat.service.CoinService;
import com.gp.quantificat.service.QuantificatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author gongpeng
 * @date 2021/4/26 15:20
 */

@Service
@Transactional
public class QuantificatServiceImpl implements QuantificatService {

    @Autowired
    CoinHistoryDetailService coinHistoryDetailService;

    @Autowired
    CoinService coinService;

    @Autowired
    PrivateWealthMapper privateWealthMapper;

    @Autowired
    CoinTriggerMapper coinTriggerMapper;

    @Autowired
    MyTriggerMapper myTriggerMapper;


    @Override
    public PrivateWealth getCurrentPrivateWealth(){

        return privateWealthMapper.selectOneByDate(DateUtil.getLastNDays(0));
    }

    @Override
    public List<PrivateWealth> getListOfPrivateWealth(){
        List<PrivateWealth> privateWealths = privateWealthMapper.selectAllOrderByDateDescLimit10();
        return privateWealths;
    }

    @Override
    public PrivateWealth getTodayWealth(){
        List<PrivateWealth> list = getListOfPrivateWealth();
        return list.get(0);
    }


    //对总价和list 做汇率转换
    @Override
    public GetPersonalWealthResponse getPersonalWealth() {
        Double exchangeRate = getExchangeRate();
        String content = new GetPersonalWealthRequest().execute();
        JSONObject data = JSON.parseObject(content).getJSONArray("data").getJSONObject(0);
        GetPersonalWealthResult personalWeathResult = JSON.parseObject(data.toJSONString(), GetPersonalWealthResult.class);
        GetPersonalWealthResponse getPersonalWealthResponse = personalWeathResult.toGetPersonalWealthResponse();
        //汇率转换
        Double wealth = getPersonalWealthResponse.getWealth();
        getPersonalWealthResponse.setWealth(wealth*exchangeRate);
        List<CoinItem> coinItemList = getPersonalWealthResponse.getCoinItemList();
        for (CoinItem coinItem : coinItemList) {
            String coinName = coinItem.getCoinName();
            if("USDT".equals(coinName)){
                coinItem.setAvailable(coinItem.getAvailable()*exchangeRate);
                coinItem.setUsed(coinItem.getUsed()*exchangeRate);
                coinItem.setEarn(coinItem.getEarn()*exchangeRate);
                coinItem.setTotal(coinItem.getTotal()*exchangeRate);
            }
            coinItem.setRealCost(wealth*coinItem.getMultiple()*exchangeRate);
        }
        return getPersonalWealthResponse;
    }
    @Override
    public List<MarketAllResult> getMarketAll()  {
        List<MarketAllResult> list = new ArrayList<>();
        String content = new MarketAllRequest("SPOT", "").execute();
        JSONArray array = JSON.parseObject(content).getJSONArray("data");
        for (Object o : array) {
            MarketAllResult item = JSONObject.parseObject(o.toString(), MarketAllResult.class);
            Double vol24 = Double.valueOf(item.getVolCcy24h());
            //USDT交易并且大于 一万刀
            if(item.getInstId().endsWith("-USDT") && vol24 > 10000){
                Instant instant = Instant.ofEpochMilli(Long.parseLong(item.getTs()));
                String time = Utils.convertTsToDateTime(item.getTs());
                item.setTs(time);
                list.add(item);
            }
        }
        return list;
    }
    @Override
    public Double getExchangeRate(){
        String content = new ExchangeRateRequest().execute();
        //请求太频繁会导致 出错
        if(content.contains("msg")){
            return Double.parseDouble("6.908");
        }
        JSONArray array = JSON.parseObject(content).getJSONArray("data");
        return JSON.parseObject(array.get(0).toString()).getDouble("usdCny");
    }

    @Override
    public List<CoinHistoryDetail> getCoinHistory(CoinPriceHistoryRequest coinPriceHistoryRequest ) {
        List<CoinHistoryDetail> list = new ArrayList<>();
        String content = coinPriceHistoryRequest.execute();
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
            CoinHistoryDetail detailItem = item.getCoinHistoryDetail(coinPriceHistoryRequest.getInstId());
            list.add(detailItem);
        }
        return  list;
    }

    //获取当前的对应币种的价格,获取的是最后一次交易的价格
    @Override
    public List<CoinPrice> getCoinPrice(List<String> coinNames)  {
        List<CoinPrice> coinPriceList = new LinkedList<>();
        List<MarketAllResult> market = getMarketAll();
        Map<String ,MarketAllResult> all = new HashMap<>();
        for (MarketAllResult item : market) {
            all.put(item.getInstId(),item);
        }
        for (String coinName : coinNames) {
            MarketAllResult tempItem = all.get(coinName);
            if(tempItem != null ) {
                CoinPrice coinPrice = new CoinPrice();
                coinPrice.setCoinName(coinName);
                coinPrice.setCurrentPrice(BigDecimal.valueOf(Double.parseDouble(tempItem.getLast())));
                coinPriceList.add(coinPrice);
            }else{
                System.out.println("未找到对应的 "+ coinName);
            }
        }
        return coinPriceList;
    }

    @Override
    public void getExactlyCoinPriceAndInsertHistory(String  coinName,Integer begin, Integer size){

        LocalDateTime start = LocalDateTime.now().minusDays(begin);
        System.out.println(start.format(DateTimeFormatter.ISO_DATE_TIME));
        String e = ""+ Timestamp.valueOf(start).getTime();
        LocalDateTime end = LocalDateTime.now().minusDays(begin-size);
        System.out.println(end.format(DateTimeFormatter.ISO_DATE_TIME));
        long s = end.toInstant(ZoneOffset.UTC).toEpochMilli();
        CoinPriceHistoryRequest request = new CoinPriceHistoryRequest(coinName, "1D", ""+s , "" + e, ""+size);
        List<CoinHistoryDetail> details = getCoinHistory(request);
        //清理掉之前的，保留现在的
        for (CoinHistoryDetail detail : details) {
            //删除之前的
            coinHistoryDetailService.deleteCoinHistoryDetailByNameAndTimeExactly(detail.getName(),detail.getTime());
            //保留今天的
            coinHistoryDetailService.save(detail);

//            coinHistoryDetailService.sav
        }

    }



    //获取历史
    @Override
    public Map<String,Integer> getCoinPriceHistory(List<String> coinNames,int days){
        Map<String,Integer> map = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,(-(days - 1)));
        String minDate = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
        List<CoinTrigger> coinTriggers = new ArrayList<>();
        List<String> messages = new ArrayList<>();
        if(days == 1 ){
            coinTriggers = coinTriggerMapper.selectAll();
        }
        for (String coinName : coinNames) {
            try {
                Thread.sleep(1000);
                System.out.println("休眠一秒");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            //按照coinName进行删除
            coinHistoryDetailService.deleteCoinHistoryDetailByNameAndTimeAfter(coinName, minDate);
            long start = System.currentTimeMillis();
            List<CoinHistoryDetail> coinHistoryDetailList = getCoinHistoryDetailFromCoinHistory(coinName,days);
            if(days ==1 && !coinTriggers.isEmpty()){
                triggerCoin(coinHistoryDetailList, coinTriggers,messages);
            }
            long end1 = System.currentTimeMillis();
            coinHistoryDetailService.saveBatch(coinHistoryDetailList);
            long end = System.currentTimeMillis();
            System.out.println("time is :"+(end1-start) + "----------"+ (end-end1));
        }
        StringBuffer sb = new StringBuffer();
        messages.forEach(sb::append);
//        WeixinUtils.sendMessage(sb.toString());
        return map;
    }



    //触发告警（发送数据到 短信）
    @Override
    public void triggerCoin(List<CoinHistoryDetail> coinHistoryDetailList , List<CoinTrigger> triggers,List<String> messageList){
        for (CoinHistoryDetail coinHistoryDetail : coinHistoryDetailList) {
            BigDecimal close = coinHistoryDetail.getClose();
            for (CoinTrigger coinTrigger : triggers) {
                if(coinTrigger.getCoinName().equalsIgnoreCase(coinHistoryDetail.getName())){
                    if(close.compareTo(coinTrigger.getCoinPrice())>0){
                        if(coinTrigger.getType().equalsIgnoreCase("up")){
                            coinTrigger.setType("down");
                            coinTriggerMapper.insert(coinTrigger);
                            String message = coinTrigger.getCoinName()+"当前价格为："+coinHistoryDetail.getClose()+"上涨超过"+coinTrigger.getCoinPrice();
                            messageList.add(message);
                        }
                    }else{
                        if(coinTrigger.getType().equalsIgnoreCase("down")){
                            coinTrigger.setType("up");
                            coinTriggerMapper.insert(coinTrigger);
                            String message = coinTrigger.getCoinName()+"当前价格为："+coinHistoryDetail.getClose()+"下跌超过"+coinTrigger.getCoinPrice();
                            messageList.add(message);
                        }
                    }
                }
            }
        }
    }


    //获取过去多少天的
    @Override
    public Map<String,List<CoinHistoryDetail>> getTodayPrecent(List<String> coinNames,Integer days){
        //当天的价格
        Map<String,List<CoinHistoryDetail>> resultMap = new HashMap<>();
        List<CoinPrice> coinPriceList = getCoinPrice(coinNames);
        for (String coinName : coinNames) {
            List<CoinHistoryDetail> coinHistoryDetailList = coinHistoryDetailService.getAllByNameAndTimeLimit(coinName, days);
            for (CoinPrice coinPrice : coinPriceList) {
                CoinHistoryDetail coinHistoryDetail = coinHistoryDetailList.get(0);
                BigDecimal close = coinHistoryDetail.getClose();
                if(coinPrice.getCoinName().equalsIgnoreCase(coinName)){
                    CoinHistoryDetail todayCoinDetail = new CoinHistoryDetail();
                    String todayDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                    todayCoinDetail.setTime(todayDate);
                    todayCoinDetail.setPrecent(getPercent(coinPrice.getCurrentPrice(),close));
                    coinHistoryDetailList.add(todayCoinDetail);
                }
            }
            resultMap.put(coinName,coinHistoryDetailList);
        }
        return  resultMap;
    }




    private List<CoinHistoryDetail> getCoinHistoryDetailFromCoinHistory(String coinName,Integer days){
        System.out.println("获取 "+coinName+" "+days+"天前的价格数据");
        String dayStr = days+"";
        List<CoinHistoryDetail> list = getCoinHistory(new CoinPriceHistoryRequest(coinName, "1D", "", "", "" + dayStr));
        return list;

    }

    // return (a-b)/b
    private Double getPercent(BigDecimal a, BigDecimal b){
        BigDecimal temp = a.subtract(b);
        BigDecimal result = temp.multiply(BigDecimal.valueOf(100L)).divide(b,2,BigDecimal.ROUND_HALF_DOWN);
        return result.doubleValue();
    }


    //凌晨写入  前天和当前比较 写入昨天
    //白天写入  昨天和当前比较 写入今天
    @Override
    public void insertTargetPrivateWealth(){
        GetPersonalWealthResponse personalWealth = getPersonalWealth();
        //最后的两个
        List<PrivateWealth> list = privateWealthMapper.selectAllOrderByDateDesc();
        //数据库中最新的
        String lastDate = list.get(0).getDate();
        String today = DateUtil.getLastNDays(0);
        PrivateWealth one = null;
        if(today.equals(lastDate)){
            one = list.get(1);
        }else{
            one= list.get(0);
        }
        BigDecimal compareClose = null;
        if(one == null){
            compareClose =BigDecimal.valueOf(personalWealth.getWealth());
        }else {
            compareClose = one.getClose();
        }
        //删除
        if(privateWealthMapper.selectOneByDate(today)!=null) {
            privateWealthMapper.deleteById(today);
        }
        //添加
        PrivateWealth privateWealth = new PrivateWealth();
        privateWealth.setOpen(compareClose);
        privateWealth.setDate(today);
        BigDecimal todayClose = BigDecimal.valueOf(personalWealth.getWealth());
        privateWealth.setClose(todayClose);
        privateWealth.setEarn(todayClose.subtract(compareClose));
        privateWealth.setPrecent(BigDecimal.valueOf(getPercent(todayClose,compareClose)));
        privateWealthMapper.insert(privateWealth);
    }


    //获取当前总金额 获取之前的金额
    @Override
    public Map<String,String> getCurrentTotalBalance(){
        GetPersonalWealthResponse personalWealth = getPersonalWealth();
        String yesterday = DateUtil.getLastNDays(1);
        PrivateWealth privateWealth = privateWealthMapper.selectOneByDate(yesterday);
        //昨天的收盘价
        BigDecimal LastDayClose = privateWealth.getClose();
        //Today wealth
        BigDecimal todayCurrency = BigDecimal.valueOf(personalWealth.getWealth());
        int todayEarn = todayCurrency.subtract(LastDayClose).intValue();

        Double percent = getPercent(todayCurrency, LastDayClose);

        String resultCNY = "总余额为："+todayCurrency.intValue()
                + "昨日收盘为："+LastDayClose.intValue()
                + "当日盈亏为："+ todayEarn
                + "当日盈亏比为："+percent ;

        HashMap<String, String> map = new HashMap<>();
        map.put("title",""+todayEarn);
        map.put("balance",resultCNY+"<br/>");
        map.put("wx",resultCNY);

        return map;
    }


    //获取当前总金额 获取之前的金额
    @Override
    public void getCurrentTriggerMessage(){

        String yesterday = DateUtil.getLastNDays(1);
        PrivateWealth privateWealth = privateWealthMapper.selectOneByDate(yesterday);
        //昨天的收盘价
        BigDecimal LastDayClose = privateWealth.getClose();
        GetPersonalWealthResponse personalWealth = getPersonalWealth();
        BigDecimal todayCurrency =BigDecimal.valueOf(personalWealth.getWealth());
        BigDecimal todayEarn = todayCurrency.subtract(LastDayClose);

        StringBuffer message = new StringBuffer();

        List<MyTrigger> triggers = myTriggerMapper.selectAll();
        for (MyTrigger trigger : triggers) {
            if(trigger.getStatus()) {
                if(todayEarn.compareTo(trigger.getEarn())>0 && trigger.getType().equalsIgnoreCase("up")){
                    trigger.setType("down");
                    myTriggerMapper.insert(trigger);
                    if(todayEarn.compareTo(BigDecimal.ZERO)>0){
                        message.append("当前盈利为： "+todayEarn.intValue());
                    }else{
                        message.append("当前亏损为： "+todayEarn.intValue());
                    }
                }else if(todayEarn.compareTo(trigger.getEarn())< 0 && trigger.getType().equalsIgnoreCase("down")){
                    trigger.setType("up");
                    myTriggerMapper.insert(trigger);
                    if(todayEarn.compareTo(BigDecimal.ZERO)>0){
                        message.append("当前盈利为： "+todayEarn.intValue());
                    }else{
                        message.append("当前亏损为： "+todayEarn.intValue());
                    }
                }
            }
        }
        //发送告警信息到微信
        WeixinUtils.sendMessage(message.toString());

    }


    @Override
    public void insertCoins(List<String> favorite_coin) {
        for (int i = 0; i < favorite_coin.size(); i++) {
            Coin coin = new Coin();
            coin.setCoinName(favorite_coin.get(i));
            coin.setIsChosen(true);
            coin.setChosenRank(i+1);
            coin.setIsFavorite(false);
            coin.setFavoriteRank(i+1);
            coinService.save(coin);
        }
    }

    //重置 自定义告警
    @Override
    public void resetMyTrigger() {
        myTriggerMapper.updateTypeByEarn1();
        myTriggerMapper.updateTypeByEarn2();
    }

    //涨跌幅排行
    @Override
    public String getZDFPH() {

        List<String> chosenCoin = coinService.getChosenCoin();

        Map<String,List<BigDecimal>> map = new HashMap<>();
        List<String> dateList = Arrays.asList(
                "今天",
                "半年前",
                "三月前",
                "两月前",
                "一月前",
                "两周前",
                "一周前"
        );
        List<CoinHistoryDetail> coinHistoryDetails = coinHistoryDetailService.getAllByTime(DateUtil.getLastNDays(0));
        List<CoinHistoryDetail> coinHistoryDetails1 = coinHistoryDetailService.getAllByTime(DateUtil.getLastNDays(7));
        List<CoinHistoryDetail> coinHistoryDetails2 = coinHistoryDetailService.getAllByTime(DateUtil.getLastNDays(15));
        List<CoinHistoryDetail> coinHistoryDetails3 = coinHistoryDetailService.getAllByTime(DateUtil.getLastNDays(30));
        List<CoinHistoryDetail> coinHistoryDetails4 = coinHistoryDetailService.getAllByTime(DateUtil.getLastNDays(60));
        List<CoinHistoryDetail> coinHistoryDetails5 = coinHistoryDetailService.getAllByTime(DateUtil.getLastNDays(90));
        List<CoinHistoryDetail> coinHistoryDetails6 = coinHistoryDetailService.getAllByTime(DateUtil.getLastNDays(180));


        for (CoinHistoryDetail coinHistoryDetail : coinHistoryDetails) {
            LinkedList<BigDecimal> list = new LinkedList<>();
            list.add(coinHistoryDetail.getClose());
            list.add(BigDecimal.ZERO);
            list.add(BigDecimal.ZERO);
            list.add(BigDecimal.ZERO);
            list.add(BigDecimal.ZERO);
            list.add(BigDecimal.ZERO);
            list.add(BigDecimal.ZERO);
            map.put(coinHistoryDetail.getName(),list);
        }

        for (CoinHistoryDetail coinHistoryDetail : coinHistoryDetails1) {
            map.get(coinHistoryDetail.getName()).set(6,coinHistoryDetail.getClose());
        }
        for (CoinHistoryDetail coinHistoryDetail : coinHistoryDetails2) {
            map.get(coinHistoryDetail.getName()).set(5,coinHistoryDetail.getClose());
        }
        for (CoinHistoryDetail coinHistoryDetail : coinHistoryDetails3) {
            map.get(coinHistoryDetail.getName()).set(4,coinHistoryDetail.getClose());
        }
        for (CoinHistoryDetail coinHistoryDetail : coinHistoryDetails4) {
            map.get(coinHistoryDetail.getName()).set(3,coinHistoryDetail.getClose());
        }
        for (CoinHistoryDetail coinHistoryDetail : coinHistoryDetails5) {
            map.get(coinHistoryDetail.getName()).set(2,coinHistoryDetail.getClose());
        }
        for (CoinHistoryDetail coinHistoryDetail : coinHistoryDetails6) {
            map.get(coinHistoryDetail.getName()).set(1,coinHistoryDetail.getClose());
        }

        return ZDFPHUtils.zdfph(map, chosenCoin,dateList);
    }


    @Override
    public String recommandPrice() {
        //获取昨日收盘价格
        String lastNDays = DateUtil.getLastNDays(1);
        List<String> chosenCoin = coinService.getChosenCoin();
        List<CoinHistoryDetail> list = coinHistoryDetailService.getAllByTime(lastNDays);
        StringBuffer sb = new StringBuffer();
        for (String coin : chosenCoin) {
            for (CoinHistoryDetail item : list) {
                if(item.getName().equalsIgnoreCase(coin)) {
                    BigDecimal close = item.getClose();
                    double yesterdayPrice = close.setScale(2, RoundingMode.DOWN).doubleValue();
                    Double buy = close.multiply(BigDecimal.valueOf(0.97)).setScale(4, RoundingMode.DOWN).doubleValue();
                    Double sell = close.multiply(BigDecimal.valueOf(1.08)).setScale(1, RoundingMode.DOWN).doubleValue();
                    sb.append("昨日"+coin+" 价格为 "+yesterdayPrice+" 推荐购买为 "+buy+" 推荐卖出为 "+sell+"<br/>") ;
                    break;
                }

            }
        }
        return sb.toString();
    }

    @Override
    public String test(int s ,int p,int l){
        List<CoinHistoryDetail> list = coinHistoryDetailService.queryCoinHistoryDetailByNameOrderBYTime(Arrays.asList("btcusdt"), "2020-01-01");
        Collections.reverse(list);
        double currency = 10000;
        for (CoinHistoryDetail detail : list) {
            String time = detail.getTime();
            currency = QuantificatUtil.calc(currency,detail.getMaxDiePrecent(),detail.getPrecent(),
                    detail.getOpen().doubleValue(),s,100,1,p,l);
        }
        return s+ " " + p + " " + l+ "final is :"+ currency;
    }

    //更新当天的数据
    @Override
    public void updateTodayData() {
        List<MarketAllResult> marketAll = getMarketAll();
        String date = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE);
        //删除当天的数据，写入新的数据
        coinHistoryDetailService.deleteAllByTime(date);
        for (MarketAllResult item : marketAll) {
            CoinHistoryDetail detail = item.getCoinHistoryDetail();
            coinHistoryDetailService.save(detail);
        }

    }

}
