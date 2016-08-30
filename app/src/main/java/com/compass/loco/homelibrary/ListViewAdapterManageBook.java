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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.compass.loco.homelibrary.widge.CacheBookImages;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by EXIAOQU on 7/26/2016.
 */
public class ListViewAdapterManageBook extends BaseAdapter {

    // Andorid objects
    private Activity activity;
    private CheckBox checkBoxBook;
    private TextView textViewBookName;
    private TextView textViewBookAuthor;
    private TextView textViewBookPublisher;
    private TextView textViewBookIsbn;
    private TextView textViewBookDetail;
    private TextView textViewBookState;
    private ImageView imageViewBookPicture;

    // selected or non-selected bookinfo arraylist
    public ArrayList<SelectedBookInfo> arrayListSelectedBookInfo;

    public ListViewAdapterManageBook(Activity activity, ArrayList<SelectedBookInfo> list) {
        super();
        this.activity = activity;
        this.arrayListSelectedBookInfo = list;
    }

    @Override
    public int getCount() {
        return arrayListSelectedBookInfo.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayListSelectedBookInfo.get(position);
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
            convertView = inflater.inflate(R.layout.activity_manage_library_book_row, null);
        }
        else {
            Log.v("convertView not null, position=", new Integer(position).toString());
        }

        checkBoxBook = (CheckBox)convertView.findViewById(R.id.checkBoxBook);
        textViewBookName = (TextView)convertView.findViewById(R.id.textViewBookName);
        textViewBookAuthor = (TextView)convertView.findViewById(R.id.textViewBookAuthor);
        textViewBookPublisher = (TextView)convertView.findViewById(R.id.textViewBookPublisher);
        textViewBookIsbn = (TextView)convertView.findViewById(R.id.textViewBookIsbn);
        textViewBookDetail = (TextView)convertView.findViewById(R.id.textViewBookDetail);
        textViewBookState = (TextView)convertView.findViewById(R.id.textViewBookState);
        imageViewBookPicture = (ImageView)convertView.findViewById(R.id.imageViewBookPicture);

        checkBoxBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CheckBox cb = (CheckBox)v;

                SelectedBookInfo selectedBookInfo = (SelectedBookInfo)cb.getTag();

                selectedBookInfo.setSelected(cb.isChecked());

                if(!BuildConfig.DEBUG) {

                    Toast.makeText(activity.getApplicationContext(),
                            selectedBookInfo.getBookInfo().getName() + " is " + cb.isChecked(),
                            Toast.LENGTH_SHORT).show();

                }
            }

        });

        SelectedBookInfo selectedBookInfo =  arrayListSelectedBookInfo.get(position);

        checkBoxBook.setTag(selectedBookInfo);

        textViewBookName.setText(selectedBookInfo.getBookInfo().getName());
        textViewBookAuthor.setText("作者：" + selectedBookInfo.getBookInfo().getAuthor());
        textViewBookPublisher.setText("出版社：" +selectedBookInfo.getBookInfo().getPublisher());
        textViewBookIsbn.setText("ISBN号：" + selectedBookInfo.getBookInfo().getIsbn());
        textViewBookDetail.setText("简介：" + selectedBookInfo.getBookInfo().getDetail());

        if(selectedBookInfo.getBookInfo().getState()) {

            textViewBookState.setText("在库");

        }
        else
        {
            textViewBookState.setText("借出");

            checkBoxBook.setEnabled(false);
        }

        String imageUrl = selectedBookInfo.getBookInfo().getImageUrl();
        if(imageUrl.length() > 0) {
            loadImg( selectedBookInfo.getBookInfo().getIsbn(),imageUrl);
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
            new CacheBookImages(image,isbn).execute();
        }
    }
}
