package com.chensd.funnydemo.ui;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Toast;

import butterknife.ButterKnife;

/**
 * Created by chen on 2017/1/17.
 */
public class BaseFragment extends Fragment implements View.OnClickListener{

    protected BaseActivity mActivity;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mActivity = (BaseActivity)getActivity();

    }

    protected void showtToast(String resStr){
        Toast.makeText(mActivity, resStr, Toast.LENGTH_SHORT).show();
    }

    protected void showToast(int resId){
        Toast.makeText(mActivity, resId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {

    }

}
