package com.dysen.im_demo.entry;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @package com.dysen.im_demo.entry
 * @email dy.sen@qq.com
 * created by dysen on 2019-07-29 - 17:59
 * @info
 */
public class Msg extends RealmObject {

    @PrimaryKey
    private String id="";//
    String msg, name, imgUrl;
    long time;
    String sysMsg;
    Bean.Msg.MsgType type;

    public Msg(String id, String msg, String name, String imgUrl, long time, String sysMsg, Bean.Msg.MsgType type) {
        this.id = id;
        this.msg = msg;
        this.name = name;
        this.imgUrl = imgUrl;
        this.time = time;
        this.sysMsg = sysMsg;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
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

    public long getTime() {
        return time;
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

    public Bean.Msg.MsgType getType() {
        return type;
    }

    public void setType(Bean.Msg.MsgType type) {
        this.type = type;
    }
}
