package com.compass.loco.homelibrary;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ShowMessagesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_messages);

        listMessages();
    }

    DBAdapter db = new DBAdapter(this);

    ArrayAdapter<String> arrayAdapter;

    CheckingThread myThread = new CheckingThread();

    ListView messageList;

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

/*        ArrayList<String> messageArrayList = new ArrayList<String>();
        messageArrayList.add("Get request1.");
        messageArrayList.add("Get request2.");
        messageArrayList.add("Get request3.");
        messageArrayList.add("Get request4.");
        messageArrayList.add("Get request5.");
        messageArrayList.add("Get request6.");
        messageArrayList.add("Get request7.");
        messageArrayList.add("Get request8.");
        messageArrayList.add("Get request9.");
        messageArrayList.add("Get request10.");
        messageArrayList.add("Get request11.");
        messageArrayList.add("Get request12.");
        messageArrayList.add("Get request13.");
        messageArrayList.add("Get request14.");
        messageArrayList.add("Get request15.");
        messageArrayList.add("Get request16.");*/

        messageList=(ListView)findViewById(R.id.listViewMessages);

        // Create The Adapter with passing ArrayList as 3rd parameter
        arrayAdapter =
                new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, messageArrayList);
        // Set The Adapter
        messageList.setAdapter(arrayAdapter);

        myThread.start();
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
