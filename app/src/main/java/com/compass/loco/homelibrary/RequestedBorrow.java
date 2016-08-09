package com.compass.loco.homelibrary;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.compass.loco.homelibrary.http.HttpUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RequestedBorrow extends ListActivity {

    private ListView  mListView;
    private ArrayList<BorrowedBookInfo> arrayListSelectedBookInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requested_borrow);
        init();
        getBorrowedbookes();
    }
    public void init(){

        mListView = getListView();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {

                // show a toast with the TextView test when clicked
                /*Toast.makeText(getApplicationContext(),
                        arrayListSelectedBookInfo.get(position).getBookName() + "(line " + Integer.toString(position) + ") clicked!",
                        Toast.LENGTH_SHORT).show();*/

                String bookName = arrayListSelectedBookInfo.get(position).getBookName();
                String shopName = arrayListSelectedBookInfo.get(position).getShopName();
                String user = arrayListSelectedBookInfo.get(position).getShopOwner();

                Intent intent = new Intent(getApplicationContext(), ManageBookActivity.class);

                intent.putExtra("user", user);
                intent.putExtra("shopname", shopName);
                intent.putExtra("bookname",  bookName);
                intent.putExtra("request",  "browse");

                startActivity(intent);

            }
        });

    }

    public void getBorrowedbookes(){
        final RequestedBorrow activity = this;
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String jsonText = msg.getData().getString("responseBody");
                Log.v("Borrow responseBody:", jsonText);
                try {

                    JSONObject jsonObj = new JSONObject(jsonText);
                    String result = jsonObj.getString("result");
                    if(result.equals("success")) {
                        JSONArray jsonArray = jsonObj.getJSONArray("books");
                        int numofBooks = jsonArray.length();
                        if(numofBooks>0) {
                            arrayListSelectedBookInfo = new ArrayList<BorrowedBookInfo>();
                            for (int i = 0; i < jsonArray.length(); ++i) {

                                jsonObj = jsonArray.getJSONObject(i);
                                BorrowedBookInfo bookInfo;
                                bookInfo = new BorrowedBookInfo(
                                        jsonObj.getString("book"),
                                        jsonObj.getString("shop"),
                                        jsonObj.getString("owner"),
                                        "",
                                        jsonObj.getString("starttime"),
                                        jsonObj.getString("accepttime"),
                                        jsonObj.getString("finishtime"),
                                        "",
                                        jsonObj.getString("state"));
                                arrayListSelectedBookInfo.add(bookInfo);
                            }
                            RequestBorrowAdapter myRequestBorrowAdapter = new RequestBorrowAdapter(activity, arrayListSelectedBookInfo);
                            setListAdapter(myRequestBorrowAdapter);
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "您还没有借书记录哈", Toast.LENGTH_SHORT).show();
                        }
                    }

                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "unknown response from remote service!", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        };

        SharedPreferences sharedata = getSharedPreferences(GlobalParams.PREF_NAME,Context.MODE_PRIVATE);
        String token = sharedata.getString("token", null);
        if(token!=null)
        {
            if(!token.equals(""))
            {
                HttpUtil httptd = new HttpUtil();
                httptd.submitAsyncHttpClientGetCurrentBorrowBook(token,handler);
                Log.v("Borrow:","HTTP request " + "token:"+ token);
            }else {
                Toast.makeText(getApplicationContext(), "请先登陆或注册", Toast.LENGTH_SHORT).show();
            }

        }

        /*HttpUtil httptd = new HttpUtil();
        httptd.submitAsyncHttpClientGetCurrentBorrowBook("compass",handler);
        Log.v("Borrow:","HTTP request");
        arrayListSelectedBookInfo = new ArrayList<BorrowedBookInfo>();
        BorrowedBookInfo bookInfo;
        bookInfo = new BorrowedBookInfo(
                "天线宝宝",
                "新华书店",
                "李泽西园",
                "2016-07-15",
                "2016-07-15",
                "2016-07-25",
                "",
                "borrowing");
        arrayListSelectedBookInfo.add(bookInfo);

        bookInfo = new BorrowedBookInfo(
                "花园宝宝",
                "新华书店",
                "李泽西园",
                "2016-07-16",
                "2016-07-16",
                "2016-07-25",
                "",
                "borrowed");
        arrayListSelectedBookInfo.add(bookInfo);

        RequestBorrowAdapter myRequestBorrowAdapter = new RequestBorrowAdapter(activity, arrayListSelectedBookInfo);
        setListAdapter(myRequestBorrowAdapter);*/

    }
}