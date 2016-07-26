package com.compass.loco.homelibrary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ShowMessages extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_messages);

        listMessages();
    }

    public void listMessages()
    {
        ListView messageList=(ListView)findViewById(R.id.listViewMessages);

        ArrayList<String> messageArrayList = new ArrayList<String>();
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
        messageArrayList.add("Get request16.");


        // Create The Adapter with passing ArrayList as 3rd parameter
        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, messageArrayList);
        // Set The Adapter
        messageList.setAdapter(arrayAdapter);
    }
}
