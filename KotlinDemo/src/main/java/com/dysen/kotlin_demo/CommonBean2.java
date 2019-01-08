package com.dysen.kotlin_demo;

/**
 * @package com.dysen.kotlin_demo
 * @email dy.sen@qq.com
 * created by dysen on 2019/1/8 - 3:07 PM
 * @info
 */
public class CommonBean2 {

    /**
     * datas : {"amount":"123.34","date":"20180818","price":"3.345","tid":"123456"}
     * name : dysen
     * resMsg : {"code":"1000","message":"操作成功","method":"getAppVersion"}
     */

    private DatasBean datas;
    private String name;
    private ResMsgBean resMsg;

    public DatasBean getDatas() {
        return datas;
    }

    public void setDatas(DatasBean datas) {
        this.datas = datas;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ResMsgBean getResMsg() {
        return resMsg;
    }

    public void setResMsg(ResMsgBean resMsg) {
        this.resMsg = resMsg;
    }

    public static class DatasBean {
        /**
         * amount : 123.34
         * date : 20180818
         * price : 3.345
         * tid : 123456
         */

        private String amount;
        private String date;
        private String price;
        private String tid;

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getTid() {
            return tid;
        }

        public void setTid(String tid) {
            this.tid = tid;
        }

        @Override
        public String toString() {
            return "DatasBean{" +
                    "amount='" + amount + '\'' +
                    ", date='" + date + '\'' +
                    ", price='" + price + '\'' +
                    ", tid='" + tid + '\'' +
                    '}';
        }
    }

    public static class ResMsgBean {
        /**
         * code : 1000
         * message : 操作成功
         * method : getAppVersion
         */

        private String code;
        private String message;
        private String method;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }
    }
}
