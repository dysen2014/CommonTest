package com.dysen.common_library.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * @package com.vip.zb.tool
 * @email dy.sen@qq.com
 * created by dysen on 2018/7/4 - 下午4:34
 * @info 跟App相关的辅助类
 */
public class AppUtils {
    public static int APP_RUNNING = 1;
    public static int APP_NOT_RUNNING = 0;
    public static int APP_NOT_INSTALL = -1;

    String TAG = getClass().getSimpleName();
    public static Context mContext;

    public static AppUtils newInstance(Context context) {
        mContext = context;
        AppUtils appUtils = new AppUtils();
        return appUtils;
    }

    /**
     * 获取应用程序名称
     */
    public synchronized String getAppName() {
        try {
            PackageManager packageManager = mContext.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    mContext.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            Log.e(TAG, "packageInfo:"+packageInfo.toString());
            return mContext.getResources().getString(labelRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * [获取应用程序版本名称信息]
     *
     * @return 当前应用的版本名称
     */
    public synchronized String getVersionName() {
        try {
            PackageManager packageManager = mContext.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    mContext.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * [获取应用程序版本名称信息]
     *
     * @return 当前应用的版本名称
     */
    public synchronized int getVersionCode() {
        try {
            PackageManager packageManager = mContext.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    mContext.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * [获取应用程序版本名称信息]
     *
     * @return 当前应用的版本名称
     */
    public synchronized String getPackageName() {
        try {
            PackageManager packageManager = mContext.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    mContext.getPackageName(), 0);
            return packageInfo.packageName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取图标 bitmap
     *
     */
    public synchronized Bitmap getBitmap() {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = mContext.getApplicationContext()
                    .getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(
                    mContext.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }
        Drawable d = packageManager.getApplicationIcon(applicationInfo); //xxx根据自己的情况获取drawable
        BitmapDrawable bd = (BitmapDrawable) d;
        Bitmap bm = bd.getBitmap();
        return bm;
    }

    /**
     * 判断App是否在后台运行
     * @return true--后台， false--前台
     */
    public boolean isBackground() {
        ActivityManager activityManager = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(mContext.getPackageName())) {
                if (appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    Log.i(mContext.getPackageName(), "后台"
                            + appProcess.processName);
                    return true;
                } else {
                    Log.i(mContext.getPackageName(), "前台"
                            + appProcess.processName);
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * 方法描述：判断某一应用是否正在运行
     * Created by cafeting on 2017/2/4.
     *
     * @param packageName 应用的包名
     * @return true 表示正在运行，false 表示没有运行
     */
    public boolean isAppRunning(String packageName) {
        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
        if (list.size() <= 0) {
            return false;
        }
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.baseActivity.getPackageName().equals(packageName)) {
                return true;
            }
        }
        return false;
    }


    //获取已安装应用的 uid，-1 表示未安装此应用或程序异常
    public int getPackageUid(String packageName) {
        try {
            ApplicationInfo applicationInfo = mContext.getPackageManager().getApplicationInfo(packageName, 0);
            if (applicationInfo != null) {
                Log.d(TAG, applicationInfo.uid + "");
                return applicationInfo.uid;
            }
        } catch (Exception e) {
            return -1;
        }
        return -1;
    }

    /**
     * 判断某一 uid 的程序是否有正在运行的进程，即是否存活
     * Created by cafeting on 2017/2/4.
     *
     * @param uid     已安装应用的 uid
     * @return true 表示正在运行，false 表示没有运行
     */
    public boolean isProcessRunning(int uid) {
        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServiceInfos = am.getRunningServices(200);
        if (runningServiceInfos.size() > 0) {
            for (ActivityManager.RunningServiceInfo appProcess : runningServiceInfos) {
                if (uid == appProcess.uid) {
                    return true;
                }
            }
        }
        return false;
    }

    public int isRunning(String pgkName) {
        int uid = getPackageUid(pgkName);
        if (uid > 0) {
            boolean rstA = isAppRunning(pgkName);
            boolean rstB = isProcessRunning(uid);
            if (rstA || rstB) {
                //指定包名的程序正在运行中
                return APP_RUNNING;
            } else {
                //指定包名的程序未在运行中
                return APP_NOT_RUNNING;
            }
        } else {
            //应用未安装
            return APP_NOT_INSTALL;
        }
    }

    //获得当前activity的名字
    public String getRunningActivityName() {
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        //完整类名
        String runningActivity = activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
        String contextActivity = runningActivity.substring(runningActivity.lastIndexOf(".") + 1);
        return runningActivity;
    }


    //获得当前activity的名字
    public String getRunningActivitySimpleName() {
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        //完整类名
        String runningActivity = activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
        String contextActivity = runningActivity.substring(runningActivity.lastIndexOf(".") + 1);
        return contextActivity;
    }

    //获得当前应用包名
    public String getAppPackageName() {
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfo = activityManager.getRunningTasks(1);
        ComponentName componentInfo = taskInfo.get(0).topActivity;
        Log.d(TAG, "当前应用:" + componentInfo.getPackageName());
        return componentInfo.getPackageName();
    }

//判断是否是系统桌面

    /**
     * 判断当前界面是否是桌面
     */
    public boolean isHome(Context context) {
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);
        return getHomes(context).contains(rti.get(0).topActivity.getPackageName());
    }

    /**
     * 跳转Activity
     * @param cls
     * @param jsonData
     */
    public Intent transAty(Class<?> cls, String jsonData, int...flag) {
//        MainActivity2.newInstance(jsonData);
        //打开自定义的Activity
        Intent intent = new Intent(mContext, cls);
        intent.putExtra(cls.getSimpleName(), jsonData);
        if (flag.length > 0)
            intent.setFlags(flag[0]);
        mContext.startActivity(intent);

        return intent;
    }

    /**
     * 跳转Activity(跳转前设置好数据)
     * @param cls
     * @param flag
     * @return
     */
    public Intent transAty(Class<?> cls, int...flag) {
//        MainActivity2.newInstance(jsonData);
        //打开自定义的Activity
        Intent intent = new Intent(mContext, cls);
        if (flag.length > 0)
            intent.setFlags(flag[0]);
        mContext.startActivity(intent);

        return intent;
    }

    /**
     * 发送广播
     * @param action
     */
    public void trans2Broadcast(String action, int...flag) {
        //打开自定义的Activity
        Intent intent = new Intent(action);
        if (flag.length > 0)
            intent.addFlags(flag[0]);
        mContext.sendBroadcast(intent);
    }

    /**
     * 通过包名和类名 启动Activity
     * @param packageName
     * @param className
     */
    public void transAty(String packageName, String className) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setComponent(new ComponentName(packageName, className));
        //前名一个参数是应用程序的包名,后一个是这个应用程序的主Activity名
        mContext.startActivity(intent);
    }

    /**
     * 通过包名 启动Activity
     * @param packagename
     */
    public void doStartApplicationWithPackageName(String packagename) {

        // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
        PackageInfo packageinfo = null;
        try {
            packageinfo = mContext.getPackageManager().getPackageInfo(packagename, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageinfo == null) {
            return;
        }

        // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(packageinfo.packageName);

        // 通过getPackageManager()的queryIntentActivities方法遍历
        List<ResolveInfo> resolveinfoList = mContext.getPackageManager()
                .queryIntentActivities(resolveIntent, 0);

        ResolveInfo resolveinfo = resolveinfoList.iterator().next();
        if (resolveinfo != null) {
            // packagename = 参数packname
            String packageName = resolveinfo.activityInfo.packageName;
            // 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packagename.mainActivityname]
            String className = resolveinfo.activityInfo.name;
            // LAUNCHER Intent
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);

            // 设置ComponentName参数1:packagename参数2:MainActivity路径
            ComponentName cn = new ComponentName(packageName, className);

            intent.setComponent(cn);
            mContext.startActivity(intent);
        }
    }

    /**
     * 获得属于桌面的应用的应用包名称
     *
     * @return 返回包含所有包名的字符串列表
     */
    public List<String> getHomes(Context context) {
        List<String> names = new ArrayList<String>();
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo ri : resolveInfo) {
            names.add(ri.activityInfo.packageName);
        }
        return names;
    }

    public void luachApp(String packageName){

        PackageManager packageManager = mContext.getPackageManager();
        Intent intent=new Intent();
        intent =packageManager.getLaunchIntentForPackage("com.vict.fsd");
        if(intent==null){
            Toast.makeText(mContext, "App 未安装", Toast.LENGTH_LONG).show();
        }else{
            mContext.startActivity(intent);

        }
    }
}

