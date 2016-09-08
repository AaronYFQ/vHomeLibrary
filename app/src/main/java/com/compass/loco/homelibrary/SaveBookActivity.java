package com.compass.loco.homelibrary;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.compass.loco.homelibrary.model.DoubanBook;
import com.compass.loco.homelibrary.widge.CacheBookImages;

import java.io.File;

public class SaveBookActivity extends AppCompatActivity {
    public final static String INTENT_KEY_DOUBAN_BOOK = "com.compass.loco.vhomelibrary.INTENT_KEY_DOUBAN_BOOK";

    private DoubanBook mDoubanBook;
    private String mToken;
    private String mShopName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_book);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if ( extras.containsKey(INTENT_KEY_DOUBAN_BOOK)) {
                DoubanBook result = (DoubanBook) extras.getSerializable(INTENT_KEY_DOUBAN_BOOK);

                loadImg(result.getIsbn13(),result.getImage());

                TextView titleView = (TextView) findViewById(R.id.book_title);
                titleView.setText(result.getTitle());

                TextView authorView = (TextView) findViewById(R.id.book_author);
                StringBuilder sb = new StringBuilder();
                for (String a : result.getAuthor()) {
                    if (sb.length() > 0) {
                        sb.append(",");
                    }
                    sb.append(a);
                }
                authorView.setText(sb.toString());

                TextView publisherView = (TextView) findViewById(R.id.book_publisher);
                publisherView.setText(result.getPublisher());

                TextView isbnView = (TextView) findViewById(R.id.book_isbn);
                isbnView.setText(result.getIsbn13());

                TextView summaryView = (TextView) findViewById(R.id.book_summary);
                summaryView.setText(result.getSummary());

                mDoubanBook = result;
            }
            if (extras.containsKey(ManageLibraryActivity.INTENT_KEY_TOKEN)) {
                mToken = extras.getString(ManageLibraryActivity.INTENT_KEY_TOKEN);
            }
            if (extras.containsKey(ManageLibraryActivity.INTENT_KEY_SHOPNAME)) {
                mShopName = extras.getString(ManageLibraryActivity.INTENT_KEY_SHOPNAME);
            }
        }
    }

    public void loadImg(String isbn, String imageUrl)  {

        String filePath = Environment.getExternalStorageDirectory().toString() + getResources().getString(R.string.cache_path);
        String cacheImg = filePath + "/"+ isbn;
        ImageView image1 = (ImageView) findViewById(R.id.book_image_view);

        File f = new File(cacheImg);
        if (f.exists()) {
           /* Bitmap bitmap = BitmapFactory.decodeFile(cacheImg);
            image1.setImageBitmap(bitmap);*/
            Glide.with(this)
                    .load(cacheImg)
                   // .skipMemoryCache(true) //不使用内存缓存
                    .diskCacheStrategy(DiskCacheStrategy.NONE)//不使用硬盘缓存;
                    .into(image1);
        }
        else
        {
            Glide.with(this)
                    .load(imageUrl)
                    //.skipMemoryCache(true) //不使用内存缓存
                    .diskCacheStrategy(DiskCacheStrategy.NONE)//不使用硬盘缓存;
                    .into(image1);

            //cache the img
            new CacheBookImages(imageUrl,isbn ).execute();
            //new ImageLoadTask(image, (ImageView) findViewById(R.id.book_image_view)).execute();
        }
    }

    public void onDoubanLinkClick(View view) {
        if (mDoubanBook != null) {
            openWebPage(mDoubanBook.getAlt());
        }
    }

    public void openWebPage(String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void onSaveBookClick(View view) {
        EditText comments = (EditText) view.findViewById(R.id.book_comments);
        HttpUtil http = new HttpUtil();
        StringBuilder authors = new StringBuilder();
        if (mDoubanBook.getAuthor() != null) {
            for (String author : mDoubanBook.getAuthor()) {
                if (authors.length() > 0) {
                    authors.append(",");
                }
                authors.append(author);
            }
        }
        http.submitAsyncHttpClientPostAddBook(mToken, mShopName, mDoubanBook.getTitle(), authors.toString(), mDoubanBook.getPublisher(),
                mDoubanBook.getIsbn13(), mDoubanBook.getSummary(), mDoubanBook.getImage(), mDoubanBook.getAlt(),  new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        String jsonText = msg.getData().getString("responseBody");
                        Log.d("save book", "jsonText = " + jsonText);
                        Intent intent = new Intent(SaveBookActivity.this, ManageLibraryActivity.class);
                        startActivity(intent);
                    }
                });
    }
}
