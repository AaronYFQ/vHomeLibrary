package com.compass.loco.homelibrary.widge;

import android.app.ListActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.compass.loco.homelibrary.BorrowedBookInfo;
import com.compass.loco.homelibrary.BorrowedHistoryAdapter;
import com.compass.loco.homelibrary.GlobalParams;
import com.compass.loco.homelibrary.HttpUtil;
import com.compass.loco.homelibrary.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BorrowedHistory extends ListActivity {

    private ArrayList<BorrowedBookInfo> arrayListSelectedBookInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrowed_history);
        getBorrowedHistorybookes();
    }

    public void getBorrowedHistorybookes(){
        final BorrowedHistory activity = this;
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String jsonText = msg.getData().getString("responseBody");
                Log.v("BorrowHis resp:", jsonText);
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
                                        "",
                                        jsonObj.getString("starttime"),
                                        jsonObj.getString("accepttime"),
                                        jsonObj.getString("finishtime"),
                                        "",
                                        jsonObj.getString("state"));
                                arrayListSelectedBookInfo.add(bookInfo);
                            }

                            BorrowedHistoryAdapter myBorrowedHistoryAdapter = new BorrowedHistoryAdapter(activity, arrayListSelectedBookInfo);
                            setListAdapter(myBorrowedHistoryAdapter);
                        }else {
                            Toast.makeText(getApplicationContext(), "您还没有借书记录哈", Toast.LENGTH_SHORT).show();
                        }
                    }

                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "unknown response from remote service!", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        };

        SharedPreferences sharedata = getSharedPreferences(GlobalParams.PREF_NAME, Context.MODE_PRIVATE);
        String token = sharedata.getString("token", null);
        if(token!=null)
        {
            if(!token.equals(""))
            {
                HttpUtil httptd = new HttpUtil();
                httptd.submitAsyncHttpClientGetHistoryBorrowBook(token,handler);
                Log.v("BorrowHis","HTTP request " + "token:"+ token);
            }else {
                Toast.makeText(getApplicationContext(), "请先登陆或注册", Toast.LENGTH_SHORT).show();
            }

        }
        /*HttpUtil httptd = new HttpUtil();
        httptd.submitAsyncHttpClientGetHistoryBorrowBook("compass",handler);
        Log.v("BorrowHistory:","HTTP request");
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

        BorrowedHistoryAdapter myBorrowedHistoryAdapter = new BorrowedHistoryAdapter(activity, arrayListSelectedBookInfo);
        setListAdapter(myBorrowedHistoryAdapter);*/

    }
}
