package com.compass.loco.homelibrary;

import android.app.Application;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.compass.loco.homelibrary.chatting.receiver.NotificationClickEventReceiver;
import com.compass.loco.homelibrary.chatting.utils.AssetsDatabaseManager;
import com.compass.loco.homelibrary.chatting.utils.SharePreferenceManager;

import java.io.UnsupportedEncodingException;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.im.android.api.JMessageClient;

/**
 * For developer startup JPush SDK
 * 
 * 一般建议在自定义 Application 类里初始化。也可以在主 Activity 里。
 */
public class MyApplication extends Application {
    private static final String TAG = "MyApplication";
    private SQLiteDatabase region_db;

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

        //copy region.db from /assets to data/%s/database directory
        // 初始化，只需要调用一次
        AssetsDatabaseManager.initManager(getApplicationContext());
        // 获取管理对象，因为数据库需要通过管理对象才能够获取
        AssetsDatabaseManager mg = AssetsDatabaseManager.getManager();
        // 通过管理对象获取数据库
        region_db = mg.getDatabase("region.db");
//        // 对数据库进行操作
//        Cursor result = region_db.rawQuery("SELECT * FROM region where region_id=2;", null);
//        result.moveToFirst();
//        byte[] b = result.getBlob(2);
//
//        String s = null;
//        try {
//            s = new String(b, "GBK");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        Log.d(TAG, s.trim() + " 省市");
    }
    public SQLiteDatabase getRegionDB(){
        return region_db;
    }
}
