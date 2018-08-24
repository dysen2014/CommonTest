package com.dysen.share_dialog;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.util.ListUpdateCallback;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static android.content.Context.NOTIFICATION_SERVICE;


/**
 * @package com.dysen.image
 * @email dy.sen@qq.com
 * created by dysen on 2018/8/20 - 下午6:29
 * @info
 */
public class ScreenShot {
    private static final String TAG = "ScreenShot";

    public static String saveFileName = "";
    public static Activity mActivity;
    private static final int SAVE_AUTHORITY = Context.MODE_PRIVATE;
    private static BottomSheetDialog bottomSheetDialog;
    private static List<App> appList;

    private static Intent shareIntent;
    private static Bitmap baseBitmap, new_bitmap;//需要添加的图片
    private static Uri uri;
    private static CustomPopWindow mCustomPopWindow;
    private static float scaleWidth, scaleHeight;
    private static boolean num = true;
    private static String packageName, className;

    // 获取指定Activity的截屏，保存到png文件
    public static Bitmap takeScreenShot(Activity activity) {

        //View是你需要截图的View
        View view = activity.getWindow().getDecorView();// 获取Activity整个窗口最顶层的View
        view.setDrawingCacheEnabled(true);// 设置控件允许绘制缓存
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();// 获取控件的绘制缓存（快照）

        //获取状态栏高度
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        System.out.println(statusBarHeight);

        //获取屏幕长和高
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = activity.getWindowManager().getDefaultDisplay().getHeight();

        //去掉标题栏
        //Bitmap b = Bitmap.createBitmap(b1, 0, 25, 320, 455);
        baseBitmap = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height - statusBarHeight);
        view.destroyDrawingCache();

