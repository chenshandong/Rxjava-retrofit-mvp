package com.chensd.funnydemo.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.chensd.funnydemo.presenter.BasePresenter;
import com.chensd.funnydemo.ui.BaseFragment;

/**
 * Created by chen on 2017/1/17.
 */
public abstract class MvpFragment<p extends BasePresenter> extends BaseFragment {
    protected p mvpPresenter;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mvpPresenter = createPresenter();
    }

    protected abstract p createPresenter();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mvpPresenter != null){
            mvpPresenter.detachView();
        }
    }
}
