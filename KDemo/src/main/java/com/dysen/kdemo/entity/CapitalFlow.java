package com.dysen.kdemo.entity;

import java.util.List;

/**
 * @package com.vip.zb.entity
 * @email dy.sen@qq.com
 * created by dysen on 2018/9/14 - 上午10:32
 * @info 资金流向模块的 bean处理类
 */
public class CapitalFlow {
    public static class HistoryFunds{

        private List<List<String>> array;

        public List<List<String>> getArray() {
            return array;
        }

        public void setArray(List<List<String>> array) {
            this.array = array;
        }

        @Override
        public String toString() {
            return "HistoryFunds{" +
                    "array=" + array +
                    '}';
        }

        public static class ListBean{
            long time;
            float buy, sell, net;

            public ListBean(long time, float buy, float sell, float net) {
                this.time = time;
                this.buy = buy;
                this.sell = sell;
                this.net = net;
            }

            public long getTime() {
                return time;
            }

            public void setTime(long time) {
                this.time = time;
            }

            public float getBuy() {
                return buy;
            }

            public void setBuy(float buy) {
                this.buy = buy;
            }

            public float getSell() {
                return sell;
            }

            public void setSell(float sell) {
                this.sell = sell;
            }

            public float getNet() {
                return net;
            }

            public void setNet(float net) {
                this.net = net;
            }
        }
    }

    public static class FundDistribute{

        /**
         * obj : {"sellVolume":1850069.3,"middleTransSellVolume":84983.89,"largeTransSellCount":0,"middleTransBuyCount":0,"middleTransSellCount":9,"largeTransBuyCount":0,"middleTransBuyVolume":0,"largeTransSellVolume":0,"buyAmount":327378.057,"sellAmount":10150.598,"smallTransSellCount":14918,"smallTransSellVolume":1765085.41,"smallTransBuyCount":215761,"largeTransBuyVolume":0,"buyVolume":5.966861216E7,"priceCoin":"usdt","smallTransBuyVolume":5.966861216E7}
         */

        private ObjBean obj;

        public ObjBean getObj() {
            return obj;
        }

        public void setObj(ObjBean obj) {
            this.obj = obj;
        }

        public static class ObjBean {
            /**
             * sellVolume : 1850069.3
             * middleTransSellVolume : 84983.89
             * largeTransSellCount : 0
             * middleTransBuyCount : 0
             * middleTransSellCount : 9
             * largeTransBuyCount : 0
             * middleTransBuyVolume : 0
             * largeTransSellVolume : 0
             * buyAmount : 327378.057
             * sellAmount : 10150.598
             * smallTransSellCount : 14918
             * smallTransSellVolume : 1765085.41
             * smallTransBuyCount : 215761
             * largeTransBuyVolume : 0
             * buyVolume : 5.966861216E7
             * priceCoin : usdt
             * smallTransBuyVolume : 5.966861216E7
             */

            private double sellVolume;
            private double middleTransSellVolume;
            private int largeTransSellCount;
            private int middleTransBuyCount;
            private int middleTransSellCount;
            private int largeTransBuyCount;
            private double middleTransBuyVolume;
            private double largeTransSellVolume;
            private double buyAmount;
            private double sellAmount;
            private int smallTransSellCount;
            private double smallTransSellVolume;
            private int smallTransBuyCount;
            private double largeTransBuyVolume;
            private double buyVolume;
            private String priceCoin;
            private double smallTransBuyVolume;

            public double getSellVolume() {
                return sellVolume;
            }

            public void setSellVolume(double sellVolume) {
                this.sellVolume = sellVolume;
            }

            public double getMiddleTransSellVolume() {
                return middleTransSellVolume;
            }

            public void setMiddleTransSellVolume(double middleTransSellVolume) {
                this.middleTransSellVolume = middleTransSellVolume;
            }

            public int getLargeTransSellCount() {
                return largeTransSellCount;
            }

            public void setLargeTransSellCount(int largeTransSellCount) {
                this.largeTransSellCount = largeTransSellCount;
            }

            public int getMiddleTransBuyCount() {
                return middleTransBuyCount;
            }

            public void setMiddleTransBuyCount(int middleTransBuyCount) {
                this.middleTransBuyCount = middleTransBuyCount;
            }

            public int getMiddleTransSellCount() {
                return middleTransSellCount;
            }

            public void setMiddleTransSellCount(int middleTransSellCount) {
                this.middleTransSellCount = middleTransSellCount;
            }

