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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.compass.loco.homelibrary.adapter.SearchAdapter;
import com.compass.loco.homelibrary.adapter.SearchAearAdapter;
import com.compass.loco.homelibrary.model.Bean;
import com.compass.loco.homelibrary.model.ShopBean;
import com.compass.loco.homelibrary.widge.SearchView;
import com.compass.loco.homelibrary.http.HttpUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;




public class SearchMainActivity extends AppCompatActivity implements com.compass.loco.homelibrary.widge.SearchView.SearchViewListener {

        /**
         * 默认提示框显示项的个数
         */
        private static int DEFAULT_HINT_SIZE = 14;



        /**
         * 搜索结果列表view
         */
        private ListView lvResults;
        private ListView lvAearResults;

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
        private SearchAearAdapter resultAdapter2;

        private List<Bean> dbData;
        private List<ShopBean> dbAreaData;

        /**
         * 热搜版数据
         */
        private List<String> hintData;

        /**
         * 搜索过程中自动补全数据
         */
        private List<String> autoCompleteData;
        private List<String> LocalData;


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
            //setContentView(R.layout.activity_search_main);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            initData();
            initViews();
            initListViews();
    }

    private void initListViews()
    {
        lvResults = (ListView) findViewById(R.id.main_lv_search_results);
        lvResults.setTextFilterEnabled(true);
        lvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //Toast.makeText(getApplicationContext(), position + "", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),ManageBookActivity.class);
                intent.putExtra("user",dbData.get(position).getUserName());
                intent.putExtra("shopname",dbData.get(position).getShopName());
                intent.putExtra("bookname",dbData.get(position).getBookName());
                intent.putExtra("request","browse");
                startActivity(intent);
            }
        });

        lvAearResults = (ListView) findViewById(R.id.lv_search_area_results);
        lvAearResults.setTextFilterEnabled(true);
        lvAearResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //Toast.makeText(getApplicationContext(), position + "", Toast.LENGTH_SHORT).show();

                SharedPreferences sharedata = getSharedPreferences(GlobalParams.PREF_NAME, Context.MODE_PRIVATE);
                String username = sharedata.getString("username", null);

                if(username != null && username.equals(dbAreaData.get(position).getUserName()))
                {
                    Intent intent = new Intent(getApplicationContext(),ManageLibraryActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent(getApplicationContext(),BrowseLibraryActivity.class);
                    intent.putExtra("user",dbAreaData.get(position).getUserName());
                    intent.putExtra("shopname",dbAreaData.get(position).getShopName());
                    //intent.putExtra("request","browse");
                    startActivity(intent);
                }

            }
        });
    }
    /**
     * 初始化视图
     */
    private void initViews() {

         searchView = (SearchView) findViewById(R.id.main_search_layout);
        //设置监听
        searchView.setSearchViewListener(this);
        //设置adapter
        searchView.setTipsHintAdapter(hintAdapter);
        searchView.setAutoCompleteAdapter(autoCompleteAdapter);

    }

    /**
     * 初始化数据
     */
    private void initData() {

        if (dbData == null) {
            dbData = new ArrayList<>();
        }
        if (dbAreaData == null) {
            dbAreaData = new ArrayList<>();
        }
        //从数据库获取数据
        //getDbData();
        //初始化热搜版数据
        //
        // getHintData();
        //初始化自动补全数据
        getLocalData();
        getAutoCompleteData(null);
        //初始化搜索结果数据
        //getResultData(null);
    }

    /**
     * 获取db 数据
     */
    private void getDbDataByBookName(final String bookName) {

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

                            if(bookName.equals("*")) {
                                LocalData.add(jsonObj.getString("name"));
                            }
                            else
                            {
                                dbData.add(
                                        new Bean(
                                                jsonObj.getString("name"),
                                                (jsonObj.getBoolean("state")) ? "在库" : "借出",
                                                jsonObj.getString("author"),
                                                jsonObj.getString("publisher"),
                                                jsonObj.getString("shopname"),
                                                jsonObj.getString("shopaddr"),
                                                jsonObj.getString("username")
                                        ));
                            }
                        }

                        if(jsonArray.length() == 0)
                        {
                            Toast.makeText(getApplicationContext(), "完成搜素,暂时没有符合您需求的书", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            //Toast.makeText(getApplicationContext(), "完成搜素", Toast.LENGTH_SHORT).show();
                        }
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
                dbAreaData.clear();
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
                    resultAdapter2 = new SearchAearAdapter(getApplicationContext(), dbAreaData, R.layout.item_bean_list);
                    lvAearResults.setAdapter(resultAdapter2);
                    // resultAdapter.notifyDataSetChanged();
                    resultAdapter2.notifyDataSetChanged();

                    Toast.makeText(getApplicationContext(), "完成搜素,该小区暂时没有符合您需求的书店", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    try {

                        JSONArray jsonArray = jsonObj.getJSONArray("shops");

                        Log.v("number of shops: ", new Integer(jsonArray.length()).toString());
                        for (int i = 0; i < jsonArray.length(); ++i) {
                            jsonObj = jsonArray.getJSONObject(i);
                            Log.v("json object to string.", jsonObj.toString());
                            dbAreaData.add(
                                    new ShopBean(
                                            jsonObj.getString("name"),
                                            jsonObj.getString("addr"),
                                            jsonObj.getString("username"),
                                            jsonObj.getString("bookcnt")

                                    ));
                        }
                      /*  resultAdapter = new SearchAdapter(getApplicationContext(), dbData, R.layout.item_bean_list);
                        lvResults.setAdapter(resultAdapter);
                        // resultAdapter.notifyDataSetChanged();*/
                        if(jsonArray.length() == 0)
                        {
                            Toast.makeText(getApplicationContext(), "完成搜素,该小区暂时没有符合您需求的书店", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            //Toast.makeText(getApplicationContext(), "完成搜素", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), "unknown response from remote service!", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                    finally {
                        resultAdapter2 = new SearchAearAdapter(getApplicationContext(), dbAreaData, R.layout.item_shop_bean_list);
                        lvAearResults.setAdapter(resultAdapter2);
                        // resultAdapter.notifyDataSetChanged();
                        resultAdapter2.notifyDataSetChanged();
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


        hintData.add("望京花园");
        hintData.add("IC");
        hintData.add("菜鸟工程师完美进阶");
        hintData.add("利泽西园");

        hintAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, hintData);
        hintAdapter.notifyDataSetChanged();

    }
    /**
     * 获取自动补全data 和adapter
     */
    private void getLocalData() {

        LocalData = new ArrayList<>();
        getDbDataByBookName("*");
      /*  for (int i = 0; i < dbData.size(); i++) {
            LocalData.add(dbData.get(i).getBookName());
        }*/
        String [] villageList = getResources().getStringArray(R.array.village_array);
        for(int i = 0; i< villageList.length; i++)
        {
            LocalData.add(villageList[i]);
        }

            /*autoCompleteData.add("望京新城");
            autoCompleteData.add("首开知语");
            autoCompleteData.add("利泽西园");
            autoCompleteData.add("南湖东园");
            autoCompleteData.add("澳洲康都");
            autoCompleteData.add("望京花园");*/


    }
    /**
     * 获取自动补全data 和adapter
     */
    private void getAutoCompleteData(String text) {

        if (autoCompleteData == null) {
            //初始化
            autoCompleteData = new ArrayList<>(hintSize);
           /* autoCompleteData.add("利泽西园");
            autoCompleteData.add("\"胡\"说IC--菜鸟工程师完美进阶");*/
        } else {
            // 根据text 获取auto data
            autoCompleteData.clear();

            if(LocalData != null && ! LocalData.isEmpty())
            {
                for (int i = 0, count = 0; i < LocalData.size()
                        && count < hintSize; i++) {
                    String tempS = LocalData.get(i);
                    if (tempS.contains(text.trim().toLowerCase()) || tempS.contains(text.trim().toUpperCase())) {
                        if(!checkExist(autoCompleteData,tempS))
                        {
                            autoCompleteData.add(tempS);
                        }
                        count++;
                    }
                }
            }
        }
        if (autoCompleteAdapter == null) {
            autoCompleteAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, autoCompleteData);
        } else {
            autoCompleteAdapter.notifyDataSetChanged();
        }
    }

    private boolean checkExist(List<String> list, String text)
    {
        if(list == null || list.size() == 0)
            return false;
        for(int i = 0; i< list.size();i++)
        {
            if(list.get(i).equals(text))
                return  true;
        }
        return false;
    }

    /**
     * 获取搜索结果data和adapter
     */
    private void getResultData(String text, boolean searchStyleFlag) {
       /* if (resultData == null) {
            // 初始化
            resultData = new ArrayList<>();
        } else {
            resultData.clear();*/
            if(searchStyleFlag){
                if (dbData == null) {
                    dbData = new ArrayList<>();
                }
                getDbDataByBookName(text);
            } else {
                if (dbAreaData == null) {
                    dbAreaData = new ArrayList<>();
                }
                getDbDataByVillageName(text);
            }

            /*for (int i = 0; i < dbData.size(); i++) {
                if (dbData.get(i).getBookName().contains(text.trim())) {
                    resultData.add(dbData.get(i));
                    autoCompleteData.add(dbData.get(i).getBookName());
                }
            }*/
       // }
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
    public void onRefreshAutoComplete(String text,boolean searchStyleFlag) {
        //更新数据
        //if(!searchStyleFlag)
        {
            getAutoCompleteData(text);
        }
       /* if(searchStyleFlag)
        {
            hintData.clear();
            hintData.add("小");
            hintData.add("IC");
            hintData.add("\"胡\"说IC--菜鸟工程师完美进阶");
            hintData.add("小老鼠分果果");
            if(!checkExist(hintData,text))
            {
                hintData.add(0,text);
            }

        }
        else
        {
            hintData.clear();
            String [] villageList = getResources().getStringArray(R.array.village_array);
            for(int i = 0; i< villageList.length; i++)
            {
                hintData.add(villageList[i]);
            }

        }

        hintAdapter.notifyDataSetChanged();*/

    }

    /**
     * 点击搜索键时edit text触发的回调
     *
     * @param text
     */
    @Override
    public void onSearch(String text, boolean searchStyle) {
        //更新result数据
        if(text.equals(""))
            return;

        //hintAdapter.notifyDataSetChanged();
        lvResults.setVisibility(View.INVISIBLE);
        lvAearResults.setVisibility(View.INVISIBLE);

        getResultData(text,searchStyle);

        if(searchStyle)
        {
            lvAearResults.setVisibility(View.INVISIBLE);
            lvResults.setVisibility(View.VISIBLE);
        }
        else
        {
            lvResults.setVisibility(View.INVISIBLE);
            lvAearResults.setVisibility(View.VISIBLE);
        }


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
