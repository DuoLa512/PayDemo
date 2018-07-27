package com.example.liangjianhua.paydemo.utils;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import android.widget.Toast;

import com.alipay.sdk.app.PayTask;

import com.example.liangjianhua.paydemo.alipay.AliPayConfig;
import com.example.liangjianhua.paydemo.alipay.PayResult;
import com.example.liangjianhua.paydemo.wxpay.WxChatPayEntity;
import com.example.liangjianhua.paydemo.wxpay.WxPayConfig;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.Map;


/**
 * @author Ljh
 * @date 2018/7/26
 */
public class PayUtils {

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;

    private static PayUtils sPayUtlis;
    private PayHandle mHandle = new PayHandle();

    private PayUtils() {
    }

    public static PayUtils getInstance() {
        if (sPayUtlis == null) {
            synchronized (PayUtils.class) {
                if (sPayUtlis == null) {
                    sPayUtlis = new PayUtils();
                }
            }
        }
        return sPayUtlis;
    }


    /**
     * 支付宝支付
     *
     * @param activity  Activity
     * @param orderInfo 订单信息
     */
    public void startAliPay(final Activity activity, String orderInfo) {

        if (activity == null) {
            return;
        }

        if (TextUtils.isEmpty(AliPayConfig.APPID) || (TextUtils.isEmpty(AliPayConfig.RSA2_PRIVATE) && TextUtils.isEmpty(AliPayConfig.RSA_PRIVATE))) {

            Toast.makeText(activity.getApplicationContext(), "需要配置APPID | RSA_PRIVATE", Toast.LENGTH_LONG).show();
            return;
        }

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(activity);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandle.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * 微信支付
     *
     * @param activity Activity
     */
    public void startWxPay(final Activity activity, WxChatPayEntity wxChatPayEntity) {

        if (activity == null) {
            return;
        }

        else if (TextUtils.isEmpty(WxPayConfig.APP_ID)) {

            Toast.makeText(activity.getApplicationContext(), "需要配置APP_ID ", Toast.LENGTH_LONG).show();
            return;
        }

        if (!WxPayConfig.APP_ID.equals(wxChatPayEntity.getAppid())) {
            return;
        }
        IWXAPI wxapi = WXAPIFactory.createWXAPI(activity, WxPayConfig.APP_ID, true);
        // 将该app注册到微信
        wxapi.registerApp(WxPayConfig.APP_ID);

        PayReq req = new PayReq();
        req.appId = WxPayConfig.APP_ID;
        req.partnerId = wxChatPayEntity.getPartnerid();
        req.prepayId = wxChatPayEntity.getPrepayid();
        req.nonceStr = wxChatPayEntity.getNoncestr();
        req.timeStamp = wxChatPayEntity.getTimeStamp();
        // "Sign=WXPay"
        req.packageValue = wxChatPayEntity.getPackageValue();
        req.sign = wxChatPayEntity.getSign();
        wxapi.sendReq(req);


    }

    private static class PayHandle extends Handler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case SDK_PAY_FLAG:
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 返回码
                    //9000	订单支付成功
                    //8000	正在处理中，支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
                    //4000	订单支付失败
                    //5000	重复请求
                    //6001	用户中途取消
                    //6002	网络连接出错
                    //6004	支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
                    //其它	其它支付错误
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(ApplicationUtils.getApp(), "支付成功", Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.equals(resultStatus, "6001")) {
                        Toast.makeText(ApplicationUtils.getApp(), "用户取消支付", Toast.LENGTH_SHORT).show();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(ApplicationUtils.getApp(), "支付失败", Toast.LENGTH_SHORT).show();
                    }

                    break;

                default:
                    break;
            }
        }
    }

}
