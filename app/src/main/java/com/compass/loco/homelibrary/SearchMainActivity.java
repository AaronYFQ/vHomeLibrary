package com.compass.loco.homelibrary;

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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.compass.loco.homelibrary.adapter.SearchAdapter;
import com.compass.loco.homelibrary.model.Bean;
import com.compass.loco.homelibrary.widge.SearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;




public class SearchMainActivity extends AppCompatActivity implements com.compass.loco.homelibrary.widge.SearchView.SearchViewListener {

        /**
         * 默认提示框显示项的个数
         */
        private static int DEFAULT_HINT_SIZE = 4;



        /**
         * 搜索结果列表view
         */
        private ListView lvResults;

        /**
         * 搜索view
         */
        private SearchView searchView;


        /**
         * 热搜框列表adapter
         */
        private ArrayAdapter<String> hintAdapter;

        /**
         * 自动补全列表adapter
         */
        private ArrayAdapter<String> autoCompleteAdapter;

        /**
         * 搜索结果列表adapter
         */
        private SearchAdapter resultAdapter;

        private List<Bean> dbData;

        /**
         * 热搜版数据
         */
        private List<String> hintData;

        /**
         * 搜索过程中自动补全数据
         */
        private List<String> autoCompleteData;

        /**
         * 搜索结果的数据
         */
        private List<Bean> resultData;

        /**
         * 设置提示框显示项的个数     *
         *
         * @param hintSize 提示框显示个数
         */
        private static int hintSize = DEFAULT_HINT_SIZE;

