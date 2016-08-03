package com.compass.loco.homelibrary;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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

    private static final int REQUEST = 0;
    private static final int AGREE = 1;
    private static final int RETURN = 2;

    // Android objects
    private Button buttonBook;
    private ImageView imageViewBook;
    private TextView textViewBookState;

    private String token;
    private String user;
    private String shopName;
    private String bookName;
    private String request;

    private int flag;

    private String externalDoubanLink;

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

        textViewBookState = (TextView) findViewById(R.id.book_state);

        buttonBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(flag == REQUEST) {

                    registerRequest();
                }
                else if(flag == AGREE){

                    ratifyRequest();
                }
                else
                {
                    registerReturn();
                }

            }
        });


        Intent intent = getIntent();

        user = intent.getStringExtra("user");
        shopName = intent.getStringExtra("shopname");
        bookName = intent.getStringExtra("bookname");
        request = intent.getStringExtra("request");    // request: "browse" or "borrow"

        Log.d(TAG, "pass through intent: " + "user = " + user + ", shopname = " + shopName + ", bookname = " + bookName + ", request=" + request);


        // get application private shared preference
        SharedPreferences sharePref = getSharedPreferences(GlobalParams.PREF_NAME, Context.MODE_PRIVATE);

        // 1. token
        token = sharePref.getString("token", "");

        if(token.equals(user)) {

            if(!request.equals("borrow"))
            {

                buttonBook.setVisibility(View.INVISIBLE);

                flag = RETURN;

            }
            else
            {

                buttonBook.setText("同意借出");

                flag = AGREE;

            }

        }
        else
        {

            buttonBook.setText("发借书请求");


            flag = REQUEST;

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
                        String detail = jsonBookObj.getString("detail");
                        String imageUrl = jsonBookObj.getString("imageurl");

                        externalDoubanLink = jsonBookObj.getString("extlink");

                        ((TextView)activity.findViewById(R.id.book_title)).setText(name);
                        ((TextView)activity.findViewById(R.id.book_author)).setText(author);
                        ((TextView)activity.findViewById(R.id.book_publisher)).setText(publisher);
                        ((TextView)activity.findViewById(R.id.book_isbn)).setText(isbn);
                        ((TextView)activity.findViewById(R.id.book_state)).setText(state? "在库" : "借出");
                        ((TextView)activity.findViewById(R.id.book_summary)).setText(detail);


                        if(flag == REQUEST) {

                            if(state) {

                                buttonBook.setVisibility(View.VISIBLE);

                            }
                            else {

                                buttonBook.setVisibility(View.INVISIBLE);

                            }
                        }
                        else if(flag == AGREE){

                            if(state) {

                                buttonBook.setVisibility(View.VISIBLE);

                            }
                            else {

                                buttonBook.setVisibility(View.INVISIBLE);

                            }

                        }
                        else {

                            if(state) {

                                buttonBook.setVisibility(View.INVISIBLE);

                            }
                            else {

                                buttonBook.setVisibility(View.VISIBLE);

                            }
                        }

                        if(imageUrl.length() > 0) {

                            new ImageLoadTask(imageUrl, imageViewBook).execute();

                        }
                        else
                        {

                            imageViewBook.setImageResource(getResources().getIdentifier("@drawable/default_book_picture", null, getPackageName()));

                        }
                    }
                    else
                    {

                        Toast.makeText(getApplicationContext(), "book not found!", Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {

                    Toast.makeText(getApplicationContext(), "unknown response from remote service!", Toast.LENGTH_SHORT).show();

                    e.printStackTrace();

                }
            }
        };


        externalDoubanLink = null;

        HttpUtil httptd = new HttpUtil();
        httptd.submitAsyncHttpClientGetViewBook(user, shopName, bookName, handler);

    }

    private void registerRequest() {

        final Handler handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {

                String jsonText = msg.getData().getString("responseBody");

                Log.d(TAG, "jsonText = " + jsonText);

                try {

                    JSONObject jsonObj = new JSONObject(jsonText);

                    String result = jsonObj.getString("result");

                    if(result.equals("success")) {

                        Toast.makeText(getApplicationContext(), "request has registered!", Toast.LENGTH_SHORT).show();

                    }
                    else {

                        Toast.makeText(getApplicationContext(), "accept borrow request failed! result=" + result, Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {

                    Toast.makeText(getApplicationContext(), "unknown response remote service!", Toast.LENGTH_SHORT).show();

                    e.printStackTrace();

                }

            }
        };

        HttpUtil httptd = new HttpUtil();

        httptd.submitAsyncHttpClientGetRequestBorrowBook(token, user, shopName, bookName, handler);

    }


    private void ratifyRequest() {

        final Handler handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {

                String jsonText = msg.getData().getString("responseBody");

                Log.d(TAG, "jsonText = " + jsonText);

                try {

                    JSONObject jsonObj = new JSONObject(jsonText);

                    String result = jsonObj.getString("result");

                    if(result.equals("success")) {

                        buttonBook.setText("归还本书");

                        flag = RETURN;

                        textViewBookState.setText("借出");

                    }
                    else {

                        Toast.makeText(getApplicationContext(), "accept borrow request failed! result=" + result, Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {

                    Toast.makeText(getApplicationContext(), "unknown response remote service!", Toast.LENGTH_SHORT).show();

                    e.printStackTrace();

                }

            }
        };

        HttpUtil httptd = new HttpUtil();

        httptd.submitAsyncHttpClientPostBorrowAction(token, shopName, bookName, user, "accept" /* "refuse" */, handler);

    }

    private void registerReturn () {

        final Handler handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {

                String jsonText = msg.getData().getString("responseBody");

                Log.d(TAG, "jsonText = " + jsonText);

                try {

                    JSONObject jsonObj = new JSONObject(jsonText);

                    String result = jsonObj.getString("result");

                    if(result.equals("success")) {

                        textViewBookState.setText("在库");

                        buttonBook.setVisibility(View.INVISIBLE);

                    }
                    else {

                        Toast.makeText(getApplicationContext(), "accept borrow request failed! result=" + result, Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {

                    Toast.makeText(getApplicationContext(), "unknown response remote service!", Toast.LENGTH_SHORT).show();

                    e.printStackTrace();

                }

            }
        };

        HttpUtil httptd = new HttpUtil();
        
        httptd.submitAsyncHttpClientPostReturnBook(token, shopName, bookName,  handler);
    }

    public void onDoubanLinkClick(View view) {

        if(externalDoubanLink != null && externalDoubanLink.length() > 0) {

            Log.d(TAG, "open external web page: " + externalDoubanLink);

            openWebPage(externalDoubanLink);

        }

    }

    private void openWebPage(String url) {

        Uri webpage = Uri.parse(url);

        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);

        if (intent.resolveActivity(getPackageManager()) != null) {

            startActivity(intent);

        }
    }
}
