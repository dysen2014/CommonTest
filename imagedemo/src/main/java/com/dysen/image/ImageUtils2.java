package com.dysen.image;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.dysen.common_library.utils.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @package com.dysen.image
 * @email dy.sen@qq.com
 * created by dysen on 2018/8/20 - 上午11:00
 * @info
 */
public class ImageUtils2 {
    private Context mContext;

    public ImageUtils2() {
    }

    public ImageUtils2(Context mContext) {
        this.mContext = mContext;
    }
    private static String newFilePath = "";


    public static ImageUtils2 newInstance(Context mContext) {
        mContext = mContext;
        ImageUtils2 imgUtils = new ImageUtils2();
        return imgUtils;
    }
    /**
     * 定义动画
     * @param mBg 父级容器
     * @param mImage 将要显示的图片控件
     * @param bitmap 图片
     */
    public  void startAnim(final LinearLayout mBg, final ImageView mImage, final Bitmap bitmap){
        //设置为半透明
        mBg.setBackgroundColor(Color.parseColor("#e0000000"));

        PropertyValuesHolder values1=PropertyValuesHolder.ofFloat("alpha", 0f,1f,0.5f);
        PropertyValuesHolder values2=PropertyValuesHolder.ofFloat("scaleX", 1,3/4f);
        PropertyValuesHolder values3=PropertyValuesHolder.ofFloat("scaleY", 1,3/4f);
        ObjectAnimator animator=ObjectAnimator.ofPropertyValuesHolder(mImage, values1,values2,values3);
        animator.setDuration(1000);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.start();

        animator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator arg0) {

            }

            @Override
            public void onAnimationRepeat(Animator arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animator arg0) {
                mImage.setVisibility(View.INVISIBLE);
                //重置背景
                mBg.setBackgroundColor(Color.parseColor("#00000000"));

            }

            @Override
            public void onAnimationCancel(Animator arg0) {
                // TODO Auto-generated method stub

            }
        });
    }

    /**
     * 把布局转换成bitmap
     * @param view
     * @return bitmap
     */
    public  Bitmap getBitmapByView(View view) {
        Bitmap bitmap = null;

        // 创建对应大小的bitmap
        bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
                Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        // 测试输出
        FileOutputStream out = null;
        try {

            out = new FileOutputStream("/sdcard/screen_test.png");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            if (null != out) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
    /**
     * 保存图片至/sdcard/myFolder文件夹下
     * @param bmp
     */
    public static void saveCroppedImage(Bitmap bmp) {
        File file = new File("/sdcard/myFolder");
        if (!file.exists())
            file.mkdir();
        long time= System.currentTimeMillis();
        file = new File("/sdcard/"+time+".jpg".trim());
        String fileName = file.getName();
        String mName = fileName.substring(0, fileName.lastIndexOf("."));
        String sName = fileName.substring(fileName.lastIndexOf("."));

        newFilePath = "/sdcard/myFolder" + "/" + mName + sName;
        file = new File(newFilePath);
        try {
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 50, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 有水印的保存
     * @param photo 当前截图的bitmap
     *  mark 水印的图片
     *  image 控件
     * @return
     */
    public static Bitmap addPic(Context mContext, Bitmap photo){
        Bitmap mark= getBitmap(mContext, R.mipmap.ico_pickoff);
        Bitmap photoMark = Bitmap.createBitmap(photo.getWidth(), photo.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(photoMark);
        canvas.drawBitmap(photo, 0, 0, null);
        Bitmap bitmapMark =mark.copy(Bitmap.Config.ARGB_8888, true);
        canvas.drawBitmap(bitmapMark, photo.getWidth()/2 - bitmapMark.getWidth()/2, photo.getHeight() - bitmapMark.getHeight(), null);
//        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.save();
        canvas.restore();
        return photoMark;
    }
    /**
     * 添加文字到图片，类似水印文字。
     * @param bitmap
     * @param gText
     * @return
     */
    public  Bitmap drawTextToBitmap(Bitmap bitmap, String gText) {
        Resources resources = mContext.getResources();
        float scale = resources.getDisplayMetrics().density;

        android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();
        if (bitmapConfig == null) {
            bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
        }
        bitmap = bitmap.copy(bitmapConfig, true);

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.parseColor("#458EFD"));
        paint.setTextSize((int) (3 * scale*5));
        paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);
        Rect bounds = new Rect();
        paint.getTextBounds(gText, 0, gText.length(), bounds);
        int x = (bitmap.getWidth() - bounds.width())/10*9 ;
        int y = (bitmap.getHeight() + bounds.height())/10*9;
        canvas.drawText(gText, x , y, paint);

        return bitmap;
    }

    public static Bitmap getBitmap(Context context, int vectorDrawableId) {
        Bitmap bitmap=null;
        if (Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP){
            Drawable vectorDrawable = context.getDrawable(vectorDrawableId);
            bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                    vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            vectorDrawable.draw(canvas);
        }else {
            bitmap = BitmapFactory.decodeResource(context.getResources(), vectorDrawableId);
        }
        return bitmap;
    }

    /**
     * 缩放图片
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
     * @param context
     * @param dp
     * @return
     */
    public static int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
