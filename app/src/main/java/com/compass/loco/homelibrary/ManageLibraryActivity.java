package com.compass.loco.homelibrary;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by EXIAOQU on 7/25/2016.
 */
public class ManageLibraryActivity extends AppCompatActivity {
    static final String[] BOOKS = new String[] {
            "Apple", "Banana", "Blackberry"
    };

    TextView myTextViewLibraryName;
    Button myButtonAdd;
    Button myButtonDelete;
    Button myButtonApply;
    ListView myListViewBooks;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_library);

        myTextViewLibraryName = (TextView)findViewById(R.id.textViewLibraryName);

        myButtonAdd = (Button)findViewById(R.id.buttonAdd);
        myButtonDelete = (Button)findViewById(R.id.buttonDelete);
        myButtonApply = (Button)findViewById(R.id.buttonApply);

        myListViewBooks = (ListView)findViewById(R.id.listViewBooks);

        myTextViewLibraryName.setText("XXX的小店");

        myListViewBooks.setTextFilterEnabled(true);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.activity_manage_library_book_row, BOOKS);
        myListViewBooks.setAdapter(arrayAdapter);

        myListViewBooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // show a toast with the TextView test when clicked
                Toast.makeText(getApplicationContext(), ((TextView)view).getText(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}
