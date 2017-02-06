package com.chensd.funnydemo.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.chensd.funnydemo.R;
import com.chensd.funnydemo.utils.WxShareUtil;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "weixin";

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wx_pay_results);
        api = WXAPIFactory.createWXAPI(this, WxShareUtil.WEIXIN_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {

    }

    @Override
    public void onResp(BaseResp resp) {
        Log.e(TAG, "shareFinish, errCode = [" + resp.errCode + "]");

        if(resp != null && resp.errCode == 0){
            Toast.makeText(WXEntryActivity.this, "分享成功", Toast.LENGTH_SHORT).show();
            this.finish();
        }else if(resp != null && resp.errCode == -2){
            Toast.makeText(WXEntryActivity.this, "取消分享", Toast.LENGTH_SHORT).show();
            this.finish();
        }else{
            Toast.makeText(this, "errCode = [" + resp.errCode + "]", Toast.LENGTH_LONG).show();
            this.finish();
        }
    }
}