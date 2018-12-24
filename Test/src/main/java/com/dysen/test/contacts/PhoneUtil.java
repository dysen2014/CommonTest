package com.dysen.test.contacts;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.IdRes;

import com.dysen.common_library.utils.FormatUtil;

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
public class PhoneUtil {

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

    public PhoneUtil(Context context) {
        this.context = context;
    }

    //获取所有联系人
    public List<PhoneBean> getPhone() {
        List<PhoneBean> phoneBeans = new ArrayList<>();
        cr = context.getContentResolver();
        Cursor cursor = cr.query(phoneUri, new String[]{NUM, NAME, ID, IMG}, null, null, null);
        while (cursor.moveToNext()) {
            //得到联系人名称
            String contactName = cursor.getString(cursor.getColumnIndex(NAME));
            //得到手机号码
            String contactNum = cursor.getString(cursor.getColumnIndex(NUM));
            //得到联系人ID
            Long contactId = cursor.getLong(cursor.getColumnIndex(ID));
            //得到联系人头像ID
            Long photoId = cursor.getLong(cursor.getColumnIndex(IMG));
            if (!FormatUtil.isMobileNO(contactNum.replace(" ", "")))
                continue;
            PhoneBean phoneBean = new PhoneBean(contactName, contactNum, contactId, photoId);
            System.out.println("===============" + phoneBean.toString());

            phoneBeans.add(phoneBean);
        }
        return sortList(phoneBeans);
    }

    public List<PhoneBean> sortList(List<PhoneBean> phoneBeans) {
        //对集合排序
        Collections.sort(phoneBeans, new Comparator<PhoneBean>() {
            @Override
            public int compare(PhoneBean lhs, PhoneBean rhs) {
                //根据拼音进行排序
                return lhs.getPinyin().compareTo(rhs.getPinyin());
            }
        });

        return phoneBeans;
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

