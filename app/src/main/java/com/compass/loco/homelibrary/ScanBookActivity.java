package com.compass.loco.homelibrary;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.compass.loco.homelibrary.model.DoubanBook;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class ScanBookActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_book);
    }


    public void onScanBtnClick(View view) {
        new IntentIntegrator(this).initiateScan();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            String re = scanResult.getContents();
            Log.d("code", re);

            new DownloadWebpageTask((TextView) findViewById(R.id.scan_content)).execute("https://api.douban.com/v2/book/isbn/:" + re);
        }
        // else continue with any other code you need in the method

    }

    private class DownloadWebpageTask extends AsyncTask<String, Void, DoubanBook> {
        TextView view;
        public DownloadWebpageTask(TextView view) {
            this.view = view;
        }
        @Override
        protected DoubanBook doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            InputStream in = null;
            try {
                URL url = new URL(urls[0]);
                URLConnection urlConnection = url.openConnection();
                in = urlConnection.getInputStream();

                Gson gson = new Gson();
                return gson.fromJson(new InputStreamReader(in, "UTF-8"), DoubanBook.class);
            } catch (IOException e) {
                Log.e("", "", e);
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        Log.e("", "", e);
                    }
                }
            }
            return null;
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(DoubanBook result) {
            view.setText(result.getTitle() + "\n" + result.getAuthor()[0] + "\n" + result.getPublisher() + "\n" + result.getAlt());
        }
    }
}