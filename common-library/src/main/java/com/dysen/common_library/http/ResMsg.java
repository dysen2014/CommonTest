package com.dysen.common_library.http;

/**
 * dysen
 * 2018-08-18 11:20
 * dy.sen@qq.com
 */
public class ResMsg {
    private String code="";
    private String message="";
    private String method="";
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

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("code=" + code + " message=" + message+ " method=" + method);
        return sb.toString();
    }
}
