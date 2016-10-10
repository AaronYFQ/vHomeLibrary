package com.compass.loco.homelibrary;

/**
 * Created by esaabbh on 7/27/2016.
 */


import android.os.Handler;

import com.compass.loco.homelibrary.http.NativeAsyncHttpRequest;
import com.compass.loco.homelibrary.http.ThirdPartyAsyncHttpRequest;

import java.util.HashMap;
import java.util.Map;


public class HttpUtil {
    static final String REMOTE_URL = "http://52.87.254.11:8011";
    //static final String REMOTE_URL = "http://ec2-54-204-114-208.compute-1.amazonaws.com:8000";
    //static final String REMOTE_URL = "http://123.206.62.57";
    final String REGIST_URL = REMOTE_URL+"/book/regist/";
    final String LOGIN_URL = REMOTE_URL+"/book/login/";
    final String CREATESHOP_URL = REMOTE_URL+"/book/createShop/";
    final String MANAGESHOP_URL = REMOTE_URL+"/book/manageShop/";
    final String BROWSESHOP_URL = REMOTE_URL+"/book/browseShop/";
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
    final String POST_RETURN_BOOK_URL = REMOTE_URL+"/book/returnBook/";

    static final String CHECK_MESSAGE_URL  = REMOTE_URL + "/book/checkMessage/";
    public static final String GET_MESSAGE_URL = REMOTE_URL + "/book/getMessage/";
    public static final String POST_REGISTER_ID = REMOTE_URL + "/book/postRegisterID/";
    private ThirdPartyAsyncHttpRequest proxy = new ThirdPartyAsyncHttpRequest();

    public void submitAsyncHttpClientPostRegisterUser(String userName, String pwd, final Handler handler) {
        Map<String, String> param = new HashMap<String, String>();
        param.put("username", userName);
        param.put("password", pwd);

        proxy.asyncHttpRequestPostHandler(REGIST_URL, param, handler);
    }

    public void submitAsyncHttpClientPostLogin(String userName, String pwd, final Handler handler){

        Map<String, String> param = new HashMap<String, String>();
        param.put("username", userName);
        param.put("password", pwd);

        proxy.asyncHttpRequestPostHandler(LOGIN_URL, param, handler);
    }

    public void submitAsyncHttpClientPostCreateShop(String token, String shopName,
                                                    String shopAddr, String shopComment, final Handler handler) {

        Map<String, String> param = new HashMap<String, String>();
        param.put("token", token);
        param.put("shopname", shopName);
        param.put("shopaddr", shopAddr);
        param.put("shopcomment", shopComment);

        proxy.asyncHttpRequestPostHandler(CREATESHOP_URL, param, handler);
    }

    public void submitAsyncHttpClientGetManageShop(String token, String shopName, final Handler handler) {

        Map<String, String> param = new HashMap<String, String>();
        param.put("token", token);
        param.put("shopname", shopName);

        proxy.asyncHttpRequestGetHandler(MANAGESHOP_URL, param, handler);
    }

    public void submitAsyncHttpClientGetBrowseShop(String user, String shopName, final Handler handler) {

        Map<String, String> param = new HashMap<String, String>();
        param.put("user", user);
        param.put("shopname", shopName);

        proxy.asyncHttpRequestGetHandler(BROWSESHOP_URL, param, handler);
    }

    public void submitAsyncHttpClientPostModifyShopName(String token, String shopOldName, String shopNewName, final Handler handler){

        Map<String, String> param = new HashMap<String, String>();
        param.put("token", token);
        param.put("oldname", shopOldName);
        param.put("newname", shopNewName);

        proxy.asyncHttpRequestPostHandler(MODIFYSHOPNAME_URL, param, handler);
    }
    public void submitAsyncHttpClientPostAddBook(String token, String shopName, String bookName,String bookAuthor,
                                                 String bookPublisher, String bookIsbn, String bookComments, String imageURL, String extLink, String bookNum, final Handler handler){

        Map<String, String> param = new HashMap<String, String>();
        param.put("token", token);
        param.put("shopname", shopName);
        param.put("bookname", bookName);
        param.put("bookauthor", bookAuthor);
        param.put("bookpublisher", bookPublisher);
        param.put("bookisbn", bookIsbn);
        param.put("bookcomments", bookComments);
        param.put("imageurl", imageURL);
        param.put("extlink", extLink);
        param.put("booknum",bookNum);
        ///param.put("availNum","1");

        proxy.asyncHttpRequestPostHandler(ADDBOOK_URL, param, handler);
    }

