package com.compass.loco.homelibrary;

import android.database.Cursor;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ShowMessagesActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_messages);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeViewMessages);
        swipeRefreshLayout.setOnRefreshListener(this);

        messageArrayList = new ArrayList<String>();
        messageList=(ListView)findViewById(R.id.listViewMessages);
        arrayAdapter =
                new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, messageArrayList);

        messageList.setAdapter(arrayAdapter);
    }

    DBAdapter db = new DBAdapter(this);
    ArrayList<String> messageArrayList;
    ArrayAdapter<String> arrayAdapter;
    ListView messageList;
    SwipeRefreshLayout swipeRefreshLayout;

    public void listMessages()
    {
        ArrayList<String> messageArrayList = new ArrayList<String>();
        db.open();
        db.cleanMessage();

        try {
            db.insertMessage("new message1.");
            db.insertMessage("new message2.");
            db.insertMessage("new message3.");
            db.insertMessage("new message4.");
            db.insertMessage("new message5.");

            Cursor cursor = db.getAllMessages();

            if (cursor.moveToLast()) {
                do {
                    messageArrayList.add(cursor.getString(cursor.getColumnIndex(DBAdapter.KEY_CONTENT)));
                } while (cursor.moveToPrevious());
            }
        }
        finally {
            db.close();
        }

        //myThread.start();
        MessageIntentService.startActionPoll(this, "Test");
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        arrayAdapter.insert("number", 0);
        arrayAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

    private class CheckingThread extends Thread
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
    }
}
