package com.compass.loco.homelibrary;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

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

    private Bundle mExtras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_book);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mExtras = getIntent().getExtras();
    }

    public void onScanBtnClick(View view) {
        new IntentIntegrator(this).initiateScan();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            String re = scanResult.getContents();
            Log.d("code", re);

            new DownloadWebpageTask().execute("https://api.douban.com/v2/book/isbn/:" + re);
        }
        // else continue with any other code you need in the method

    }

    private void showErrorMessage(final String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message).setTitle("Error");
        AlertDialog dialog = builder.create();
    }

    private class DownloadWebpageTask extends AsyncTask<String, Void, DoubanBook> {
        @Override
        protected DoubanBook doInBackground(final String... urls) {

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
            ScanBookActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(ScanBookActivity.this, "Get book info failed from [" + urls[0] + "]", Toast.LENGTH_SHORT).show();
                }
            });
            return null;
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(DoubanBook result) {
            if (result == null) {
                showErrorMessage("get book info failed.");
                return;
            }

            Intent intent = new Intent(ScanBookActivity.this, SaveBookActivity.class);
            intent.putExtra(SaveBookActivity.INTENT_KEY_DOUBAN_BOOK, result);
            intent.putExtra(ManageLibraryActivity.INTENT_KEY_TOKEN, mExtras.getString(ManageLibraryActivity.INTENT_KEY_TOKEN));
            intent.putExtra(ManageLibraryActivity.INTENT_KEY_SHOPNAME, mExtras.getString(ManageLibraryActivity.INTENT_KEY_SHOPNAME));
            startActivity(intent);
        }
    }
}
