package com.dysen.im_demo.entry;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;

/**
 * @package com.dysen.im_demo.entry
 * @email dy.sen@qq.com
 * created by dysen on 2019-07-24 - 15:28
 * @info
 */
public class Bean {

    public static class Msg {

        public enum MsgType{
            SYSTEM_MSG,
            SEND_MSG,
            RECEIVE_MSG,
            NOMAL_MSG,
            TRADE_STATE_MSG
        }

        String msg, name, imgUrl;
        long time;
        boolean isSend;
        String sysMsg;
        EMMessage message;
        private EMTextMessageBody body;
        MsgType type;

        public Msg(MsgType type, String imgUrl, EMMessage message) {
            this.type = type;
            this.imgUrl = imgUrl;
            this.message = message;
        }
        public Msg(MsgType type, EMMessage message) {
            this.type = type;
            this.message = message;
        }
        public Msg(MsgType type, String imgUrl, String sysMsg) {
            this.type = type;
            this.imgUrl = imgUrl;
            this.sysMsg = sysMsg;
        }

        public MsgType getType() {
            return type;
        }

        public EMMessage getMessage() {
            return message;
        }

        public void setMessage(EMMessage message) {
            this.message = message;
        }

        public long getTime() {
            return message.getMsgTime();
        }

        public void setTime(long time) {
            this.time = time;
        }

        public String getSysMsg() {
            return sysMsg;
        }

        public void setSysMsg(String sysMsg) {
            this.sysMsg = sysMsg;
        }

        public boolean isSend() {
            return isSend;
        }

        public void setSend(boolean send) {
            isSend = send;
        }

        public String getMsg() {

            // 这里只是简单的demo，也只是测试文字消息的收发，所以直接将body转为EMTextMessageBody去获取内容
            body = (EMTextMessageBody) message.getBody();
            return body.getMessage();
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getName() {
            return message.getFrom();
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        @Override
        public String toString() {
            return "Msg{" +
                    "msg='" + msg + '\'' +
                    ", name='" + name + '\'' +
                    ", imgUrl='" + imgUrl + '\'' +
                    ", time=" + time +
                    ", isSend=" + isSend +
                    ", sysMsg='" + sysMsg + '\'' +
                    '}';
        }
    }

    public static class Menu {
        String name, imgUrl;
        int imgId;

        public Menu(String name, int imgId) {
            this.name = name;
            this.imgId = imgId;
        }

        public Menu(String name, String imgUrl) {
            this.name = name;
            this.imgUrl = imgUrl;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public int getImgId() {
            return imgId;
        }

        public void setImgId(int imgId) {
            this.imgId = imgId;
        }
    }
}
