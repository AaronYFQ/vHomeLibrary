package com.compass.loco.homelibrary;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by EXIAOQU on 7/29/2016.
 */
public class ManageBookActivity extends AppCompatActivity {

    private static final String TAG = "ManageBookActivity";

    // Android objects
    private Button buttonBook;
    private ImageView imageViewBook;

    private String token;
    private String user;
    private String shopName;
    private String bookName;
    private String request;

    private Boolean requestOrAgree = false;

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

        buttonBook = (Button) findViewById(R.id.book_button);

        imageViewBook = (ImageView) findViewById(R.id.book_image_view);

        buttonBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requestOrAgree();

            }
        });

        Intent intent = getIntent();

        user = intent.getStringExtra("user");
        shopName = intent.getStringExtra("shopname");
        bookName = intent.getStringExtra("bookname");
        request = intent.getStringExtra("request");    // request: "browse" or "borrow"

        Log.d(TAG, "pass through intent: " + "user = " + user + ", shopname = " + shopName + ", bookname = " + bookName + ", requsettype" + request);


        // get application private shared preference
        SharedPreferences sharePref = getSharedPreferences(GlobalParams.PREF_NAME, Context.MODE_PRIVATE);

        // 1. token
        token = sharePref.getString("token", "");

        if(token.equals(user)) {

            if(request.equals("browse"))
            {

                buttonBook.setVisibility(View.INVISIBLE);

            }
            else
            {
                requestOrAgree = false;  /* agree */
            }

        }
        else
        {

            buttonBook.setText("发借书请求");

            requestOrAgree = true; /* request */
        }
    }

    private void getBook() {

        final ManageBookActivity activity = this;

        final Handler handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {

                String jsonText = msg.getData().getString("responseBody");

                Log.d(TAG, "jsonText = " + jsonText);

                try {

                    JSONObject jsonObj = new JSONObject(jsonText);

                    String result = jsonObj.getString("result");
                    String jsonBook = jsonObj.getString("book");

                    if(result.equals("success") && jsonBook.length() > 0) {

                        JSONObject jsonBookObj = new JSONObject(jsonBook);

                        String name = jsonBookObj.getString("name");
                        String author = jsonBookObj.getString("author");
                        String publisher = jsonBookObj.getString("publisher");
                        String isbn = jsonBookObj.getString("isbn");
                        Boolean state = jsonBookObj.getBoolean("state");
                        // String detail = jsonBookObj.getString("detail");
                        String imageUrl = jsonBookObj.getString("imageurl");

                        ((TextView)activity.findViewById(R.id.book_title)).setText(name);
                        ((TextView)activity.findViewById(R.id.book_author)).setText(author);
                        ((TextView)activity.findViewById(R.id.book_publisher)).setText(publisher);
                        ((TextView)activity.findViewById(R.id.book_isbn)).setText(isbn);
                        ((TextView)activity.findViewById(R.id.book_state)).setText(state? "在库" : "借出");

                        // ((TextView)activity.findViewById(R.id.book_detail)).setText(detail);

                        if(imageUrl.length() > 0) {

                            new ImageLoadTask(imageUrl, imageViewBook).execute();

                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "book not found!", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {

                    Toast.makeText(getApplicationContext(), "unknown response from remote service!", Toast.LENGTH_SHORT).show();

                    e.printStackTrace();

                } finally {

                }
            }
        };


        HttpUtil httptd = new HttpUtil();
        httptd.submitAsyncHttpClientGetViewBook(token, shopName, bookName, handler);

    }

    private void requestOrAgree() {

    }
}
