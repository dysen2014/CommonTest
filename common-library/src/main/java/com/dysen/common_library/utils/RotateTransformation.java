package com.dysen.common_library.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.RequestOptions;

import java.security.MessageDigest;

/**
 * Created by integrated on 2018/5/29.
 */

public class RotateTransformation extends BitmapTransformation {

    private float rotateRotationAngle = 0f;

    public RotateTransformation(Context context, float rotateRotationAngle) {
        super( context );

        this.rotateRotationAngle = rotateRotationAngle;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        Matrix matrix = new Matrix();

        matrix.postRotate(rotateRotationAngle);

        return Bitmap.createBitmap(toTransform, 0, 0, toTransform.getWidth(), toTransform.getHeight(), matrix, true);
    }

    public String getId() {
        return "rotate" + rotateRotationAngle;
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {

    }

//    Glide.with(this)//旋转图片
//            .load(bitmap)
//                .apply(new RequestOptions().transform(new RotateTransformation(getContext(), (float) screenAngle)))
//            .into(imageView);

//    Glide.with(this)//显示
//            .load(teacher.getPhotoPath(getMainActivity().getExternalFilesDir(null).getAbsolutePath()))
//            .apply(new RequestOptions().placeholder(R.mipmap.ic_teacher_place_hold))
//            .into(holder.mImgPhoto);

//    Glide.with(this)//显示圆图
//            .load(teacher.getPhotoPath(getMainActivity().getExternalFilesDir(null).getAbsolutePath()))
//            .apply(new RequestOptions().circleCropTransform())
//            .into(holder.mImgPhoto);
}
