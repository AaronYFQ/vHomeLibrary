package com.compass.loco.homelibrary;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Created by EXIAOQU on 7/25/2016.
 */
public class ManageLibraryActivity extends AppCompatActivity {

    // Android objects
    private TextView editTextLibraryName;

    private ImageButton buttonAdd;
    private ImageButton buttonDelete;
    private ImageButton buttonSave;

    private ListView listViewBooks;

    // selected or non-selected bookinfo arraylist
    private ArrayList<SelectedBookInfo> arrayListSelectedBookInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_library);

        init();

        threadGetLibraryBooks();

    }

    private void init() {

        editTextLibraryName = (TextView) findViewById(R.id.editTextLibraryName);

        buttonAdd = (ImageButton) findViewById(R.id.buttonAdd);
        buttonDelete = (ImageButton) findViewById(R.id.buttonDelete);
        buttonSave = (ImageButton) findViewById(R.id.buttonApply);

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
                apply();
            }
        });

        listViewBooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                // show a toast with the TextView test when clicked
                Toast.makeText(getApplicationContext(), "line " + Integer.toString(position + 1) + " clicked!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void threadGetLibraryBooks() {

        getLibraryBooks();

//        new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//
//                getLibraryBooks();
//
//            }
//
//        }).start();

    }

    String token = "zhong";
    private void getLibraryBooks() {

        final ManageLibraryActivity activity = this;

        final Handler handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {

                String jsonText = msg.getData().getString("responseBody");

                Log.v("responseBody", jsonText);

                try {

                    JSONObject jsonObj = new JSONObject(jsonText);

                    JSONArray jsonArray = jsonObj.getJSONArray("books");

                    arrayListSelectedBookInfo = new ArrayList<SelectedBookInfo>();

                    Log.v("number of books: ", new Integer(jsonArray.length()).toString());

                    for (int i = 0; i < jsonArray.length(); ++i) {

                        jsonObj = jsonArray.getJSONObject(i);

                        arrayListSelectedBookInfo.add(
                                new SelectedBookInfo(
                                        new BookInfo(
                                                jsonObj.getString("name"),
                                                jsonObj.getString("author"),
                                                jsonObj.getString("publisher"),
                                                jsonObj.getString("isdn"),
                                                jsonObj.getString("detail"),
                                                null,
                                                (jsonObj.getInt("state") == 1)),
                                        false));
                    }

                    ListViewAdapterBook myListViewAdapterBook = new ListViewAdapterBook(activity, arrayListSelectedBookInfo);
                    listViewBooks.setAdapter(myListViewAdapterBook);

                } catch (JSONException e) {

                    Toast.makeText(getApplicationContext(), "remote service down!", Toast.LENGTH_SHORT).show();

                    e.printStackTrace();
                }
            }
        };

        editTextLibraryName.setText("wangfujing");

        HttpUtil httptd = new HttpUtil();

        httptd.submitAsyncHttpClientGetManageShop(token, "wangfujing", handler);

//        try {
//            /*
//            URL uri =  new URL("http://ec2-54-67-32-254.us-west-1.compute.amazonaws.com/1");
//            URLConnection ucon = uri.openConnection();
//
//            InputStream is = ucon.getInputStream();
//
//            BufferedInputStream bis = new BufferedInputStream(is);
//
//            StringBuffer sb = new StringBuffer();
//
//            byte[] bytes = new byte[200];
//            int count;
//            while ((count = bis.read(bytes)) != -1) {
//                sb.append(new String(bytes, 0, count));
//            }
//
//            myTextViewLibraryName.setText(sb.toString());
//            */
//
//            editTextLibraryName.setText("exiaoqu的水果书店");
//
//            arrayListSelectedBookInfo = new ArrayList<SelectedBookInfo>();
//
//            arrayListSelectedBookInfo.add(new SelectedBookInfo(new BookInfo("Apple Book", "AppleAuthor", "ApplePublisher", "AppleIsbn", "Apple Detail", null, true), false));
//            arrayListSelectedBookInfo.add(new SelectedBookInfo(new BookInfo("Banana Book", "BananaAuthor", "BananaPublisher", "BananaIsbn", "Banana Detail", null, true), false));
//
//            arrayListSelectedBookInfo.add(new SelectedBookInfo(new BookInfo("Blackberry Book", "BlackberryAuthor", "BlackberryPublisher", "BlackberryIsbn", "Blackberry Detail", null, true), false));
//            arrayListSelectedBookInfo.add(new SelectedBookInfo(new BookInfo("Peach Book", "PeachAuthor", "PeachPublisher", "PeachIsbn", "Peach Detail", null, true), false));
//            arrayListSelectedBookInfo.add(new SelectedBookInfo(new BookInfo("Pear Book", "PearAuthor", "PearPublisher", "PearIsbn", "Pear Detail", null, false), false));
//
//            arrayListSelectedBookInfo.add(new SelectedBookInfo(new BookInfo("Grape Book", "GrapeAuthor", "GrapePublisher", "GrapeIsbn", "Grape Detail", null, false), false));
//            arrayListSelectedBookInfo.add(new SelectedBookInfo(new BookInfo("Pineapple Book", "PineappleAuthor", "PineapplePublisher", "Pineapple", "Pineapple Detail", null, false), false));
//            arrayListSelectedBookInfo.add(new SelectedBookInfo(new BookInfo("Mango Book", "MangoAuthor", "MangoPublisher", "MangoIsbn", "Mango Detail", null, false), false));
//
//            ListViewAdapterBook myListViewAdapterBook = new ListViewAdapterBook(this, arrayListSelectedBookInfo);
//            listViewBooks.setAdapter(myListViewAdapterBook);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }


    private void add() {
        Toast.makeText(getApplicationContext(), "Add...", Toast.LENGTH_SHORT).show();

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                String json = msg.getData().getString("responseBody");
                Log.v("handleMessage", json);
                try {
                    // handler item from Json
                    JSONObject item = new JSONObject(json);
                    String comment = item.getString("result");
                    token = item.getString("token");
                    Log.v("handleMessage", ": " + comment);
                    Log.v("handleMessage", ": " + token);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        };

        HttpUtil httptd = new HttpUtil();
        // httptd.submitAsyncHttpClientPostRegisterUser("yang", "456", handler);
        // httptd.submitAsyncHttpClientPostLogin("yang", "456", handler);
        // httptd.submitAsyncHttpClientPostCreateShop(token,"xinhua","BJ","Welcome", handler);
        // httptd.submitAsyncHttpClientPostAddBook(token, "xinhua", "Book1l", "luxun", "China", "Good book", handler);
        // httptd.submitAsyncHttpClientGetManageShop(token, "xinhua", handler);
    }

    private void delete() {
        Toast.makeText(getApplicationContext(), "Delete...", Toast.LENGTH_SHORT).show();
    }

    private void apply() {
        Toast.makeText(getApplicationContext(), "Apply...", Toast.LENGTH_SHORT).show();
    }
}
