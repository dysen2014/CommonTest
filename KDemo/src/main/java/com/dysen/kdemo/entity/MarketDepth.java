package com.dysen.kdemo.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * xiezuofei
 * 2016-08-02 18:20
 * 793169940@qq.com
 */
public class MarketDepth {
    //当前价格
    private String currentPrice;
    //折合人民币汇率
    private String rate;
    //卖方深度
    private List<List<String>> asks= new ArrayList<>();
    //买方深度
    private List<List<String>> bids= new ArrayList<>();

    public String getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(String currentPrice) {
        this.currentPrice = currentPrice;
    }

    public List<List<String>> getAsks() {
        return asks;
    }

    public void setAsks(List<List<String>> asks) {
        this.asks = asks;
    }

    public List<List<String>> getBids() {
        return bids;
    }

    public void setBids(List<List<String>> bids) {
        this.bids = bids;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("currentPrice=" + currentPrice);
        sb.append("rate=" + rate);
        if (null != asks) {
            sb.append(" asks:" + asks.toString());
        }
        if (null != bids) {
            sb.append(" bids:" + bids.toString());
        }
        return sb.toString();
    }
}
