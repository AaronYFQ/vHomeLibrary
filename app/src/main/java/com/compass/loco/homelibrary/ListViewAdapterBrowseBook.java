package com.compass.loco.homelibrary;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.compass.loco.homelibrary.widge.CacheBookImages;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by EXIAOQU on 7/29/2016.
 */
public class ListViewAdapterBrowseBook extends BaseAdapter {

    // Andorid objects
    private Activity activity;

    private TextView textViewBookName;
    private TextView textViewBookAuthor;
    private TextView textViewBookPublisher;
    private TextView textViewBookIsbn;
    private TextView textViewBookDetail;
    private TextView textViewBookState;

    private ImageView imageViewBookPicture;

    // selected or non-selected bookinfo arraylist
    public ArrayList<BookInfo> arrayListBookInfo;

    public ListViewAdapterBrowseBook(Activity activity, ArrayList<BookInfo> list) {
        super();
        this.activity = activity;
        this.arrayListBookInfo = list;
    }

    @Override
    public int getCount() {
        return arrayListBookInfo.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayListBookInfo.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null)
        {
            Log.v("convertView null, position=", new Integer(position).toString());

            LayoutInflater inflater = activity.getLayoutInflater();
            convertView = inflater.inflate(R.layout.activity_browse_library_book_row, null);
        }
        else {
            Log.v("convertView not null, position=", new Integer(position).toString());
        }

        textViewBookName = (TextView)convertView.findViewById(R.id.textViewBookName);
        textViewBookAuthor = (TextView)convertView.findViewById(R.id.textViewBookAuthor);
        textViewBookPublisher = (TextView)convertView.findViewById(R.id.textViewBookPublisher);
        textViewBookIsbn = (TextView)convertView.findViewById(R.id.textViewBookIsbn);
        textViewBookDetail = (TextView)convertView.findViewById(R.id.textViewBookDetail);
        textViewBookState = (TextView)convertView.findViewById(R.id.textViewBookState);
        imageViewBookPicture = (ImageView)convertView.findViewById(R.id.imageViewBookPicture);


        BookInfo bookInfo =  arrayListBookInfo.get(position);
        
        textViewBookName.setText(bookInfo.getName());
        textViewBookName.setTag(bookInfo);
        
        textViewBookAuthor.setText("作者：" + bookInfo.getAuthor());
        textViewBookPublisher.setText("出版社：" +bookInfo.getPublisher());
        textViewBookIsbn.setText("ISBN号：" + bookInfo.getIsbn());
        textViewBookDetail.setText("简介：" + bookInfo.getDetail());

        if(bookInfo.getState()) {

            textViewBookState.setText("在库");

        }
        else
        {

            textViewBookState.setText("借出");

        }

        String imageUrl = bookInfo.getImageUrl();
        if(imageUrl.length() > 0) {
            loadImg( bookInfo.getIsbn(),imageUrl);
            //new ImageLoadTask(imageUrl, imageViewBookPicture).execute();

        }
        else
        {

            imageViewBookPicture.setImageResource(
                    activity.getResources().getIdentifier("@drawable/default_book_picture", null,
                            activity.getPackageName()));

        }

        return convertView;
    }

    public void loadImg(String isbn, String image)  {

        //String cacheImg = cacheDir + "/" + isbn;
        SharedPreferences sharedata = activity.getSharedPreferences(GlobalParams.PREF_NAME, Context.MODE_PRIVATE);
        String filePath = sharedata.getString("cachePath", null);

        String cacheImg = filePath + "/"+ isbn;


        File f = new File(cacheImg);
        if (f.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(cacheImg);
            imageViewBookPicture.setImageBitmap(bitmap);
        }
        else
        {
            new ImageLoadTask(image, imageViewBookPicture).execute();
            //cache the img
            new CacheBookImages(image,isbn ).execute();
        }
    }

}
