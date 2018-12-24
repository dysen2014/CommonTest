package com.dysen.common_library.utils;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.RemoteViews;

import com.dysen.common_library.R;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * 显示通知栏工具类
 * Created by Administrator on 2016-11-14.
 */

public class NotificationUtil {

    static Context mContext;
    static Activity mActivity;
    static Notification mNotification;
    static Notification.Builder mBuilder ;
    static NotificationManager mNotificationManager;

    public static NotificationUtil newInstance(Context context) {
        mContext = context;
        mActivity = (Activity) mContext;
        NotificationUtil notificationUtil = new NotificationUtil();
        mNotificationManager = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= 26) {//当sdk版本大于26
            String id = "channel_1";
            String description = "143";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel(id, description, importance);
//            channel.enableLights(true);
//            channel.enableVibration(true);//
            mNotificationManager.createNotificationChannel(channel);
            mBuilder = new Notification.Builder(mContext, id)
                    .setCategory(Notification.CATEGORY_MESSAGE);
        } else {//当sdk版本小于26

            mBuilder = new Notification.Builder(mContext);
        }
        return notificationUtil;
    }
    
    /**
     * 显示一个普通的通知
     *
     */
    public void showNotification(Context mContext) {
        mNotification = new NotificationCompat.Builder(mContext)
                /**设置通知左边的大图标**/
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher))
                /**设置通知右边的小图标**/
                .setSmallIcon(R.mipmap.ic_launcher)
                /**通知首次出现在通知栏，带上升动画效果的**/
                .setTicker("通知来了")
                /**设置通知的标题**/
                .setContentTitle("这是一个通知的标题")
                /**设置通知的内容**/
                .setContentText("这是一个通知的内容这是一个通知的内容")
                /**通知产生的时间，会在通知信息里显示**/
                .setWhen(System.currentTimeMillis())
                /**设置该通知优先级**/
                .setPriority(Notification.PRIORITY_DEFAULT)
                /**设置这个标志当用户单击面板就可以让通知将自动取消**/
                .setAutoCancel(true)
                /**设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)**/
                .setOngoing(false)
                /**向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：**/
                .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                .setContentIntent(PendingIntent.getActivity(mContext, 1, new Intent(mContext, mContext.getClass()), PendingIntent.FLAG_CANCEL_CURRENT))
                .build();

        /**发起通知**/
        mNotificationManager.notify(0, mNotification);
    }

    /**
     * 显示一个下载带进度条的通知
     *
     */
    public void showNotificationProgress() {
        //进度条通知
        mBuilder.setContentTitle("下载中");
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setTicker("进度条通知");
        mBuilder.setProgress(100, 0, false);
        mNotification = mBuilder.build();
        //发送一个通知
        mNotificationManager.notify(2, mNotification);
        /**创建一个计时器,模拟下载进度**/
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            int progress = 0;

            @Override
            public void run() {
                Log.i("progress", progress + "");
                while (progress <= 100) {
                    progress++;
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    //更新进度条
                    mBuilder.setProgress(100, progress, false);
                    //再次通知
                    mNotificationManager.notify(2, mBuilder.build());
                }
                //计时器退出
                this.cancel();
                //进度条退出
                mNotificationManager.cancel(2);
                return;//结束方法
            }
        }, 0);
    }

    /**
     * 悬挂式，支持6.0以上系统
     *
     */
    public void showFullScreen() {
        Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://blog.csdn.net/itachi85/"));
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, mIntent, 0);
        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher));
        mBuilder.setAutoCancel(true);
        mBuilder.setContentTitle("悬挂式通知");
        //设置点击跳转
//        Intent hangIntent = new Intent();
//        hangIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        hangIntent.setClass(mContext, MainActivity.class);
        //如果描述的PendingIntent已经存在，则在产生新的Intent之前会先取消掉当前的
//        PendingIntent hangPendingIntent = PendingIntent.getActivity(mContext, 0, hangIntent, PendingIntent.FLAG_CANCEL_CURRENT);
//        builder.setFullScreenIntent(hangPendingIntent, true);
        mNotificationManager.notify(3, mBuilder.build());
    }
    public void showShare(Bitmap bitmap) {
        Uri uri = null;
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
//        String filePath = BitmapUtils.saveBitmap(bitmap, "/storage/emulated/0/Pictures/sendy");
        String filePath = BitmapUtils.saveBitmap((Activity) mContext, bitmap, "Pictures/sendy", true);
        File file = FileUtils.getFile(filePath);
        if (Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(mContext, mContext.getPackageName()+".fileprovider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        System.out.println(file+"=========filePath======="+uri.getPath());
//        FileUtils.deleteFile(file);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, shareIntent, 0);
        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher));
        mBuilder.setAutoCancel(true);
        mBuilder.setContentTitle("悬挂式通知");
        //设置点击跳转
        Intent hangIntent = new Intent();
        hangIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        hangIntent.setClass(mContext, mContext.getClass());
        hangIntent.putExtra(Intent.EXTRA_STREAM, uri);
//        如果描述的PendingIntent已经存在，则在产生新的Intent之前会先取消掉当前的
        PendingIntent hangPendingIntent = PendingIntent.getActivity(mContext, 0, hangIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        mBuilder.setFullScreenIntent(hangPendingIntent, true);
        mNotificationManager.notify(3, mBuilder.build());
    }
    /**
     * 折叠式
     *
     */
    public void shwoNotify() {
        //先设定RemoteViews
        RemoteViews view_custom = new RemoteViews(mContext.getPackageName(), R.layout.view_custom);
        //设置对应IMAGEVIEW的ID的资源图片
        view_custom.setImageViewResource(R.id.custom_icon, R.mipmap.icon);
        view_custom.setTextViewText(R.id.tv_custom_title, "今日头条");
        view_custom.setTextColor(R.id.tv_custom_title, Color.BLACK);
        view_custom.setTextViewText(R.id.tv_custom_content, "金州勇士官方宣布球队已经解雇了主帅马克-杰克逊，随后宣布了最后的结果。");
        view_custom.setTextColor(R.id.tv_custom_content, Color.BLACK);
        mBuilder.setContent(view_custom)
                .setContentIntent(PendingIntent.getActivity(mContext, 4, new Intent(mContext, mContext.getClass()), PendingIntent.FLAG_CANCEL_CURRENT))
                .setWhen(System.currentTimeMillis())// 通知产生的时间，会在通知信息里显示
                .setTicker("有新资讯")
                .setPriority(Notification.PRIORITY_HIGH)// 设置该通知优先级
                .setOngoing(false)//不是正在进行的   true为正在进行  效果和.flag一样
                .setSmallIcon(R.mipmap.icon);
        mNotification = mBuilder.build();
        mNotificationManager.notify(4, mNotification);
    }
}
