package com.example.alipaywx_pay;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.example.alipaywx_pay.test.PayResult;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int SDK_PAY_FLAG = 1;
    Button btn_alipay,btn_wxpay;
    private IWXAPI api;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_alipay = (Button) findViewById(R.id.btn_alipay);
        btn_wxpay= (Button) findViewById(R.id.btn_wxpay);
        btn_alipay.setOnClickListener(this);
        btn_wxpay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_alipay:
                String orderInfo="app_id=2015052600090779&biz_content=%7B%22timeout_express%22%3A%2230m%22%2C%22seller_id%22%3A%22%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22total_amount%22%3A%220.02%22%2C%22subject%22%3A%221%22%2C%22body%22%3A%22%E6%88%91%E6%98%AF%E6%B5%8B%E8%AF%95%E6%95%B0%E6%8D%AE%22%2C%22out_trade_no%22%3A%22314VYGIAGG7ZOYY%22%7D&charset=utf-8&method=alipay.trade.app.pay&sign_type=RSA2&timestamp=2016-08-15%2012%3A12%3A15&version=1.0&sign=MsbylYkCzlfYLy9PeRwUUIg9nZPeN9SfXPNavUCroGKR5Kqvx0nEnd3eRmKxJuthNUx4ERCXe552EV9PfwexqW%2B1wbKOdYtDIb4%2B7PL3Pc94RZL0zKaWcaY3tSL89%2FuAVUsQuFqEJdhIukuKygrXucvejOUgTCfoUdwTi7z%2BZzQ%3D";
                alipay(orderInfo);
                break;
            case R.id.btn_wxpay:
                wxPay();
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(MainActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(MainActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }

                default:
                    break;
            }
        }

        ;
    };

    public void alipay(final String orderInfo) {// 订单信息
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(MainActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                // 调用支付接口，获取支付结果
                //String result = alipay.pay(orderInfo, true);
                Log.i("msp", result.toString());
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }





    public void wxPay(){
        api = WXAPIFactory.createWXAPI(this, "你在微信开放平台创建的app的APPID");
        //我将后端反给我的信息放到了WeiXinPay中，这步是获取数据
        WeiXinPay weiXinPay = new WeiXinPay();

        //这个在官网里就会看到，将你获取的信息赋给payReq，这块就是调起微信的关键
        PayReq payReq = new PayReq();
        payReq.appId = weiXinPay.getAppId();// appid
        payReq.partnerId = weiXinPay.getPartnerId();// 微信支付分配的商户号
        payReq.prepayId = weiXinPay.getPrepayId();// 预支付订单号，app服务器调用“统一下单”接口获取
        payReq.packageValue = weiXinPay.getPackageValue();// 固定值Sign=WXPay，可以直接写死，服务器返回的也是这个固定值
        payReq.nonceStr = weiXinPay.getNonceStr();// 随机字符串，不长于32位，服务器小哥会给咱生成
        payReq.timeStamp = weiXinPay.getTimeStamp(); // 时间戳，app服务器小哥给出
        payReq.sign = weiXinPay.getSign();// 签名，服务器小哥给出，他会根据：https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=4_3指导得到这个
        api.sendReq(payReq);
    }

}
