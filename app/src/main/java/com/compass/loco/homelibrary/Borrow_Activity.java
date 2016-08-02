package com.compass.loco.homelibrary;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Borrow_Activity extends AppCompatActivity {

    BorrowedBookInfo  myBorrowedBookInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow);
    }

    private void getWaitingApprovedList() {

        final Handler handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {

                String jsonText = msg.getData().getString("responseBody");
                Log.v("responseBody", jsonText);
                try {

                    JSONObject jsonObj = new JSONObject(jsonText);

                    JSONArray jsonArray = jsonObj.getJSONArray("books");

                   // myBorrowedBookInfo = new BorrowedBookInfo();

                    Log.v("number of books: ", new Integer(jsonArray.length()).toString());

                    for (int i = 0; i < jsonArray.length(); ++i) {

                        jsonObj = jsonArray.getJSONObject(i);

                        /*arrayListSelectedBookInfo.add(
                                new SelectedBookInfo(
                                        new BookInfo(
                                                jsonObj.getString("name"),
                                                jsonObj.getString("author"),
                                                jsonObj.getString("publisher"),
                                                jsonObj.getString("Shopname"),
                                                null,
                                                (jsonObj.getBoolean("state"))),
                                        false));*/
                    }
                    /*
                    ListViewAdapterManageBook myListViewAdapterManageBook = new ListViewAdapterManageBook(activity, arrayListSelectedBookInfo);
                    listViewBooks.setAdapter(myListViewAdapterManageBook);*/

                } catch (JSONException e) {

                    Toast.makeText(getApplicationContext(), "unknown response from remote service!", Toast.LENGTH_SHORT).show();

                    e.printStackTrace();
                }
            }
        };

       /* editTextLibraryName.setText(shopName);

        HttpUtil httptd = new HttpUtil();

        httptd.submitAsyncHttpClientGetManageShop(token, shopName, handler);*/

    }
}


