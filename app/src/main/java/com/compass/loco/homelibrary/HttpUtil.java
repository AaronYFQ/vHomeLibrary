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
    //String url = "http://54.204.114.208:8000/";
    String registUrl = "http://192.168.56.1:8000/book/regist/";
    String loginUrl = "http://192.168.56.1:8000/book/login/";
    String creatShopUrl = "http://192.168.56.1:8000/book/createShop/";
    String manageShopUrl = "http://192.168.56.1:8000/book/manageShop/";
    String getBookInfoUrl = "http://192.168.56.1:8000/book/getBook/";
    String searchBookUrl = "http://192.168.56.1:8000/book/searchBook/";
    String addBookUrl = "http://192.168.56.1:8000/book/addBook/";
    String searchAreaUrl = "http://192.168.56.1:8000/book/searchArea/";

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
        String url = searchAreaUrl + "?" + "areaname=" + areaName;

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
