package com.compass.loco.homelibrary;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by EXIAOQU on 7/26/2016.
 */
public class ListViewAdapterBook extends BaseAdapter {

    public ArrayList<HashMap<String, String>> myList;

    Activity myActivity;

    CheckBox myCheckBoxBook;
    TextView myTextViewBookName;
    TextView myTextViewBookAuthor;
    TextView myTextViewBookPublisher;
    TextView myTextViewBookIsbn;
    TextView myTextViewBookDetail;

    public ListViewAdapterBook(Activity activity, ArrayList<HashMap<String, String>> list) {
        super();
        this.myActivity = activity;
        this.myList = list;
    }

    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public Object getItem(int position) {
        return myList.get(position);
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

            myCheckBoxBook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox cb = (CheckBox)v;
                    Toast.makeText(myActivity.getApplicationContext(), cb.getText() + " is " + cb.isChecked(), Toast.LENGTH_SHORT ).show();
                }

            });

        }

        HashMap<String, String> map =  myList.get(position);

        myTextViewBookName.setText(map.get("Name"));
        myTextViewBookAuthor.setText("作者：" + map.get("Author"));
        myTextViewBookPublisher.setText("出版社：" + map.get("Publisher"));
        myTextViewBookIsbn.setText("ISBN号：" + map.get("Isbn"));
        myTextViewBookDetail.setText("简介：" + map.get("Detail"));

        return convertView;
    }
}
