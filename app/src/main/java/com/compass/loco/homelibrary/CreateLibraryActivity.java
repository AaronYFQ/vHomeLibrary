package com.compass.loco.homelibrary;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class CreateLibraryActivity extends AppCompatActivity {
    private Spinner citySpinner;
    private Spinner districtSpinner;
    private ArrayAdapter<CharSequence> districtAdapter;
    private ArrayAdapter<CharSequence> cityAdapter;
    private String cityString = "";
    private String districtString = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_create);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        citySpinner = (Spinner) findViewById(R.id.citySpinner);
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

        districtSpinner = (Spinner) findViewById(R.id.districtSpinner);
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

    }

    public void storeLibrary(View view)
    {
        //get library name @String
     //   EditText library = (EditText) findViewById(R.id.library_name);
     //   String libraryName = library.getText().toString();
        String libraryName = "aaaaaa";
/*
        //get city name @String
        //EditText city = (EditText) findViewById(R.id.city);
        //String cityName = city.getText().toString();

        //get city name @String
        EditText community = (EditText) findViewById(R.id.community);
        String communityName = community.getText().toString();

        //get city name @String
        EditText advertisement = (EditText) findViewById(R.id.advertisement);
        String advertisementDesc = community.getText().toString();
        */
        //call httpClient API to store the data to server
        //@libraryName, @cityString, @districtString, @community，@advertisement
        //TODO
/*
        SharedPreferences.Editor sharedata = getSharedPreferences("data", 0).edit();
        sharedata.putString("libraryName",libraryName);
        sharedata.commit();
*/
        //start library management library.
        Intent manageLibraryIntent = new Intent(this, ManageLibraryActivity.class);
        startActivity(manageLibraryIntent);

    }

    public void cancelReturn(View view) {

        //return to main activity.
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
    }
}
