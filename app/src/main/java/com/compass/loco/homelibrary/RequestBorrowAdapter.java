/**
 * Created by elanywa on 2016/8/2.
 */
package com.compass.loco.homelibrary;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by elanywa on 2016/8/2.
 */
public class RequestBorrowAdapter extends BaseAdapter {
    private ArrayList<BorrowedBookInfo> arrayBorrowedList;
    private Activity activity;

    public RequestBorrowAdapter(Activity activity, ArrayList<BorrowedBookInfo> list) {
        super();
        this.activity = activity;
        this.arrayBorrowedList = list;
    }

    private int[] colors = new int[]{0xff626569, 0xff4f5257};

    public int getCount() {
        return arrayBorrowedList.size();
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        //ImageView image = null;
        TextView bookname = null;
        TextView shopname = null;
        TextView bookstate = null;
        TextView shopaddress = null;
        TextView borrowRequestTime = null;
        //Button button = null;

        if (convertView == null) {
            Log.v("convertView null at", new Integer(position).toString());

            LayoutInflater inflater = activity.getLayoutInflater();
            convertView = inflater.inflate(R.layout.activity_requested_borrow, null);
        } else {
            Log.v("\"convertView null at ", new Integer(position).toString());
        }

        // image = (ImageView) convertView.findViewById(R.id.array_image);
        bookname = (TextView) convertView.findViewById(R.id.array_book_name);
        shopname = (TextView) convertView.findViewById(R.id.array_shop_name);
        shopaddress = (TextView) convertView.findViewById(R.id.array_shop_address);
        borrowRequestTime = (TextView) convertView.findViewById(R.id.array_borrowed_requested_time);
        bookstate = (TextView) convertView.findViewById(R.id.array_book_state);
        /*button = (Button) convertView.findViewById(R.id.array_button);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Log.v("\"Borrow book: ", "test button");

            }
        });*/


        /*int colorPos = position % colors.length;
        convertView.setBackgroundColor(colors[colorPos]);*/

        BorrowedBookInfo bookInfo = arrayBorrowedList.get(position);
        bookname.setText(bookInfo.getBookName());
        shopname.setText(bookInfo.getShopName());
        shopaddress.setText(bookInfo.getShopAddress());

        String state = bookInfo.getState();
        if (state.equals("borrowing")) {
            bookstate.setText("等待");
            borrowRequestTime.setText("求借时间：" + bookInfo.getRequestTime());
        } else {
            bookstate.setText("已借");
            borrowRequestTime.setText("借阅时间：" + bookInfo.getAcceptTime());
        }

        /*if(colorPos == 0)
            image.setImageResource(R.drawable.jay);
        else
            image.setImageResource(R.drawable.image);*/
        return convertView;
    }
}