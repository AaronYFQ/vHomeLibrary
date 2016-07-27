package com.compass.loco.homelibrary;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * Created by EXIAOQU on 7/25/2016.
 */
public class ManageLibraryActivity extends AppCompatActivity {

    // Android objects
    TextView myEditTextLibraryName;

    ImageButton myButtonAdd;
    ImageButton myButtonDelete;
    ImageButton myButtonApply;

    ListView myListViewBooks;

    // private ArrayList<HashMap<String, String>> myArrayList;

    // selected or non-selected bookinfo arraylist
    private ArrayList<SelectedBookInfo> myArrayListSelectedBookInfo;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_library);

        init();

        threadGetLibraryBooks();
    }

    private void init() {

        myEditTextLibraryName = (TextView) findViewById(R.id.editTextLibraryName);

        myButtonAdd = (ImageButton) findViewById(R.id.buttonAdd);
        myButtonDelete = (ImageButton) findViewById(R.id.buttonDelete);
        myButtonApply = (ImageButton) findViewById(R.id.buttonApply);

        myListViewBooks = (ListView) findViewById(R.id.listViewBooks);
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

        new Thread(new Runnable() {

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

            myEditTextLibraryName.setText("exiaoqu的水果书店");

            myArrayListSelectedBookInfo = new ArrayList<SelectedBookInfo>();

            myArrayListSelectedBookInfo.add(new SelectedBookInfo(new BookInfo("Apple Book", "AppleAuthor", "ApplePublisher", "AppleIsbn", "Apple Detail", null), false));
            myArrayListSelectedBookInfo.add(new SelectedBookInfo(new BookInfo("Banana Book", "BananaAuthor", "BananaPublisher", "BananaIsbn", "Banana Detail", null), false));

            myArrayListSelectedBookInfo.add(new SelectedBookInfo(new BookInfo("Blackberry Book", "BlackberryAuthor", "BlackberryPublisher", "BlackberryIsbn", "Blackberry Detail", null), false));
            myArrayListSelectedBookInfo.add(new SelectedBookInfo(new BookInfo("Peach Book", "PeachAuthor", "PeachPublisher", "PeachIsbn", "Peach Detail", null), false));
            myArrayListSelectedBookInfo.add(new SelectedBookInfo(new BookInfo("Pear Book", "PearAuthor", "PearPublisher", "PearIsbn", "Pear Detail", null), false));

            myArrayListSelectedBookInfo.add(new SelectedBookInfo(new BookInfo("Grape Book", "GrapeAuthor", "GrapePublisher", "GrapeIsbn", "Grape Detail", null), false));
            myArrayListSelectedBookInfo.add(new SelectedBookInfo(new BookInfo("Pineapple Book", "PineappleAuthor", "PineapplePublisher", "Pineapple", "Pineapple Detail", null), false));
            myArrayListSelectedBookInfo.add(new SelectedBookInfo(new BookInfo("Mango Book", "MangoAuthor", "MangoPublisher", "MangoIsbn", "Mango Detail", null), false));

            ListViewAdapterBook myListViewAdapterBook = new ListViewAdapterBook(this, myArrayListSelectedBookInfo);
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