        return baseBitmap;
    }

    /**
     * 有水印的保存
     *
     * @param srcBitmap 当前截图的bitmap
     * @return
     */
    public static Bitmap addMarkPic(Bitmap srcBitmap, Bitmap markBitmap) {

        if (markBitmap.getWidth() < srcBitmap.getWidth())
            srcBitmap = scaleWithWH(srcBitmap, markBitmap.getWidth(), srcBitmap.getHeight());
        Bitmap photoMark = Bitmap.createBitmap(srcBitmap.getWidth(), srcBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(photoMark);
        canvas.drawBitmap(srcBitmap, 0, 0, null);
        Bitmap bitmapMark = markBitmap.copy(Bitmap.Config.ARGB_8888, true);
//        bitmapMark = scaleWithWH(bitmapMark, srcBitmap.getWidth(), bitmapMark.getHeight());
        canvas.drawBitmap(bitmapMark, srcBitmap.getWidth() - bitmapMark.getWidth(), srcBitmap.getHeight() - bitmapMark.getHeight(), null);
        canvas.save(Canvas.ALL_SAVE_FLAG);
//        canvas.save();
        canvas.restore();
        new_bitmap = scaleWithWH(photoMark, photoMark.getWidth(), photoMark.getHeight());
        return new_bitmap;
    }

    /**
     * 把一个view转化成bitmap对象
     */
    public static Bitmap getViewBitmap(Activity activity, int layoutId) {

        View view = activity.getLayoutInflater().inflate(layoutId, null);

        int me = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);

        view.measure(me, me);

//        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        view.buildDrawingCache();

        Bitmap bitmap = view.getDrawingCache();

        return bitmap;
    }

    public static Bitmap convertViewToBitmap(View view) {
        int me = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);

        view.measure(me, me);

        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        //利用bitmap生成画布
        Canvas canvas = new Canvas(bitmap);

        //把view中的内容绘制在画布上
        view.draw(canvas);

        return bitmap;
    }

    /**
     * 把图片转成bitmap对象
     *
     * @param context
     * @param vectorDrawableId
     * @return
     */
    public static Bitmap getBitmap(Context context, int vectorDrawableId) {
        Bitmap bitmap = null;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            Drawable vectorDrawable = context.getDrawable(vectorDrawableId);
            bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                    vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            vectorDrawable.draw(canvas);
        } else {
            bitmap = BitmapFactory.decodeResource(context.getResources(), vectorDrawableId);
        }
        return bitmap;
    }

    //保存到sdcard
    public static String savePic(Activity mActivity, Bitmap b) {
        /**
         * 动态获取权限，Android 6.0 新特性，一些保护权限，除了要在AndroidManifest中声明权限，还要使用如下代码动态获取
         */
        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            //验证是否许可权限
            for (String str : permissions) {
                if (mActivity.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    mActivity.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                    return "";
                }
            }
        }
        FileOutputStream fos = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss");

        try {
            //imagePath=/storage/emulated/0/Pictures/Screenshots/Screenshot_20180822-165201.jpg
//            fos = new FileOutputStream(strFileName);
            if (saveFileName.isEmpty()) {
                //按钮截图
                saveFileName = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/Screenshots" + "/Screenshot_" + sdf.format(new Date()) + ".png";
            } else {
                //系统截图
            }
            final File dir = new File(saveFileName);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            if (dir.exists()) {
                dir.delete();
            }
            fos = new FileOutputStream(dir);
            if (null != fos) {
                b.compress(Bitmap.CompressFormat.PNG, 90, fos);
                sendNotification(mActivity);
                fos.flush();
                fos.close();
            }
        } catch (IOException e) {
            Log.e(TAG, "savePic e = ", e);
        }
        return saveFileName;
    }

    private static void sendNotification(Context context) {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        Notification.Builder builder = new Notification.Builder(context);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/*");
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, shareIntent, 0);
        //打开自定义的Activity
        Intent clickIntent = new Intent();
        clickIntent.setClass(context, MainActivity.class);
        MainActivity.code = saveFileName;
        clickIntent.putExtra("code", saveFileName);
//        clickIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        //设置点击跳转
        //如果描述的PendingIntent已经存在，则在产生新的Intent之前会先取消掉当前的
        PendingIntent hangPendingIntent = PendingIntent.getActivity(context, 0, clickIntent, PendingIntent.FLAG_CANCEL_CURRENT);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
//                clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (Build.VERSION.SDK_INT >= 26) {//当sdk版本大于26
            String id = "channel_1";
            String description = "143";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel(id, description, importance);
//            channel.enableLights(true);
//            channel.enableVibration(true);//
            mNotificationManager.createNotificationChannel(channel);
            builder = new Notification.Builder(context, id)
                    .setCategory(Notification.CATEGORY_MESSAGE);
        } else {//当sdk版本小于26

            builder = new Notification.Builder(context);
        }

        builder.setFullScreenIntent(hangPendingIntent, true);
        builder.setSmallIcon(R.mipmap.ic_launcher);

        Notification notification = builder
                .setTicker("正在截屏...")
                .setContentTitle("已捕捉屏幕截图")
                .setContentText("点击以查看截屏")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeFile(saveFileName, new BitmapFactory.Options()))
                .setContentIntent(hangPendingIntent)
                .setAutoCancel(true)
                .build();
        mNotificationManager.notify(0, notification);
    }

    /**
     * 缩放图片
     *
     * @param src
     * @param w
     * @param h
     * @return
     */
    public static Bitmap scaleWithWH(Bitmap src, double w, double h) {
        if (w == 0 || h == 0 || src == null) {
            return src;
        } else {
            // 记录src的宽高
            int width = src.getWidth();
            int height = src.getHeight();
            // 创建一个matrix容器
            Matrix matrix = new Matrix();
            // 计算缩放比例
            float scaleWidth = (float) (w / width);
            float scaleHeight = (float) (h / height);
            // 开始缩放
            matrix.postScale(scaleWidth, scaleHeight);
            // 创建缩放后的图片
            return Bitmap.createBitmap(src, 0, 0, width, height, matrix, true);
        }
    }

    /**
     * dip转pix
     *
     * @param context
     * @param dp
     * @return
     */
    public static int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    /**
     * 通过 pic path获得pic的bitmap对象
     *
     * @param pathString
     * @return
     */
    public static Bitmap getDiskBitmap(String pathString) {

        Bitmap getBbitmap = null;

        try {
            File file = new File(pathString);
            if (file.exists()) {

                getBbitmap = BitmapFactory.decodeFile(pathString, new BitmapFactory.Options());
            }
        } catch (Exception e) {
        }
        return getBbitmap;
    }

    public static void shareAct(Activity activity, String saveFileName) {
        mActivity = activity;
        Uri uri = null;

        try {
            FileInputStream input = new FileInputStream(saveFileName);
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            uri = Uri.parse(MediaStore.Images.Media.insertImage(mActivity.getContentResolver(), bitmap, null, null));
            input.close();
        } catch (Exception e) {
            Log.v(TAG, "shareAct e = ", e);
        }

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/*");
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PackageManager packageManager = mActivity.getPackageManager();
        List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(shareIntent, 0);
        if (!resolveInfo.isEmpty()) {
            List<Intent> targetedShareIntents = new ArrayList<Intent>();
            for (ResolveInfo info : resolveInfo) {
                Intent tarIntent = new Intent(Intent.ACTION_SEND);
                tarIntent.setType("image/*");
                ActivityInfo activityInfo = info.activityInfo;

                // judgments : activityInfo.packageName, activityInfo.name, etc.
                if (checkShareApp(info.activityInfo.name)) {
                    tarIntent.setComponent(new ComponentName(activityInfo.packageName, activityInfo.name));
                    tarIntent.putExtra(Intent.EXTRA_STREAM, uri);
                    tarIntent.setPackage(activityInfo.packageName);
                    targetedShareIntents.add(tarIntent);
                }
            }
            Intent chooserIntent = Intent.createChooser(targetedShareIntents.remove(0), "Select app to share");
            if (chooserIntent == null) {
                return;
            }
            // A Parcelable[] of Intent or LabeledIntent objects as set with
            // putExtra(String, Parcelable[]) of additional activities to place
            // a the front of the list of choices, when shown to the user with a
            // ACTION_CHOOSER.
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toArray(new Parcelable[]{}));

            try {
                mActivity.startActivity(chooserIntent);
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(mActivity, "Can't find share component to share", Toast.LENGTH_SHORT).show();
            }
        }

//        mActivity.startActivity(Intent.createChooser(shareIntent, mActivity.getTitle()));

    }

    public static Bitmap shareAct2(Activity activity, String saveFileName) {
        mActivity = activity;
        try {
            FileInputStream input = new FileInputStream(saveFileName);
            new_bitmap = BitmapFactory.decodeStream(input);
            uri = Uri.parse(MediaStore.Images.Media.insertImage(mActivity.getContentResolver(), new_bitmap, null, null));
            input.close();
        } catch (Exception e) {
            Log.v(TAG, "shareAct e = ", e);
        }
        shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);

        View contentView = LayoutInflater.from(ScreenShot.mActivity).inflate(R.layout.custom_pop, null);
        if (new_bitmap != null) {
            ImageView ivPic = contentView.findViewById(R.id.iv_pic);
            new_bitmap = scaleWithWH(new_bitmap, new_bitmap.getWidth() * 4 / 5, new_bitmap.getHeight() * 4 / 5);

            ivPic.setImageBitmap(new_bitmap);
                imgZoom(ivPic);
        }
//            initBottomDialog(contentView);
//        showBottomsheetDialog(contentView);
        showPopMenu(contentView);
//        timer.start();

        return new_bitmap;
    }

    private static void imgZoom(final ImageView ivPic) {
        DisplayMetrics dm = new DisplayMetrics();//创建矩阵
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = new_bitmap.getWidth();
        int height = new_bitmap.getHeight();
        int w = dm.widthPixels; //得到屏幕的宽度
        int h = dm.heightPixels; //得到屏幕的高度
        scaleWidth = ((float) w) / width;
        scaleHeight = ((float) h) / height;

        ivPic.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {

                    case MotionEvent.ACTION_DOWN:  //当屏幕检测到第一个触点按下之后就会触发到这个事件。
                        if (num == true) {
                            Matrix matrix = new Matrix();
                            matrix.postScale(scaleWidth, scaleHeight);

                            Bitmap newBitmap = Bitmap.createBitmap(new_bitmap, 0, 0, new_bitmap.getWidth(), new_bitmap.getHeight(), matrix, true);
                            ivPic.setImageBitmap(newBitmap);
                            num = false;
                        } else {
                            Matrix matrix = new Matrix();
                            matrix.postScale(1.0f, 1.0f);
                            Bitmap newBitmap = Bitmap.createBitmap(new_bitmap, 0, 0, new_bitmap.getWidth(), new_bitmap.getHeight(), matrix, true);
                            ivPic.setImageBitmap(newBitmap);
                            num = true;
                        }
                        break;
                }


                return false;
            }
        });
    }

    /**
     * 系统弹窗
     */
    private static void showBottomsheetDialog(View contentView) {
        bottomSheetDialog = new BottomSheetDialog(mActivity, R.style.BottomSheetDialog);
        bottomSheetDialog.setContentView(contentView);

        contentView.findViewById(R.id.ll).setBackgroundResource(R.color.whtie);
        if (bottomSheetDialog.isShowing())
            bottomSheetDialog.dismiss();
        bottomSheetDialog.show();
    }

    /**
     * 自定义弹窗
     *
     * @param contentView
     */
    private static void showPopMenu(final View contentView) {
        //创建并显示popWindow
        mCustomPopWindow = new CustomPopWindow.PopupWindowBuilder(mActivity)
                .setView(contentView)
                .enableBackgroundDark(true) //弹出popWindow时，背景是否变暗
                .setBgDarkAlpha(0.7f) // 控制亮度
                .create();
        if (mCustomPopWindow.getPopupWindow().isShowing())
            mCustomPopWindow.dissmiss();
//        mCustomPopWindow.showAtLocation(contentView.findViewById(R.id.iv_pic), Gravity.CENTER, 0, 0);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //处理popWindow 显示内容
                handleLogic(contentView);
            }
        }, 1000);
    }

    /**
     * 处理弹出显示内容、点击事件等逻辑
     *
     * @param contentView
     */
    private static void handleLogic(final View contentView) {
        mCustomPopWindow.showAtLocation(contentView.findViewById(R.id.iv_pic), Gravity.CENTER, 0, 0);
        mCustomPopWindow.getPopupWindow().update();
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new_bitmap = addMarkPic(new_bitmap, getViewBitmap(mActivity, R.layout.add_pic));

                uri = Uri.parse(MediaStore.Images.Media.insertImage(mActivity.getContentResolver(), new_bitmap, null, null));
                //    		sendIntent.setClassName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI");//微信朋友
//    		sendIntent.setClassName("com.tencent.mm", "com.tencent.mm.ui.tools.AddFavoriteUI");//微信收藏
//			sendIntent.setClassName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI");//微信朋友圈，仅支持分享图片
//			sendIntent.setClassName("com.tencent.mobileqq", "cooperation.qqfav.widget.QfavJumpActivity");//保存到QQ收藏
//			sendIntent.setClassName("com.tencent.mobileqq", "cooperation.qlink.QlinkShareJumpActivity");//QQ面对面快传
//			sendIntent.setClassName("com.tencent.mobileqq", "com.tencent.mobileqq.activity.qfileJumpActivity");//传给我的电脑
//			sendIntent.setClassName("com.tencent.mobileqq", "com.tencent.mobileqq.activity.JumpActivity");//QQ好友或QQ群
//			sendIntent.setClassName("com.weico.international", "com.weico.international.activity.compose.SeaComposeActivity");//微博

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                switch (v.getId()) {
                    case R.id.img_share_wx:
                        packageName = "com.tencent.mm";
                        className = "com.tencent.mm.ui.tools.ShareImgUI";
                        break;
                    case R.id.img_share_wxpyq:
                        packageName = "com.tencent.mm";
                        className = "com.tencent.mm.ui.tools.ShareToTimeLineUI";
                        break;
                    case R.id.img_share_qq:
                        packageName = "com.tencent.mobileqq";
                        className = "com.tencent.mobileqq.activity.JumpActivity";
                        break;
                    case R.id.img_share_wb:
                        packageName = "com.weico.international";
                        className = "com.weico.international.activity.compose.SeaComposeActivity";
                        break;
                }

                Intent launchIntentForPackage = mActivity.getPackageManager().getLaunchIntentForPackage(packageName);
                if (launchIntentForPackage == null) {
                    Toast.makeText(mActivity, "应用 未安装", Toast.LENGTH_LONG).show();
                } else {
                    onClode();//点击了分享按钮  分享页面自动隐藏
                    intent.setComponent(new ComponentName(packageName, className));
                    mActivity.startActivity(intent);
                }
            }
        };
        contentView.findViewById(R.id.img_share_wx).setOnClickListener(listener);
        contentView.findViewById(R.id.img_share_wxpyq).setOnClickListener(listener);
        contentView.findViewById(R.id.img_share_qq).setOnClickListener(listener);
        contentView.findViewById(R.id.img_share_wb).setOnClickListener(listener);
    }

    private static void initBottomDialog(View contentView) {
        appList = getShareAppList(mActivity, shareIntent);
        if (appList == null || appList.size() == 0) {
            contentView.findViewById(R.id.tv_no_app).setVisibility(View.VISIBLE);
        } else {
            contentView.findViewById(R.id.tv_no_app).setVisibility(View.GONE);
        }
        AppShareAdapter appShareAdapter = new AppShareAdapter(mActivity, appList);
        appShareAdapter.setClickListener(new AppShareAdapter.OnClickListener() {
            @Override
            public void onClick(int position) {
                onClode();//点击了分享按钮  分享页面自动隐藏
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setComponent(new ComponentName(appList.get(position).getPackageName(), appList.get(position).getMainClassName()));
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                mActivity.startActivity(intent);
            }
        });
        appShareAdapter.setLongClickListener(new AppShareAdapter.OnLongClickListener() {
            @Override
            public void onLongClick(int position) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.parse("package:" + appList.get(position).getPackageName()));
                mActivity.startActivity(intent);
            }
        });
        RecyclerView rv_appList = contentView.findViewById(R.id.rv_appList);
        if (rv_appList != null) {
            rv_appList.setLayoutManager(new GridLayoutManager(mActivity, 3));
            rv_appList.setAdapter(appShareAdapter);
        }
    }

    static CountDownTimer timer = new CountDownTimer(5000, 10) {

        @Override
        public void onTick(long millisUntilFinished) {
        }

        @Override
        public void onFinish() {
            mCustomPopWindow.dissmiss();
        }
    };


