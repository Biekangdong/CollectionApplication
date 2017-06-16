package com.example.wxqqsina_share_login.login;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/6/16.
 */

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private static final String APP_SECRET = "填写自己的AppSecret";
    private IWXAPI mWeixinAPI;
    public static final String WEIXIN_APP_ID = "填写自己的APP_id";
    private static String uuid;
    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mWeixinAPI = WXAPIFactory.createWXAPI(this, WEIXIN_APP_ID, true);
        mWeixinAPI.handleIntent(this.getIntent(), this);
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onReq(BaseReq arg0) { }

    //发送到微信请求的响应结果
    @Override
    public void onResp(BaseResp resp) {
        Log.d("WXEntryActivity","onResp");
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                Log.d("WXEntryActivity","ERR_OK");
                //发送成功
                SendAuth.Resp sendResp = (SendAuth.Resp) resp;
                if (sendResp != null) {
                    String code = sendResp.code;
                    //getAccess_token(code);
                }
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                Log.d("WXEntryActivity","ERR_USER_CANCEL");
                //发送取消
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                Log.d("WXEntryActivity","ERR_AUTH_DENIED");
                //发送被拒绝
                break;
            default:
                //发送返回
                break;
        }

    }



    /**
     * 获取openid accessToken值用于后期操作
     * @param code 请求码
     */
//    private void getAccess_token(final String code) {
//        String path = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
//                + WEIXIN_APP_ID
//                + "&secret="
//                + APP_SECRET
//                + "&code="
//                + code
//                + "&grant_type=authorization_code";
//        Log.d("WXEntryActivity","getAccess_token：" + path);
//        //网络请求，根据自己的请求方式
//        VolleyRequest.get(this, path, "getAccess_token", false, null, new VolleyRequest.Callback() {
//            @Override
//            public void onSuccess(String result) {
//                Log.d("WXEntryActivity","getAccess_token_result:" + result);
//                JSONObject jsonObject = null;
//                try {
//                    jsonObject = new JSONObject(result);
//                    String openid = jsonObject.getString("openid").toString().trim();
//                    String access_token = jsonObject.getString("access_token").toString().trim();
//                    getUserMesg(access_token, openid);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//
//            @Override
//            public void onError(String errorMessage) {
//
//            }
//        });
//    }


    /**
     * 获取微信的个人信息
     * @param access_token
     * @param openid
     */
//    private void getUserMesg(final String access_token, final String openid) {
//        String path = "https://api.weixin.qq.com/sns/userinfo?access_token="
//                + access_token
//                + "&openid="
//                + openid;
//        Log.d("","getUserMesg：" + path);
//        //网络请求，根据自己的请求方式
//        VolleyRequest.get(this, path, "getAccess_token", false, null, new VolleyRequest.Callback() {
//            @Override
//            public void onSuccess(String result) {
//                Log.d("WXEntryActivity","getUserMesg_result:" + result);
//                JSONObject jsonObject = null;
//                try {
//                    jsonObject = new JSONObject(result);
//                    String nickname = jsonObject.getString("nickname");
//                    int sex = Integer.parseInt(jsonObject.get("sex").toString());
//                    String headimgurl = jsonObject.getString("headimgurl");
//
//                    Log.d("WXEntryActivity","用户基本信息:");
//                    Log.d("WXEntryActivity","nickname:" + nickname);
//                    Log.d("WXEntryActivity","sex:" + sex);
//                    Log.d("WXEntryActivity","headimgurl:" + headimgurl);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                finish();
//            }
//
//            @Override
//            public void onError(String errorMessage) {
//
//            }
//        });
//    }

}
