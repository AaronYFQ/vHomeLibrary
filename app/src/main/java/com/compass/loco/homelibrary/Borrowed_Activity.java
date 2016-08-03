package com.compass.loco.homelibrary;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;
import android.widget.Toast;

import com.compass.loco.homelibrary.widge.BorrowedHistory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Borrowed_Activity extends TabActivity {

    public TabHost tabHost;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow);

        /* tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();*/
        tabHost = getTabHost();


        TabHost.TabSpec requestBorrowedSpec = tabHost.newTabSpec("在借书单");
        requestBorrowedSpec.setIndicator("在借书单");
        Intent requestBorrowedIntent = new Intent(this, RequestedBorrow.class);
        requestBorrowedSpec.setContent(requestBorrowedIntent);
        // TextView x = (TextView) tabHost.getTabWidget().getChildAt(0).findViewById(android.R.id.title);
        //  x.setTextSize(17);
        //在借书单
        /*TabHost.TabSpec BorrowedNotReturnSpec = tabHost.newTabSpec("在借书单");
        BorrowedNotReturnSpec.setIndicator("在借书单");
        Intent BorrowedNotReturnIntent = new Intent(this, BorrowedNotReturn.class);
        BorrowedNotReturnSpec.setContent(BorrowedNotReturnIntent);
        // TextView y = (TextView) tabHost.getTabWidget().getChildAt(1).findViewById(android.R.id.title);
        //y.setTextSize(17);
        */
        //
        TabHost.TabSpec BorrowedHistorySpec = tabHost.newTabSpec("历史借书");
        BorrowedHistorySpec.setIndicator("历史借书");
        Intent BorrowedHistoryIntent = new Intent(this, BorrowedHistory.class);
        BorrowedHistorySpec.setContent(BorrowedHistoryIntent);
        // TextView z = (TextView) tabHost.getTabWidget().getChildAt(2).findViewById(android.R.id.title);
        //z.setTextSize(17);

        // Adding all TabSpec to TabHost
        tabHost.addTab(requestBorrowedSpec);
        //tabHost.addTab(BorrowedNotReturnSpec);
        tabHost.addTab(BorrowedHistorySpec);

        tabHost.setCurrentTabByTag("在借书单");

        //标签切换事件处理，setOnTabChangedListener
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
               /* if (tabId.equals("求借书单")) {   //第二个标签
                    tabHost.setCurrentTabByTag("求借书单");
                }*/
                if (tabId.equals("在借书单")) {   //第三个标签
                    tabHost.setCurrentTabByTag("在借书单");
                }
                if (tabId.equals("历史借书")) {   //第三个标签
                    tabHost.setCurrentTabByTag("历史借书");
                }
            }
        });
    }
}