//    		sendIntent.setClassName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI");//微信朋友
//    		sendIntent.setClassName("com.tencent.mm", "com.tencent.mm.ui.tools.AddFavoriteUI");//微信收藏
//			sendIntent.setClassName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI");//微信朋友圈，仅支持分享图片
//			sendIntent.setClassName("com.tencent.mobileqq", "cooperation.qqfav.widget.QfavJumpActivity");//保存到QQ收藏
//			sendIntent.setClassName("com.tencent.mobileqq", "cooperation.qlink.QlinkShareJumpActivity");//QQ面对面快传
//			sendIntent.setClassName("com.tencent.mobileqq", "com.tencent.mobileqq.activity.qfileJumpActivity");//传给我的电脑
//			sendIntent.setClassName("com.tencent.mobileqq", "com.tencent.mobileqq.activity.JumpActivity");//QQ好友或QQ群
//			sendIntent.setClassName("com.weico.international", "com.weico.international.activity.compose.SeaComposeActivity");//微博　

    /**
     * 获取当前系统里 所以支持 分享的App
     *
     * @param context
     * @param intent
     * @return
     */
    public static List<App> getShareAppList(Context context, Intent intent) {
        List<App> appList = new ArrayList<>();

        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(intent, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
        if (resolveInfoList == null || resolveInfoList.size() == 0) {
            return null;
        } else {
            for (ResolveInfo resolveInfo : resolveInfoList) {
                if (checkShareApp(resolveInfo.activityInfo.name)) {
                    App appInfo = new App(resolveInfo.loadLabel(packageManager).toString(), resolveInfo.activityInfo.packageName,
                            resolveInfo.activityInfo.name, resolveInfo.loadIcon(packageManager));

                    appList.add(appInfo);
                }
            }
        }
        appList = removeDuplicate(appList);
        return appList;
    }

    /**
     * 需要保留分享的App 需在此方法中添加，类名
     *
     * @param appName
     * @return
     */
    private static boolean checkShareApp(String appName) {
        if (appName.equals("com.tencent.mm.ui.tools.ShareImgUI")
                || appName.equals("com.tencent.mobileqq.activity.JumpActivity")
                || appName.contains("com.weico.international.activity.compose.SeaComposeActivity")
//                || appName.contains("com.")
                )
            return true;
        else
            return false;
    }


    /**
     * 去除list<T>重复的数据
     *
     * @param list
     * @param <T>
     * @return
     */
    public static <T> List<T> removeDuplicate(List<T> list) {
        Set set = new LinkedHashSet<T>();
        set.addAll(list);
        list.clear();
        list.addAll(set);
        return list;
    }

    public static void onClode() {
        if (mCustomPopWindow != null)
            mCustomPopWindow.dissmiss();
        if (bottomSheetDialog != null)
            bottomSheetDialog.dismiss();
    }

    public static void share(Activity act) {
        mActivity = act;
        ScreenShot.takeScreenShot(mActivity);//生成截图
        savePic(mActivity, baseBitmap);//保存截图
        shareAct2(mActivity, saveFileName);//分享截图
    }
}
