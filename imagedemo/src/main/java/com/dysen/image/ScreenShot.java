package com.dysen.image;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.dysen.common_library.utils.LogUtils;
import com.dysen.common_library.utils.Tools;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * @package com.dysen.image
 * @email dy.sen@qq.com
 * created by dysen on 2018/8/20 - 下午6:29
 * @info
 */
public class ScreenShot {
    private static final String TAG = "ScreenShot";


    private static final int SAVE_AUTHORITY = Context.MODE_PRIVATE;

    // 获取指定Activity的截屏，保存到png文件
    private static Bitmap takeScreenShot(Activity activity) {

//View是你需要截图的View
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();


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
        Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height - statusBarHeight);
        view.destroyDrawingCache();
        Bitmap bitmap = ImageUtils2.addPic(activity, b);
        return bitmap;
    }

    //保存到sdcard
    private static void savePic(Activity act, Bitmap b, String strFileName) {
        FileOutputStream fos = null;
        try {
//            fos = new FileOutputStream(strFileName);
            fos = act.openFileOutput(strFileName, SAVE_AUTHORITY);
            if (null != fos) {
                b.compress(Bitmap.CompressFormat.WEBP, 90, fos);
                fos.flush();
                fos.close();
            }
        } catch (IOException e) {
            LogUtils.e(TAG, "savePic e = ", e);
        }
    }


    private static void shareAct(Activity act, String fileName, String text) {

        Uri uri = null;

        try {
            FileInputStream input = act.openFileInput(fileName);
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            uri = Uri.parse(MediaStore.Images.Media.insertImage(act.getContentResolver(), bitmap, null, null));
            input.close();
        } catch (Exception e) {
            LogUtils.v(TAG, "shareAct e = ", e);
        }

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/*");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "好友推荐");
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        act.startActivity(Intent.createChooser(shareIntent, act.getTitle()));

//        Intent shareIntent = new Intent(Intent.ACTION_SEND);
//        File file = new File(absolutePath);
//        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
//        shareIntent.setType("image/jpeg");
//        act.startActivity(Intent.createChooser(shareIntent, act.getTitle()));
//
//
//        Intent intent = new Intent(Intent.ACTION_SEND);
//        intent.setType("image/*");//intent.setType("text/plain");
//        intent.putExtra(Intent.EXTRA_SUBJECT, "好友推荐");
//        intent.putExtra(Intent.EXTRA_TEXT, text);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(Intent.createChooser(intent, getTitle()));
    }

    private static void shareAct2(Activity act, String fileName, String text) {

        Uri uri = null;

        try {
            FileInputStream input = act.openFileInput(fileName);
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            uri = Uri.parse(MediaStore.Images.Media.insertImage(act.getContentResolver(), bitmap, null, null));
            input.close();
        } catch (Exception e) {
            LogUtils.v(TAG, "shareAct e = ", e);
        }

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/*");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "好友推荐");
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        List<ResolveInfo> resolveInfos = act.getPackageManager().queryIntentActivities(shareIntent, PackageManager.MATCH_DEFAULT_ONLY);
        if (!resolveInfos.isEmpty()) {
            List<Intent> targetedShareIntents = new ArrayList<Intent>();
            List<Intent> intents = new ArrayList<>();
            for (ResolveInfo resolveInfo : resolveInfos) {

                if (resolveInfo.activityInfo.packageName.contains("tencent.mm") || resolveInfo.activityInfo.name.contains("tencent.mm")
                        || resolveInfo.activityInfo.packageName.contains("tencent.mobileqq") || resolveInfo.activityInfo.name.contains("tencent.mobileqq")) {
                    shareIntent.setPackage(resolveInfo.activityInfo.packageName);
                    targetedShareIntents.add(shareIntent);
                    intents = Tools.removeDuplicate(targetedShareIntents);
                    LogUtils.e("packageName", targetedShareIntents.size()+"packageName:" + resolveInfo.activityInfo.packageName);
                } else
                    continue;
            }
            Intent chooserIntent = Intent.createChooser(intents.remove(0), "Select app to share");
            if (chooserIntent == null) {
                return;
            }
            try {
                chooserIntent.putExtra(Intent.EXTRA_STREAM, uri);// 分享的内容;
                act.startActivity(chooserIntent);
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(act, "Can't find share component to share", Toast.LENGTH_SHORT).show();
            }
        }
//        act.startActivity(Intent.createChooser(shareIntent, act.getTitle()));

//        Intent shareIntent = new Intent(Intent.ACTION_SEND);
//        File file = new File(absolutePath);
//        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
//        shareIntent.setType("image/jpeg");
//        act.startActivity(Intent.createChooser(shareIntent, act.getTitle()));
//
//
//        Intent intent = new Intent(Intent.ACTION_SEND);
//        intent.setType("image/*");//intent.setType("text/plain");
//        intent.putExtra(Intent.EXTRA_SUBJECT, "好友推荐");
//        intent.putExtra(Intent.EXTRA_TEXT, text);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(Intent.createChooser(intent, getTitle()));
    }
    private static void shareAct3(Activity act, String fileName, String text) {

        Uri uri = null;

        try {
            FileInputStream input = act.openFileInput(fileName);
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            uri = Uri.parse(MediaStore.Images.Media.insertImage(act.getContentResolver(), bitmap, null, null));
            input.close();
        } catch (Exception e) {
            LogUtils.v(TAG, "shareAct e = ", e);
        }

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/*");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "好友推荐");
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //过滤出需要分享到对应的平台：微信好友、朋友圈、QQ好友。  可自行修改
        String[] targetApp = new String[]{"com.tencent.mm", "com.tencent.mm", "com.tencent.mobileqq"};
        if (targetApp.length > 0) {
            List<Intent> targetedShareIntents = new ArrayList<Intent>();
            List<Intent> intents = new ArrayList<>();
            for (int i = 0; i < targetApp.length; i++) {

//                    shareIntent.setPackage(targetApp[i]);
                shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                shareIntent.setType("image/*");
                    targetedShareIntents.add(shareIntent);
                    intents = Tools.removeDuplicate(targetedShareIntents);
                    LogUtils.e("packageName", targetedShareIntents.size()+"packageName:" + targetApp[i]);
            }
            Intent chooserIntent = Intent.createChooser(intents.remove(0), "Select app to share");
            if (chooserIntent == null) {
                return;
            }
            try {
                chooserIntent.putExtra(Intent.EXTRA_STREAM, uri);// 分享的内容;
                act.startActivity(chooserIntent);
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(act, "Can't find share component to share", Toast.LENGTH_SHORT).show();
            }
        }
//        act.startActivity(Intent.createChooser(shareIntent, act.getTitle()));

//        Intent shareIntent = new Intent(Intent.ACTION_SEND);
//        File file = new File(absolutePath);
//        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
//        shareIntent.setType("image/jpeg");
//        act.startActivity(Intent.createChooser(shareIntent, act.getTitle()));
//
//
//        Intent intent = new Intent(Intent.ACTION_SEND);
//        intent.setType("image/*");//intent.setType("text/plain");
//        intent.putExtra(Intent.EXTRA_SUBJECT, "好友推荐");
//        intent.putExtra(Intent.EXTRA_TEXT, text);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(Intent.createChooser(intent, getTitle()));
    }

    public static void share(Activity act, String text) {
        String saveFileName = "share_pic.jpg";
        savePic(act, ScreenShot.takeScreenShot(act), saveFileName);
        shareAct3(act, saveFileName, text);
    }

}
