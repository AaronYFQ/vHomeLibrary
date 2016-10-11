package com.compass.loco.homelibrary;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class ManageLibraryActivity extends AppCompatActivity {
    public final static String INTENT_KEY_TOKEN = "token";
    public final static String INTENT_KEY_SHOPNAME = "shopname";

    private static final String TAG = "ManageLibraryActivity";

    // Android objects
    private TextView editTextLibraryName;

    private ImageButton buttonAdd;
    private ImageButton buttonDelete;
    private ImageButton buttonSave;

    private ListView listViewBooks;

    private CheckBox checkBoxAll;
    private CheckBox checkBoxBorrow;
    private CheckBox checkBoxLent;

    // selected or non-selected bookinfo arraylist
    private ArrayList<SelectedBookInfo> arrayListSelectedBookInfo;

    private String callerActivity;

    private int removeBookCount;

    private String token;
    private String user;
    private String shopName;
    private String newShopName;
    ManageLibraryActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_library);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Log.d(TAG, "onCreate() called");

        init();

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

        if(callerActivity == null || !callerActivity.equals("CreateLibActivity"))
        {
            getLibraryBooks();
        }

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
        activity = this;
        editTextLibraryName = (TextView) findViewById(R.id.editTextLibraryName);

        buttonAdd = (ImageButton) findViewById(R.id.buttonAdd);
        buttonDelete = (ImageButton) findViewById(R.id.buttonDelete);
        //buttonSave = (ImageButton) findViewById(R.id.buttonSave);

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
                //delete();
                deleteBookDiag();
            }
        });

        /*buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });*/

        listViewBooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {

                if(!BuildConfig.DEBUG) {

                    // show a toast with the TextView test when clicked
                    Toast.makeText(getApplicationContext(),
                            arrayListSelectedBookInfo.get(position).getBookInfo().getName() + "(line " + Integer.toString(position) + ") clicked!",
                            Toast.LENGTH_SHORT).show();

                }

                String bookName = arrayListSelectedBookInfo.get(position).getBookInfo().getName();
                String borrower = arrayListSelectedBookInfo.get(position).getBookInfo().getBorrower();

                Intent intent = new Intent(getApplicationContext(), ManageBookActivity.class);

                intent.putExtra("user", user);
                intent.putExtra("shopname", shopName);
                intent.putExtra("bookname",  bookName);
                intent.putExtra("borrower",  borrower);
                intent.putExtra("request",  "browse");
                startActivity(intent);

            }
        });


        // get application private shared preference
        SharedPreferences sharePref = getSharedPreferences(GlobalParams.PREF_NAME, Context.MODE_PRIVATE);

        // 1. token
        token = sharePref.getString("token", "");

        // 2. shop name
        shopName = sharePref.getString("shopname", "");

        Log.d(TAG, "from private shared preference: token = " + token + ", shopname = " + shopName);

        // set shop name
        if(shopName.length() > 0) {

            editTextLibraryName.setText(shopName);

        }

        callerActivity = getIntent().getStringExtra("callerActivity");

        checkBoxInit();
    }

    private void  checkBoxInit()
    {

        checkBoxAll = (CheckBox)this.findViewById(R.id.checkBoxAll);
        checkBoxBorrow = (CheckBox)this.findViewById(R.id.checkBoxBorrow);
        checkBoxLent = (CheckBox)this.findViewById(R.id.checkBoxLent);

        checkBoxAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if(isChecked){
                    checkBoxBorrow.setChecked(false);
                    checkBoxLent.setChecked(false);
                    checkBoxAll.setChecked(true) ;
                }else{
                    checkBoxAll.setChecked(false);
                }
                if(arrayListSelectedBookInfo == null)
                    return;

               /* ArrayList<SelectedBookInfo> tempList = new ArrayList<SelectedBookInfo>() ;
                for(SelectedBookInfo s : arrayListSelectedBookInfo )
                {
                    if(s.getBookInfo().getState())
                    {
                        tempList.add(s);
                    }
                }

                ListViewAdapterManageBook myListViewAdapterManageBook = new ListViewAdapterManageBook(activity,tempList);*/
                ListViewAdapterManageBook myListViewAdapterManageBook = new ListViewAdapterManageBook(activity,arrayListSelectedBookInfo);
                listViewBooks.setAdapter(myListViewAdapterManageBook);

            }
        });

        checkBoxBorrow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if(isChecked){
                    checkBoxBorrow.setChecked(true);
                    checkBoxLent.setChecked(false);
                    checkBoxAll.setChecked(false) ;
                }else{
                    checkBoxBorrow.setChecked(false);
                }
                if(arrayListSelectedBookInfo == null)
                    return;

                ArrayList<SelectedBookInfo> tempList = new ArrayList<SelectedBookInfo>() ;
                for(SelectedBookInfo s : arrayListSelectedBookInfo )
                {
                    if(s.getBookInfo().getState())
                    {
                        tempList.add(s);
                    }
                }

                ListViewAdapterManageBook myListViewAdapterManageBook = new ListViewAdapterManageBook(activity,tempList);
                listViewBooks.setAdapter(myListViewAdapterManageBook);

            }
        });

        checkBoxLent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if(isChecked){
                    checkBoxBorrow.setChecked(false);
                    checkBoxLent.setChecked(true);
                    checkBoxAll.setChecked(false) ;
                }else{
                    checkBoxLent.setChecked(false);
                }
                if(arrayListSelectedBookInfo == null)
                    return;

                ArrayList<SelectedBookInfo> tempList = new ArrayList<SelectedBookInfo>() ;
                for(SelectedBookInfo s : arrayListSelectedBookInfo )
                {
                    if( s.getBookInfo().getaVaiNum() !=  s.getBookInfo().getBookNum())
                    {
                        tempList.add(s);
                    }
                }

                ListViewAdapterManageBook myListViewAdapterManageBook = new ListViewAdapterManageBook(activity,tempList);
                listViewBooks.setAdapter(myListViewAdapterManageBook);

            }
        });
    }
    private void getLibraryBooks() {

        //final ManageLibraryActivity activity = this;

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

                        user = jsonObj.getString("user");

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
                                                    (jsonObj.getBoolean("state")),
                                                    jsonObj.getString("borrower"),
                                                    (jsonObj.getInt("bookNum")),
                                                    jsonObj.getInt("availNum")),
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

        if(!BuildConfig.DEBUG) {

            Toast.makeText(getApplicationContext(), "Add...", Toast.LENGTH_SHORT).show();

        }

        Intent intent = new Intent(this, ScanBookActivity.class);

        intent.putExtra(INTENT_KEY_TOKEN, token);
        intent.putExtra(INTENT_KEY_SHOPNAME, shopName);

        startActivity(intent);
    }

    private void delete(String bookName, int bookNum) {

        if(!BuildConfig.DEBUG) {

            Toast.makeText(getApplicationContext(), "Delete...", Toast.LENGTH_SHORT).show();

        }

        final Handler handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {

                String jsonText = msg.getData().getString("responseBody");

                Log.d(TAG, "jsonText = " + jsonText);

                try {

                    JSONObject jsonObj = new JSONObject(jsonText);

                    String result = jsonObj.getString("result");

                    if(!result.equals("success")) {

                        if(!BuildConfig.DEBUG) {

                            Toast.makeText(getApplicationContext(), "delete book failed! result=" + result, Toast.LENGTH_SHORT).show();

                        }

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
        httptd.submitAsyncHttpClientPostRemoveBook(token, shopName, bookName, bookNum, handler);

       
    }

    private void save() {

        if(!BuildConfig.DEBUG) {

            Toast.makeText(getApplicationContext(), "Save...", Toast.LENGTH_SHORT).show();

        }

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

    private void deleteBookDiag() {
        if(!BuildConfig.DEBUG) {

            Toast.makeText(getApplicationContext(), "Delete...", Toast.LENGTH_SHORT).show();

        }

        removeBookCount = 0;

        for(final SelectedBookInfo selectedBookInfo : arrayListSelectedBookInfo) {

            if(selectedBookInfo.isSelected()) {

                Log.d(TAG, "delete book = " + selectedBookInfo.getBookInfo().getName());
                String bookNum = "" + selectedBookInfo.getBookInfo().getBookNum();

                final EditText editText = new EditText(this);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("请输入要删去的书本数目：")
                        .setView(editText)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which ) {
                                Editable text = editText.getText();
                                int deleteNum = 0;

                                try{
                                    deleteNum =Integer.valueOf(text.toString());
                                    deleteNum = getDeleteBookNum(selectedBookInfo.getBookInfo(),deleteNum);
                                    Toast.makeText(getApplicationContext(), "" + deleteNum, Toast.LENGTH_SHORT).show();
                                }
                                catch (Exception e){
                                    deleteNum = -1;
                                    Toast.makeText(getApplicationContext(), "Get unknown delete book number!", Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }
                                if(deleteNum > 0)
                                {
                                    delete(selectedBookInfo.getBookInfo().getName(), deleteNum);
                                }
                            }
                        })
                        .show();
						
                ++removeBookCount;
            }
        }
    }

    private int getDeleteBookNum(BookInfo bookInfo, int deleteNum)
    {
        if( deleteNum <= 0)
        {
            Toast.makeText(getApplicationContext(), "delete book number is " +bookInfo.getaVaiNum() + " not correct" , Toast.LENGTH_SHORT).show();
            return  -1;
        }
        if(bookInfo.getaVaiNum() < deleteNum)
        {
            Toast.makeText(getApplicationContext(), "当前在库为" +bookInfo.getaVaiNum()  , Toast.LENGTH_SHORT).show();
            return  -1;
        }

        return deleteNum;
    }

}
