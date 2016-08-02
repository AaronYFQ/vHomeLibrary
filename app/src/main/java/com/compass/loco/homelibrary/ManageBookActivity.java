package com.compass.loco.homelibrary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

/**
 * Created by EXIAOQU on 7/29/2016.
 */
public class ManageBookActivity extends AppCompatActivity {

    private static final String TAG = "ManageBookActivity";

    private String token;
    private String shopName;
    private String bookName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_book);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.d(TAG, "onCreate() called");

        init();

        getBook();

    }

    private void init() {

        Intent intent = getIntent();

        token = intent.getStringExtra("token");
        shopName = intent.getStringExtra("shopname");
        bookName = intent.getStringExtra("bookname");

        Log.d(TAG, "pass through intent: " + "token = " + token + ", shopname = " + shopName + ", bookname = " + bookName);

    }

    private void getBook() {

    }
}
