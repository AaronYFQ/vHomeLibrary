package com.compass.loco.homelibrary;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SelectCityActivity extends AppCompatActivity {

    private ListView cityListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_city);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cityListView = (ListView) findViewById(R.id.all_city_list);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.city_array, R.layout.select_city_list_item);
        cityListView.setAdapter(adapter);

        cityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String cityName = (String)cityListView.getItemAtPosition(position);
                Intent intent = new Intent(SelectCityActivity.this, MainActivity.class);
                intent.putExtra(MainActivity.INTENT_KEY_CITY_NAME,cityName);
                startActivity(intent);
            }
        });
    }
}
