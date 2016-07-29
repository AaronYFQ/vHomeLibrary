package com.compass.loco.homelibrary;
/**
 * Created by esaabbh on 7/27/2016.
 */


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;



public class HttpUtil {
    String remoteUrl = "http://54.204.114.208:8000";
    String registUrl = remoteUrl+"/book/regist/";
    String loginUrl = remoteUrl+"/book/login/";
    String creatShopUrl = remoteUrl+"/book/createShop/";
    String manageShopUrl = remoteUrl+"/book/manageShop/";
    String getBookInfoUrl = remoteUrl+"/book/getBook/";
    String searchBookUrl = remoteUrl+"/book/searchBook/";
    String addBookUrl = remoteUrl+"/book/addBook/";
    // FIXME: 7/29/2016
    static final String CHECK_MESSAGE_URL  = "";//remoteUrl + "/book/checkMessage/";
    static final String GET_MESSAGE_URL = "";//remoteUrl + "/book/getMessage/";

    public void submitAsyncHttpClientPostRegisterUser(String userName, String pwd, final Handler handler) {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams param = new RequestParams();
        String url = registUrl;
        param.put("username", userName);
        param.put("password", pwd);

        client.post(url, param, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                String iString = new String(responseBody);
                Bundle bundle = new Bundle();
                bundle.putString("responseBody", iString);
                Message message = new Message();
                message.setData(bundle);
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {

            }

        });

        return;
    }
    public void submitAsyncHttpClientPostLogin(String userName, String pwd, final Handler handler){

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams param = new RequestParams();
        String url = loginUrl;
        param.put("username", userName);
        param.put("password", pwd);

        client.post(url, param, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                String iString = new String(responseBody);
                Bundle bundle = new Bundle();
                bundle.putString("responseBody", iString);
                Message message = new Message();
                message.setData(bundle);
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {

            }

        });

        return;
    }
    public void submitAsyncHttpClientPostCreateShop(String token, String shopName,
                                                    String shopAddr, String shopComment, final Handler handler) {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams param = new RequestParams();
        String url = creatShopUrl;
        param.put("token", token);
        param.put("shopname", shopName);
        param.put("shopaddr", shopAddr);
        param.put("shopcomment", shopComment);

        client.post(url, param, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                String iString = new String(responseBody);
                Bundle bundle = new Bundle();
                bundle.putString("responseBody", iString);
                Message message = new Message();
                message.setData(bundle);
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {

            }

        });

        return;

    }
    public void submitAsyncHttpClientGetManageShop(String token, String shopName, final Handler handler) {

        AsyncHttpClient client = new AsyncHttpClient();
        String url = manageShopUrl + "?" + "token=" + token + "&shopname=" + shopName;

        client.get(url, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                String iString = new String(responseBody);
                Bundle bundle = new Bundle();
                bundle.putString("responseBody", iString);
                Message message = new Message();
                message.setData(bundle);
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {

            }

        });

        return;
    }

    public void submitAsyncHttpClientPostAddBook(String token, String shopName, String bookName,String bookAuthor,
                                                 String bookPublisher, String bookIsdn, String bookComments, final Handler handler){

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams param = new RequestParams();
        String url = addBookUrl;
        param.put("token", token);
        param.put("shopname", shopName);
        param.put("bookname", bookName);
        param.put("bookauthor", bookAuthor);
        param.put("bookpublisher", bookPublisher);
        param.put("bookisdn", bookIsdn);
        param.put("bookcomments", bookComments);
     //   Log.v("handleMessage", token);
        client.post(url, param, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                String iString = new String(responseBody);
                Bundle bundle = new Bundle();
                bundle.putString("responseBody", iString);
                Message message = new Message();
                message.setData(bundle);
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {

            }

        });

        return;
    }

    public void submitAsyncHttpClientGetViewBook(String token, String shopName, String bookName,final Handler handler) {

        AsyncHttpClient client = new AsyncHttpClient();
        String url = getBookInfoUrl + "?" + "token=" + token + "&shopname=" + shopName + "&bookname=" + bookName;

        client.get(url, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                String iString = new String(responseBody);
                Bundle bundle = new Bundle();
                bundle.putString("responseBody", iString);
                Message message = new Message();
                message.setData(bundle);
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {

            }

        });

        return;
    }


    public void submitAsyncHttpClientSearchBook(String bookName,final Handler handler) {

        AsyncHttpClient client = new AsyncHttpClient();
        String url = searchBookUrl + "?" + "bookname=" + bookName;

        client.get(url, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                String iString = new String(responseBody);
                Bundle bundle = new Bundle();
                bundle.putString("responseBody", iString);
                Message message = new Message();
                message.setData(bundle);
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {

            }

        });

        return;
    }

    public void submitAsyncHttpClientSearchArea(String areaName,final Handler handler) {

        AsyncHttpClient client = new AsyncHttpClient();
        //String url = searchAreaUrl + "?" + "areaname=" + areaName;
        // FIXME: 7/29/2016
        String url = "";

        client.get(url, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                String iString = new String(responseBody);
                Bundle bundle = new Bundle();
                bundle.putString("responseBody", iString);
                Message message = new Message();
                message.setData(bundle);
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {

            }

        });

        return;
    }

}
