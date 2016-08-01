package com.compass.loco.homelibrary;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.compass.loco.homelibrary.model.DoubanBook;

public class SaveBookActivity extends AppCompatActivity {
    public final static String INTENT_KEY_DOUBAN_BOOK = "com.compass.loco.vhomelibrary.INTENT_KEY_DOUBAN_BOOK";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_book);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey(INTENT_KEY_DOUBAN_BOOK)) {
            DoubanBook result = (DoubanBook) extras.getSerializable(INTENT_KEY_DOUBAN_BOOK);

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

            TextView linkView = (TextView) findViewById(R.id.douban_link);
            linkView.setText(Html.fromHtml("<a href=\"" + result.getAlt() + "\">豆瓣链接</a>"));
            linkView.setMovementMethod(LinkMovementMethod.getInstance());

            new ImageLoadTask(result.getImage(), (ImageView) findViewById(R.id.book_image_view)).execute();
        }
    }

}
