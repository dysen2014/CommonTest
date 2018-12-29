package com.dysen.kdemo.entity;

import com.dysen.kdemo.utils.SystemConfig;

import java.math.BigDecimal;

public class TickerData {
    private String symbol="";
    private String buy = "";                   // 买一价（人民币）
    private String buydollar = "";               // 买一价（美元）
    private String date = "";                    // 时间戳
    private String dollar = "";                  // 最新成交价（美元）
    private String high = "";                  // 最高价（人民币）
    private String highdollar = "";              // 最高价（美元）
    private String last = "";                // 最新成交价
    private String lastRmb = "";                // 最新成交价（人民币）
    private String low = "";                     // 最低价（人民币）
    private String lowdollar = "";             // 最低价（美元）
    private String sell = "";                   // 卖一价（人民币）
    private String selldollar = "";              // 卖一价（美元）
    private String vol = "";                     // 成交量
    private String riseRate = "";

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }


    public String getBuy() {
        return buy;
    }

    public void setBuy(String buy) {
        this.buy = buy;
    }

    public String getBuydollar() {
        return buydollar;
    }

    public void setBuydollar(String buydollar) {
        this.buydollar = buydollar;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDollar() {
        if (dollar==null||"".equals(dollar)){
            dollar="0.00";
        }
        return dollar;
    }

    public void setDollar(String dollar) {
        this.dollar = dollar;
    }

    public String getHigh() {
        if (high==null||"".equals(high)){
            high="0.00";
        }
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getHighdollar() {
        return highdollar;
    }

    public void setHighdollar(String highdollar) {
        this.highdollar = highdollar;
    }

    public String getLast() {
        if (last==null||"".equals(last)){
            last="0.00";
        }
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public String getLow() {
        if (low==null||"".equals(low)){
            low="0.00";
        }
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getLowdollar() {
        return lowdollar;
    }

    public void setLowdollar(String lowdollar) {
        this.lowdollar = lowdollar;
    }

    public String getSell() {
        return sell;
    }

    public void setSell(String sell) {
        this.sell = sell;
    }

    public String getSelldollar() {
        return selldollar;
    }

    public void setSelldollar(String selldollar) {
        this.selldollar = selldollar;
    }

    public String getVol() {
        return vol;
    }

    public double getTurnover() {
        return new BigDecimal(vol).multiply(new BigDecimal(last)).doubleValue();
    }

    public String getTurnoverString() {
        return SystemConfig.deFormat(String.valueOf(getTurnover()), 2);
    }

    public void setVol(String vol) {
        this.vol = vol;
    }

    public String getRiseRate() {
        if (riseRate==null||"".equals(riseRate)){
            riseRate="0.00";
        }
        return riseRate;
    }

    public void setRiseRate(String riseRate) {
        this.riseRate = riseRate;
    }

    public String getLastRmb() {
        return lastRmb;
    }

    public void setLastRmb(String lastRmb) {
        this.lastRmb = lastRmb;
    }

    @Override
    public String toString() {
        return "TickerData{" +
                "symbol='" + symbol + '\'' +
                ", buy='" + buy + '\'' +
                ", buydollar='" + buydollar + '\'' +
                ", date='" + date + '\'' +
                ", dollar='" + dollar + '\'' +
                ", high='" + high + '\'' +
                ", highdollar='" + highdollar + '\'' +
                ", last='" + last + '\'' +
                ", lastRmb='" + lastRmb + '\'' +
                ", low='" + low + '\'' +
                ", lowdollar='" + lowdollar + '\'' +
                ", sell='" + sell + '\'' +
                ", selldollar='" + selldollar + '\'' +
                ", vol='" + vol + '\'' +
                ", riseRate='" + riseRate + '\'' +
                '}';
    }
}
