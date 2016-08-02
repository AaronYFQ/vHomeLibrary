package com.compass.loco.homelibrary;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by EXIAOQU on 7/29/2016.
 */
public class BrowseLibraryActivity extends AppCompatActivity {

    private static final String TAG = "BrowseLibraryActivity";

    // Android objects
    private TextView textViewLibraryName;

    private ListView listViewBooks;

    // bookinfo arraylist
    private ArrayList<BookInfo> arrayListBookInfo;

    private String token;
    private String shopName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_library);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.d(TAG, "onCreate() called");

        init();

        getLibraryBooks();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy() called");
        super.onDestroy();
    }

    private void init() {

        textViewLibraryName = (TextView) findViewById(R.id.textViewLibraryName);

        listViewBooks = (ListView) findViewById(R.id.listViewBooks);
        listViewBooks.setTextFilterEnabled(true);

        listViewBooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {

                // show a toast with the TextView test when clicked
                Toast.makeText(getApplicationContext(),
                        arrayListBookInfo.get(position).getName() + "(line " + Integer.toString(position) + ") clicked!",
                        Toast.LENGTH_SHORT).show();

                String bookName = arrayListBookInfo.get(position).getName();

                Intent intent = new Intent(getApplicationContext(), ManageBookActivity.class);

                intent.putExtra("user", token);
                intent.putExtra("shopname", shopName);
                intent.putExtra("bookname",  bookName);
                intent.putExtra("request",  "browse");

                startActivity(intent);

            }
        });


        Intent intent = getIntent();

        token = intent.getStringExtra("token");
        shopName = intent.getStringExtra("shopname");

        Log.d(TAG, "pass through intent: " + "token = " + token + ", shopname = " + shopName);

    }

    private void getLibraryBooks() {

        final BrowseLibraryActivity activity = this;

        final Handler handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {

                String jsonText = msg.getData().getString("responseBody");

                Log.d(TAG, "jsonText = " + jsonText);

                arrayListBookInfo = new ArrayList<BookInfo>();

                try {

                    JSONObject jsonObj = new JSONObject(jsonText);

                    String result = jsonObj.getString("result");

                    if(result.equals("success")) {

                        JSONArray jsonArray = jsonObj.getJSONArray("books");

                        Log.d(TAG, "total of books = " + new Integer(jsonArray.length()).toString());

                        for (int i = 0; i < jsonArray.length(); ++i) {

                            jsonObj = jsonArray.getJSONObject(i);

                            arrayListBookInfo.add(
                                    new BookInfo(
                                            jsonObj.getString("name"),
                                            jsonObj.getString("author"),
                                            jsonObj.getString("publisher"),
                                            jsonObj.getString("isbn"),
                                            jsonObj.getString("detail"),
                                            jsonObj.getString("imageurl"),
                                            (jsonObj.getBoolean("state"))));
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "no book found!", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {

                    Toast.makeText(getApplicationContext(), "unknown response from remote service!", Toast.LENGTH_SHORT).show();

                    e.printStackTrace();
                } finally {
                    ListViewAdapterBrowseBook myListViewAdapterBrowseBook = new ListViewAdapterBrowseBook(activity, arrayListBookInfo);
                    listViewBooks.setAdapter(myListViewAdapterBrowseBook);
                }
            }
        };

        textViewLibraryName.setText(shopName);

        HttpUtil httptd = new HttpUtil();

        httptd.submitAsyncHttpClientGetManageShop(token, shopName, handler);

    }

}
