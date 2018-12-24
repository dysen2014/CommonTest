package com.dysen.common_library.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.dysen.common_library.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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
    static String TAG = "BitmapUtils";
    static String ScreenshotPrefixName = "ZBApp_";

    /**
     * 设置截图图片的前缀名称 (默认 ctivity activity, )
     *
     * @param screenshotPrefixName
     */
    public static void setScreenshotPrefixName(String screenshotPrefixName) {
        ScreenshotPrefixName = screenshotPrefixName + "_";
    }

    /**
     * 图片缩放
     *
     * @param view
     */
    static boolean num;
    static Bitmap baseBitmap, newBitmap;

    public static void viewZoom(Activity activity, final ImageView ivPic, Bitmap bitmap, @DrawableRes final int resImgId) {
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
                        ivPic.setImageBitmap(newBitmap);
                        break;
                }

                return false;
            }
        });
    }

    // 获取指定Activity的截屏，保存到png文件
    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
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

    public static Bitmap addMarkPic2(Bitmap srcBitmap, Bitmap markBitmap, Bitmap magicBitmap, int move_range) {

        Bitmap photoMark = Bitmap.createBitmap(srcBitmap.getWidth(), srcBitmap.getHeight() + markBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(photoMark);
        canvas.drawBitmap(srcBitmap, 0, 0, null);
        Bitmap bitmapMark = markBitmap.copy(Bitmap.Config.ARGB_8888, true);
        canvas.drawBitmap(bitmapMark, 0, srcBitmap.getHeight(), null);
        canvas.drawBitmap(magicBitmap, 0, srcBitmap.getHeight() + move_range, null);
        canvas.save();
        canvas.restore();
        Bitmap new_bitmap = scaleWithWH(photoMark, photoMark.getWidth(), photoMark.getHeight());
        return new_bitmap;
    }

    public static Bitmap addNewsPic(Bitmap srcBitmap, Bitmap markBitmap, int topHeight) {

        Bitmap photoMark = Bitmap.createBitmap(srcBitmap.getWidth(), srcBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(photoMark);
        canvas.drawBitmap(srcBitmap, 0, 0, null);
        Bitmap bitmapMark = markBitmap.copy(Bitmap.Config.ARGB_8888, true);
        canvas.drawBitmap(bitmapMark, 0, topHeight, null);
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

    static Bitmap bitmap;

    public static Bitmap getUrl2BitMap(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL imageurl = null;

                try {
                    imageurl = new URL(url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try {
                    HttpURLConnection conn = (HttpURLConnection) imageurl.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

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
    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
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

    public static Bitmap compressScale(Bitmap bitmap, float scale) {
        Matrix matrix = new Matrix();
        matrix.setScale(scale, scale);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);

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
     * 图片的缩放方法
     *
     * @param bitmap  ：源图片资源
     * @param maxSize ：图片允许最大空间  单位:KB
     * @return
     */
    public static Bitmap getZoomImage(Bitmap bitmap, double maxSize) {
        if (null == bitmap) {
            return null;
        }
        if (bitmap.isRecycled()) {
            return null;
        }

        // 单位：从 Byte 换算成 KB
        double currentSize = bitmapToByteArray(bitmap, false).length / 1024;
        // 判断bitmap占用空间是否大于允许最大空间,如果大于则压缩,小于则不压缩
        while (currentSize > maxSize) {
            // 计算bitmap的大小是maxSize的多少倍
            double multiple = currentSize / maxSize;
            // 开始压缩：将宽度和高度压缩掉对应的平方根倍
            // 1.保持新的宽度和高度，与bitmap原来的宽高比率一致
            // 2.压缩后达到了最大大小对应的新bitmap，显示效果最好
            bitmap = getZoomImage(bitmap, bitmap.getWidth() / Math.sqrt(multiple), bitmap.getHeight() / Math.sqrt(multiple));
            currentSize = bitmapToByteArray(bitmap, false).length / 1024;
        }
        return bitmap;
    }

    /**
     * 图片的缩放方法
     *
     * @param orgBitmap ：源图片资源
     * @param newWidth  ：缩放后宽度
     * @param newHeight ：缩放后高度
     * @return
     */
    public static Bitmap getZoomImage(Bitmap orgBitmap, double newWidth, double newHeight) {
        if (null == orgBitmap) {
            return null;
        }
        if (orgBitmap.isRecycled()) {
            return null;
        }
        if (newWidth <= 0 || newHeight <= 0) {
            return null;
        }

        // 获取图片的宽和高
        float width = orgBitmap.getWidth();
        float height = orgBitmap.getHeight();
        // 创建操作图片的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(orgBitmap, 0, 0, (int) width, (int) height, matrix, true);
        return bitmap;
    }

    /**
     * 质量压缩
     *
     * @param bitmap
     * @param quality 0 --- 100
     * @return
     */
    public static Bitmap qualityCompress(Bitmap bitmap, int quality) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //这里100表示不压缩，把压缩后的数据存放到baos中
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 95;
        baos.reset();
        //每次减少5%质量
        if (quality > 5) {//避免出现options<=0
            options = quality;
        } else {
            options = 5;
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);


        // 把压缩后的数据baos存放到ByteArrayInputStream中
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        // 把ByteArrayInputStream数据生成图片
        Bitmap bit = BitmapFactory.decodeStream(isBm, null, null);
        return bit;
    }

    /**
     * bitmap转换成byte数组
     *
     * @param bitmap
     * @param needRecycle
     * @return
     */
    public static byte[] bitmapToByteArray(Bitmap bitmap, boolean needRecycle) {
        if (null == bitmap) {
            return null;
        }
        if (bitmap.isRecycled()) {
            return null;
        }

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bitmap.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            LogUtils.e(TAG, e.toString());
        }
        return result;
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
     *
     * @param bitmap
     * @param width
     * @param height
     * @return 裁剪长图分割返回
     */
    public static List<Bitmap> cropBitmap(@NonNull Bitmap bitmap, int width, int height) {
        List<Bitmap> newBitmaps = new ArrayList<>();
        int w = bitmap.getWidth(); // 得到图片的宽，高
        int h = bitmap.getHeight();
        int cropWidth = width;// 裁切后所取的正方形区域边长
        int cropHeight = height;
        for (int i = 0; i < h; i += cropHeight) {
            newBitmaps.add(Bitmap.createBitmap(bitmap, 0, i, cropWidth, ((h - i) < cropHeight ? (h - i) : cropHeight), null, false));
        }
        return newBitmaps;
    }

    /**
     * 保存bitmap 对象到本地
     * 默认保存到Pictures目录下， 也可自定义目录
     *
     * @param bitmap
     * @param path
     * @return
     */
    public static String saveBitmap(Activity activity, Bitmap bitmap, String path, boolean isTip) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss");
        File file = new File(path.isEmpty() || path == null ? Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/ZB/Screenshots/" : path);
        FileUtils.checkDirectory(file);
        String saveFileName = file.getAbsolutePath() + "/" + ScreenshotPrefixName + sdf.format(new Date()) + ".jpg";
        file = new File(saveFileName);
        boolean b = FileUtils.saveBitmap(bitmap, file);
//        if (isTip)
//            Tools.toast(b ? Tools.getString(R.string.share_save_success) : Tools.getString(R.string.share_save_failed));
        notifyImageMedia(activity, file);
        return saveFileName;
    }

    /**
     * 保存到应用目录下
     *
     * @param activity
     * @param bitmap
     * @param isTip
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.FROYO)
    public static String saveBitmap(Activity activity, Bitmap bitmap, boolean isTip) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss");
        File file = new File(activity.getExternalFilesDir(null).getAbsolutePath() + "/ZB/Screenshots/");
        FileUtils.checkDirectory(file);
        String path = file.getAbsolutePath() + "/" + ScreenshotPrefixName + sdf.format(new Date()) + ".jpg";
        file = new File(path);
        boolean b = FileUtils.saveBitmap(bitmap, file);
        if (isTip)
            Tools.toast(b ? Tools.getString(R.string.share_save_success) : Tools.getString(R.string.share_save_failed));
//        notifyImageMedia(activity, file);
        return path;
    }

    /**
     * 保存图片到本地，通知系统图库
     * @param context
     * @param file
     */
    private static void notifyImageMedia(Context context, File file) {

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), file.getName(), null);
            Tools.toast(Tools.getString(R.string.share_save_success));
        } catch (FileNotFoundException e) {
            Tools.toast(Tools.getString(R.string.share_save_failed));
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.fromFile(new File(file.getPath()))));
    }


    public static void actionSendApp(Activity activity) {

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PackageManager packageManager = activity.getPackageManager();
        List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(shareIntent, 0);
        if (!resolveInfo.isEmpty()) {
            List<Intent> targetedShareIntents = new ArrayList<Intent>();
            for (ResolveInfo info : resolveInfo) {
                LogUtils.e("dysen", info.activityInfo.packageName + "====" + info.activityInfo.name);
            }
        }
    }
}
