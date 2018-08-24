package com.dysen.common_library.http;
/**
 * dysen
 * 2018-08-18 11:20
 * dy.sen@qq.com
 */
public class HttpResult<T> {

    private ResMsg resMsg;
    //用来模仿Data
    private T datas;

    public ResMsg getResMsg() {
        return resMsg;
    }

    public void setResMsg(ResMsg resMsg) {
        this.resMsg = resMsg;
    }

    public T getDatas() {
        return datas;
    }

    public void setDatas(T datas) {
        this.datas = datas;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("");
        if (null != resMsg) {
            sb.append(" resMsg:" + resMsg.toString());
        }
        if (null != datas) {
            sb.append(" datas:" + datas.toString());
        }
        return sb.toString();
    }
}
