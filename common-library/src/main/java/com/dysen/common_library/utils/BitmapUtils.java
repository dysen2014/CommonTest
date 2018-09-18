package com.dysen.common_library.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.DrawableRes;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.RequestOptions;
import com.dysen.common_library.R;

import java.io.File;
import java.io.FilenameFilter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @package com.dysen.common_library.utils
 * @email dy.sen@qq.com
 * created by dysen on 2018/8/31 - 下午3:35
 * @info
 */
public class BitmapUtils {

    /**
     * 图片缩放
     *
     * @param view
     */
    static boolean num;
    static Bitmap baseBitmap, newBitmap;

    public static void viewZoom(final Activity activity, final ImageView ivPic, Bitmap bitmap, @DrawableRes final int resImgId) {
        if (bitmap == null)
            baseBitmap = getResBitmap(activity, resImgId);
        else
            baseBitmap = bitmap;
        DisplayMetrics dm = new DisplayMetrics();//创建矩阵
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        final int width = baseBitmap.getWidth();
        final int height = baseBitmap.getHeight();
        int w = dm.widthPixels; //得到屏幕的宽度
        int h = dm.heightPixels; //得到屏幕的高度
        final float scaleWidth = ((float) w) / width;
        final float scaleHeight = ((float) h) / height;

        ivPic.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {

                    case MotionEvent.ACTION_DOWN:  //当屏幕检测到第一个触点按下之后就会触发到这个事件。
                        Matrix matrix = new Matrix();
                        if (num == false) {
                            matrix.postScale(scaleWidth, scaleHeight);
                            num = true;
                        } else {
                            matrix.postScale(1.0f, 1.0f);
                            num = false;
                        }
                        newBitmap = Bitmap.createBitmap(baseBitmap, 0, 0, baseBitmap.getWidth(), baseBitmap.getHeight(), matrix, true);
//                        ivPic.setImageBitmap(newBitmap);
                        Glide.with(activity.getBaseContext())
                                .load(newBitmap)
                                .apply(new RequestOptions().circleCropTransform())
                                .into(ivPic);
                        break;
                }

                return false;
            }
        });
    }

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
        Bitmap baseBitmap = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height - statusBarHeight);
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
        canvas.save();
        canvas.restore();
        Bitmap new_bitmap = scaleWithWH(photoMark, photoMark.getWidth(), photoMark.getHeight());
        return new_bitmap;
    }

    /**
     * 把一个layout转化成bitmap对象
     */
    public static Bitmap getLayoutBitmap(Activity activity, int layoutId) {

        View view = activity.getLayoutInflater().inflate(layoutId, null);

        int me = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);

        view.measure(me, me);

        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        view.buildDrawingCache();

        Bitmap bitmap = view.getDrawingCache();

        return bitmap;
    }

    /**
     * 把一个view转化成bitmap对象
     */
    public static Bitmap getViewBitmap(View view) {
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
    public static Bitmap getResBitmap(Context context, @DrawableRes int vectorDrawableId) {
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

    /**
     * 通过 pic path获得pic的bitmap对象
     *
     * @param pathString
     * @return
     */
    public static Bitmap getDiskBitmap(String pathString) {
        Bitmap getBbitmap = null;
        //Bitmap复用  减小内存开销
        try {
            File file = new File(pathString);
            if (file.exists()) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                if (getBbitmap == null) {
                    options.inMutable = true;
                    getBbitmap = BitmapFactory.decodeFile(pathString, options);
                } else {
                    // 使用inBitmap属性，这个属性必须设置；
                    options.inBitmap = getBbitmap;
                    options.inMutable = true;
                    getBbitmap = BitmapFactory.decodeFile(pathString, options);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                    System.out.println(getBbitmap+"========bitmap=====1======="+getBbitmap.getAllocationByteCount());
                } else {
//                    System.out.println(getBbitmap+"========bitmap=====2======="+getBbitmap.getByteCount());
                }
            }
        } catch (Exception e) {
        }
        return getBbitmap;
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
     * 保存bitmap 对象到本地
     *
     * @param bitmap
     * @param path
     * @return
     */
    public static String saveBitmap(Bitmap bitmap, String path) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss");
        String dirPath = FileUtils.getPath(path);
        if (!FileUtils.isFolderExist(dirPath))
            FileUtils.getFile(dirPath).mkdirs();
        String saveFileName = dirPath + sdf.format(new Date()) + ".jpg";
        FileUtils.saveBitmap(bitmap, FileUtils.getFile(saveFileName));
        return saveFileName;
    }

    public static String saveBitmap(Activity activity, Bitmap bitmap) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss");
        File file = new File(activity.getExternalFilesDir(null).getAbsolutePath() + "/ZB/Screenshots/");
        FileUtils.checkDirectory(file);
        String path = file.getAbsolutePath() + "/Screenshot_" + sdf.format(new Date()) + ".jpg";
        FileUtils.saveBitmap(bitmap, new File(path));
        return path;
    }

    /**
     * 系统方式分享
     *
     * @param activity
     * @param saveFileName
     */
    public static void shareAct(Activity activity, String saveFileName) {
        Bitmap bitmap = BitmapUtils.getDiskBitmap(saveFileName);
        // 默认保存添加二维码后的截图(/storage/emulated/0/Pictures/1535686862202.jpg)
        Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(activity.getContentResolver(), bitmap, null, null));
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/*");
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(Intent.createChooser(shareIntent, activity.getTitle()));
    }

    public static void shareAct2(Activity activity, String saveFileName) {
        activity = activity;
        Uri uri = null;

        Bitmap bitmap = BitmapUtils.getDiskBitmap(saveFileName);
        uri = Uri.parse(MediaStore.Images.Media.insertImage(activity.getContentResolver(), bitmap, null, null));

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/*");
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PackageManager packageManager = activity.getPackageManager();
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
                activity.startActivity(chooserIntent);
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(activity, "Can't find share component to share", Toast.LENGTH_SHORT).show();
            }
        }
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
     * 通过uri获取文件的绝对路径
     *
     * @param context
     * @param uri
     * @return
     */
    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    /**
     * 文件删除
     *
     * @param autoClearDay 文件保存天数
     */
    public static void autoClear(String dirPath, final int autoClearDay) {
        FileUtils.delete(dirPath, new FilenameFilter() {

            @Override
            public boolean accept(File file, String filename) {
                String s = FileUtils.getFileNameWithoutExtension(filename);
                int day = autoClearDay < 0 ? autoClearDay : -1 * autoClearDay;
                String date = DateUtils.getOtherDay(day);
                if (s.contains("Screenshot_")) {
                    s = s.substring(s.indexOf("_") + 1, s.indexOf("-"));
                    String ss = DateUtils.dateSimpleFormat(DateUtils.getOtherFormat(day), DateUtils.SHORT_DATE_FORMAT);
                    return ss.compareTo(s) >= 0;
                }
                if (FormatUtil.isNumeric(s)) {
                    String newStr = DateUtils.getNormalDateString(Long.valueOf(s));//把毫秒数转成 "yyyy-MM-dd 格式
                    return date.compareTo(newStr) >= 0;
                } else
                    return false;
            }
        });
    }
}