    public void submitAsyncHttpClientPostRemoveBook(String token, String shopName, String bookName, int bookNum,  final Handler handler) {

        Map<String, String> param = new HashMap<String, String>();
        param.put("token", token);
        param.put("shopname", shopName);
        param.put("bookname", bookName);
        param.put("booknum", "" + bookNum);

        proxy.asyncHttpRequestPostHandler(REMOVEBOOK_URL, param, handler);
    }

    public void submitAsyncHttpClientGetViewBook(String shopUser, String shopName, String bookName,final Handler handler) {

        Map<String, String> param = new HashMap<String, String>();
        param.put("shopuser", shopUser);
        param.put("shopname", shopName);
        param.put("bookname", bookName);

        proxy.asyncHttpRequestGetHandler(GETBOOKINFO_URL, param, handler);
    }

    public void submitAsyncHttpClientGetRequestBorrowBook(String token, String shopUser, String shopName, String bookName, final Handler handler) {
        Map<String, String> param = new HashMap<String, String>();
        param.put("token",token);
        param.put("shopuser", shopUser);
        param.put("shopname", shopName);
        param.put("bookname", bookName);

        proxy.asyncHttpRequestGetHandler(GET_REQ_BORROW_BOOK_URL, param, handler);
    }

    public void submitAsyncHttpClientPostBorrowAction(String ownerToken, String ownerShop,
                                                      String ownerBook, String borrower, String action, final Handler handler) {

        Map<String, String> param = new HashMap<String, String>();
        param.put("token", ownerToken);
        param.put("borrower", borrower);
        param.put("shopname", ownerShop);
        param.put("bookname", ownerBook);
        param.put("action", action);

        proxy.asyncHttpRequestPostHandler(POST_BORROW_ACTION_URL, param, handler);
    }

    public void submitAsyncHttpClientPostReturnBook(String ownerToken, String ownerShop,
                                                    String ownerBook, String borrows, final Handler handler) {

        Map<String, String> param = new HashMap<String, String>();
        param.put("token", ownerToken);
        param.put("shopname", ownerShop);
        param.put("bookname", ownerBook);
        param.put("borrows", borrows);

        proxy.asyncHttpRequestPostHandler(POST_RETURN_BOOK_URL, param, handler);
    }

    public void submitAsyncHttpClientGetCurrentBorrowBook(String token, final Handler handler) {

        Map<String, String> param = new HashMap<String, String>();
        param.put("token", token);

        proxy.asyncHttpRequestGetHandler(GET_CURRENT_BORROW_URL, param, handler);
    }

    public void submitAsyncHttpClientGetHistoryBorrowBook(String token, final Handler handler) {
        Map<String, String> param = new HashMap<String, String>();
        param.put("token", token);

        proxy.asyncHttpRequestGetHandler(GET_HISTORY_BORROW_URL, param, handler);
    }

    public void submitAsyncHttpClientGetAttentionBook(String token, final Handler handler) {

        Map<String, String> param = new HashMap<String, String>();
        param.put("token", token);

        proxy.asyncHttpRequestGetHandler(GET_ATTENTION_BOOK_URL, param, handler);
    }

    public void submitAsyncHttpClientGetAttentionShop(String token, final Handler handler) {

        Map<String, String> param = new HashMap<String, String>();
        param.put("token", token);

        proxy.asyncHttpRequestGetHandler(GET_ATTENTION_SHOP_URL, param, handler);
    }

    public void submitAsyncHttpClientGetSearchBook(String bookName,final Handler handler) {

        Map<String, String> param = new HashMap<String, String>();
        param.put("bookname", bookName);

        proxy.asyncHttpRequestGetHandler(SEARCHBOOK_URL, param, handler);
    }

    public void submitAsyncHttpClientGetSearchArea(String areaName,final Handler handler) {

        Map<String, String> param = new HashMap<String, String>();
        param.put("areaname", areaName);

        proxy.asyncHttpRequestGetHandler(SEARCHAREA_URL, param, handler);
    }

    public void submitAsyncHttpClientGetMessage(String token, final Handler handler) {

        Map<String, String> param = new HashMap<String, String>();
        param.put("token", token);

        proxy.asyncHttpRequestGetHandler(GET_MESSAGE_URL, param, handler);
    }

    public void submitAsyncHttpClientPostRegisterID(String token, String registerID, final Handler handler) {

        Map<String, String> param = new HashMap<String, String>();
        param.put("token", token);
        param.put("regid", registerID);

        proxy.asyncHttpRequestPostHandler(POST_REGISTER_ID, param, handler);
    }
}
