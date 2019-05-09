package com.dysen.common_library.utils;

import android.content.Context;

import com.dysen.common_library.BuildConfig;
import com.dysen.common_library.R;

/**
 * @author SoMustYY
 * @create 2018/7/9 下午4:26
 * @organize 卓世达科
 * @describe
 * @update
 */
public class StringResourceUtil {
    private static final String TAG = "StringResourceUtil";

    /**
     * 根据名称name来获取string
     *
     * @param context
     * @param keyName
     * @return
     */
    public static String getStringForName(Context context, String keyName) {
        String strValue = null;
        try {
            int resId = context.getResources().getIdentifier(keyName, "string", BuildConfig.APPLICATION_ID);
            LogUtils.e(TAG, "resId：" + resId);
            if(resId != 0){
                strValue = context.getResources().getString(resId);
            }else{
                resId = context.getResources().getIdentifier("Code_888888888", "string", BuildConfig.APPLICATION_ID);
                strValue = context.getResources().getString(resId);
            }
            LogUtils.e(TAG, "strValue：" + strValue);
            return strValue;

        }catch (Exception e){
            return context.getResources().getString(R.string.Code_888888888);
        }

    }

}
