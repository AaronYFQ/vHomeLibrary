package com.compass.loco.homelibrary;

import android.app.Application;
import android.util.Log;

import com.compass.loco.homelibrary.chatting.receiver.NotificationClickEventReceiver;
import com.compass.loco.homelibrary.chatting.utils.SharePreferenceManager;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.im.android.api.JMessageClient;

/**
 * For developer startup JPush SDK
 * 
 * 一般建议在自定义 Application 类里初始化。也可以在主 Activity 里。
 */
public class MyApplication extends Application {
    private static final String TAG = "MyApplication";

    @Override
    public void onCreate() {    	     
    	 Log.d(TAG, "[MyApplication] onCreate");
         super.onCreate();

        JPushInterface.setDebugMode(true);
        JMessageClient.init(this);     		// 初始化 JPush
        JMessageClient.setDebugMode(true);
        JMessageClient.setNotificationMode(JMessageClient.NOTI_MODE_DEFAULT);
        new NotificationClickEventReceiver(getApplicationContext());
        SharePreferenceManager.init(getApplicationContext(), GlobalParams.JCHAT_CONFIGS);
    }
}
