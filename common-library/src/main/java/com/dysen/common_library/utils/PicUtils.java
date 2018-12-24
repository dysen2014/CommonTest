package com.dysen.common_library.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @package com.vip.zb.tool
 * @email dy.sen@qq.com
 * created by dysen on 2018/11/27 - 10:40 AM
 * @info 图片格式转换
 */
public class PicUtils {

    public static String webp2JPG(String path) {
        byte[] b = null;
        File PNG = null;
        BufferedOutputStream stream = null;
        try {
            PNG = new File(path);
            FileOutputStream fstream = new FileOutputStream(PNG);
            stream = new BufferedOutputStream(fstream);
            stream.write(b);
        } catch (Exception e) {
            LogUtils.e("helper:get file from byte process error!");
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    LogUtils.e("helper:get file from byte process error!");
                    e.printStackTrace();
                }
            }
        }
        Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
        String mPath = path.replace(path.substring(path.lastIndexOf(".") + 1), "jpg");
        boolean isSuccess = FileUtils.saveBitmap(bitmap, new File(mPath));
        if (isSuccess) {
            FileUtils.deleteFile(mPath);
            return mPath;
        } else
            return "";
    }

    public static String img2JPG(String path) {
        String mPath = path.replace(path.substring(path.lastIndexOf(".") + 1), "jpg");
        FileUtils.copyFile2(path, mPath);
//        FileUtils.deleteFile(mPath);
        return mPath;
    }
}
