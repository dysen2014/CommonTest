package com.dysen.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * @package com.dysen.alarm
 * @email dy.sen@qq.com
 * created by dysen on 2018/12/24 - 4:32 PM
 * @info
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent i = new Intent(context, LongRunningService.class);

        context.startService(i);

    }
}
