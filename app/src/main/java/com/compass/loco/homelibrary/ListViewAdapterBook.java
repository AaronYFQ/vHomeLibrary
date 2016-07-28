package com.compass.loco.homelibrary;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by EXIAOQU on 7/26/2016.
 */
public class ListViewAdapterBook extends BaseAdapter {

    // Andorid objects
    Activity myActivity;
    CheckBox myCheckBoxBook;
    TextView myTextViewBookName;
    TextView myTextViewBookAuthor;
    TextView myTextViewBookPublisher;
    TextView myTextViewBookIsbn;
    TextView myTextViewBookDetail;
    ImageView myImageViewBookPicture;

    // selected or non-selected bookinfo arraylist
    public ArrayList<SelectedBookInfo> myArrayListSelectedBookInfo;

    public ListViewAdapterBook(Activity activity, ArrayList<SelectedBookInfo> list) {
        super();
        this.myActivity = activity;
        this.myArrayListSelectedBookInfo = list;
    }

    @Override
    public int getCount() {
        return myArrayListSelectedBookInfo.size();
    }

    @Override
    public Object getItem(int position) {
        return myArrayListSelectedBookInfo.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = myActivity.getLayoutInflater();
        if(convertView == null)
        {
            convertView = inflater.inflate(R.layout.activity_manage_library_book_row, null);

            myCheckBoxBook = (CheckBox)convertView.findViewById(R.id.checkBoxBook);
            myTextViewBookName = (TextView)convertView.findViewById(R.id.textViewBookName);
            myTextViewBookAuthor = (TextView)convertView.findViewById(R.id.textViewBookAuthor);
            myTextViewBookPublisher = (TextView)convertView.findViewById(R.id.textViewBookPublisher);
            myTextViewBookIsbn = (TextView)convertView.findViewById(R.id.textViewBookIsbn);
            myTextViewBookDetail = (TextView)convertView.findViewById(R.id.textViewBookDetail);
            myImageViewBookPicture = (ImageView)convertView.findViewById(R.id.imageViewBookPicture);

            myCheckBoxBook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox cb = (CheckBox)v;
                    SelectedBookInfo selectedBookInfo = (SelectedBookInfo)cb.getTag();
                    selectedBookInfo.setSelected(cb.isSelected());

                    Toast.makeText(myActivity.getApplicationContext(), selectedBookInfo.getBookInfo().getName() + " is " + cb.isChecked(), Toast.LENGTH_SHORT ).show();
                }

            });

        }

        SelectedBookInfo selectedBookInfo =  myArrayListSelectedBookInfo.get(position);

        myCheckBoxBook.setTag(selectedBookInfo);
        myTextViewBookName.setText(selectedBookInfo.getBookInfo().getName());
        myTextViewBookAuthor.setText("作者：" + selectedBookInfo.getBookInfo().getAuthor());
        myTextViewBookPublisher.setText("出版社：" +selectedBookInfo.getBookInfo().getPublisher());
        myTextViewBookIsbn.setText("ISBN号：" + selectedBookInfo.getBookInfo().getIsbn());
        myTextViewBookDetail.setText("简介：" + selectedBookInfo.getBookInfo().getDetail());

        if(selectedBookInfo.getBookInfo().getBitmap() != null) {
            myImageViewBookPicture.setImageBitmap(selectedBookInfo.getBookInfo().getBitmap());
        }

        return convertView;
    }
}
