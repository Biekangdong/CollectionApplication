package com.example.alipaywx_pay;

import com.tencent.mm.opensdk.modelpay.PayReq;

/**
 * Created by Administrator on 2017/6/16.
 */

public class WeiXinPay {
    String appId ;// appid
    String partnerId;// 微信支付分配的商户号
    String prepayId;// 预支付订单号，app服务器调用“统一下单”接口获取
    String packageValue;// 固定值Sign=WXPay，可以直接写死，服务器返回的也是这个固定值
    String nonceStr;// 随机字符串，不长于32位，服务器小哥会给咱生成
    String timeStamp; // 时间戳，app服务器小哥给出
    String sign; // 签名，服务器小哥给出，他会根据：https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=4_3指导得到这个


    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public String getPackageValue() {
        return packageValue;
    }

    public void setPackageValue(String packageValue) {
        this.packageValue = packageValue;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
