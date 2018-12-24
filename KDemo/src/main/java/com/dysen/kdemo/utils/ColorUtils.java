package com.dysen.kdemo.utils;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;

import com.dysen.kdemo.R;

/**
 * @package com.dysen.kdemo
 * @email dy.sen@qq.com
 * created by dysen on 2018/12/21 - 4:33 PM
 * @info
 */
public class ColorUtils {
    
    //背景色
    public static int getBackground(Context mContext){
        int color_int;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            color_int = mContext.getColor(R.color.background_color);
        } else {
            color_int = Color.parseColor("#EFF0F2");
        }
        return color_int;
    }
    //白色
    public static int getWhite(Context mContext){
        int color_int;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            color_int = mContext.getColor(R.color.background_color_white);
        } else {
            color_int = Color.parseColor("#ffffff");
        }
        return color_int;
    }
    //MagicIndicatorView 用到的颜色
    public static int getMagicindicator(Context mContext){
        int color_int;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            color_int = mContext.getColor(R.color.bnt_register_color_normal2);
        } else {
            color_int = Color.parseColor("#324765");
        }
        return color_int;
    }

    //红色
    public static int getRed(Context mContext){
        int color_int;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            color_int = mContext.getColor(R.color.text_color_red);
        } else {
            color_int = Color.parseColor("#E70101");
        }
        return color_int;
    }
    //绿色
    public static int getGreen(Context mContext){
        int color_int;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            color_int = mContext.getColor(R.color.text_color_green);
        } else {
            color_int = Color.parseColor("#08BA52");
        }
        return color_int;
    }
    //黄色
    public static int getOrange(Context mContext){
        int color_int;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            color_int = mContext.getColor(R.color.text_color_orange);
        } else {
            color_int = Color.parseColor("#c0ffbd21");
        }
        return color_int;
    }
    //蓝色
    public static int getBlue(Context mContext){
        int color_int;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            color_int = mContext.getColor(R.color.text_color_blue);
        } else {
            color_int = Color.parseColor("#3B77D1");
        }
        return color_int;
    }
    //
    public static int getGray(Context mContext){
        int color_int;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            color_int = mContext.getColor(R.color.text_color_gray);
        } else {
            color_int = Color.parseColor("#999999");
        }
        return color_int;
    }
    public static int getBlack2(Context mContext){
        int color_int;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            color_int = mContext.getColor(R.color.text_color_black2);
        } else {
            color_int = Color.parseColor("#5D5D5D");
        }
        return color_int;
    }

    public static int getKBG(Context mContext) {
        int color_int;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            color_int = mContext.getColor(R.color.kbg);
        } else {
            color_int = Color.parseColor("#172432");
        }
        return color_int;
    }
    public static int getHeaderColor(Context mContext) {
        int color_int;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            color_int = mContext.getColor(R.color.main_header_bg);
        } else {
            color_int = Color.parseColor("#243040");
        }
        return color_int;
    }
}

