package com.dysen.test.contacts;

/**
 * @package com.vip.zb.activity.user.phone
 * @email dy.sen@qq.com
 * created by dysen on 2018/12/15 - 10:30 AM
 * @info
 */
public class PhoneBean {
    private String name;        //联系人姓名
    private String telPhone;    //电话号码
    private Long contactId;     //联系人Id
    private Long photoId;       //联系人图像Id


    //拼音
    private String pinyin;
    //拼音首字母
    private String headerWord;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelPhone() {
        return telPhone;
    }

    public void setTelPhone(String telPhone) {
        this.telPhone = telPhone;
    }

    public Long getContactId() {
        return contactId;
    }

    public void setContactId(Long contactId) {
        this.contactId = contactId;
    }

    public Long getPhotoId() {
        return photoId;
    }

    public void setPhotoId(Long photoId) {
        this.photoId = photoId;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getHeaderWord() {
        return headerWord;
    }

    public void setHeaderWord(String headerWord) {
        this.headerWord = headerWord;
    }

    public PhoneBean(String name, String telPhone) {
        this.name = name;
        this.telPhone = telPhone;
        this.pinyin = PinYinUtils.getPinyin(name);
        this.headerWord = pinyin.substring(0, 1);
    }

    public PhoneBean(String name, String telPhone, Long contactId, Long photoId) {
        this.name = name;
        this.telPhone = telPhone;
        this.contactId = contactId;
        this.photoId = photoId;
        this.pinyin = PinYinUtils.getPinyin(name);
        this.headerWord = pinyin.substring(0, 1);
    }

    @Override
    public String toString() {
        return "PhoneBean{" +
                "name='" + name + '\'' +
                ", telPhone='" + telPhone + '\'' +
                ", contactId=" + contactId +
                ", photoId=" + photoId +
                '}';
    }
}

