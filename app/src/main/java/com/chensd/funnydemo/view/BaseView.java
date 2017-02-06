package com.chensd.funnydemo.view;

import com.chensd.funnydemo.model.ImageInfo;

import java.util.List;

/**
 * Created by chen on 2017/1/17.
 */
public interface BaseView {
    void showLoading();
    void hideLoading();
    void getDataSuccess(List<ImageInfo> info);
    void getDataFailure(String msg);
}