        public static void setHintSize(int hintSize) {
            hintSize = hintSize;
        }

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setContentView(R.layout.activity_search_main);
        initData();
        initViews();

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    /**
     * 初始化视图
     */
    private void initViews() {
        lvResults = (ListView) findViewById(R.id.main_lv_search_results);
        searchView = (SearchView) findViewById(R.id.main_search_layout);
        //设置监听

        searchView.setSearchViewListener(this);
        //设置adapter
        searchView.setTipsHintAdapter(hintAdapter);
        searchView.setAutoCompleteAdapter(autoCompleteAdapter);
        // searchView.
        lvResults.setTextFilterEnabled(true);
        lvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Toast.makeText(getApplicationContext(), position + "", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        //从数据库获取数据
        //getDbData();
        //初始化热搜版数据
        getHintData();
        //初始化自动补全数据
        getAutoCompleteData(null);
        //初始化搜索结果数据
        //getResultData(null);
    }

    /**
     * 获取db 数据
     */
    private void getDbDataByBookName(String bookName) {
       /* int size = 100;
        dbData = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            //dbData.add(new Bean(R.drawable.icon, "android开发必备技能" + (i + 1),"shopName","bookAddr"));
            //dbData.add(new Bean(R.drawable.icon, "android开发必备技能" + (i + 1), "Android自定义view——自定义搜索view", i * 20 + 2 + ""));
        }

        for (int i = 0; i < 20; i++) {
            dbData.add(new Bean(*//*R.drawable.icon,*//* "bookName" + (i + 1),"已借出","bookAuthor","bookPubliser","shopName","bookAddr_bookAddrbookAddrbookAddrbookAddrbookAddrbookAddr"));
            dbData.add(new Bean(*//*R.drawable.icon,*//* "bookName" + (i + 2),"未借出","bookAuthor","bookPubliser","shopName","bookAddr_bookAddrbookAddrbookAddrbookAddrbookAddrbookAddr"));
        }*/

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String jsonText = msg.getData().getString("responseBody");
                Log.v("responseBody", jsonText);
                dbData.clear();

                String dbResult = "";
                JSONObject jsonObj = null;


                try {
                    jsonObj = new JSONObject(jsonText);
                    dbResult = jsonObj.getString("result");
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "unknown exception in result  from remote service!", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

                if(dbResult.contains("null"))
                //if(jsonText.contains("\"result\": null"))
                {
                    resultAdapter = new SearchAdapter(getApplicationContext(), dbData, R.layout.item_bean_list);
                    lvResults.setAdapter(resultAdapter);
                    // resultAdapter.notifyDataSetChanged();
                    resultAdapter.notifyDataSetChanged();

                    Toast.makeText(getApplicationContext(), "完成搜素,暂时没有符合您需求的书", Toast.LENGTH_SHORT).show();

                }
                else if(dbResult.contains("success"))
                {
                    try {
                        //JSONObject jsonObj = new JSONObject(jsonText);
                        JSONArray jsonArray = jsonObj.getJSONArray("books");

                        Log.v("number of books: ", new Integer(jsonArray.length()).toString());
                        for (int i = 0; i < jsonArray.length(); ++i) {
                            jsonObj = jsonArray.getJSONObject(i);
                            Log.v("json object to string.", jsonObj.toString());
                            dbData.add(
                                    new Bean(
                                            jsonObj.getString("name"),
                                            (jsonObj.getBoolean("state")) ? "可借阅" : "已借出",
                                            jsonObj.getString("author"),
                                            jsonObj.getString("publisher"),
                                            jsonObj.getString("shopname"),
                                            jsonObj.getString("shopaddr")
                                    ));
                        }
                       /* resultAdapter = new SearchAdapter(getApplicationContext(), dbData, R.layout.item_bean_list);
                        lvResults.setAdapter(resultAdapter);
                        // resultAdapter.notifyDataSetChanged();
                        resultAdapter.notifyDataSetChanged();
*/
                        Toast.makeText(getApplicationContext(), "完成搜素", Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), "unknown response from remote service!", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                    finally {
                        resultAdapter = new SearchAdapter(getApplicationContext(), dbData, R.layout.item_bean_list);
                        lvResults.setAdapter(resultAdapter);
                        // resultAdapter.notifyDataSetChanged();
                        resultAdapter.notifyDataSetChanged();
                    }
                }


            }
        };
        HttpUtil httptd = new HttpUtil();
        httptd.submitAsyncHttpClientGetSearchBook(bookName, handler);
    }

    private void getDbDataByVillageName(String villageName) {

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String jsonText = msg.getData().getString("responseBody");
                Log.v("responseBody", jsonText);
                dbData.clear();
                String dbResult = "";
                JSONObject jsonObj = null;


                try {
                    jsonObj = new JSONObject(jsonText);
                    dbResult = jsonObj.getString("result");
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "unknown exception in result  from remote service!", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

                if(dbResult.contains("null"))
                {
                    resultAdapter = new SearchAdapter(getApplicationContext(), dbData, R.layout.item_bean_list);
                    lvResults.setAdapter(resultAdapter);
                    // resultAdapter.notifyDataSetChanged();
                    resultAdapter.notifyDataSetChanged();

                    Toast.makeText(getApplicationContext(), "完成搜素,暂时没有符合您需求的书", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    try {

                        JSONArray jsonArray = jsonObj.getJSONArray("books");

                        Log.v("number of books: ", new Integer(jsonArray.length()).toString());
                        for (int i = 0; i < jsonArray.length(); ++i) {
                            jsonObj = jsonArray.getJSONObject(i);
                            Log.v("json object to string.", jsonObj.toString());
                            dbData.add(
                                    new Bean(
                                            jsonObj.getString("name"),
                                            (jsonObj.getBoolean("state")) ? "可借阅" : "已借出",
                                            jsonObj.getString("author"),
                                            jsonObj.getString("publisher"),
                                            jsonObj.getString("shopname"),
                                            jsonObj.getString("shopaddr")
                                    ));
                        }
                      /*  resultAdapter = new SearchAdapter(getApplicationContext(), dbData, R.layout.item_bean_list);
                        lvResults.setAdapter(resultAdapter);
                        // resultAdapter.notifyDataSetChanged();*/
                        Toast.makeText(getApplicationContext(), "完成搜素", Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), "unknown response from remote service!", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                    finally {
                        resultAdapter = new SearchAdapter(getApplicationContext(), dbData, R.layout.item_bean_list);
                        lvResults.setAdapter(resultAdapter);
                        // resultAdapter.notifyDataSetChanged();
                        resultAdapter.notifyDataSetChanged();
                    }
                }


            }
        };
        HttpUtil httptd = new HttpUtil();
        httptd.submitAsyncHttpClientGetSearchArea(villageName, handler);
    }

    /**
     * 获取热搜版data 和adapter
     */
    private void getHintData() {
        hintData = new ArrayList<>(hintSize);

        hintData.add("apple2");
        hintData.add("apple3");
        hintData.add("apple4");
        hintData.add("bookName");

        hintAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, hintData);
    }

    /**
     * 获取自动补全data 和adapter
     */
    private void getAutoCompleteData(String text) {

        if (autoCompleteData == null) {
            //初始化
            autoCompleteData = new ArrayList<>(hintSize);
        } else {
            // 根据text 获取auto data
            //autoCompleteData.clear();
            //autoCompleteData.add(text);

            /*if(dbData != null && ! dbData.isEmpty())
            {
                for (int i = 0, count = 0; i < dbData.size()
                        && count < hintSize; i++) {
                    if (dbData.get(i).getBookName().contains(text.trim())) {
                        autoCompleteData.add(dbData.get(i).getBookName());
                        count++;
                    }
                }
            }*/

        }
        if (autoCompleteAdapter == null) {
            autoCompleteAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, autoCompleteData);
        } else {
            autoCompleteAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 获取搜索结果data和adapter
     */
    private void getResultData(String text, boolean searchStyleFlag) {
        if (resultData == null) {
            // 初始化
            resultData = new ArrayList<>();
        } else {
            resultData.clear();
            if (dbData == null) {
                dbData = new ArrayList<>();
            }
            if(searchStyleFlag){
                getDbDataByBookName(text);
            } else {
                getDbDataByVillageName(text);
            }

            for (int i = 0; i < dbData.size(); i++) {
                if (dbData.get(i).getBookName().contains(text.trim())) {
                    resultData.add(dbData.get(i));
                    autoCompleteData.add(dbData.get(i).getBookName());
                }
            }
        }
       /* if (resultAdapter == null) {
            resultAdapter = new SearchAdapter(this, resultData, R.layout.item_bean_list);
        } else {
            resultAdapter.notifyDataSetChanged();
        }*/
    }

    /**
     * 当搜索框 文本改变时 触发的回调 ,更新自动补全数据
     *
     * @param text
     */
    @Override
    public void onRefreshAutoComplete(String text) {
        //更新数据
        getAutoCompleteData(text);
    }

    /**
     * 点击搜索键时edit text触发的回调
     *
     * @param text
     */
    @Override
    public void onSearch(String text, boolean searchStyle) {
        //更新result数据

        /*if(dbData != null){
            dbData.clear();
        }
        resultAdapter = new SearchAdapter(getApplicationContext(), dbData, R.layout.item_bean_list);
        lvResults.setAdapter(resultAdapter);*/
        lvResults.setVisibility(View.INVISIBLE);
        getResultData(text,searchStyle);
        lvResults.setVisibility(View.VISIBLE);

       /* //第一次获取结果 还未配置适配器
        if (lvResults.getAdapter() == null) {
            //获取搜索数据 设置适配器
            lvResults.setAdapter(resultAdapter);
        } else {
            //更新搜索数据
            resultAdapter.notifyDataSetChanged();
        }*/
        //Toast.makeText(this, "完成搜素", Toast.LENGTH_SHORT).show();


    }

}