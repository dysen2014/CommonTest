package com.dysen.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import java.util.Date;

public class LongRunningService extends Service {
    public LongRunningService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override

    public int onStartCommand(Intent intent, int flags, int startId) {

        new Thread(new Runnable() {

            @Override

            public void run() {

//                Log.d("LongRunningService", "executed at " + new Date().toString());
                Log.d("LongRunningService", "executed at " + DateUtils.getToday());

            }

        }).start();

        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);

        int anHour = 60 * 60 * 1000;   // 这是一小时的毫秒数

        long triggerAtTime = SystemClock.elapsedRealtime() + 5*1000;
        Intent i = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);

        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);

        return super.onStartCommand(intent, flags, startId);

    }
}
