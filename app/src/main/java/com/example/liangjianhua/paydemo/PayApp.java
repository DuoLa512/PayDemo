package com.example.liangjianhua.paydemo;

import android.app.Application;
import android.content.Context;

import com.example.liangjianhua.paydemo.utils.ApplicationUtils;

/**
 * @author Ljh
 * @date 2018/7/26
 */
public class PayApp extends Application {

    private static PayApp sApplication;

    public static PayApp getInstance() {

        if (sApplication == null) {

            synchronized (PayApp.class) {
                if (sApplication == null) {
                    sApplication = new PayApp();
                }
            }
        }
        return sApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ApplicationUtils.init(this);
    }

}
