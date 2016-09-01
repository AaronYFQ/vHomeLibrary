package com.compass.loco.homelibrary;

import android.app.Application;
import android.util.Log;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.im.android.api.JMessageClient;

/**
 * For developer startup JPush SDK
 * 
 * 一般建议在自定义 Application 类里初始化。也可以在主 Activity 里。
 */
public class MyApplication extends Application {
    private static final String TAG = "JPush";

    @Override
    public void onCreate() {    	     
    	 Log.d(TAG, "[MyApplication] onCreate");
         super.onCreate();
         
         JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
         JMessageClient.init(this);     		// 初始化 JPush
    }
}
