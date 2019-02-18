package com.dysen.test.contacts;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @package com.vip.zb.activity.user.phone
 * @email dy.sen@qq.com
 * created by dysen on 2018/12/15 - 10:31 AM
 * @info 获取联系人工具类
 */
public class ContactsUtil {

    // 号码
    public final static String NUM = ContactsContract.CommonDataKinds.Phone.NUMBER;
    // 联系人姓名
    public final static String NAME = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME;
    //联系人ID
    public final static String ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
    //联系人图像
    public final static String IMG = ContactsContract.CommonDataKinds.Phone.PHOTO_ID;

    //上下文对象
    private Context context;
    //联系人提供者的uri
    private Uri phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
    private ContentResolver cr;
    private long mContactId = -1l;

    public ContactsUtil(Context context) {
        this.context = context;
    }

    //获取所有联系人
    public List<ContactsBean> getContactsData() {
        List<ContactsBean> contactsBeans = new ArrayList<>();
        cr = context.getContentResolver();
        Cursor cursor = cr.query(phoneUri, new String[]{NUM, NAME, ID, IMG}, null, null, null);
        while (cursor.moveToNext()) {
            //得到联系人名称
            String contactName = cursor.getString(cursor.getColumnIndex(NAME));
            //得到手机号码
            String contactNum = cursor.getString(cursor.getColumnIndex(NUM));
            //得到联系人ID
            long contactId = cursor.getLong(cursor.getColumnIndex(ID));
            //得到联系人头像ID
            long photoId = cursor.getLong(cursor.getColumnIndex(IMG));
//            contactNum = contactNum.replace(" ", "");
//            contactNum = contactNum.replace("-", "");
//            if (!FormatUtil.isMobileNO(contactNum))
//                continue;

            if (mContactId != contactId) {//过滤同一个用户有多个号码（仅取第一个）
                ContactsBean contactsBean = new ContactsBean(contactName.toUpperCase(), contactNum, contactId, photoId);
//                System.out.println(mContactId +"=============="+contactId + "========contactId=======" + contactsBean.toString());
                contactsBeans.add(contactsBean);
            }
            mContactId = contactId;
        }
        return sortList(contactsBeans);
    }

    public List<ContactsBean> sortList(List<ContactsBean> contactsBeans) {
        //对集合排序
        Collections.sort(contactsBeans, new Comparator<ContactsBean>() {
            @Override
            public int compare(ContactsBean lhs, ContactsBean rhs) {
                //根据拼音进行排序
                return lhs.getPinyin().toUpperCase().compareTo(rhs.getPinyin().toUpperCase());
            }
        });

        return contactsBeans;
    }

    public Bitmap getContactsImg(Context context, long contactId) {
        //获取联系人头像的代码
        ContentResolver cr = context.getContentResolver();
        Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
        InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(cr, uri);
        Bitmap photo = BitmapFactory.decodeStream(input);
        return photo;
    }
}

