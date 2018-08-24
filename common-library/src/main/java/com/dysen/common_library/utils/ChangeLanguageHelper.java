package com.dysen.common_library.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;



import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * xiezuofei
 * 2018-07-10 13:20
 * 793169940@qq.com
 * 语言切换类
 */
public class ChangeLanguageHelper {
    private static Context mContext = null;
    private static String country = null;
    //默认
    public static final int CHANGE_LANGUAGE_DEFAULT = 0;
    //中文
    public static final int CHANGE_LANGUAGE_CHINA = 1;
    //英文
    public static final int CHANGE_LANGUAGE_ENGLISH = 2;

    public static final String KEY_APP_LANGUAGE = "zb_app_language";
    private static String mLanguage = "";
    private static Resources mResources;
    private static Locale mDefaultLocale;
    public static void init(Context context) {
        mResources = context.getResources();
        country = context.getResources().getConfiguration().locale.getCountry();
        mContext = context;
        changeLanguage();
    }

    /**
     * 获取当前字符串资源的内容
     *
     * @param id
     * @return
     */
    public static String getStringById(int id) {
        String string ;
        if (mLanguage != null && !"".equals(mLanguage)){
            string = mResources.getString(id,mLanguage);
        }else {
            string = mResources.getString(id,"");
        }

        if (string != null && string.length() > 0) {
            return string;
        }
        return "";
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static void changeLanguage() {
        int language =getAppLanguage();
        Configuration config = mResources.getConfiguration();// 获得设置对象
        DisplayMetrics dm = mResources.getDisplayMetrics();
        switch (language) {
            case CHANGE_LANGUAGE_CHINA:
                config.locale = Locale.SIMPLIFIED_CHINESE;     // 中文
                config.setLayoutDirection(Locale.SIMPLIFIED_CHINESE);
                mLanguage = "zh-CN";
                country = "CN";
                setAppLanguage(CHANGE_LANGUAGE_CHINA);
                break;
            case CHANGE_LANGUAGE_ENGLISH:
                config.locale = Locale.ENGLISH;   // 英文
                config.setLayoutDirection(Locale.ENGLISH);
                mLanguage = "en";
                country = "US";
                setAppLanguage(CHANGE_LANGUAGE_ENGLISH);
                break;
            case CHANGE_LANGUAGE_DEFAULT:
                country = Locale.getDefault().getCountry();
                String language_ls = Locale.getDefault().getLanguage();
                if ("zh".equals(language_ls)){
                    setAppLanguage(CHANGE_LANGUAGE_CHINA);
                    mDefaultLocale = Locale.SIMPLIFIED_CHINESE;
                    mLanguage = "zh-CN";
                    country = "CN";
                }else {
                    setAppLanguage(CHANGE_LANGUAGE_ENGLISH);
                    mDefaultLocale =  Locale.ENGLISH;
                    mLanguage = "en";
                    country = "US";
                }
                config.locale = mDefaultLocale;// 系统默认语言
                config.setLayoutDirection(mDefaultLocale);
                break;
        }
        mResources.updateConfiguration(config, dm);
    }
    public static int getAppLanguage(){
        return SharedPreUtils.getInstance(mContext).get(KEY_APP_LANGUAGE,0);
    }
    public static void setAppLanguage(int app_language){
        SharedPreUtils.getInstance(mContext).put(KEY_APP_LANGUAGE,app_language);
    }
//    public static List<LanguageData> getLanguages(){
//        List<LanguageData> languages=new ArrayList<>();
//        /*languages.add(new LanguageData("系统默认","",CHANGE_LANGUAGE_DEFAULT));*/
//        languages.add(new LanguageData("简体中文","",CHANGE_LANGUAGE_CHINA));
//        languages.add(new LanguageData("English","",CHANGE_LANGUAGE_ENGLISH));
//        return languages;
//    }
}

