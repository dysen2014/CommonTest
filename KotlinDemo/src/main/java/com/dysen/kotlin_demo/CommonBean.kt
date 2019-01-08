package com.dysen.kotlin_demo

/**
 * @package com.dysen.kotlin_demo
 * @email dy.sen@qq.com
 * created by dysen on 2019/1/8 - 3:07 PM
 * @info
 */
class CommonBean {

    /**
     * datas : {"amount":"123.34","date":"20180818","price":"3.345","tid":"123456"}
     * name : dysen
     * resMsg : {"code":"1000","message":"操作成功","method":"getAppVersion"}
     */

    var datas: DatasBean? = null
    var name: String? = null
    var resMsg: ResMsgBean? = null

    class DatasBean {
        /**
         * amount : 123.34
         * date : 20180818
         * price : 3.345
         * tid : 123456
         */

        var amount: String? = null
        var date: String? = null
        var price: String? = null
        var tid: String? = null

        override fun toString(): String {
            return "DatasBean{" +
                    "amount='" + amount + '\''.toString() +
                    ", date='" + date + '\''.toString() +
                    ", price='" + price + '\''.toString() +
                    ", tid='" + tid + '\''.toString() +
                    '}'.toString()
        }
    }

    class ResMsgBean {
        /**
         * code : 1000
         * message : 操作成功
         * method : getAppVersion
         */

        var code: String? = null
        var message: String? = null
        var method: String? = null
        override fun toString(): String {
            return "ResMsgBean(code=$code, message=$message, method=$method)"
        }
    }
}
