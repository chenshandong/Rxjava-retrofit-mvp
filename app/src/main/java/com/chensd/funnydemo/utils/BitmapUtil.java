package com.chensd.funnydemo.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Created by chen on 2017/1/24.
 */
public class BitmapUtil {

    public static Bitmap createBitmapThumbnail(Bitmap bitMap, boolean needRecycle) {

        int width = bitMap.getWidth();
        int height = bitMap.getHeight();
        // 设置想要的大小
        int newWidth = width/10;
        int newHeight = height/10;
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float ) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片

        Bitmap newBitMap = Bitmap.createBitmap(bitMap, 0, 0, width, height, matrix, true);
        if (needRecycle)
            bitMap.recycle();

        return newBitMap;
    }

    /**
     * 按比例缩小
     */
    public static Bitmap creatSmallBitmap(Bitmap bmp, float multiple, boolean needRecycle){
        int width = bmp.getWidth();
        int height = bmp.getHeight();

        //压缩后的新高度
        float newWidth = width/multiple;
        float newHeight = height/multiple;

        float scallWidth =  newWidth / width;
        float scallHeight = newHeight / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scallWidth, scallHeight);

        Bitmap newBmp = Bitmap.createBitmap(bmp, 0, 0, width, height, matrix, true);

        if (needRecycle)
            bmp.recycle();

        return newBmp;
    }

    /**
     *
     */
    public static Bitmap creatDecBitmap(Bitmap bmp, int des, boolean needRecycle){
        int width = bmp.getWidth();
        int height = bmp.getHeight();

        //压缩后的新高度
        float newWidth = des;
        float newHeight =(newWidth * height) / width;

        float scallWidth =  newWidth / (float)width;
        float scallHeight = newHeight / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scallWidth, scallHeight);

        Bitmap newBmp = Bitmap.createBitmap(bmp, 0, 0, width, height, matrix, true);

        if (needRecycle)
            bmp.recycle();

        return newBmp;
    }

    /**
     * 质量压缩方法
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image, int size) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 90;

        while ((baos.toByteArray().length /1024) > size) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            Log.e("weixin_baos", "baos=" + baos.toByteArray().length/1024+"kb");
            baos.reset(); // 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.PNG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }
}
