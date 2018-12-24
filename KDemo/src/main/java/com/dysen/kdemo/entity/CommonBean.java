package com.dysen.kdemo.entity;

import org.json.JSONArray;

import java.util.Arrays;
import java.util.List;

/**
 * @package com.dysen.kdemo.entity
 * @email dy.sen@qq.com
 * created by dysen on 2018/12/20 - 10:54 AM
 * @info
 */
public class CommonBean {
    public static class BtcQc {

        /**
         * date : 1545201456209
         * symbol : zbbtcqc
         * ticker : {"lastRmb":"25510.73000000","highdollar":"3742.69850000","buyRmb":"25510.73000000","buydollar":"3706.76960000","last":"25510.73000000","sell":"25510.76000000","buy":"25510.73000000","lowdollar":"3443.81150000","lowRmb":"23701.00000000","riseRate":"5.73","dollar":"3706.76960000","vol":"16167.5376","high":"25758.00000000","low":"23701.00000000","highRmb":"25758.00000000","selldollar":"3706.77390000","sellRmb":"25510.76000000"}
         * cName : 中币
         * moneyType : 1
         * exeByRate : 1
         * name : zb
         * coinName : BTC
         * type : 100
         */

        private String date;
        private String symbol;
        private TickerBean ticker;
        private String cName;
        private int moneyType;
        private int exeByRate;
        private String name;
        private String coinName;
        private int type;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public TickerBean getTicker() {
            return ticker;
        }

        public void setTicker(TickerBean ticker) {
            this.ticker = ticker;
        }

        public String getCName() {
            return cName;
        }

        public void setCName(String cName) {
            this.cName = cName;
        }

        public int getMoneyType() {
            return moneyType;
        }

        public void setMoneyType(int moneyType) {
            this.moneyType = moneyType;
        }

        public int getExeByRate() {
            return exeByRate;
        }

        public void setExeByRate(int exeByRate) {
            this.exeByRate = exeByRate;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCoinName() {
            return coinName;
        }

        public void setCoinName(String coinName) {
            this.coinName = coinName;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public static class TickerBean {
            /**
             * lastRmb : 25510.73000000
             * highdollar : 3742.69850000
             * buyRmb : 25510.73000000
             * buydollar : 3706.76960000
             * last : 25510.73000000
             * sell : 25510.76000000
             * buy : 25510.73000000
             * lowdollar : 3443.81150000
             * lowRmb : 23701.00000000
             * riseRate : 5.73
             * dollar : 3706.76960000
             * vol : 16167.5376
             * high : 25758.00000000
             * low : 23701.00000000
             * highRmb : 25758.00000000
             * selldollar : 3706.77390000
             * sellRmb : 25510.76000000
             */

            private String lastRmb;
            private String highdollar;
            private String buyRmb;
            private String buydollar;
            private String last;
            private String sell;
            private String buy;
            private String lowdollar;
            private String lowRmb;
            private String riseRate;
            private String dollar;
            private String vol;
            private String high;
            private String low;
            private String highRmb;
            private String selldollar;
            private String sellRmb;

            public String getLastRmb() {
                return lastRmb;
            }

            public void setLastRmb(String lastRmb) {
                this.lastRmb = lastRmb;
            }

            public String getHighdollar() {
                return highdollar;
            }

            public void setHighdollar(String highdollar) {
                this.highdollar = highdollar;
            }

            public String getBuyRmb() {
                return buyRmb;
            }

            public void setBuyRmb(String buyRmb) {
                this.buyRmb = buyRmb;
            }

            public String getBuydollar() {
                return buydollar;
            }

            public void setBuydollar(String buydollar) {
                this.buydollar = buydollar;
            }

            public String getLast() {
                return last;
            }

            public void setLast(String last) {
                this.last = last;
            }

            public String getSell() {
                return sell;
            }

            public void setSell(String sell) {
                this.sell = sell;
            }

            public String getBuy() {
                return buy;
            }

            public void setBuy(String buy) {
                this.buy = buy;
            }

            public String getLowdollar() {
                return lowdollar;
            }

            public void setLowdollar(String lowdollar) {
                this.lowdollar = lowdollar;
            }

            public String getLowRmb() {
                return lowRmb;
            }

            public void setLowRmb(String lowRmb) {
                this.lowRmb = lowRmb;
            }

            public String getRiseRate() {
                return riseRate;
            }

            public void setRiseRate(String riseRate) {
                this.riseRate = riseRate;
            }

            public String getDollar() {
                return dollar;
            }

            public void setDollar(String dollar) {
                this.dollar = dollar;
            }

            public String getVol() {
                return vol;
            }

            public void setVol(String vol) {
                this.vol = vol;
            }

            public String getHigh() {
                return high;
            }

            public void setHigh(String high) {
                this.high = high;
            }

            public String getLow() {
                return low;
            }

            public void setLow(String low) {
                this.low = low;
            }

            public String getHighRmb() {
                return highRmb;
            }

            public void setHighRmb(String highRmb) {
                this.highRmb = highRmb;
            }

            public String getSelldollar() {
                return selldollar;
            }

            public void setSelldollar(String selldollar) {
                this.selldollar = selldollar;
            }

            public String getSellRmb() {
                return sellRmb;
            }

            public void setSellRmb(String sellRmb) {
                this.sellRmb = sellRmb;
            }

            @Override
            public String toString() {
                return "TickerBean{" +
                        "lastRmb='" + lastRmb + '\'' +
                        ", highdollar='" + highdollar + '\'' +
                        ", buyRmb='" + buyRmb + '\'' +
                        ", buydollar='" + buydollar + '\'' +
                        ", last='" + last + '\'' +
                        ", sell='" + sell + '\'' +
                        ", buy='" + buy + '\'' +
                        ", lowdollar='" + lowdollar + '\'' +
                        ", lowRmb='" + lowRmb + '\'' +
                        ", riseRate='" + riseRate + '\'' +
                        ", dollar='" + dollar + '\'' +
                        ", vol='" + vol + '\'' +
                        ", high='" + high + '\'' +
                        ", low='" + low + '\'' +
                        ", highRmb='" + highRmb + '\'' +
                        ", selldollar='" + selldollar + '\'' +
                        ", sellRmb='" + sellRmb + '\'' +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "BtcQc{" +
                    "date='" + date + '\'' +
                    ", symbol='" + symbol + '\'' +
                    ", ticker=" + ticker == null ? "" : ticker.toString() +
                    ", cName='" + cName + '\'' +
                    ", moneyType=" + moneyType +
                    ", exeByRate=" + exeByRate +
                    ", name='" + name + '\'' +
                    ", coinName='" + coinName + '\'' +
                    ", type=" + type +
                    '}';
        }
    }

    public static class BtcQcChart {

        private List<List<String>> chartData;
        private String[][] sChartData = new String[][]{};//当前货币类型

        public BtcQcChart(String[][] data) {
            this.sChartData = data;
        }

        public List<List<String>> getChartData() {
            return chartData;
        }

        public void setChartData(List<List<String>> chartData) {

            this.chartData = chartData;
        }

        public String[][] getsChartData() {
            return sChartData;
        }
    }
}
