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
    static final String REMOTE_URL = "http://54.204.114.208:8000";
    final String REGIST_URL = REMOTE_URL+"/book/regist/";
    final String LOGIN_URL = REMOTE_URL+"/book/login/";
    final String CREATESHOP_URL = REMOTE_URL+"/book/createShop/";
    final String MANAGESHOP_URL = REMOTE_URL+"/book/manageShop/";
    final String MODIFYSHOPNAME_URL = REMOTE_URL+"/book/modifyShop/";
    final String GETBOOKINFO_URL = REMOTE_URL+"/book/getBook/";
    final String SEARCHBOOK_URL = REMOTE_URL+"/book/searchBook/";
    final String ADDBOOK_URL = REMOTE_URL+"/book/addBook/";
    final String REMOVEBOOK_URL = REMOTE_URL+"/book/removeBook/";
    final String SEARCHAREA_URL = REMOTE_URL+"/book/searchArea/";

    final String GET_ATTENTION_SHOP_URL = REMOTE_URL+"/book/getAttentionBook/";
    final String GET_ATTENTION_BOOK_URL = REMOTE_URL+"/book/getAttentionShop/";
    final String GET_CURRENT_BORROW_URL = REMOTE_URL+"/book/getCurrentBorrowBook/";
    final String GET_HISTORY_BORROW_URL = REMOTE_URL+"/book/getHistoryBorrowBook/";
    final String GET_REQ_BORROW_BOOK_URL = REMOTE_URL+"/book/getBorrowBook/";
    final String POST_BORROW_ACTION_URL = REMOTE_URL+"/book/postBorrowAction/";

    static final String CHECK_MESSAGE_URL  = REMOTE_URL + "/book/checkMessage/";
    static final String GET_MESSAGE_URL = REMOTE_URL + "/book/getMessage/";
    private AsyncHttpClient client = new AsyncHttpClient();

    public void asyncHttpClientPostHandler(String url, RequestParams param, final Handler handler) {
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

    public void asyncHttpClientGetHandler(String url, final Handler handler) {
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
    }

    public void submitAsyncHttpClientPostRegisterUser(String userName, String pwd, final Handler handler) {
        RequestParams param = new RequestParams();
        String url = REGIST_URL;
        param.put("username", userName);
        param.put("password", pwd);
        asyncHttpClientPostHandler(url, param, handler);
        return ;
    }

    public void submitAsyncHttpClientPostLogin(String userName, String pwd, final Handler handler){

        RequestParams param = new RequestParams();
        String url = LOGIN_URL;
        param.put("username", userName);
        param.put("password", pwd);

        asyncHttpClientPostHandler(url, param, handler);
        return;
    }

    public void submitAsyncHttpClientPostCreateShop(String token, String shopName,
                                                    String shopAddr, String shopComment, final Handler handler) {

        RequestParams param = new RequestParams();
        String url = CREATESHOP_URL;
        param.put("token", token);
        param.put("shopname", shopName);
        param.put("shopaddr", shopAddr);
        param.put("shopcomment", shopComment);

        asyncHttpClientPostHandler(url, param, handler);
        return;

    }
    public void submitAsyncHttpClientGetManageShop(String token, String shopName, final Handler handler) {

        String url = MANAGESHOP_URL + "?" + "token=" + token + "&shopname=" + shopName;

        asyncHttpClientGetHandler(url, handler);
        return;
    }

    public void submitAsyncHttpClientPostModifyShopName(String token, String shopOldName, String shopNewName, final Handler handler){

        RequestParams param = new RequestParams();
        String url = MODIFYSHOPNAME_URL;
        param.put("token", token);
        param.put("oldname", shopOldName);
        param.put("newname", shopNewName);

        asyncHttpClientPostHandler(url, param, handler);
        return;
    }
    public void submitAsyncHttpClientPostAddBook(String token, String shopName, String bookName,String bookAuthor,
                                                 String bookPublisher, String bookIsdn, String bookComments, String imageURL, String extLink, final Handler handler){

        RequestParams param = new RequestParams();
        String url = ADDBOOK_URL;
        param.put("token", token);
        param.put("shopname", shopName);
        param.put("bookname", bookName);
        param.put("bookauthor", bookAuthor);
        param.put("bookpublisher", bookPublisher);
        param.put("bookisdn", bookIsdn);
        param.put("bookcomments", bookComments);
        param.put("imageurl", imageURL);
        param.put("imageurl", extLink);

        asyncHttpClientPostHandler(url, param, handler);
        return;
    }

    public void submitAsyncHttpClientPostRemoveBook(String token, String shopName, String bookName,  final Handler handler) {

        RequestParams param = new RequestParams();
        String url = REMOVEBOOK_URL;
        param.put("token", token);
        param.put("shopname", shopName);
        param.put("bookname", bookName);

        asyncHttpClientPostHandler(url, param, handler);
        return;
    }
    public void submitAsyncHttpClientGetViewBook(String token, String shopName, String bookName,final Handler handler) {

        String url = GETBOOKINFO_URL + "?" + "token=" + token + "&shopname=" + shopName + "&bookname=" + bookName;
        asyncHttpClientGetHandler(url, handler);
        return;
    }

    public void submitAsyncHttpClientGetRequestBorrowBook(String token, String borrower, String shopName, String bookName, final Handler handler) {
        String url = GET_REQ_BORROW_BOOK_URL + "?" + "token=" + token + "&borrower" + borrower + "&shopname=" + shopName + "&bookname=" + bookName;
        asyncHttpClientGetHandler(url, handler);
        return;
    }

    public void submitAsyncHttpClientPostBorrowAction(String token, String borrower, String shopName,
                                                    String bookName, String action, final Handler handler) {

        RequestParams param = new RequestParams();
        String url = POST_BORROW_ACTION_URL;
        param.put("token", token);
        param.put("borrower", borrower);
        param.put("shopname", shopName);
        param.put("bookname", bookName);
        param.put("action", action);

        asyncHttpClientPostHandler(url, param, handler);
        return;

    }

    public void submitAsyncHttpClientGetCurrentBorrowBook(String token, final Handler handler)
    {
        String url = GET_CURRENT_BORROW_URL + "?" + "token=" + token;
        asyncHttpClientGetHandler(url, handler);
        return;
    }

    public void submitAsyncHttpClientGetHistoryBorrowBook(String token, final Handler handler)
    {
        String url = GET_HISTORY_BORROW_URL + "?" + "token=" + token;
        asyncHttpClientGetHandler(url, handler);
        return;
    }

    public void submitAsyncHttpClientGetAttentionBook(String token, final Handler handler) {
        String url = GET_ATTENTION_BOOK_URL + "?" + "token=" + token;
        asyncHttpClientGetHandler(url, handler);
        return;
    }

    public void submitAsyncHttpClientGetAttentionShop(String token, final Handler handler) {
        String url = GET_ATTENTION_SHOP_URL + "?" + "token=" + token;
        asyncHttpClientGetHandler(url, handler);
        return;
    }

    public void submitAsyncHttpClientGetSearchBook(String bookName,final Handler handler) {

        String url = SEARCHBOOK_URL + "?" + "bookname=" + bookName;
        asyncHttpClientGetHandler(url, handler);
        return;
    }

    public void submitAsyncHttpClientGetSearchArea(String areaName,final Handler handler) {

        String url = SEARCHAREA_URL + "?" + "areaname=" + areaName;
        asyncHttpClientGetHandler(url, handler);
        return;
    }

    public void submitAsyncHttpClientGetMessage(String token, final Handler handler) {

        String url = GET_MESSAGE_URL + "?" + "token=" + token;
        asyncHttpClientGetHandler(url, handler);
        return;
    }

}