            public int getLargeTransBuyCount() {
                return largeTransBuyCount;
            }

            public void setLargeTransBuyCount(int largeTransBuyCount) {
                this.largeTransBuyCount = largeTransBuyCount;
            }

            public double getMiddleTransBuyVolume() {
                return middleTransBuyVolume;
            }

            public void setMiddleTransBuyVolume(double middleTransBuyVolume) {
                this.middleTransBuyVolume = middleTransBuyVolume;
            }

            public double getLargeTransSellVolume() {
                return largeTransSellVolume;
            }

            public void setLargeTransSellVolume(double largeTransSellVolume) {
                this.largeTransSellVolume = largeTransSellVolume;
            }

            public double getBuyAmount() {
                return buyAmount;
            }

            public void setBuyAmount(double buyAmount) {
                this.buyAmount = buyAmount;
            }

            public double getSellAmount() {
                return sellAmount;
            }

            public void setSellAmount(double sellAmount) {
                this.sellAmount = sellAmount;
            }

            public int getSmallTransSellCount() {
                return smallTransSellCount;
            }

            public void setSmallTransSellCount(int smallTransSellCount) {
                this.smallTransSellCount = smallTransSellCount;
            }

            public double getSmallTransSellVolume() {
                return smallTransSellVolume;
            }

            public void setSmallTransSellVolume(double smallTransSellVolume) {
                this.smallTransSellVolume = smallTransSellVolume;
            }

            public int getSmallTransBuyCount() {
                return smallTransBuyCount;
            }

            public void setSmallTransBuyCount(int smallTransBuyCount) {
                this.smallTransBuyCount = smallTransBuyCount;
            }

            public double getLargeTransBuyVolume() {
                return largeTransBuyVolume;
            }

            public void setLargeTransBuyVolume(double largeTransBuyVolume) {
                this.largeTransBuyVolume = largeTransBuyVolume;
            }

            public double getBuyVolume() {
                return buyVolume;
            }

            public void setBuyVolume(double buyVolume) {
                this.buyVolume = buyVolume;
            }

            public String getPriceCoin() {
                return priceCoin;
            }

            public void setPriceCoin(String priceCoin) {
                this.priceCoin = priceCoin;
            }

            public double getSmallTransBuyVolume() {
                return smallTransBuyVolume;
            }

            public void setSmallTransBuyVolume(double smallTransBuyVolume) {
                this.smallTransBuyVolume = smallTransBuyVolume;
            }
        }
    }

    public static class BigRecord{
        float highAmount;
        private List<List<String>> array;

        public float getHighAmount() {
            return highAmount;
        }

        public void setHighAmount(float highAmount) {
            this.highAmount = highAmount;
        }

        public List<List<String>> getArray() {
            return array;
        }

        public void setArray(List<List<String>> array) {
            this.array = array;
        }

        @Override
        public String toString() {
            return "HistoryFunds{" +
                    "array=" + array +
                    '}';
        }

        public static class ListBean{
            long time;
            int type;
            float quantitative, price, CountPrice;

            public ListBean(long time, int type, Float quantitative, Float price, Float countPrice) {
                this.time = time;
                this.type = type;
                this.quantitative = quantitative;
                this.price = price;
                CountPrice = countPrice;
            }

            public long getTime() {
                return time;
            }

            public void setTime(long time) {
                this.time = time;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public Float getQuantitative() {
                return quantitative;
            }

            public void setQuantitative(Float quantitative) {
                this.quantitative = quantitative;
            }

            public Float getPrice() {
                return price;
            }

            public void setPrice(Float price) {
                this.price = price;
            }

            public Float getCountPrice() {
                return CountPrice;
            }

            public void setCountPrice(Float countPrice) {
                CountPrice = countPrice;
            }

            @Override
            public String toString() {
                return "ListBean{" +
                        "time=" + time +
                        ", type=" + type +
                        ", quantitative=" + quantitative +
                        ", price=" + price +
                        ", CountPrice=" + CountPrice +
                        '}';
            }
        }
    }

    public static class Kline{

        private List<List<String>> array;

        public List<List<String>> getArray() {
            return array;
        }

        public void setArray(List<List<String>> array) {
            this.array = array;
        }
    }

    public static class Netfund{
        private List<List<String>> array;

        public List<List<String>> getArray() {
            return array;
        }

        public void setArray(List<List<String>> array) {
            this.array = array;
        }
    }
}
