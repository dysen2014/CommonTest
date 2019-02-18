package com.dysen.common_library.utils;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dysen.common_library.R;
import com.dysen.common_library.base.BaseAppContext;
import com.dysen.common_library.ui.UIHelper;
import com.dysen.common_library.views.AutoHeightViewPager;
import com.dysen.common_library.views.ConfirmDialog;
import com.dysen.common_library.views.CustomPopWindow;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Tools {

    public final static SimpleDateFormat yyyyMMddHHmmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static long lastClickTime;

    public static Application getApp() {
        return BaseAppContext.getInstance();
    }

    public static void printStackTrace(String tag, Exception e) {
        Log.e(tag, e.getMessage(), e);
    }

    public static void printStackTrace(Object tag, Exception e) {
        Log.e(tag.getClass().getSimpleName(), e.getMessage(), e);
    }

    public static void printStackTrace(Exception e) {
        Log.e("Tools", e.getMessage(), e);
    }

    public static void debugLog(CharSequence msg) {
        log_d("zb_debug", "" + msg);
    }

    public static void debugLog(String format, Object... args) {
        debugLog(String.format(format, args));
    }

    public static void debugLog(List<?> mList) {
        if (mList != null)
            debugLog("size=%s: %s", mList.size(), mList.toString());
        else
            debugLog("List == null");
    }

    public static void debugLog(Object object) {
        if (object != null)
            debugLog(object.toString());
        else
            debugLog("object == null");
    }

    public static void debugLog() {
        debugLog("============================================");
    }

    public static void log_d(String tag, String msg) {
        if (!isApkDebugable())
            return;
        int maxLength = 2001 - tag.length();
        while (msg.length() > maxLength) {
            Log.d(tag, msg.substring(0, maxLength));
            msg = msg.substring(maxLength);
        }
        Log.d(tag, msg);
    }

    public static void runOnUiThread(Runnable run) {
        new Handler(Looper.getMainLooper()).post(run);
    }

    public static void runOnUiThread(Runnable run, long delayMillis) {
        if (delayMillis < 100)
            runOnUiThread(run);
        else
            new Handler(Looper.getMainLooper()).postDelayed(run, delayMillis);
    }

    public static int getColor(int colorId) {
        try {
            return getApp().getResources().getColor(colorId);
        } catch (Exception e) {
            Tools.printStackTrace("Tools", e);
            return Color.BLACK;
        }
    }

    public static String getString(int id) {
        try {
            return getApp().getResources().getString(id);
        } catch (Exception e) {
            Tools.printStackTrace("Tools", e);
            return "";
        }
    }
    public static String[] getStringArray(int id) {
        try {
            return getApp().getResources().getStringArray(id);
        } catch (Exception e) {
            Tools.printStackTrace("Tools", e);
            return new String[]{};
        }
    }

    public static String getString(int id, Object... formatArgs) {
        try {
            return getApp().getResources().getString(id, formatArgs);
        } catch (Exception e) {
            Tools.printStackTrace("Tools", e);
            return "";
        }
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

    public static int getInternalDimensionSize() {
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
                return 0;
            int result = 0;
            int resourceId = getApp().getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = getApp().getResources().getDimensionPixelSize(resourceId);
            }
            return result;
        } catch (Exception e) {
            Tools.printStackTrace("Tools", e);
            return 0;
        }
    }


    public static void threadSleep(long time) {
        try {
            if (time > 0)
                Thread.sleep(time);
        } catch (Exception e) {
            Log.e("Tools", e.getMessage(), e);
        }
    }


    public static void toast(final CharSequence msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                UIHelper.ToastMessage(getApp(), msg.toString());
            }
        });
    }

    public static void toastShort(final CharSequence msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                UIHelper.ToastMessage(getApp(), msg.toString());
            }
        });
    }

    public static void toast(int resId) {
        toast(getString(resId));
    }

    public static void toastShort(int resId) {
        toastShort(getString(resId));
    }

    public static void toastShortError(String mError) {
        if (!TextUtils.isEmpty(mError) && (
                mError.contains("错误") ||
                        mError.contains("网络") ||
                        mError.contains("异常"))) {

            toastShort(mError);
        }
    }

    public static void dismissProgressDialog() {
    }

    public static void showProgressDialog(Activity activity) {
    }

    public static String getLocalDateTime() {
        return yyyyMMddHHmmss.format(new Date());
    }

    public static String formatSmsDateTime(String str, boolean fullFormat) {
        try {
            SimpleDateFormat yyyyMMddHHmmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dayNames[] = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
            StringBuffer result = new StringBuffer();
            boolean isNotTime = false;

            Calendar createCalendar = Calendar.getInstance();
            if (str.contains(":")) {
                createCalendar.setTime(yyyyMMddHHmmss.parse(str));
            } else {
                createCalendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(str));
                fullFormat = false;
                isNotTime = true;
            }

            long createTimeInMillis = createCalendar.getTimeInMillis();
            long distance = System.currentTimeMillis() - createTimeInMillis;

            Time time = new Time();
            time.set(createTimeInMillis);

            int thenYear = time.year;
            int thenMonth = time.month;
            int thenMonthDay = time.monthDay;
            int thenWeekDay = time.weekDay;
            int thenWeekNumber = time.getWeekNumber();

            time.set(System.currentTimeMillis());

            if (distance < 0) {
                return str;

            } else {
                if (thenYear == time.year && thenMonth == time.month) {
                    if (thenMonthDay == time.monthDay) {
                        if (fullFormat && distance < DateUtils.MINUTE_IN_MILLIS * 5) {
                            return "刚刚";
                        } else {
                            if (!fullFormat)
                                result.append("今天 ");
                            if (!isNotTime && fullFormat)
                                result.append(getDateSx(createCalendar));
                        }
                    } else if (thenMonthDay == time.monthDay - 1) {
                        result.append("昨天 ");
                        if (fullFormat)
                            result.append(" ").append(getDateSx(createCalendar));
                    } else if (thenMonthDay == time.monthDay - 2) {
                        result.append("前天 ");
                        if (fullFormat)
                            result.append(" ").append(getDateSx(createCalendar));
                    } else if (thenMonthDay > time.monthDay - 7) {
                        if (thenWeekNumber < time.getWeekNumber())
                            result.append("上");
                        result.append(dayNames[thenWeekDay]);
                        if (fullFormat)
                            result.append(" ").append(getDateSx(createCalendar));
                    } else
                        result.append(formatTimeStampString(createTimeInMillis, fullFormat && !isNotTime));
                } else if (thenYear == time.year && thenMonth > time.month - 3) {
                    result.append(formatTimeStampString(createTimeInMillis, fullFormat && !isNotTime));
                } else {
                    result.append(formatTimeStampString(createTimeInMillis, false));
                }
            }

            return result.toString();

        } catch (Exception e) {
            Log.e("Tools", e.getMessage(), e);
            return str;
        }
    }

    public static String formatNewsFlashTime(long time) {
        try {
            Date mDate = new Date(time);
            long distance = System.currentTimeMillis() - time;
            if (distance < 0) {
                return yyyyMMddHHmmss.format(mDate);
            } else if (distance < 120 * 1000) {
                return "刚刚";
            } else {
                return new SimpleDateFormat("HH:mm").format(mDate);
            }
        } catch (Exception e) {
            return formatDate(time);
        }
    }

    public static String getDateSx(Calendar createCalendar) {
        int hour = createCalendar.get(Calendar.HOUR_OF_DAY);
        StringBuffer result = new StringBuffer();

        if (hour >= 6 && hour < 8) {
            result.append("早上 ");
        } else if (hour >= 8 && hour < 11) {
            result.append("上午 ");
        } else if (hour >= 11 && hour < 13) {
            result.append("中午 ");
        } else if (hour >= 13 && hour < 19) {
            result.append("下午 ");
        } else {
            result.append("晚上 ");
        }

        result.append(android.text.format.DateFormat.format("h:mm", createCalendar.getTimeInMillis()).toString());

        return result.toString();
    }

    public static String formatTimeStampString(String str) {
        try {
            Date mDate = yyyyMMddHHmmss.parse(str);
            if ((new Date().getTime() - mDate.getTime()) < 60000)
                return str;
            return DateUtils.getRelativeTimeSpanString(mDate.getTime()).toString();
        } catch (Exception e) {
            Log.e("Tools", e.getMessage(), e);
            return str;
        }
    }

    public static String formatTimeStampString(long time) {
        try {
            if (time > System.currentTimeMillis()) {
                return formatDate(time);
            } else if (time > System.currentTimeMillis() - 61 * 1000) {
                return "刚刚";
            } else {
                return DateUtils.getRelativeTimeSpanString(time).toString();
            }
        } catch (Exception e) {
            Log.e("Tools", e.getMessage(), e);
            return "";
        }
    }

    public static String formatTimeStampString(long when, boolean fullFormat) {
        Time then = new Time();
        then.set(when);
        Time now = new Time();
        now.setToNow();

        int format_flags = DateUtils.FORMAT_NO_NOON_MIDNIGHT | DateUtils.FORMAT_ABBREV_ALL | DateUtils.FORMAT_CAP_AMPM;

        if (then.year != now.year) {
            format_flags |= DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_SHOW_DATE;
        } else if (then.yearDay != now.yearDay) {
            format_flags |= DateUtils.FORMAT_SHOW_DATE;
        } else {
            format_flags |= DateUtils.FORMAT_SHOW_TIME;
        }

        if (fullFormat) {
            format_flags |= (DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME);
        }

        return DateUtils.formatDateTime(getApp(), when, format_flags);
    }

    public static String formatDate(String date) {
        if (date.contains(":"))
            return formatDate(date, "yyyy-MM-dd HH:mm");
        else
            return date;
    }

    public static String formatDate(long time) {
        return formatDate(yyyyMMddHHmmss.format(new Date(time)));
    }

    public static String formatDate(String date, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        try {
            return format.format(format.parse(date));
        } catch (Exception e) {
            Log.e("Tools", e.getMessage(), e);
        }
        return date;
    }

    public static long getDayTime(long time) {
        try {
            Calendar mCalendar = Calendar.getInstance();
            mCalendar.setTime(new Date(time));
            mCalendar.set(Calendar.HOUR_OF_DAY, 0);
            mCalendar.set(Calendar.MINUTE, 0);
            mCalendar.set(Calendar.SECOND, 0);
            mCalendar.set(Calendar.MILLISECOND, 0);
            return mCalendar.getTimeInMillis();
        } catch (Exception e) {
            Tools.printStackTrace("Tools", e);
            return time;
        }
    }

    public static boolean isUIThread() {
        try {
            return Looper.myLooper() == Looper.getMainLooper();
        } catch (Exception e) {
            Tools.printStackTrace("Tools", e);
            return false;
        }
    }

    public static boolean isEmpty(TextView tv) {
        return TextUtils.isEmpty(getText(tv));
    }

    public static boolean isEmpty(TextView tv, CharSequence errorMsg) {
        if (tv == null) {
            Tools.toastShort("TextView 对象为空!");
            return true;
        }
        if (TextUtils.isEmpty(getText(tv))) {
            Tools.toastShort(errorMsg);
            return true;
        }

        return false;
    }

    public static String getText(TextView tv) {
        if (tv == null)
            return null;
        else
            return tv.getText().toString().trim();
    }

    /**
     * 隐藏软键盘
     *
     * @param activity
     */
    public static void hideSoftInput(Activity activity) {
        try {
            ((InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
        }
    }

    /**
     * 显示软键盘
     *
     * @param activity
     */
    public static void showSoftInput(Activity activity, EditText editText) {
        try {
            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);
            editText.requestFocus();
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editText, InputMethodManager.RESULT_SHOWN);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        } catch (Exception e) {
            Tools.printStackTrace("Tools", e);
        }
    }

    public static void eventBusRegister(Object object) {
        try {
            if (!EventBus.getDefault().isRegistered(object)) {
                EventBus.getDefault().register(object);
            }
        } catch (Exception e) {
            Tools.printStackTrace("Tools", e);
        }
    }

    public static void eventBusUnregister(Object object) {
        try {
            EventBus.getDefault().unregister(object);
        } catch (Exception e) {
            Tools.printStackTrace("Tools", e);
        }
    }

    public static void eventBusPost(Object object) {
        try {
            EventBus.getDefault().postSticky(object);
        } catch (Exception e) {
            Tools.printStackTrace("Tools", e);
        }
    }

    public static String double2String(double d) {
        try {
            if (d <= 0) {
                return "0.00";
            } else {
                BigDecimal mBigDecimal = new BigDecimal(Double.toString(d));
                return mBigDecimal.toString();
            }
        } catch (Exception e) {
            Tools.printStackTrace("Tools", e);
            return Double.toString(d);
        }
    }

    public static String handleZeroString(String str) {
        try {
            if (isZero(str))
                return "0.00";
            else
                return str;
        } catch (Exception e) {
            Tools.printStackTrace("Tools", e);
            return str;
        }
    }

    public static boolean isZero(String d) {
        try {
            if (TextUtils.isEmpty(d))
                return true;

            BigDecimal mBigDecimal = new BigDecimal(d);
            return mBigDecimal.doubleValue() <= 0;

        } catch (Exception e) {
            Tools.printStackTrace("Tools", e);
            return false;
        }
    }

    /**
     * 去除list<T>重复的数据
     *
     * @param list
     * @param <T>
     * @return
     */
    public static <T> List<T> removeDuplicate(List<T> list) {
        Set set = new LinkedHashSet<T>();
        set.addAll(list);
        list.clear();
        list.addAll(set);
        return list;
    }

    public static boolean isApkDebugable() {
        try {
            ApplicationInfo info = getApp().getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            Tools.printStackTrace(e);
            return false;
        }
    }

    public static boolean isActivityDestroyed(Activity activity) {
        try {
            if (activity == null || activity.isFinishing())
                return true;

            if (Build.VERSION.SDK_INT >= 17 && activity.isDestroyed())
                return true;

            return false;
        } catch (Exception e) {
            Tools.printStackTrace(e);
            return true;
        }
    }

    /**
     * 执行动画
     *
     * @param view
     * @param animationId
     */
    public static void doAnimation(final View view, final int animationId) {
        try {
            view.startAnimation(AnimationUtils.loadAnimation(getApp(), animationId));
        } catch (Exception e) {
            Tools.printStackTrace(e);
        }
    }

    public static void doFadeinAnimation(final View view) {
        doAnimation(view, R.anim.fadein);
    }

    /**
     * 显示
     */
    public static void setVisible(final View view) {
        try {
            if (view == null || view.getVisibility() == View.VISIBLE)
                return;
            view.setVisibility(View.VISIBLE);
            doAnimation(view, R.anim.fadein);
        } catch (Exception e) {
            Tools.printStackTrace(e);
        }
    }

    /**
     * 隐藏
     *
     * @param view
     */
    public static void setGone(final View view) {
        try {
            if (view == null || view.getVisibility() == View.GONE ||
                    view.getVisibility() == View.INVISIBLE)
                return;
            view.setVisibility(View.GONE);
            doAnimation(view, R.anim.fadeout);
        } catch (Exception e) {
            Tools.printStackTrace(e);
        }
    }

    /**
     * 信息确认框
     *
     * @param context
     * @param msg
     * @param mOnYesClickListener
     * @param mOnNoClickListener
     */
    public static ConfirmDialog showConfirmDialog(Context context, String msg, View.OnClickListener mOnYesClickListener, View.OnClickListener mOnNoClickListener) {
        ConfirmDialog mConfirmDialog = new ConfirmDialog(context);
        mConfirmDialog.getContentView().setText(msg);
        if (mOnYesClickListener != null) {
            mConfirmDialog.setYesOnClickListener(mOnYesClickListener);
        }
        if (mOnNoClickListener != null) {
            mConfirmDialog.setNoOnClickListener(mOnNoClickListener);
        }
        return mConfirmDialog.show();
    }

    /**
     * 信息确认框
     *
     * @param context
     * @param msg
     */
    public static ConfirmDialog showConfirmDialog(Context context, String msg, View.OnClickListener mOnYesClickListener) {
        return showConfirmDialog(context, msg, mOnYesClickListener, null);
    }

    /**
     * 信息确认框
     *
     * @param context
     * @param msg
     */
    public static ConfirmDialog showConfirmDialog(Context context, String msg) {
        ConfirmDialog mConfirmDialog = showConfirmDialog(context, msg, null, null);
        mConfirmDialog.setBtnNoGone();
        return mConfirmDialog;
    }

    /**
     * 信息确认框
     *
     * @param context
     * @param resId
     */
    public static ConfirmDialog showConfirmDialog(Context context, int resId) {
        return showConfirmDialog(context, getString(resId));
    }

    /**
     * 是否英语语言
     *
     * @return
     */
    public static boolean isLanguageEnglish() {
        return ChangeLanguageHelper.getAppLanguage() == ChangeLanguageHelper.CHANGE_LANGUAGE_ENGLISH;
    }

    /**
     * 防止控件被重复点击
     *
     * @param distance 间隔 默认300毫秒 test
     * @return
     */
    public static boolean isFastDoubleClick(int distance) {
        long time = System.currentTimeMillis();
        long timeD = ((long) time - lastClickTime);
        if (0 < timeD && timeD < (long) distance) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * 防止控件被重复点击
     *
     * @return
     */
    public static boolean isFastDoubleClick() {
        return isFastDoubleClick(300);
    }

    /**
     * 获取屏幕分辨率
     */
    public static Point getRealScreenSize(Context context) {
        Point screenSize = null;
        try {
            screenSize = new Point();
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display defaultDisplay = windowManager.getDefaultDisplay();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                defaultDisplay.getRealSize(screenSize);
            } else {
                try {
                    Method mGetRawW = Display.class.getMethod("getRawWidth");
                    Method mGetRawH = Display.class.getMethod("getRawHeight");
                    screenSize.set(
                            (Integer) mGetRawW.invoke(defaultDisplay),
                            (Integer) mGetRawH.invoke(defaultDisplay)
                    );
                } catch (Exception e) {
                    screenSize.set(defaultDisplay.getWidth(), defaultDisplay.getHeight());
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return screenSize;
    }

    /**
     * 图片缩放
     *
     * @param ivPic
     */
    static boolean num = true;
    static float scaleWidth, scaleHeight;

    private static void imgZoom(Activity activity, final ImageView ivPic, final Bitmap bitmap) {
        DisplayMetrics dm = new DisplayMetrics();//创建矩阵
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int w = dm.widthPixels; //得到屏幕的宽度
        int h = dm.heightPixels; //得到屏幕的高度
        scaleWidth = ((float) w) / width;
        scaleHeight = ((float) h) / height;

        ivPic.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {

                    case MotionEvent.ACTION_DOWN:  //当屏幕检测到第一个触点按下之后就会触发到这个事件。
                        if (num == true) {
                            Matrix matrix = new Matrix();
                            matrix.postScale(scaleWidth, scaleHeight);

                            Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                            ivPic.setImageBitmap(newBitmap);
                            num = false;
                        } else {
                            Matrix matrix = new Matrix();
                            matrix.postScale(1.0f, 1.0f);
                            Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                            ivPic.setImageBitmap(newBitmap);
                            num = true;
                        }
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 播放动画
     *
     * @param context
     * @param view
     * @param animId
     */
    public static void playAnimation(Context context, View view, int animId) {
        // 初始化需要加载的动画资源
        Animation animation = AnimationUtils
                .loadAnimation(context, animId);
        // 将View执行Animation动画
        view.startAnimation(animation);
    }

    /**
     * 几天前毫秒
     *
     * @param days
     * @return
     */
    public static long getDaysAgoTimeMillis(int days) {
        return System.currentTimeMillis() - days * 24 * 3600 * 1000L;
    }

    /**
     * 转换参数
     *
     * @param param
     * @return
     */
    public static Map<String, String> convertParameter(Map<String, Object> param) {
        Map<String, String> result = new HashMap<>();
        if (param == null) {
            return result;
        }
        for (String key : param.keySet()) {
            result.put(key, String.valueOf(param.get(key)));
        }
        return result;
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


    public static void removeView(View view) {
        if (view != null) {
            ViewGroup parentViewGroup = (ViewGroup) view.getParent();
            if (parentViewGroup != null) {
                parentViewGroup.removeView(view);
            }
        }
    }

    /**
     * 1、LinearLayoutManager:线性布局管理器，支持水平和垂直效果。
     */
    public static RecyclerView.LayoutManager setManager1(Context context, int orientation) {
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(orientation);
        return manager;
    }

    /**
     * 　2、GridLayoutManager:网格布局管理器，支持水平和垂直效果。
     */
    public static RecyclerView.LayoutManager setManager2(Context context, int spanCount) {
        GridLayoutManager manager = new GridLayoutManager(context, spanCount);
        return manager;
    }

    /**
     * 　3、StaggeredGridLayoutManager:分布型管理器，瀑布流效果
     */
    public static RecyclerView.LayoutManager setManager3(int spanCount, int orientation) {
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(spanCount, orientation);
        return manager;
    }

    /**
     * 通过时间戳 获得相应的星期
     *
     * @param date
     * @return
     */
    public static String getCurrentWeek(Date... date) {
        Calendar cal = Calendar.getInstance();
        if (date.length > 0) {
            cal.setTime(date[0]);
        } else {
            cal.setTime(com.dysen.common_library.utils.DateUtils.getToday());
        }
        int week = cal.get(Calendar.DAY_OF_WEEK);
        String strWeek = "";
        switch (week) {
            case 1:
                strWeek = Tools.getString(R.string.week_sunday);
                break;
            case 2:
                strWeek = Tools.getString(R.string.week_monday);
                break;
            case 3:
                strWeek = Tools.getString(R.string.week_tuesday);
                break;
            case 4:
                strWeek = Tools.getString(R.string.week_wednesday);
                break;
            case 5:
                strWeek = Tools.getString(R.string.week_thursday);
                break;
            case 6:
                strWeek = Tools.getString(R.string.week_friday);
                break;
            case 7:
                strWeek = Tools.getString(R.string.week_saturday);
                break;
            default:
                strWeek = Tools.getString(R.string.week_sunday);
                break;
        }
        return strWeek;
    }

    /**
     * 分享时  显示的分享时间
     *
     * @param publishDate
     * @return
     */
    public static String shareDate(long publishDate) {
        return Tools.getCurrentWeek(new Date(publishDate)) + "\t\t" + DateUtils.dateSimpleFormat(new Date(publishDate),
                DateUtils.BASE_DATE_FORMAT);
    }

    /**
     * 点击图片  全屏显示/消失
     *
     * @param mActivity
     * @param contentView
     */
    public static void showCustomView(Activity mActivity, final View contentView) {

        //创建并显示popWindow
        final CustomPopWindow mCustomPopWindow = new CustomPopWindow.PopupWindowBuilder(mActivity)
                .setView(contentView)
                .enableBackgroundDark(true) //弹出popWindow时，背景是否变暗
                .setBgDarkAlpha(0.1f) // 控制亮度
//                .size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                .create();
        contentView.findViewById(R.id.iv_pic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCustomPopWindow.getPopupWindow().isShowing())
                    mCustomPopWindow.dissmiss();
            }
        });
        if (mCustomPopWindow.getPopupWindow().isShowing())
            mCustomPopWindow.dissmiss();
        mCustomPopWindow.getPopupWindow().setAnimationStyle(R.style.pop_animation);
        mCustomPopWindow.showAtLocation(contentView, Gravity.CENTER, 0, 0);
        mCustomPopWindow.getPopupWindow().update();
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

    /**
     * 把布局转换成View
     * @param activity
     * @param layoutId
     * @return
     */
    public static View getView(Activity activity, int layoutId) {
        View contentView = activity.getLayoutInflater().inflate(R.layout.layout_common_recyclerview, null);
        return contentView;
    }
}
