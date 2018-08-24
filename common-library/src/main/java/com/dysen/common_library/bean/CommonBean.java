package com.dysen.common_library.bean;

import java.util.List;

/**
 * @package com.dysen.common_library.bean
 * @email dy.sen@qq.com
 * created by dysen on 2018/8/18 - 上午11:32
 * @info
 */
public class CommonBean {
    public static class TestBean{

        /**
         * android_pad : {"downloadUrl":"https://hbnx-crm.oss-cn-hangzhou.aliyuncs.com/android-pad-release.apk","info":["update description list 0","update description list 1","update description list 2","list 3"],"info_str":"1.更新了XX；2.优化了YY","versionNum":"1.0.1"}
         */

        private AndroidPadBean android_pad;

        public AndroidPadBean getAndroid_pad() {
            return android_pad;
        }

        public void setAndroid_pad(AndroidPadBean android_pad) {
            this.android_pad = android_pad;
        }

        public static class AndroidPadBean {
            /**
             * downloadUrl : https://hbnx-crm.oss-cn-hangzhou.aliyuncs.com/android-pad-release.apk
             * info : ["update description list 0","update description list 1","update description list 2","list 3"]
             * info_str : 1.更新了XX；2.优化了YY
             * versionNum : 1.0.1
             */

            private String downloadUrl;
            private String info_str;
            private String versionNum;
            private List<String> info;

            public String getDownloadUrl() {
                return downloadUrl;
            }

            public void setDownloadUrl(String downloadUrl) {
                this.downloadUrl = downloadUrl;
            }

            public String getInfo_str() {
                return info_str;
            }

            public void setInfo_str(String info_str) {
                this.info_str = info_str;
            }

            public String getVersionNum() {
                return versionNum;
            }

            public void setVersionNum(String versionNum) {
                this.versionNum = versionNum;
            }

            public List<String> getInfo() {
                return info;
            }

            public void setInfo(List<String> info) {
                this.info = info;
            }
        }
    }

    public static class Test2Bean{

        /**
         * datas : {"amount":"123.34","date":"20180818","price":"3.345","tid":"123456"}
         * resMsg : {"code":"1000","message":"操作成功","method":"getAppVersion"}
         */

        private DatasBean datas;
        private ResMsgBean resMsg;

        public DatasBean getDatas() {
            return datas;
        }

        public void setDatas(DatasBean datas) {
            this.datas = datas;
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
}
