package com.compass.loco.homelibrary.http;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.util.Map;

/**
 * Created by esaabbh on 8/9/2016.
 */
public class NativeAsyncHttpRequest {
    public static final int GET = 0;
    public static final int POST = 1;

    public  void asyncHttpRequestGetHandler(String url, Map<String, String> map, final Handler handler) {

        doAsynRequest(GET, map, handler, url);
    }

    public  void asyncHttpRequestPostHandler(String url, Map<String, String> map, final Handler handler) {

        doAsynRequest(POST, map , handler, url);
    }

    private  void doAsynRequest(final int sendType,
                                final Map<String, String> map,
                                final Handler handler,
                                final String url) {
        // 请求
        NativeThreadPool.execute(new MyRunnable(sendType, map, handler, url));
    }
}

class MyRunnable implements Runnable{
    final int _sendType;
    final Map<String, String> _map;
    final Handler _handler;
    final String _url;
    static int index = 0;

    public MyRunnable(final int sendType,
                      final Map<String, String> map,
                      final Handler handler,
                      final String url) {
        this._sendType = sendType;
        this._map = map;
        this._handler = handler;
        this._url = url;
    }

    @Override
    public void run() {

        String data = null;
        NativeSyncHttpRequest nativeAsyncHttpRequest = new NativeSyncHttpRequest();
/*
        String threadName = Thread.currentThread().getName();
        index ++;
        System.out.println("线程：" + threadName + ",正在执行优先级为：" + index + "的任务" + "size: " + ThreadPoolUtils.getWorkQueue());
*/

        switch (_sendType) {
            case NativeAsyncHttpRequest.GET:
                try {
                    data = nativeAsyncHttpRequest.doSyncGetHttpRequest(_url, _map);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(_handler != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("responseBody", data);
                    Message message = new Message();
                    message.setData(bundle);
                    _handler.sendMessage(message);
                }
                break;
            case NativeAsyncHttpRequest.POST:
                try {
                    data = nativeAsyncHttpRequest.doSyncPostHttpRequest(_url, _map);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(_handler != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("responseBody", data);
                    Message message = new Message();
                    message.setData(bundle);
                    _handler.sendMessage(message);
                }
                break;
            default:
                System.out.println("not support");
        }
    }
}
