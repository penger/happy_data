package com.gp.quantificat.util;

/**
 * @author gongpeng
 * @date 2021/9/22 15:39
 */
public class QuantificatUtil {



    public static void main(String[] args) {

        double currency=1000;
        int coinPrice = 100 ;
        int startDeal = 8;
        int freight =50;
        int multiple = 5;
        int stopProfitPrecent=8;
        int stopLossPrecent=15;

        //购买价格为：
        double buyAt = coinPrice*(100-startDeal)*0.01;
        //杠杆资金为：
        double marginCurrency = currency*0.01*(freight)*multiple ;
        //购买的数量为：
        double count = marginCurrency*0.01*freight/buyAt ;

        System.out.printf("下跌超过：%d 进入操作程序 \n",startDeal);
        System.out.printf("购买价格为：%.2f ,本金为 %.2f ,购买币数量为 %.3f \n",buyAt,currency,count);

        //止损
        //能够承受的最大损失为：
        double maxLoss = currency*0.01*stopLossPrecent;
        //止损本金余额为：
        double afterLossCurrency = currency - maxLoss;
        //此时该币的价格[止损价格]为：
        double atLossCoinPrice  = (marginCurrency-maxLoss)*buyAt/marginCurrency;
        //此时该币相对最初的跌幅为：
        double atLossPrecent = 100 - ((coinPrice - atLossCoinPrice)*100/coinPrice);

        System.out.printf("承受最大损失为 %.2f, 止损后本金为 %.2f 止损价格为 %.2f 当日跌幅为%.2f \n"
                ,maxLoss,afterLossCurrency,atLossCoinPrice,atLossPrecent);


        //止盈
        double maxProfit = currency*0.01*stopProfitPrecent;
        //止盈本金余额
        double afterProfitCurrency = currency+ maxProfit;
        //止盈时候的价格：
        double atProfitCoinPrice = (marginCurrency+maxProfit)*buyAt/marginCurrency;
        //止盈的时候该币的跌幅
        double stopProfileCoinPrice =100 - ((coinPrice-atProfitCoinPrice)*100/coinPrice);


        System.out.printf("获得最大的盈利为 %.2f, 止盈后本金为 %.2f 止赢价格为 %.2f 当日跌幅为%.2f \n",maxProfit,afterProfitCurrency,atProfitCoinPrice,stopProfileCoinPrice);
        //

    }


    /*
        当前币价
        开始交易的跌幅
        仓位 10 20 50 100
        杠杆倍数
        止盈
        止损
     */
    public static double calc(double currency, double maxDiePrecent,double truePrecent, double coinPrice,int startDeal, int freight ,int multiple,int stopProfitPrecent, int stopLossPrecent ){

        //购买价格为：
        double buyAt = coinPrice*(100-startDeal)*0.01;
        //杠杆资金为：
        double marginCurrency = currency*0.01*(freight)*multiple ;
        //购买的数量为：
        double count = marginCurrency*0.01*freight/buyAt ;

        if(maxDiePrecent < -startDeal) {
            System.out.printf("下跌超过：%d 进入操作程序 \n", startDeal);
            System.out.printf("购买价格为：%.2f ,本金为 %.2f ,购买币数量为 %.3f \n", buyAt, currency, count);


            //止损
            //能够承受的最大损失为：
            double maxLoss = currency * 0.01 * stopLossPrecent;
            //止损本金余额为：
            double afterLossCurrency = currency - maxLoss;
            //此时该币的价格[止损价格]为：
            double atLossCoinPrice = (marginCurrency - maxLoss) * buyAt / marginCurrency;
            //此时该币相对最初的跌幅为：
            double atLossPrecent = (coinPrice - atLossCoinPrice) * 100 / coinPrice;


            //止盈
            double maxProfit = currency * 0.01 * stopProfitPrecent;
            //止盈本金余额
            double afterProfitCurrency = currency + maxProfit;
            //止盈时候的价格：
            double atProfitCoinPrice = (marginCurrency + maxProfit) * buyAt / marginCurrency;
            //止盈的时候该币的跌幅
            double atProfileCoinPrice = (coinPrice - atProfitCoinPrice) * 100 / coinPrice;

            if(-atLossPrecent > maxDiePrecent) {
                System.out.printf("承受最大损失为 %.2f, 止损后本金为 %.2f 止损价格为 %.2f 当日跌幅为%.2f 时候成交 \n", maxLoss, afterLossCurrency, atLossCoinPrice, atLossPrecent);
                return afterLossCurrency;
            }else if(-atProfileCoinPrice < truePrecent) {
                System.out.printf("获得最大的盈利为 %.2f, 止盈后本金为 %.2f 止赢价格为 %.2f 当日跌幅为%.2f 时候成交 \n", maxProfit, afterProfitCurrency, atProfitCoinPrice, atProfileCoinPrice);
                return afterProfitCurrency;
            }else{
                System.out.println("当天未达到止损止盈条件，待完善策略！");
                return currency;
            }
        }else{
            System.out.printf("当天最大跌幅为 %.2f 未达到交易条件",maxDiePrecent);
            return currency;
        }

    }




}
