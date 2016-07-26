package com.compass.loco.homelibrary;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by EXIAOQU on 7/25/2016.
 */
public class ManageLibraryActivity extends AppCompatActivity {

    TextView myTextViewLibraryName;

    Button myButtonAdd;
    Button myButtonDelete;
    Button myButtonApply;

    ListView myListViewBooks;

    private ArrayList<HashMap<String, String>> myArrayList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_library);

        init();

        threadGetLibraryBooks();
        
    }

    private void init() {

        myTextViewLibraryName = (TextView)findViewById(R.id.textViewLibraryName);

        myButtonAdd = (Button)findViewById(R.id.buttonAdd);
        myButtonDelete = (Button)findViewById(R.id.buttonDelete);
        myButtonApply = (Button)findViewById(R.id.buttonApply);

        myListViewBooks = (ListView)findViewById(R.id.listViewBooks);
        myListViewBooks.setTextFilterEnabled(true);

        myButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add();
            }
        });

        myButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });

        myButtonApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apply();
            }
        });

        myListViewBooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                // show a toast with the TextView test when clicked
                Toast.makeText(getApplicationContext(), Integer.toString(position + 1) + " clicked!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void threadGetLibraryBooks() {

        new Thread( new Runnable() {

            @Override
            public void run() {

                getLibraryBooks();

            }

        }).start();

    }


    private void getLibraryBooks() {

        try {
            /*
            URL uri =  new URL("http://ec2-54-67-32-254.us-west-1.compute.amazonaws.com/1");
            URLConnection ucon = uri.openConnection();

            InputStream is = ucon.getInputStream();

            BufferedInputStream bis = new BufferedInputStream(is);

            StringBuffer sb = new StringBuffer();

            byte[] bytes = new byte[200];
            int count;
            while ((count = bis.read(bytes)) != -1) {
                sb.append(new String(bytes, 0, count));
            }

            myTextViewLibraryName.setText(sb.toString());
            */

            myTextViewLibraryName.setText("Unknown Library");

            myArrayList = new ArrayList<HashMap<String, String>>();

            HashMap<String, String> temp;

            temp = new HashMap<String, String>();
            temp.put("Name", "Apple Book");
            temp.put("Author", "AppleAuthor");
            temp.put("Publisher", "ApplePublisher");
            temp.put("Isbn", "AppleIsbn");
            temp.put("Detail", "Apple Detail");

            myArrayList.add(temp);

            temp = new HashMap<String, String>();
            temp.put("Name", "Banana Book");
            temp.put("Author", "BananaAuthor");
            temp.put("Publisher", "BananaPublisher");
            temp.put("Isbn", "BananaIsbn");
            temp.put("Detail", "Banana Detail");

            myArrayList.add(temp);

            temp = new HashMap<String, String>();
            temp.put("Name", "Blackberry Book");
            temp.put("Author", "BlackberryAuthor");
            temp.put("Publisher", "BlackberryPublisher");
            temp.put("Isbn", "BlackberryIsbn");
            temp.put("Detail", "Blackberry Detail");

            myArrayList.add(temp);

            ListViewAdapterBook myListViewAdapterBook = new ListViewAdapterBook(this, myArrayList);
            myListViewBooks.setAdapter(myListViewAdapterBook);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void add() {
        Toast.makeText(getApplicationContext(), "Add...", Toast.LENGTH_SHORT).show();
    }

    private void delete() {
        Toast.makeText(getApplicationContext(), "Delete...", Toast.LENGTH_SHORT).show();
    }

    private void apply() {
        Toast.makeText(getApplicationContext(), "Apply...", Toast.LENGTH_SHORT).show();
    }
}