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
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.TextureMapView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class CreateLibActivity extends AppCompatActivity {
    private Spinner citySpinner;
    private Spinner districtSpinner;
    private AutoCompleteTextView villageTextView;
    private EditText adEditText;
    private EditText libraryEditText;
    private TextureMapView mTextureMapView;


    private ArrayAdapter<CharSequence> districtAdapter;
    private ArrayAdapter<CharSequence> cityAdapter;
    private ArrayAdapter<CharSequence> villageAdapter;

    private String cityString = "";
    private String districtString = "";
    private String villageString = "";
    private String advertisement = "";
    private String library = "";

    final CreateLibActivity curActivity = this;
    ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //BMAP
        SDKInitializer.initialize(getApplicationContext());

        setContentView(R.layout.activity_create_lib);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //get bmap control
        //mTextureMapView = (TextureMapView) findViewById(R.id.bTextureMapView);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        citySpinner = (Spinner) findViewById(R.id.city_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        cityAdapter = ArrayAdapter.createFromResource(this,
                R.array.city_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        citySpinner.setAdapter(cityAdapter);
        citySpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub
                cityString = ""+ cityAdapter.getItem(arg2);
                arg0.setVisibility(View.VISIBLE);
            }
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                cityString = "";
                arg0.setVisibility(View.VISIBLE);
            }
        });
        //andle the menu screen touch event
        citySpinner.setOnTouchListener(new Spinner.OnTouchListener(){
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                return false;
            }
        });

        districtSpinner = (Spinner) findViewById(R.id.district_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        districtAdapter = ArrayAdapter.createFromResource(this,
                R.array.district_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        districtSpinner.setAdapter(districtAdapter);
        //handle the menu screen touch event
        districtSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub
                districtString = "" + districtAdapter.getItem(arg2);
                arg0.setVisibility(View.VISIBLE);
            }
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                districtString = "";
                arg0.setVisibility(View.VISIBLE);
            }
        });
        //handle the menu screen touch event
        districtSpinner.setOnTouchListener(new Spinner.OnTouchListener(){
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub

                return false;
            }
        });


        villageTextView = (AutoCompleteTextView) findViewById(R.id.village_text_view);
        villageAdapter = ArrayAdapter.createFromResource(this,
                R.array.village_array, android.R.layout.simple_dropdown_item_1line);

        villageTextView.setAdapter(villageAdapter);
        villageTextView.setThreshold(1);
        villageTextView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
               // ListView listview = (ListView) parent;
               // ArrayAdapter<String> adapter  =  (ArrayAdapter<String>) parent.getAdapter();
                TextView textview = (TextView) view;
                villageString = textview.getText().toString();

            }
        });

        adEditText = (EditText) findViewById(R.id.ad_edit_text);
        libraryEditText = (EditText) findViewById(R.id.library_name);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //mTextureMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //mTextureMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //mTextureMapView.onPause();
    }

    private void addItems() {
        HashMap<String,String> item;

        item = new HashMap<String,String>();
        item.put( "villageSearchText", "LIZEXIYUAN lizexiyuan 利泽西园");
        item.put( "villageName", "利泽西园");
        list.add( item );


        item = new HashMap<String,String>();
        item.put( "villageSearchText", "WANGJINGHUAYUANDONGQU wangjinghuayuandongqu 望京花园东区");
        item.put( "villageName", "望京花园东区");
        list.add( item );


        item = new HashMap<String,String>();
        item.put( "villageSearchText", "WANGJINGHUAYUANXIQU wangjinghuayuanxiqu 望京花园西区");
        item.put( "villageName", "望京花园西区");
        list.add( item );

    }

    public void createLibrary(View view)
    {
        //get library name @String
        advertisement = adEditText.getText().toString();
        library = libraryEditText.getText().toString();

        if(library.equals(""))
        {
            libraryEditText.requestFocus();
            return;
        }
        if(villageString.equals(""))
        {
            villageTextView.requestFocus();
            return;
        }

        final Handler handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {



                String jsonText = msg.getData().getString("responseBody");

                Log.v("responseBody", jsonText);

                try {

                    JSONObject jsonObj = new JSONObject(jsonText);

                    String result = jsonObj.getString("result");

                    if(!result.equals("success"))
                    {
                        Toast.makeText(getApplicationContext(), "create shop failed! result=" + result, Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        SharedPreferences sharedPref = getSharedPreferences(GlobalParams.PREF_NAME, Context.MODE_PRIVATE);
                        SharedPreferences.Editor sharedata = sharedPref.edit();
                        sharedata.putString("shopname", library);
                        sharedata.putString(GlobalParams.SHARED_KEY_ADVERTISEMENT, advertisement);
                        sharedata.commit();

                        Intent manageLibraryIntent = new Intent(curActivity, ManageLibraryActivity.class);
                        manageLibraryIntent.putExtra("callerActivity", "CreateLibActivity");
                        startActivity(manageLibraryIntent);
                    }

                } catch (JSONException e) {

                    Toast.makeText(getApplicationContext(), "unknown response remote service!", Toast.LENGTH_SHORT).show();

                    e.printStackTrace();
                }
            }
        };

        SharedPreferences sharedata = getSharedPreferences(GlobalParams.PREF_NAME, Context.MODE_PRIVATE);
        String token = sharedata.getString("token", null);
        String shopaddr = cityString + " " + districtString + " " + villageString;

        HttpUtil httptd = new HttpUtil();
        httptd.submitAsyncHttpClientPostCreateShop(token, library,
                shopaddr, advertisement, handler);

    }

}
