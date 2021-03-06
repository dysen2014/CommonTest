package com.dysen.kdemo.views;

import java.text.SimpleDateFormat;

/**
 * Created by maizhiquan on 15/12/24.
 */
public class MarketChartData {

    String openId = "";
    String closeId = "";
    long time = 0;
    double openPrice = 0;
    double closePrice = 0;
    double lowPrice = 0;
    double highPrice = 0;
    double vol = 0;

    public MarketChartData() {

    }
    public MarketChartData(String[] data) {
        setTime(Long.parseLong(data[0]));
        setOpenPrice(Double.parseDouble(data[3]));
        setClosePrice(Double.parseDouble(data[4]));
        setHighPrice(Double.parseDouble(data[5]));
        setLowPrice(Double.parseDouble(data[6]));
        setVol(Double.parseDouble(data[7]));
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getCloseId() {
        return closeId;
    }

    public void setCloseId(String closeId) {
        this.closeId = closeId;
    }

    public long getTime() {
        return time;
    }

    public String getTime2() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(time*1000);
    }
    public String getTime3() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(time*1000);
    }
    public String getTime4() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
        return sdf.format(time*1000);
    }

    public String getTime5() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(this.time * 1000L);
    }
    public void setTime(long time) {
        this.time = time;
    }

    public double getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(double openPrice) {
        this.openPrice = openPrice;
    }

    public double getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(double closePrice) {
        this.closePrice = closePrice;
    }

    public double getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(double lowPrice) {
        this.lowPrice = lowPrice;
    }

    public double getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(double highPrice) {
        this.highPrice = highPrice;
    }

    public double getVol() {
        return vol;
    }

    public void setVol(double vol) {
        this.vol = vol;
    }
}
