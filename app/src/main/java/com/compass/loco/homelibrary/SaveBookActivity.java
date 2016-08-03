package com.compass.loco.homelibrary;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.compass.loco.homelibrary.model.DoubanBook;

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

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if ( extras.containsKey(INTENT_KEY_DOUBAN_BOOK)) {
                DoubanBook result = (DoubanBook) extras.getSerializable(INTENT_KEY_DOUBAN_BOOK);

                new ImageLoadTask(result.getImage(), (ImageView) findViewById(R.id.book_image_view)).execute();

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
        http.submitAsyncHttpClientPostAddBook(mToken, mShopName, mDoubanBook.getTitle(), mDoubanBook.getAuthor()[0], mDoubanBook.getPublisher(),
                mDoubanBook.getIsbn13(), comments.getText().toString(), mDoubanBook.getImage(), mDoubanBook.getAlt(),  new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        Intent intent = new Intent(SaveBookActivity.this, ManageLibraryActivity.class);
                        startActivity(intent);
                    }
                });
    }
}
