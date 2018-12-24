package com.dysen.common_library.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.dysen.common_library.utils.LogUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * 监控整个APP的运行情况
 * 功能：
 * 1. 监控CPU，内存，网络等使用情况
 * 2. 自动截取当前屏幕并上传服务器
 * 3. 在线卸载APP
 * <p>
 * Created by Luis on 2018/3/5.
 */

public class MonitorService extends Service {

    private Handler mHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //installAPP("/storage/emulated/0/Androidshizhong_99920.apk");
                //uninstallAPP("com.android.deskclock");
            }
        }, 10000);
        /*
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //getScreenPicture();
            }
        });
        thread.start();
        */
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 获取当前顶层Activity，如果不是本应用，则启动主界面
        LogUtils.d("MonitorService", "onStartCommand");
        if (!isAPPForeground()) {
            /*
            Intent intent1 = new Intent(MonitorService.this, MainActivity.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent1);
            */
        }
        return START_STICKY;
    }

    /**
     * 判断前台APP是否为当前APP
     *
     * @return activity的包名
     */
    public boolean isAPPForeground() {
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTasks = activityManager.getRunningTasks(1);
        return "com.integrated.edu.cpad".equals(runningTasks.get(0).topActivity.getPackageName());
    }

    /**
     * 获取系统内存信息
     */
    private void getMemInfo() {
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        // 系统总内存 MB
        LogUtils.d("MonitorService", "System total Memory: " + (memoryInfo.totalMem * 1.0 / (1024 * 1024)));
        // 系统剩余内存 MB
        LogUtils.d("MonitorService", "System available Memory: " + (memoryInfo.availMem * 1.0 / (1024 * 1024)));
        // 应用当前内存 MB
        float totalMemory = (Runtime.getRuntime().totalMemory() * 1.0f / (1024 * 1024));
        // 应用剩余内存 MB
        float freeMemory = (Runtime.getRuntime().freeMemory() * 1.0f / (1024 * 1024));
        LogUtils.d("MonitorService", "APP total memory: " + totalMemory);
        LogUtils.d("MonitorService", "APP free memory: " + freeMemory);
    }

    /**
     * 获取本机CPU使用情况
     */
    private long getTotalCPUTime() {
        String[] cpuInfos = null;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream("/proc/stat")), 1000);
            String load = reader.readLine();
            reader.close();
            cpuInfos = load.split(" ");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        long totalCpu = Long.parseLong(cpuInfos[2])
                + Long.parseLong(cpuInfos[3]) + Long.parseLong(cpuInfos[4])
                + Long.parseLong(cpuInfos[6]) + Long.parseLong(cpuInfos[5])
                + Long.parseLong(cpuInfos[7]) + Long.parseLong(cpuInfos[8]);
        return totalCpu;
    }

    /**
     * 获取APP CPU使用情况
     */
    private long getAppCPUTime() {
        String[] cpuInfos = null;
        try {
            int pid = android.os.Process.myPid();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream("/proc/" + pid + "/stat")), 1000);
            String load = reader.readLine();
            reader.close();
            cpuInfos = load.split(" ");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        long appCpuTime = Long.parseLong(cpuInfos[13])
                + Long.parseLong(cpuInfos[14]) + Long.parseLong(cpuInfos[15])
                + Long.parseLong(cpuInfos[16]);
        return appCpuTime;
    }

    /**
     * 获取本机所有APP信息
     */
    private void getAPPInfo() {
        PackageManager packageManager = getPackageManager();
        //获取手机内所有应用
        List<PackageInfo> packageInfoList = packageManager.getInstalledPackages(0);
        for (int i = 0; i < packageInfoList.size(); i++) {
            PackageInfo packageInfo = packageInfoList.get(i);
            // 获取app名称
            LogUtils.d("MonitorService", "APP name:" + packageInfo.applicationInfo.loadLabel(packageManager).toString());
            // 获取app包名
            LogUtils.d("MonitorService", "App packageName: " + packageInfo.packageName);
        }
    }

    /**
     * 获取屏幕截图
     */
    private void getScreenPicture() {
        String filePath = "/mnt/sdcard/screenPicture.png";
        String command = "screencap " + filePath;
        Process process = null;
        DataOutputStream outputStream = null;
        BufferedReader successResult = null;
        BufferedReader errorResult = null;
        StringBuilder successMsg;
        StringBuilder errorMsg;
        try {
            //process = Runtime.getRuntime().exec("su");
            process = Runtime.getRuntime().exec("screencap -p /mnt/sdcard/test.png");
            /*
            outputStream = new DataOutputStream(process.getOutputStream());
            outputStream.write(command.getBytes());
            outputStream.writeBytes("\n");
            outputStream.writeBytes("exit\n");
            outputStream.flush();
            */
            LogUtils.d("MonitorService", "StartCapScreen");
            process.waitFor();
            LogUtils.d("MonitorService", "Take Screen finish");
            successMsg = new StringBuilder();
            errorMsg = new StringBuilder();
            successResult = new BufferedReader(new InputStreamReader(process.getInputStream()));
            errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String s;
            while ((s = successResult.readLine()) != null)
                successMsg.append(s);
            while ((s = errorResult.readLine()) != null)
                errorMsg.append(s);
            LogUtils.d("MonitorService", "Install Success:" + successMsg.toString());
            LogUtils.d("MonitorService", "Install Error:" + errorMsg.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null)
                    outputStream.close();
                if (process != null)
                    process.destroy();
                if (successResult != null)
                    successResult.close();
                if (errorResult != null)
                    errorResult.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 静默安装APP
     *
     * @param path // app安装工路径
     */
    private void installAPP(String path) {
        String cmd = "pm install -r " + path;
        Process process = null;
        DataOutputStream outputStream = null;
        BufferedReader successResult = null;
        BufferedReader errorResult = null;
        StringBuilder successMsg;
        StringBuilder errorMsg;
        try {
            process = Runtime.getRuntime().exec("su");
            outputStream = new DataOutputStream(process.getOutputStream());
            outputStream.write(cmd.getBytes());
            outputStream.writeBytes("\n");
            outputStream.writeBytes("exit\n");
            outputStream.flush();
            process.waitFor();
            successMsg = new StringBuilder();
            errorMsg = new StringBuilder();
            successResult = new BufferedReader(new InputStreamReader(process.getInputStream()));
            errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String s;
            while ((s = successResult.readLine()) != null)
                successMsg.append(s);
            while ((s = errorResult.readLine()) != null)
                errorMsg.append(s);
            LogUtils.d("MonitorService", "Install Success:" + successMsg.toString());
            LogUtils.d("MonitorService", "Install Error:" + errorMsg.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null)
                    outputStream.close();
                if (process != null)
                    process.destroy();
                if (successResult != null)
                    successResult.close();
                if (errorResult != null)
                    errorResult.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 静默卸载APP
     *
     * @param packageName app 包名
     */
    private void uninstallAPP(String packageName) {
        // 检查packageName是否存在,如果发生异常则证明不存在
        try {
            PackageManager pm = getPackageManager();
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String command = "pm uninstall " + packageName;
        Process process = null;
        DataOutputStream outputStream = null;
        BufferedReader successResult = null;
        BufferedReader errorResult = null;
        StringBuilder successMsg;
        StringBuilder errorMsg;
        try {
            // 获取root权限
            process = Runtime.getRuntime().exec("su");
            outputStream = new DataOutputStream(process.getOutputStream());
            outputStream.write(command.getBytes());
            outputStream.writeBytes("\n");
            outputStream.writeBytes("exit\n");
            outputStream.flush();
            //执行命令
            process.waitFor();
            successMsg = new StringBuilder();
            errorMsg = new StringBuilder();
            successResult = new BufferedReader(new InputStreamReader(process.getInputStream()));
            errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String s;
            while ((s = successResult.readLine()) != null)
                successMsg.append(s);
            while ((s = errorResult.readLine()) != null)
                errorMsg.append(s);
            LogUtils.d("MonitorService", "Uninstall Success:" + successMsg.toString());
            LogUtils.d("MonitorService", "Uninstall Error:" + errorMsg.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null)
                    outputStream.close();
                if (process != null)
                    process.destroy();
                if (successResult != null)
                    successResult.close();
                if (errorResult != null)
                    errorResult.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public class MonitorReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

        }
    }

}
