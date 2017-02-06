package com.chensd.funnydemo.retrofit;

import android.util.Log;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

/**
 * Created by chen on 2017/1/17.
 */
public abstract class ApiCallBack<M> extends Subscriber<M> {

    private static final String TAG = "ApiCallBack";

    public abstract void onSuccess(M model);

    public abstract void onFailure(String msg);

    public abstract void onFinish();

    @Override
    public void onCompleted() {
        onFinish();
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        if(e instanceof HttpException){
            HttpException httpException = (HttpException) e;
            int code = httpException.code();
            String msg = httpException.getMessage();
            Log.e(TAG, "code=" + code);
            if (code == 504){
                msg = "网络不给力";
            }
            if (code == 502 || code == 404){
                msg = "服务器异常";
            }
            onFailure(msg);
        }else{
            onFailure(e.getMessage());
        }
        onFinish();
    }

    @Override
    public void onNext(M m) {
        onSuccess(m);
    }
}
