package com.dysen.common_library.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.dysen.common_library.utils.LogUtils;

/**
 * Created by integrated on 2018/5/17.
 */

public class AlarmService extends Service {

    public static String ACTION_ALARM = "action_alarm";
    private Handler mHanler = new Handler(Looper.getMainLooper());

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mHanler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(AlarmService.this, "闹钟来啦", Toast.LENGTH_SHORT).show();
                LogUtils.d("--------------"+"闹钟来啦");
//                new MainActivity().checkAttendanceConfig();
            }
        });
        return super.onStartCommand(intent, flags, startId);
    }
}
