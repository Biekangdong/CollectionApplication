package com.example.wxqqsina_share_login.share;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.wxqqsina_share_login.R;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.WeiboMessage;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.constant.WBConstants;
import com.tencent.connect.common.Constants;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.open.utils.ThreadManager;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

/**
 * Created by Administrator on 2017/6/16.
 */

public class ShareActivity extends AppCompatActivity implements IWeiboHandler.Response {
    Button btn_share;
    private IWXAPI wxApi;
    private Tencent mTencent;// 新建Tencent实例用于调用分享方法

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        //微信实例化
        wxApi = WXAPIFactory.createWXAPI(this, "微信ID");
        wxApi.registerApp("微信ID");
        //QQ实例化
        mTencent = Tencent.createInstance("your APP ID", getApplicationContext());

        btn_share = (Button) findViewById(R.id.btn_share);
        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareDialog();
            }
        });
    }

    private void shareDialog() {
        View views = getLayoutInflater().inflate(R.layout.dialog_share,
                null);
        final Dialog aDialog = new Dialog(this, R.style.dialog);
        aDialog.setContentView(views);
        Window window = aDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.shareAnimation);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = (int) (display.getWidth()); //设置宽度
        window.setAttributes(lp);
        aDialog.show();
        views.findViewById(R.id.ll_qq).setOnClickListener(//QQ好友
                new View.OnClickListener() {
                    public void onClick(View arg0) {
                        aDialog.dismiss();
                        shareToQQ(false);
                    }
                });
        views.findViewById(R.id.ll_qzone).setOnClickListener(//QQ空间
                new View.OnClickListener() {
                    public void onClick(View arg0) {
                        shareToQQ(true);
                    }
                });
        views.findViewById(R.id.ll_wechat).setOnClickListener(//微信好友
                new View.OnClickListener() {
                    public void onClick(View arg0) {
                        wechatShare(0);//分享到微信好友
                    }
                });
        views.findViewById(R.id.ll_wxcircle).setOnClickListener(//微信朋友圈
                new View.OnClickListener() {
                    public void onClick(View arg0) {
                        wechatShare(1);//分享到微信朋友圈
                    }
                });
        views.findViewById(R.id.ll_sina).setOnClickListener(//新浪微博
                new View.OnClickListener() {
                    public void onClick(View arg0) {
                        sinaShare();
                    }
                });
        views.findViewById(R.id.tv_cancle).setOnClickListener(//取消
                new View.OnClickListener() {
                    public void onClick(View arg0) {
                        aDialog.dismiss();
                    }
                });
    }


    /**
     * 微信分享 （这里仅提供一个分享网页的示例，其它请参看官网示例代码）
     *
     * @param flag(0:分享到微信好友，1：分享到微信朋友圈)
     */
    private void wechatShare(int flag) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = "这里填写链接url";
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = "这里填写标题";
        msg.description = "这里填写内容";
        //这里替换一张自己工程里的图片资源
        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.share_logo);
        msg.setThumbImage(thumb);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
        wxApi.sendReq(req);
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private Bundle params;

    //QQ分享
    private void shareToQQ(boolean flag) {
        if (mTencent.isSessionValid() && mTencent.getOpenId() == null) {
            Toast.makeText(ShareActivity.this, "您还未安装QQ", Toast.LENGTH_SHORT).show();
        }
        params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, "标题");// 标题
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, "要分享的摘要");// 摘要
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, "http://www.qq.com/news/1.html");// 内容地址
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, "http://imgcache.qq.com/qzone/space_item/pre/0/66768.gif");// 网络图片地址　　params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "应用名称");// 应用名称
        //params.putString(QQShare.SHARE_TO_QQ_EXT_INT, "其它附加功能");
        if (flag) {
            params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
        }

        // 分享操作要在主线程中完成
        ThreadManager.getMainHandler().post(new Runnable() {
            @Override
            public void run() {
                mTencent.shareToQQ(ShareActivity.this, params, new mIUiListener());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
// TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        Tencent.onActivityResultData(requestCode, resultCode, data, new mIUiListener());
        if (requestCode == Constants.REQUEST_API) {
            if (resultCode == Constants.REQUEST_QQ_SHARE || resultCode == Constants.REQUEST_QZONE_SHARE || resultCode == Constants.REQUEST_OLD_SHARE) {
                Tencent.handleResultData(data, new mIUiListener());
            }
        }
    }

    //QQ分享回调
    class mIUiListener implements IUiListener {
        @Override
        public void onComplete(Object o) {
            // 操作成功
            Toast.makeText(ShareActivity.this, "分享成功", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(UiError uiError) {
            // 分享异常
            Toast.makeText(ShareActivity.this, "分享成功", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel() {
            // 取消分享
            Toast.makeText(ShareActivity.this, "分享成功", Toast.LENGTH_SHORT).show();
        }
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private IWeiboShareAPI mWeiboShareAPI;

    private void sinaShare() {
        // 创建微博分享接口实例
        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, "APP_KEY");

        // 注册第三方应用到微博客户端中，注册成功后该应用将显示在微博的应用列表中。
        // 但该附件栏集成分享权限需要合作申请，详情请查看 Demo 提示
        // NOTE：请务必提前注册，即界面初始化的时候或是应用程序初始化时，进行注册
        mWeiboShareAPI.registerApp();

        // 当 Activity 被重新初始化时（该 Activity 处于后台时，可能会由于内存不足被杀掉了），
        // 需要调用 {@link IWeiboShareAPI#handleWeiboResponse} 来接收微博客户端返回的数据。
        // 执行成功，返回 true，并调用 {@link IWeiboHandler.Response#onResponse}；
        // 失败返回 false，不调用上述回调
        mWeiboShareAPI.handleWeiboResponse(getIntent(), this);
//        if (savedInstanceState != null) {
//            mWeiboShareAPI.handleWeiboResponse(getIntent(), this);
//        }

        WeiboMessage weiboMessage = new WeiboMessage();
        ImageObject imageObject = new ImageObject();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        imageObject.setImageObject(bitmap);
        weiboMessage.mediaObject = imageObject;
        SendMessageToWeiboRequest request = new SendMessageToWeiboRequest();
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.message = weiboMessage;
        mWeiboShareAPI.sendRequest(ShareActivity.this, request);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        // 从当前应用唤起微博并进行分享后，返回到当前应用时，需要在此处调用该函数
        // 来接收微博客户端返回的数据；执行成功，返回 true，并调用
        // {@link IWeiboHandler.Response#onResponse}；失败返回 false，不调用上述回调
        mWeiboShareAPI.handleWeiboResponse(intent, this);
    }

    /**
     * 接收微客户端博请求的数据。
     * 当微博客户端唤起当前应用并进行分享时，该方法被调用。
     *
     * @param baseResp 微博请求数据对象
     * @see {@link IWeiboShareAPI#handleWeiboRequest}
     */
    @Override
    public void onResponse(BaseResponse baseResp) {
        if (baseResp != null) {
            switch (baseResp.errCode) {
                case WBConstants.ErrorCode.ERR_OK:
                    Toast.makeText(this, "success", Toast.LENGTH_LONG).show();
                    break;
                case WBConstants.ErrorCode.ERR_CANCEL:
                    Toast.makeText(this, "cancel", Toast.LENGTH_LONG).show();
                    break;
                case WBConstants.ErrorCode.ERR_FAIL:
                    Toast.makeText(this, "Error Message: " + baseResp.errMsg,
                            Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }
}
