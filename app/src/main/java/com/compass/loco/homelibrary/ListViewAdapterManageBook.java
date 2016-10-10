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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
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
    private TextView textViewBookNum;
    private TextView textViewBookAvaiNum;
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
        textViewBookNum = (TextView)convertView.findViewById(R.id.textViewBookNum);
        textViewBookAvaiNum = (TextView)convertView.findViewById(R.id.textViewBookAvaiNum);
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
        textViewBookNum.setText("共有：" + selectedBookInfo.getBookInfo().getBookNum());
        textViewBookAvaiNum.setText("可借：" + selectedBookInfo.getBookInfo().getaVaiNum());
        //textViewBookDetail.setText("借书者：" + );
        String sBorrower = selectedBookInfo.getBookInfo().getBorrower();
        if(sBorrower.equals(""))
        {
            //textViewBookDetail.setVisibility(View.INVISIBLE)
            textViewBookDetail.setText("借书者：" + "暂皆在库");;
        }
        else
        {
            textViewBookDetail.setText("借书者：" + sBorrower);
            //textViewBookDetail.setVisibility(View.VISIBLE);
        }

        if(selectedBookInfo.getBookInfo().getState()) {

            textViewBookState.setText("在库：" + selectedBookInfo.getBookInfo().getaVaiNum() );
            textViewBookState.setVisibility(View.INVISIBLE);

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

    public void loadImg(String isbn, String imageUrl)  {

        String filePath = Environment.getExternalStorageDirectory().toString() + activity.getResources().getString(R.string.cache_path);
        String cacheImg = filePath + "/"+ isbn;

        File f = new File(cacheImg);
        if (f.exists()) {

            Glide.with(activity)
                    .load(cacheImg)
                    //.skipMemoryCache(true) //不使用内存缓存
                    .diskCacheStrategy(DiskCacheStrategy.NONE)//不使用硬盘缓存;
                    .into(imageViewBookPicture);

        }
        else
        {
            /*new ImageLoadTask(image, imageViewBookPicture).execute();
            */
            Glide.with(activity)
                    .load(imageUrl)
                   // .skipMemoryCache(true) //不使用内存缓存
                    .diskCacheStrategy(DiskCacheStrategy.NONE)//不使用硬盘缓存;
                    .into(imageViewBookPicture);
            //cache the img
            new CacheBookImages(imageUrl,isbn).execute();
        }
    }
}
