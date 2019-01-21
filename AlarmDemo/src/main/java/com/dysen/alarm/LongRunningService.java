package com.dysen.alarm;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import com.dysen.alarm.utils.AppUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LongRunningService extends Service {
    Context mContext;
    private AppUtils appUtils;
    private String pakName = "com.alibaba.android.rimet";
    private String pakNameSame = "com.dysen.alarm";
    private String atyName = "com.alibaba.lightapp.runtime.activity.CommonWebViewActivity";
    private int type;

    public LongRunningService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override

    public int onStartCommand(Intent intent, int flags, int startId) {

        type = intent.getIntExtra("type",1);
        mContext = this;
        appUtils = AppUtils.newInstance(mContext);
        createNotificationChannel();

        new Thread(new Runnable() {

            @Override

            public void run() {

//                Log.d("LongRunningService", "executed at " + new Date().toString());
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                String dateStr = sdf.format(new Date());
                Log.d("LongRunningService", dateStr + "-----executed at " + DateUtils.getToday());
                if (type==1 || dateStr.equals("08:50") || dateStr.equals("09:00") || dateStr.equals("19:30") || dateStr.equals("20:00")) {
                Log.d("LongRunningService", type + "-----------------executed at " + DateUtils.getToday(DateUtils.BASE_DATE_FORMAT));

                    if (!appUtils.isAppRunning(pakName))
                        AppUtils.newInstance(mContext).luachApp(pakName);
                    if (!appUtils.isAppRunning(pakNameSame) || appUtils.isBackground())
                        AppUtils.newInstance(mContext).luachApp(pakNameSame);
                }
            }

        }).start();

        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);

        int anHour = 60 * 60 * 1000;   // 这是一小时的毫秒数

        long triggerAtTime = SystemClock.elapsedRealtime() + 30*60 * 1000;
        Intent i = new Intent(mContext, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);

        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);

        return super.onStartCommand(intent, flags, startId);

    }

    private void createNotificationChannel() {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = null;

        // 通知渠道的id
        String CHANNEL_ID = "my_channel_01";
        // 用户可以看到的通知渠道的名字.
        CharSequence name = getString(R.string.channel_name);
//         用户可以看到的通知渠道的描述
        String description = getString(R.string.channel_description);
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel mChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
//         配置通知渠道的属性
            mChannel.setDescription(description);
//         设置通知出现时的闪灯（如果 android 设备支持的话）
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
//         设置通知出现时的震动（如果 android 设备支持的话）
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
//         最后在notificationmanager中创建该通知渠道 //
            mNotificationManager.createNotificationChannel(mChannel);

            builder = new Notification.Builder(this, CHANNEL_ID)
                    .setCategory(Notification.CATEGORY_MESSAGE);
        } else {
            builder = new Notification.Builder(this);
        }

        // Create a notification and set the notification channel.
        Notification notification = builder
                .setContentTitle("New Message").setContentText("You've received new messages.")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .build();
        startForeground(1, notification);
        mNotificationManager.notify(1, notification);
    }
}
