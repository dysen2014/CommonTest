package com.dysen.common_library.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.ClipboardManager;

import com.dysen.common_library.activity.CustomScanActivity;
import com.dysen.common_library.ui.UIHelper;
import com.dysen.common_library.utils.AppUtils;
import com.dysen.common_library.utils.DateUtils;
import com.dysen.common_library.utils.LogUtils;
import com.dysen.common_library.utils.SharedPreUtils;
import com.google.zxing.integration.android.IntentIntegrator;


/**
 * @package com.dysen.gesturelock.activity
 * @email dy.sen@qq.com
 * created by dysen on 2018/7/27 - 下午2:07
 * @info
 */
public class BaseAppContext extends Application {
    public static String T_TIME_OUT = "t_time_out";
    private String TAG = getClass().getSimpleName();
    private static BaseAppContext app;
    private static Boolean deBugMode = true;

    public static BaseAppContext getInstance() {
        return app;
    }

    public static Boolean getDeBugMode() {
        return deBugMode;
    }

    public int count = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {

            @Override
            public void onActivityStopped(Activity activity) {
                LogUtils.v("viclee", activity + "\tonActivityStopped");
                count--;
                if (count == 0) {
                    LogUtils.v("viclee", ">>>>>>>>>>>>>>>>>>>切到后台  lifecycle");
                }
            }

            @Override
            public void onActivityStarted(Activity activity) {
                LogUtils.v("viclee", activity + "\tonActivityStarted");
                if (count == 0) {
                    LogUtils.v("viclee", ">>>>>>>>>>>>>>>>>>>切到前台  lifecycle" + (System.currentTimeMillis() - mLastActionTime > mTotalTime));
                    if (System.currentTimeMillis() - mLastActionTime > mTotalTime) {
                        if (!AppUtils.newInstance(app).getRunningActivitySimpleName().equals("SplashActivity"));
                    }
                }
                count++;
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                LogUtils.v("viclee", activity + "\tonActivitySaveInstanceState");
            }

            @Override
            public void onActivityResumed(Activity activity) {
                LogUtils.v("viclee", activity + "\tonActivityResumed");
            }

            @Override
            public void onActivityPaused(Activity activity) {
                LogUtils.v("viclee", activity + "\tonActivityPaused");
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                LogUtils.v("viclee", activity + "\tonActivityDestroyed");
            }

            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                LogUtils.v("viclee", activity + "\tonActivityCreated");
            }
        });

    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.fontScale != 1)//非默认值
            getResources();
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        if (res.getConfiguration().fontScale != 1) {//非默认值
            Configuration newConfig = new Configuration();
            newConfig.setToDefaults();//设置默认
            res.updateConfiguration(newConfig, res.getDisplayMetrics());
        }
        return res;
    }

    /**
     * 实现文本复制功能
     * add by wangqianzhou
     *
     * @param content
     */
    public void copy(String content, Context context) {
        try {
            // 得到剪贴板管理器
            ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            cmb.setText(content.trim());
            UIHelper.ToastMessage(context, "内容已复制到剪切板");
        } catch (Exception e) {

        }
    }

    /**
     * 实现粘贴功能
     * add by wangqianzhou
     *
     * @param context
     * @return
     */
    public String paste(Context context) {
        try {
            // 得到剪贴板管理器
            ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            String str = "";
            if (cmb.getText() != null) {
                str = cmb.getText().toString().trim();
            }
            if (str.equals("")) {
                UIHelper.ToastMessage(context, "内容为空");
            }
            return str;
        } catch (Exception e) {
            return "";
        }

    }

    /*----------------------------------定时器 开始--------------------------------------------*/

    public static long mLastActionTime = -1; // 上一次操作时间
    public static long mNowActionTime = -1; // 当前操作时间
    //    protected static long mTotalTime = 5 * 60 * 1000;//默认5分钟自动退出
    protected static long mTotalTime = 10 * 1000;//默认5分钟自动退出

    public static String ANSWER = "answer";
    public static String ISUNLOCK = "isunlock";
    public static final String KEY_USER_LOCK = "user_lock";

    public void show() {
        mLastActionTime = mNowActionTime;
        // 初始化上次操作时间为登录成功的时间
        if (mLastActionTime == -1)
            mLastActionTime = mNowActionTime = System.currentTimeMillis();
        else
            mNowActionTime = System.currentTimeMillis();

        LogUtils.e(TAG, DateUtils.getDateString(mLastActionTime) + "==user touch==" + DateUtils.getDateString(mNowActionTime));
        long time = SharedPreUtils.getInstance(app).get(BaseAppContext.T_TIME_OUT, 0l);
        if (time > 0)
            mTotalTime = time;
        LogUtils.e(TAG, time+"========time=mTotalTime=========="+mTotalTime);
        if (mNowActionTime - mLastActionTime > mTotalTime);
    }

    public long getmTotalTime() {
        return mTotalTime;
    }

    public void setmTotalTime(long totalTime) {
        mTotalTime = totalTime * 1000;
    }

    /*------------------------------------定时器 结束🔚------------------------------------------*/

    /*------------------------------------通知 常量------------------------------------------*/
    public static final String KEY_MSGID = "msg_id";
    public static final String KEY_WHICH_PUSH_SDK = "rom_type";
    public static final String KEY_TITLE = "n_title";
    public static final String KEY_CONTENT = "n_content";
    public static final String KEY_EXTRAS = "n_extras";
    public static final String MAIN_TYPE = "main_type";
    public static final String MAIN_TYPE_NOTIFICATION = "101";
    public static final String MAIN_TYPE_MESSAGE = "102";
    /*------------------------------------通知 结束🔚------------------------------------------*/

    /**
     * scanQR
     * @param activity
     */
    public static void customScan(Activity activity) {
        new IntentIntegrator(activity)
                .setOrientationLocked(false)
                .setCaptureActivity(CustomScanActivity.class) // 设置自定义的activity是CustomActivity
                .initiateScan(); // 初始化扫描
    }
}