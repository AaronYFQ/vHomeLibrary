package com.compass.loco.homelibrary;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ManageLibraryActivity extends AppCompatActivity {
    public final static String INTENT_KEY_TOKEN = "token";
    public final static String INTENT_KEY_SHOPNAME = "shopname";

    private static final String TAG = "ManageLibraryActivity";

    // Android objects
    private EditText editTextLibraryName;

    private ImageButton buttonAdd;
    private ImageButton buttonDelete;
    private ImageButton buttonSave;

    private ListView listViewBooks;

    // selected or non-selected bookinfo arraylist
    private ArrayList<SelectedBookInfo> arrayListSelectedBookInfo;

    private int removeBookCount;

    private String token;
    private String shopName;
    private String newShopName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_library);
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

        editTextLibraryName = (EditText) findViewById(R.id.editTextLibraryName);

        buttonAdd = (ImageButton) findViewById(R.id.buttonAdd);
        buttonDelete = (ImageButton) findViewById(R.id.buttonDelete);
        buttonSave = (ImageButton) findViewById(R.id.buttonSave);

        listViewBooks = (ListView) findViewById(R.id.listViewBooks);
        listViewBooks.setTextFilterEnabled(true);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add();
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });

        listViewBooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {

                // show a toast with the TextView test when clicked
                Toast.makeText(getApplicationContext(),
                        arrayListSelectedBookInfo.get(position).getBookInfo().getName() + "(line " + Integer.toString(position) + ") clicked!",
                        Toast.LENGTH_SHORT).show();

                String bookName = arrayListSelectedBookInfo.get(position).getBookInfo().getName();

                Intent intent = new Intent(getApplicationContext(), ManageBookActivity.class);

                intent.putExtra("user", token);
                intent.putExtra("shopname", shopName);
                intent.putExtra("bookname",  bookName);
                intent.putExtra("request",  "browse");

                startActivity(intent);

            }
        });

        // get application private shared preference
        SharedPreferences sharePref = getSharedPreferences(GlobalParams.PREF_NAME, Context.MODE_PRIVATE);

        // 1. token
        token = sharePref.getString("token", "zhong");

        // 2. shop name
        shopName = sharePref.getString("shopname", "wangfujing");

        Log.d(TAG, "from private shared preference: token = " + token + ", shopname = " + shopName);
    }


    private void getLibraryBooks() {

        final ManageLibraryActivity activity = this;

        final Handler handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {

                String jsonText = msg.getData().getString("responseBody");

                Log.d(TAG, "jsonText = " + jsonText);

                arrayListSelectedBookInfo = new ArrayList<SelectedBookInfo>();

                try {

                    JSONObject jsonObj = new JSONObject(jsonText);

                    String result = jsonObj.getString("result");

                    if(result.equals("success")) {

                        JSONArray jsonArray = jsonObj.getJSONArray("books");

                        Log.d(TAG, "total of books = " + new Integer(jsonArray.length()).toString());

                        for (int i = 0; i < jsonArray.length(); ++i) {

                            jsonObj = jsonArray.getJSONObject(i);

                            arrayListSelectedBookInfo.add(
                                    new SelectedBookInfo(
                                            new BookInfo(
                                                    jsonObj.getString("name"),
                                                    jsonObj.getString("author"),
                                                    jsonObj.getString("publisher"),
                                                    jsonObj.getString("isbn"),
                                                    jsonObj.getString("detail"),
                                                    jsonObj.getString("imageurl"),
                                                    (jsonObj.getBoolean("state"))),
                                            false));
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
                    ListViewAdapterManageBook myListViewAdapterManageBook = new ListViewAdapterManageBook(activity, arrayListSelectedBookInfo);
                    listViewBooks.setAdapter(myListViewAdapterManageBook);
                }
            }
        };

        editTextLibraryName.setText(shopName);

        HttpUtil httptd = new HttpUtil();

        httptd.submitAsyncHttpClientGetManageShop(token, shopName, handler);

    }


    private void add() {
        Toast.makeText(getApplicationContext(), "Add...", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, ScanBookActivity.class);

        intent.putExtra(INTENT_KEY_TOKEN, token);
        intent.putExtra(INTENT_KEY_SHOPNAME, shopName);

        startActivity(intent);
    }

    private void delete() {

        Toast.makeText(getApplicationContext(), "Delete...", Toast.LENGTH_SHORT).show();

        final Handler handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {

                String jsonText = msg.getData().getString("responseBody");

                Log.d(TAG, "jsonText = " + jsonText);

                try {

                    JSONObject jsonObj = new JSONObject(jsonText);

                    String result = jsonObj.getString("result");

                    if(!result.equals("success")) {

                        Toast.makeText(getApplicationContext(), "delete book failed! result=" + result, Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {

                    Toast.makeText(getApplicationContext(), "unknown response remote service!", Toast.LENGTH_SHORT).show();

                    e.printStackTrace();

                } finally {

                    if(--removeBookCount == 0) {

                        getLibraryBooks(); // refresh listview

                    }
                }

            }
        };

        HttpUtil httptd = new HttpUtil();

        removeBookCount = 0;

        for(SelectedBookInfo selectedBookInfo : arrayListSelectedBookInfo) {

            if(selectedBookInfo.isSelected()) {

                Log.d(TAG, "delete book = " + selectedBookInfo.getBookInfo().getName());

                httptd.submitAsyncHttpClientPostRemoveBook(token, shopName, selectedBookInfo.getBookInfo().getName(), handler);

                ++removeBookCount;
            }
        }
    }

    private void save() {

        Toast.makeText(getApplicationContext(), "Save...", Toast.LENGTH_SHORT).show();

        newShopName = editTextLibraryName.getText().toString();
        if(newShopName.length() > 0) {

            final Handler handler = new Handler() {

                @Override
                public void handleMessage(Message msg) {

                    String jsonText = msg.getData().getString("responseBody");

                    Log.d(TAG, "jsonText = " + jsonText);

                    try {

                        JSONObject jsonObj = new JSONObject(jsonText);

                        String result = jsonObj.getString("result");

                        if(result.equals("success"))
                        {
                            // get application private shared preference
                            SharedPreferences sharedPref  = getSharedPreferences(GlobalParams.PREF_NAME, Context.MODE_PRIVATE);

                            // store new shop name
                            SharedPreferences.Editor sharedata = sharedPref.edit();
                            sharedata.putString("shopname", newShopName);
                            sharedata.commit();

                            Log.d(TAG, "change shop name response received: " + shopName + "==>" + newShopName);

                            shopName = newShopName;

                        }
                        else
                        {

                            Toast.makeText(getApplicationContext(), "modify shop name failed! result=" + result, Toast.LENGTH_SHORT).show();

                        }

                    } catch (JSONException e) {

                        Toast.makeText(getApplicationContext(), "unknown response from remote service!", Toast.LENGTH_SHORT).show();

                        e.printStackTrace();

                    } finally {

                        editTextLibraryName.setText(shopName);

                    }
                }
            };

            HttpUtil httptd = new HttpUtil();

            Log.d(TAG, "send change shop name request: " + shopName + "==>" + newShopName);

            httptd.submitAsyncHttpClientPostModifyShopName(token, shopName, newShopName, handler);

        }
        else {

            Toast.makeText(getApplicationContext(), "Please input the name...", Toast.LENGTH_SHORT).show();
        }

    }

}
