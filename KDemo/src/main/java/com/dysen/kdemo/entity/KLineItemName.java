package com.dysen.kdemo.entity;

public class KLineItemName {
    private String name="";
    private int klineType=1;
    private int timeInterval=0;

    public KLineItemName(){

    }
    public KLineItemName(int klineType, String name, int timeInterval){
        this.klineType=klineType;
        this.name=name;
        this.timeInterval=timeInterval;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getKlineType() {
        return klineType;
    }

    public void setKlineType(int klineType) {
        this.klineType = klineType;
    }

    public int getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(int timeInterval) {
        this.timeInterval = timeInterval;
    }

    @Override
    public String toString() {
        return "KLineItemName{" +
                "name='" + name + '\'' +
                ", klineType=" + klineType +
                ", timeInterval=" + timeInterval +
                '}';
    }
}
