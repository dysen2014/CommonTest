package com.dysen.im_demo.entry;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @package com.dysen.im_demo.entry
 * @email dy.sen@qq.com
 * created by dysen on 2019-07-24 - 15:28
 * @info
 */
public class Bean {

    public static class Msg {

        public enum MsgType {
            SYSTEM_MSG,
            SEND_MSG,
            RECEIVE_MSG,
            NOMAL_MSG,
            TRADE_STATE_MSG
        }
        @PrimaryKey
        private String id="";//
        String msg, name, imgUrl;
        long time;
        String sysMsg;
        EMMessage message;
        private EMTextMessageBody body;
        MsgType type;

        public Msg() {
        }

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

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
        public MsgType getType() {
            return type;
        }

        public EMMessage getMessage() {
            return message;
        }

        public long getTime() {
            return message.getMsgTime();
        }

        public String getMsg() {

            // 这里只是简单的demo，也只是测试文字消息的收发，所以直接将body转为EMTextMessageBody去获取内容
            body = (EMTextMessageBody) message.getBody();
            return body.getMessage();
        }

        public String getName() {
            return message.getFrom();
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
                    ", sysMsg='" + sysMsg + '\'' +
                    ", message=" + message == null ? "" : message.toString() +
                    ", body=" + getMsg()  +
                    ", type=" + type +
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
