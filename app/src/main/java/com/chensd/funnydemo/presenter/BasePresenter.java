package com.chensd.funnydemo.presenter;

import com.chensd.funnydemo.retrofit.ImageApi;
import com.chensd.funnydemo.retrofit.RetrofitApi;

/**
 * Created by chen on 2017/1/17.
 */
public class BasePresenter<V> {
    public V mvpView;

    public void attachView(V mvpView){
        this.mvpView = mvpView;
    }

    public void detachView(){
        this.mvpView = null;
    }
}

