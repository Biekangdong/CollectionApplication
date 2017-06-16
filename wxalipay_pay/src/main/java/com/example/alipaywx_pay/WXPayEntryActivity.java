package com.example.alipaywx_pay;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Created by Administrator on 2017/6/16.
 */

public class WXPayEntryActivity  extends AppCompatActivity implements IWXAPIEventHandler {

    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, "wx72e0123dd9b54a73");
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }
    @Override
    /**
     * ，微信APP会返回到商户APP并回调onResp函数，开发者需要在该函数中接收通知，判断返回错误码，
     * 如果支付成功则去后台查询支付结果再展示用户实际支付结果。
     * 注意一定不能以客户端返回作为用户支付的结果，应以服务器端的接收的支付通知或查询API返回的结果为准
     *
     *    resp.errCode== 0 ：表示支付成功
     resp.errCode== -1 ：表示支付失败
     resp.errCode== -2 ：表示取消支付
     */

    public void onResp(BaseResp resp) {
        Log.d("WXPayEntryActivity","onResp, 微信回调成功 = " + resp.errCode+"---"+(resp.errCode==-2));
        if (resp.errCode == 0) {
            Toast.makeText(WXPayEntryActivity.this, "支付成功",Toast.LENGTH_SHORT).show();
            finish();
        } else if (resp.errCode == -1) {
            Toast.makeText(WXPayEntryActivity.this, "支付失败",Toast.LENGTH_SHORT).show();
            finish();
        } else if (resp.errCode == -2) {
            Toast.makeText(WXPayEntryActivity.this, "取消支付",Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
