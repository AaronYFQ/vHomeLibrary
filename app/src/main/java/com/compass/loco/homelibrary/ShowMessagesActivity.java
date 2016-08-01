package com.compass.loco.homelibrary;

import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ShowMessagesActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_messages);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeViewMessages);
        swipeRefreshLayout.setOnRefreshListener(this);

        messageList=(ListView)findViewById(R.id.listViewMessages);
        cursorAdapter = new MessageCursorAdapter(this, null, 0);
        messageList.setAdapter(cursorAdapter);

        db.open();
        db.cleanMessage();
        db.close();

         MessageIntentService.startActionPoll(this, "Test");
    }

    DBAdapter db = new DBAdapter(this);
    MessageCursorAdapter cursorAdapter;
    ListView messageList;
    SwipeRefreshLayout swipeRefreshLayout;

    public void getNewMessages()
    {
        final Handler handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                String json = msg.getData().getString("responseBody");
                Log.v("handleMessage", json);
                try {
                    // handler item from Json
                    JSONObject jsonObj = new JSONObject(json);
                    JSONArray jsonArray = jsonObj.getJSONArray("messages");

                    Log.v("number of Messages: ", new Integer(jsonArray.length()).toString());

                    if(jsonArray.length() == 0)
                        return;

                    db.open();

                    for (int i = 0; i < jsonArray.length(); ++i) {
                        jsonObj = jsonArray.getJSONObject(i);

                        Log.v("Message: ", jsonObj.toString());

                        MessageInfo msgInfo = new MessageInfo(
                                jsonObj.getString("book"),
                                jsonObj.getString("shop"),
                                jsonObj.getString("owner"),
                                jsonObj.getString("borrower"),
                                jsonObj.getString("action"),
                                jsonObj.getString("time"));

                        db.insertMessage(msgInfo);
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                finally {
                    db.close();
                }
            }
        };

        HttpUtil httptd = new HttpUtil();
        httptd.submitAsyncHttpClientGetMessage("zhong", handler);
    }

    private int count = 0;
    public void DisplayMessages()
    {
        db.open();

        try {
            count++;
            //MessageInfo msgInfo = new MessageInfo("Book" + count, "Shop", "Owner", "Borrower", "Action", "Time");
            //db.insertMessage(msgInfo);

            Cursor cursor = db.getAllMessages();
            cursorAdapter.swapCursor(cursor);
        }
        finally {
            db.close();
        }
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        getNewMessages();
        DisplayMessages();
        swipeRefreshLayout.setRefreshing(false);
    }


/*    private class CheckingThread extends Thread
    {
        @Override
        public void run()
        {
            int count = 0;

            while(true)
            {
                count ++;

                try {
                    Thread.sleep(5000);

                    db.open();

                    //String msg = db.getMessage(1);
                    arrayAdapter.insert("number" + count, 0);
                    arrayAdapter.notifyDataSetChanged();
                    messageList.setSelection(0);

                    //db.deleteMessage(1);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally
                {
                    db.close();
                }
            }
        }
    }*/
}
