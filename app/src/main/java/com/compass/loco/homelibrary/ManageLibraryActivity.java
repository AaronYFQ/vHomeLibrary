package com.compass.loco.homelibrary;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by EXIAOQU on 7/25/2016.
 */
public class ManageLibraryActivity extends AppCompatActivity {

    TextView myEditTextLibraryName;

    ImageButton myButtonAdd;
    ImageButton myButtonDelete;
    ImageButton myButtonApply;

    ListView myListViewBooks;

    private ArrayList<HashMap<String, String>> myArrayList;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_library);

        init();

        threadGetLibraryBooks();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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

            temp = new HashMap<String, String>();
            temp.put("Name", "peach Book");
            temp.put("Author", "peachAuthor");
            temp.put("Publisher", "peachPublisher");
            temp.put("Isbn", "peachIsbn");
            temp.put("Detail", "peach Detail");

            myArrayList.add(temp);

            temp = new HashMap<String, String>();
            temp.put("Name", "pear Book");
            temp.put("Author", "pearAuthor");
            temp.put("Publisher", "pearPublisher");
            temp.put("Isbn", "pearIsbn");
            temp.put("Detail", "pear Detail");

            myArrayList.add(temp);

            temp = new HashMap<String, String>();
            temp.put("Name", "grape Book");
            temp.put("Author", "grapeAuthor");
            temp.put("Publisher", "grapePublisher");
            temp.put("Isbn", "grapeIsbn");
            temp.put("Detail", "grape Detail");

            myArrayList.add(temp);

            temp = new HashMap<String, String>();
            temp.put("Name", "pineapple Book");
            temp.put("Author", "pineappleAuthor");
            temp.put("Publisher", "pineapplePublisher");
            temp.put("Isbn", "pineappleIsbn");
            temp.put("Detail", "pineapple Detail");

            myArrayList.add(temp);

            temp = new HashMap<String, String>();
            temp.put("Name", "mango Book");
            temp.put("Author", "mangoAuthor");
            temp.put("Publisher", "mangoPublisher");
            temp.put("Isbn", "mangoIsbn");
            temp.put("Detail", "mango Detail");

            myArrayList.add(temp);

            ListViewAdapterBook myListViewAdapterBook = new ListViewAdapterBook(this, myArrayList);
            myListViewBooks.setAdapter(myListViewAdapterBook);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    String token = "yang";
    private void add() {
        Toast.makeText(getApplicationContext(), "Add...", Toast.LENGTH_SHORT).show();

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                String json = msg.getData().getString("responseBody");
                Log.v("handleMessage", json);
                try {
                    // handler item from Json
                    JSONObject item = new JSONObject(json);
                    String comment = item.getString("result");
                    token = item.getString("token");
                    Log.v("handleMessage", ": " + comment);
                    Log.v("handleMessage", ": " + token);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        };

        HttpUtil httptd = new HttpUtil();
       // httptd.submitAsyncHttpClientPostRegisterUser("yang", "456", handler);
       // httptd.submitAsyncHttpClientPostLogin("yang", "456", handler);
       // httptd.submitAsyncHttpClientPostCreateShop(token,"xinhua","BJ","Welcome", handler);
       // httptd.submitAsyncHttpClientPostAddBook(token, "xinhua", "Book1l", "luxun", "China", "Good book", handler);
       // httptd.submitAsyncHttpClientGetManageShop(token, "xinhua", handler);
        
    }

    private void delete() {
        Toast.makeText(getApplicationContext(), "Delete...", Toast.LENGTH_SHORT).show();
    }

    private void apply() {
        Toast.makeText(getApplicationContext(), "Apply...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "ManageLibrary Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.compass.loco.homelibrary/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "ManageLibrary Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.compass.loco.homelibrary/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
