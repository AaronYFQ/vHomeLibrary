package com.compass.loco.homelibrary;

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

    private int removeBookCount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_library);

        init();

        getLibraryBooks();

    }

    private void init() {

        editTextLibraryName = (TextView) findViewById(R.id.editTextLibraryName);

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
            }
        });

        // get input parameter from application global data
        // 1. token
        // 2. shop name

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

                    ListViewAdapterManageBook myListViewAdapterManageBook = new ListViewAdapterManageBook(activity, arrayListSelectedBookInfo);
                    listViewBooks.setAdapter(myListViewAdapterManageBook);

                } catch (JSONException e) {

                    Toast.makeText(getApplicationContext(), "unknown response from remote service!", Toast.LENGTH_SHORT).show();

                    e.printStackTrace();
                }
            }
        };

        editTextLibraryName.setText("wangfujing");

        HttpUtil httptd = new HttpUtil();

        httptd.submitAsyncHttpClientGetManageShop(token, "wangfujing", handler);

    }


    private void add() {
        Toast.makeText(getApplicationContext(), "Add...", Toast.LENGTH_SHORT).show();
    }

    private void delete() {
        Toast.makeText(getApplicationContext(), "Delete...", Toast.LENGTH_SHORT).show();

        final Handler handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {

                if(--removeBookCount == 0) {
                    getLibraryBooks(); // refresh listview
                }
            }
        };

        HttpUtil httptd = new HttpUtil();

        // get shop name from gloabl data

        removeBookCount = 0;

        for(SelectedBookInfo selectedBookInfo : arrayListSelectedBookInfo) {

            if(selectedBookInfo.isSelected()) {

                // httptd.submitAsyncHttpClientPostRemoveBook(token, "wangfujing", selectedBookInfo.getBookInfo().getName(), handler);

                ++removeBookCount;
            }
        }
    }

    private void save() {

        Toast.makeText(getApplicationContext(), "Save...", Toast.LENGTH_SHORT).show();

        String libraryName = editTextLibraryName.getText().toString();
        if(libraryName.length() > 0) {

            final Handler handler = new Handler() {

                @Override
                public void handleMessage(Message msg) {

                    String jsonText = msg.getData().getString("responseBody");

                    Log.v("responseBody", jsonText);

                    try {

                        JSONObject jsonObj = new JSONObject(jsonText);

                        //  store new shop name into global data if succeed
                        // or Toast error message

                    } catch (JSONException e) {

                        Toast.makeText(getApplicationContext(), "unknown response from remote service!", Toast.LENGTH_SHORT).show();

                        e.printStackTrace();
                    }
                }
            };

            HttpUtil httptd = new HttpUtil();

            // get shop name from application global data

            // httptd.submitAsyncHttpClientPostModifyShopName(token, "wangfujing", libraryName, handler);

        }
        else {

            Toast.makeText(getApplicationContext(), "Please input the name...", Toast.LENGTH_SHORT).show();

        }

    }
}
