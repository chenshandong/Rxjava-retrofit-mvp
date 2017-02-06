package com.chensd.funnydemo.utils;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.internal.$Gson$Types;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by chen on 2017/1/26.
 */
public class OkHttpClientManager {

    private static volatile OkHttpClientManager mInstance;
    private Handler mDelivery;
    private OkHttpClient mOkHttpClient;

    private OkHttpClientManager(){
        mOkHttpClient = new OkHttpClient();
        mDelivery = new Handler(Looper.getMainLooper());

    }

    public static OkHttpClientManager getInstance(){
        if (mInstance == null){
            synchronized (OkHttpClientManager.class){
                if (mInstance == null){
                    mInstance = new OkHttpClientManager();
                }
            }
        }

        return mInstance;
    }

    private void _downloadAsyn(final String url, final String destFileDir, final ResultCallback callback){

        final Request request = new Request.Builder()
                .url(url)
                .build();
        final Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendFailedStringCallback(call, e, callback);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                try {
                    is = response.body().byteStream();
                    File file = new File(destFileDir, getFileName(url));
                    fos = new FileOutputStream(file);
                    while((len = is.read(buf)) != -1){
                        fos.write(buf, 0, len);
                    }
                    fos.flush();
                    sendSuccessResultCallback(file.getAbsolutePath(), callback);
                } catch (IOException e) {
                    sendFailedStringCallback(call, e, callback);
                } finally {
                    if (is != null){
                        is.close();
                    }

                    if (fos != null){
                        fos.close();
                    }
                }

            }
        });

    }

    public static void downLoadAsyn(String url, String destDir, ResultCallback callBack){
        getInstance()._downloadAsyn(url, destDir, callBack);
    }

    public static abstract class ResultCallback<T>
    {
        public abstract void onError(Call request, Exception e);

        public abstract void onResponse(T response);
    }

    private void sendFailedStringCallback(final Call request, final Exception e, final ResultCallback callback)
    {
        mDelivery.post(new Runnable()
        {
            @Override
            public void run()
            {
                if (callback != null)
                    callback.onError(request, e);
            }
        });
    }

    private void sendSuccessResultCallback(final Object object, final ResultCallback callback)
    {
        mDelivery.post(new Runnable()
        {
            @Override
            public void run()
            {
                if (callback != null)
                {
                    callback.onResponse(object);
                }
            }
        });
    }

    public String getFileName(String path)
    {
        int separatorIndex = path.lastIndexOf("/");
        return (separatorIndex < 0) ? path : path.substring(separatorIndex + 1, path.length());
    }


}
