package com.dysen.kdemo.entity;

public class TransRecord {

   /*     | tid| long  |     |     |成交记录Id    |
            | date| long     |     |     | 成交时间(时间戳)，单位秒  |
            | price| String     |     |     | 价格    |
            | amount| String  |     |     | 数量    |
            | type| String  |     |     | 交易类型，buy(买)/sell(卖)    |
            | trade_type| String  |     |     | 委托类型，ask(卖)/bid(买)  |*/
    private double tid;
    private double date;
    private String price;
    private String amount;
    private String type;
    private String trade_type;

    public TransRecord(double tid, double date, String price, String amount, String type, String trade_type) {
        this.tid = tid;
        this.date = date;
        this.price = price;
        this.amount = amount;
        this.type = type;
        this.trade_type = trade_type;
    }

    public double getTid() {
        return tid;
    }

    public void setTid(double tid) {
        this.tid = tid;
    }

    public double getDate() {
        return date;
    }

    public void setDate(double date) {
        this.date = date;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }

    @Override
    public String toString() {
        return "TransRecord{" +
                "tid=" + tid +
                ", date=" + date +
                ", price='" + price + '\'' +
                ", amount='" + amount + '\'' +
                ", type='" + type + '\'' +
                ", trade_type='" + trade_type + '\'' +
                '}';
    }
}
