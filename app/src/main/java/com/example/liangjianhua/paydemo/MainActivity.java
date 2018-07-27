package com.example.liangjianhua.paydemo;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.EnvUtils;
import com.example.liangjianhua.paydemo.alipay.AliPayConfig;
import com.example.liangjianhua.paydemo.alipay.OrderInfoUtil2_0;
import com.example.liangjianhua.paydemo.utils.PayUtils;
import com.unionpay.UPPayAssistEx;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements  Runnable {

    private TextView tvAliPay, tvWxPay, tvYinLianPay;

    /**
     * "00" - 启动银联正式环境 "01" - 连接银联测试环境
     */
    private static final String serverMode = "01";
    private static final String TN_URL_01 = "http://101.231.204.84:8091/sim/getacptn";

    private UpayHandle mHandler ;
    private static Context sContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //沙箱调试
        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHandler = new UpayHandle(this);

        sContext = this;

        tvAliPay = findViewById(R.id.tv_AliPay);
        tvWxPay = findViewById(R.id.tv_WxPay);
        tvYinLianPay = findViewById(R.id.tv_YinLianPay);

        //支付宝支付
        tvAliPay.setOnClickListener(view -> {
            /**
             * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
             * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
             * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
             *
             * orderInfo的获取必须来自服务端；
             */
            boolean rsa2 = (AliPayConfig.RSA2_PRIVATE.length() > 0);
            Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(AliPayConfig.APPID, rsa2);
            String orderParam = OrderInfoUtil2_0.buildOrderParam(params);

            String privateKey = rsa2 ? AliPayConfig.RSA2_PRIVATE : AliPayConfig.RSA_PRIVATE;
            String sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2);
            final String orderInfo = orderParam + "&" + sign;
            PayUtils.getInstance().startAliPay(MainActivity.this, orderInfo);

        });

        //微信支付
        tvWxPay.setOnClickListener(view -> {

            //wxChatPayEntity后台返回的实体类
//                PayUtils.getInstance().startWxPay(MainActivity.this,wxChatPayEntity);
            Toast.makeText(getApplicationContext(), "微信支付", Toast.LENGTH_LONG).show();
        });

        //银联支付
        tvYinLianPay.setOnClickListener(view -> {
            /*************************************************
             * 步骤1：从网络开始,获取交易流水号即TN
             ************************************************/
            new Thread(MainActivity.this).start();
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        /*************************************************
         * 步骤3：处理银联手机支付控件返回的支付结果
         ************************************************/
        if (data == null) {
            return;
        }

        String msg = "";
        /*
         * 支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消
         */
        String str = data.getExtras().getString("pay_result");
        if (str.equalsIgnoreCase("success")) {

            // 如果想对结果数据验签，可使用下面这段代码，但建议不验签，直接去商户后台查询交易结果
            // result_data结构见c）result_data参数说明
            if (data.hasExtra("result_data")) {
                String result = data.getExtras().getString("result_data");
                try {
                    JSONObject resultJson = new JSONObject(result);
                    String sign = resultJson.getString("sign");
                    String dataOrg = resultJson.getString("data");
                    // 此处的verify建议送去商户后台做验签
                    // 如要放在手机端验，则代码必须支持更新证书
                    boolean ret = verify(dataOrg, sign, serverMode);
                    if (ret) {
                        // 验签成功，显示支付结果
                        msg = "支付成功！";
                    } else {
                        // 验签失败
                        msg = "支付失败！";
                    }
                } catch (JSONException e) {
                }
            }
            // 结果result_data为成功时，去商户后台查询一下再展示成功
            msg = "支付成功！";
        } else if (str.equalsIgnoreCase("fail")) {
            msg = "支付失败！";
        } else if (str.equalsIgnoreCase("cancel")) {
            msg = "用户取消了支付";
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("支付结果通知");
        builder.setMessage(msg);
        builder.setInverseBackgroundForced(true);
        // builder.setCustomTitle();
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private static class UpayHandle extends Handler {

        private final WeakReference<MainActivity> mWeakReference;

        public UpayHandle(MainActivity activity) {
            mWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity activity = mWeakReference.get();
            if (activity != null) {

                String tn = "";
                if (msg.obj == null || ((String) msg.obj).length() == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(sContext);
                    builder.setTitle("错误提示");
                    builder.setMessage("网络连接失败,请重试!");
                    builder.setNegativeButton("确定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    builder.create().show();
                } else {
                    tn = (String) msg.obj;
                    /*************************************************
                     * 步骤2：通过银联工具类启动支付插件
                     ************************************************/

                    UPPayAssistEx.startPay(sContext
                            , null
                            , null
                            , tn
                            , serverMode);
                }
            }
        }
    }

    @Override
    public void run() {
        String tn = null;
        InputStream is;
        try {

            String url = TN_URL_01;

            URL myURL = new URL(url);
            URLConnection ucon = myURL.openConnection();
            ucon.setConnectTimeout(120000);
            is = ucon.getInputStream();
            int i = -1;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while ((i = is.read()) != -1) {
                baos.write(i);
            }

            tn = baos.toString();
            is.close();
            baos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Message msg = mHandler.obtainMessage();
        msg.obj = tn;
        mHandler.sendMessage(msg);
    }

    private boolean verify(String msg, String sign64, String mode) {
        // 此处的verify，商户需送去商户后台做验签
        return true;

    }
}
