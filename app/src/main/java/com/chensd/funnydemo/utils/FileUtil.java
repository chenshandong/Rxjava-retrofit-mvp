package com.chensd.funnydemo.utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.RandomAccessFile;

/**
 * Created by chen on 2017/1/26.
 */
public class FileUtil {

    public static final String TAG = "FunnyDemo";
    public static final String FILE_DIR = "栋栋斗图";
    public static final String SDCARD = Environment.getExternalStorageDirectory().getAbsolutePath();

    public static byte[] readFromFile(String fileName, int offset, int len) {
        if (fileName == null) {
            return null;
        }

        File file = new File(fileName);
        if (!file.exists()) {
            Log.i(TAG, "readFromFile: file not found");
            return null;
        }

        if (len == -1) {
            len = (int) file.length();
        }

        Log.d(TAG, "readFromFile : offset = " + offset + " len = " + len + " offset + len = " + (offset + len));

        if(offset <0){
            Log.e(TAG, "readFromFile invalid offset:" + offset);
            return null;
        }
        if(len <=0 ){
            Log.e(TAG, "readFromFile invalid len:" + len);
            return null;
        }
        if(offset + len > (int) file.length()){
            Log.e(TAG, "readFromFile invalid file len:" + file.length());
            return null;
        }

        byte[] b = null;
        try {
            RandomAccessFile in = new RandomAccessFile(fileName, "r");
            b = new byte[len]; // 创建合适文件大小的数组
            in.seek(offset);
            in.readFully(b);
            in.close();

        } catch (Exception e) {
            Log.e(TAG, "readFromFile : errMsg = " + e.getMessage());
            e.printStackTrace();
        }
        return b;
    }

    /**
     * 根据路径创建dir
     */
    public static File createDir(String dir){
        File fileDir = new File(dir);
        if (!fileDir.exists()){
            fileDir.mkdir();
        }
        return fileDir;
    }

    public static void createAppDir(){
        String root = SDCARD + "/" + FILE_DIR;
        String cache = root + "/cache";

        String image = cache + "/image";

        //创建
        FileUtil.createDir(root);
        FileUtil.createDir(cache);

        //创建子目录
        File dir = FileUtil.createDir(image);

        Log.e("dir", "dir=["+ dir.getAbsolutePath()+"]" + "是否存在？" + dir.exists());
    }

    public static String getImageDir(){
        String root = SDCARD + "/" + FILE_DIR;
        String cache = root + "/cache";

        String image = cache + "/image";
        return image;
    }
}
