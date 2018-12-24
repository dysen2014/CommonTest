package com.dysen.common_library.receiver;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.dysen.common_library.service.MonitorService;
import com.dysen.common_library.utils.LogUtils;

/**
 * 系统重启事件监听
 * <p>
 * Created by Luis on 2018/3/2.
 */

public class BootReceiver extends BroadcastReceiver {

    private static final String BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED";
    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtils.i("BootReceiver", context+"-----------------"+intent.getAction());
        if (BOOT_COMPLETED.equals(intent.getAction())) {//开机启动
            // 启动监控服务
            context.startService(new Intent(context, MonitorService.class));
            // 开机之后必须关闭定时开关机，不然会一直重复
            // 关闭定时开关机
//            int fileID = posix.open("/dev/McuCom", 3, 438);
//            if (fileID < 0)
//                LogUtils.d("BootReceiver", "打开节点失败");
//            else
//                posix.poweronoff((byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, fileID);
            // 启动MainActivity
            Intent intent1 = new Intent();
            intent1.setAction(Intent.ACTION_MAIN);
            intent1.addCategory(Intent.CATEGORY_LAUNCHER);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            intent1.setComponent(new ComponentName("com.integrated.edu.kpad","com.integrated.edu.kpad.LoginActivity"));
            context.startActivity(intent1);
        }
    }

}
