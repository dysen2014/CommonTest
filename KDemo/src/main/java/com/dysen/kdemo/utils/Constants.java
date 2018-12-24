package com.dysen.kdemo.utils;

/**
 * Created by zuofei on 2017/3/6.
 */
public class Constants {
    public enum CurrencyType {
        CNY("CNY"),
        USD("USD"),
        BTC("BTC"),
        ETH("ETH"),
        LTC("LTC"),
        ETC("ETC");

        private String code;

        CurrencyType(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }

        /// 货币标识
        public String symbol() {
            String s = "";
            switch (this) {
                case CNY:
                    s = "￥";
                    break;
                case USD:
                    s = "$";
                    break;
                case BTC:
                    s = "฿";
                    break;
                case LTC:
                    s = "Ł";
                    break;
                case ETH:
                    s = "E";
                    break;
                case ETC:
                    s = "E";
                    break;
            }
            return s;
        }
    }
    public enum TickerType {

        //chbtc
        CHBTC_BTC_CNY("chbtcbtccny"),
        CHBTC_ETH_CNY("chbtcethcny"),
        CHBTC_LTC_CNY("chbtcltccny"),
        CHBTC_ETC_CNY("chbtcetccny");


        private String code;

        TickerType(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }

    }
}
