package com.chensd.funnydemo.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.chensd.funnydemo.R;
import com.chensd.funnydemo.utils.WxShareUtil;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import butterknife.ButterKnife;

/**
 * Created by chen on 2017/1/17.
 */
public abstract class BaseActivity extends AppCompatActivity {

    public Activity mActivity;
    protected InputMethodManager mInputMethodManager;
    public IWXAPI wxapi;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        mActivity = this;
        regToWx();
    }

    private void regToWx()
    {
        wxapi = WXAPIFactory.createWXAPI(this, WxShareUtil.WEIXIN_ID, true);
        wxapi.registerApp(WxShareUtil.WEIXIN_ID);

    }

    protected void showDialog(){
        View view = getLayoutInflater().inflate(getAlertLayoutId(), null);
        new AlertDialog.Builder(this)
                .setTitle(getAlertTitle())
                .setView(view)
                .show();
        TextView helpTv = (TextView) view.findViewById(R.id.helpTv);
        if(helpTv != null){
            helpTv.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    protected abstract int getAlertLayoutId();

    protected abstract String getAlertTitle();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    protected void showToast(String str){
        Toast.makeText(mActivity, str, Toast.LENGTH_SHORT).show();
    }

    protected void showToast(int resId){
        Toast.makeText(mActivity, resId, Toast.LENGTH_SHORT).show();
    }

    protected void showSnack(View v, String str){
        Snackbar.make(v,str,Snackbar.LENGTH_SHORT).show();
    }

    protected void showSnack(View v, int resId){
        Snackbar.make(v,resId,Snackbar.LENGTH_SHORT).show();
    }

    protected void showSoftInput(boolean show){
        if(mInputMethodManager == null){
            mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        }

        if(show){
            mInputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }else{
            View view = getCurrentFocus();
            if (view != null){
                mInputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

}
