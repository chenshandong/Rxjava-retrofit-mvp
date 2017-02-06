package com.chensd.funnydemo;

import rx.Subscriber;

/**
 * Created by chen on 2017/1/18.
 */
public abstract class SearchDbCallBack<M> extends Subscriber<M> {

    public abstract void onSuccess(M model);

    public abstract void onFailure(String msg);

    public abstract void onFinish();

    @Override
    public void onCompleted() {
        onFinish();
    }

    @Override
    public void onError(Throwable e) {
        onFailure(e.getMessage());
    }

    @Override
    public void onNext(M m) {
        onSuccess(m);
    }
}
