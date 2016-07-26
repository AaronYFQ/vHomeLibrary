package com.compass.loco.homelibrary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class CreateLibraryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_create);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
/*
        TextView textView = new TextView(this);
        textView.setTextSize(40);
        textView.setText(message);

        RelativeLayout layout = (RelativeLayout) findViewById(R.id.content);
        layout.addView(textView);
*/
    }

    public void storeLibrary(View view)
    {
        //get library name @String
        EditText library = (EditText) findViewById(R.id.library_name);
        String libraryName = library.getText().toString();

        //get city name @String
        EditText city = (EditText) findViewById(R.id.city);
        String cityName = city.getText().toString();

        //get city name @String
        EditText community = (EditText) findViewById(R.id.community);
        String communityName = community.getText().toString();

        //get city name @String
        EditText advertisement = (EditText) findViewById(R.id.advertisement);
        String advertisementDesc = community.getText().toString();


        /*Intent displayLibraryIntent = new Intent(Intent.ACTION_VIEW, webpage);
        startActivity(displayLibraryIntent);
        */
    }


}
