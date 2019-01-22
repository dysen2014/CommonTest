package com.dysen.common_library.service;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.webkit.MimeTypeMap;

import com.dysen.common_library.utils.LogUtils;

import java.io.File;

/**
 * Created by integrated on 2018/5/3.
 */

public class DownloadService extends Service {

    private DownloadManager downloadManager;
    private long mTaskId;
    private String downloadPath;
    String versionUrl, versionName;
    private Context context;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
//        return new MyBinder();
        return null;
    }

    public class MyBinder extends Binder {//为了再activity里得到我们的 DownloadService对象
        public DownloadService getMyService(){
            return DownloadService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();
//        versionUrl = (String) SharedPreUtils.getInstance(context).get(BaseAppContext.APP_DOWNLOAD_URL, "");
//        versionName = (String) SharedPreUtils.getInstance(context).get(BaseAppContext.APP_VERSION_NAME, "");

        downloadAPK(versionUrl, versionName);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 下载文件
     * @param downloadUrl 下载的url
     * @param downloadPath 文件保存路径(缺省是根目录) 前后都需要加上/ 如 "/Integrated/kpad/download/fengcai/"
     * @param fileName  文件名
     */
    public void downloadFile(String downloadUrl, String downloadPath, String fileName) {
        //创建下载任务,downloadUrl就是下载链接
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadUrl));
        request.setAllowedOverRoaming(false);//漫游网络是否可以下载

        //指定下载路径和下载文件名
//        request.setDestinationInExternalPublicDir("/Integrated/kpad/download/fengcai/", fileName);//也可以自己制定下载路径
        request.setDestinationInExternalPublicDir(downloadPath, fileName);
//        request.setDestinationInExternalFilesDir(downloadPath, fileName);

        //获取下载管理器
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        //将下载任务加入下载队列，否则不会进行下载
        downloadManager.enqueue(request);

    }

    //使用系统下载器下载
    public void downloadAPK(String versionUrl, String versionName) {
        this.versionName = versionName;
        //创建下载任务
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(versionUrl));
        request.setAllowedOverRoaming(false);//漫游网络是否可以下载

        //设置文件类型，可以在下载结束后自动打开该文件
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(versionUrl));
        request.setMimeType(mimeString);

        //在通知栏中显示，默认就是显示的
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setVisibleInDownloadsUi(true);

        //sdcard的目录下的download文件夹，必须设置
        request.setDestinationInExternalPublicDir("/download/", versionName);
        //request.setDestinationInExternalFilesDir(),也可以自己制定下载路径

        //将下载请求加入下载队列
        downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        //加入下载队列后会给该任务返回一个long型的id，
        //通过该id可以取消任务，重启任务等等，看上面源码中框起来的方法
        mTaskId = downloadManager.enqueue(request);

        //注册广播接收者，监听下载状态
        context.registerReceiver(receiver,
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    //广播接受者，接收下载状态
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            checkDownloadStatus();//检查下载状态
        }
    };

    //检查下载状态
    private void checkDownloadStatus() {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(mTaskId);//筛选下载任务，传入任务ID，可变参数
        Cursor c = downloadManager.query(query);
        if (c.moveToFirst()) {
            int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
            switch (status) {
                case DownloadManager.STATUS_PAUSED:
                    LogUtils.i(">>>下载暂停");
//                    Toast.makeText(context, ">>>下载暂停", Toast.LENGTH_LONG).show();
                case DownloadManager.STATUS_PENDING:
                    LogUtils.i(">>>下载延迟");
//                    Toast.makeText(context, ">>>下载延迟", Toast.LENGTH_LONG).show();
                case DownloadManager.STATUS_RUNNING:
                    LogUtils.i(">>>正在下载");
//                    Toast.makeText(context, ">>>正在下载", Toast.LENGTH_LONG).show();
                    break;
                case DownloadManager.STATUS_SUCCESSFUL:
                    LogUtils.i(">>>下载完成");
//                    Toast.makeText(context, ">>>下载完成", Toast.LENGTH_LONG).show();
                    //下载完成安装APK
                    downloadPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()
                            + File.separator + versionName;
                    installAPK(new File(downloadPath));
                    break;
                case DownloadManager.STATUS_FAILED:
                    LogUtils.i(">>>下载失败");
//                    Toast.makeText(context, ">>>下载失败", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }

    //下载到本地后执行安装
    protected void installAPK(File file) {
        if (!file.exists()) return;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.parse("file://" + file.toString());
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        //在服务中开启activity必须设置flag,后面解释
        /**
         * https://blog.csdn.net/fenggering/article/details/52880980?locationNum=4&fps=1
         * 如果一个外部的Activity Context调用startActivity方法，那么，Intent对象必须包含 FLAG_ACTIVITY_NEW_TASK标志，这是因为，
         * 待创建的Activity并没有从一个已经存在的Activity启动（任务栈中并没有此Activity），它并没有已经存在的任务，
         * 因此它需要被放置在自己独立的任务中（也就是在任务栈中新建一个任务）*/
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
