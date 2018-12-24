package com.dysen.kdemo.entity;
/**
 * xiezuofei
 * 2016-08-19 17:50
 * 793169940@qq.com
 * 盘口信息
 */
public class MarketDepthData {
    private String id="";
    private String number="";
    private String price="";
    public MarketDepthData(){

    }
    public MarketDepthData(String id, String price, String number){
        this.id=id;
        this.number=number;
        this.price=price;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
    @Override
    public String toString() {
        return "MarketDepthData{" +
                "id='" + id + '\'' +
                ", number='" + number + '\'' +
                ", price='" + price + '\'' +
                '}';
    }
}
