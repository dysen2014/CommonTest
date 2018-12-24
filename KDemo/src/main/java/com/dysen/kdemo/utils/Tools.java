package com.dysen.kdemo.utils;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.dysen.kdemo.AppContext;
import com.dysen.kdemo.views.AutoHeightViewPager;

import java.math.BigDecimal;

import static com.dysen.kdemo.utils.ViewUtils.getText;

/**
 * @package com.dysen.kdemo
 * @email dy.sen@qq.com
 * created by dysen on 2018/12/19 - 4:29 PM
 * @info
 */
public class Tools {

    public static Application getApp() {
        return AppContext.getInstance();
    }

    public static void runOnUiThread(Runnable run) {
        new Handler(Looper.getMainLooper()).post(run);
    }

    public static void toast(final CharSequence msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!TextUtils.isEmpty(msg)) {
                    Toast.makeText(getApp(), msg, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * 1、LinearLayoutManager:线性布局管理器，支持水平和垂直效果。
     */
    public static RecyclerView.LayoutManager setManager1(int orientation) {
        LinearLayoutManager manager = new LinearLayoutManager(getApp());
        manager.setOrientation(orientation);
        return manager;
    }

    /**
     * 　2、GridLayoutManager:网格布局管理器，支持水平和垂直效果。
     */
    public static RecyclerView.LayoutManager setManager2(int spanCount) {
        GridLayoutManager manager = new GridLayoutManager(getApp(), spanCount);
        return manager;
    }

    /**
     * 　3、StaggeredGridLayoutManager:分布型管理器，瀑布流效果
     */
    public static RecyclerView.LayoutManager setManager3(int spanCount, int orientation) {
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(spanCount, orientation);
        return manager;
    }

    public static int getScreenWidth() {
        return getApp().getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return getApp().getResources().getDisplayMetrics().heightPixels;
    }

    public static int dp2px(float dpValue) {
        return DeviceUtil.dp2px(getApp(), dpValue);
    }

    public static int sp2px(float spValue) {
        float scale = getApp().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * scale + 0.5f);
    }

    /**
     * 判断字符是否是正数
     *
     * @param sVal
     * @return
     */
    public static boolean checkPositiveNumber(String sVal) {
        return Character.isDigit(sVal.charAt(0)) ? true : false;
    }

    public static int getColor(int colorId) {
        try {
            return getApp().getResources().getColor(colorId);
        } catch (Exception e) {
            Tools.printStackTrace("zb_debug", e);
            return Color.BLACK;
        }
    }

    public static String getString(int id) {
        try {
            return getApp().getResources().getString(id);
        } catch (Exception e) {
            Tools.printStackTrace("zb_debug", e);
            return "";
        }
    }

    public static String getString(int id, Object... formatArgs) {
        try {
            return getApp().getResources().getString(id, formatArgs);
        } catch (Exception e) {
            Tools.printStackTrace("zb_debug", e);
            return "";
        }
    }

    public static void printStackTrace(String tag, Exception e) {
        Log.e(tag, e.getMessage(), e);
    }

    /**
     * TextView.getText 转double
     *
     * @param tv
     * @return
     */
    public static double getDoubleValue(TextView tv) {
        return toDouble(getText(tv));
    }

    /**
     * 转double
     *
     * @param str
     * @return
     */
    public static double toDouble(String str) {
        double doub = 0.00;
        try {
            BigDecimal bigDecimal = new BigDecimal(str);
            doub = bigDecimal.doubleValue();
        } catch (Exception e) {
            doub = 0.00;
        }
        return doub;
    }

    /**
     * 转double
     *
     * @param str
     * @return
     */
    public static int toInt(String str) {
        try {
            return new BigDecimal(str).intValue();
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 设置viewpager自适应高度
     *
     * @param vpType
     */
    public static void setAutoHeight(final AutoHeightViewPager vpType) {
        vpType.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                // 切换到当前页面，重置高度
                vpType.requestLayout();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

}
