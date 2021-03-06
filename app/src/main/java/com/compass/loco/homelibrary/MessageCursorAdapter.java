package com.compass.loco.homelibrary;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by ehoixie on 7/29/2016.
 */
public class MessageCursorAdapter extends CursorAdapter {
    public MessageCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.message_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvBook = (TextView) view.findViewById(R.id.message_info);
/*        TextView tvShop = (TextView) view.findViewById(R.id.message_shop);
        TextView tvOwner = (TextView) view.findViewById(R.id.message_owner);
        TextView tvBorrower = (TextView) view.findViewById(R.id.message_borrower);
        TextView tvAction = (TextView) view.findViewById(R.id.message_action);*/
        TextView tvTime = (TextView) view.findViewById(R.id.message_time);

        // Extract properties from cursor
        String book = cursor.getString(cursor.getColumnIndexOrThrow("book"));
        String shop = cursor.getString(cursor.getColumnIndexOrThrow("shop"));
        String owner = cursor.getString(cursor.getColumnIndexOrThrow("owner"));
        String borrower = cursor.getString(cursor.getColumnIndexOrThrow("borrower"));
        String action = cursor.getString(cursor.getColumnIndexOrThrow("action"));
        String time = cursor.getString(cursor.getColumnIndexOrThrow("time"));
        Boolean newMsg = cursor.getInt(cursor.getColumnIndex("new")) > 0;

        String message;
        if(action.equals("borrow"))
        {
            message = borrower + "发出借《" + book + "》请求。";
        }
        else if(action.equals("accept"))
        {
            message = owner + "已同意你借《" + book + "》。";
        }
        else if(action.equals("refuse"))
        {
            message = owner + "拒绝你借《" + book + "》的请求。";
        }
        else
        {
            Log.v("Invalid action", action);
            return;
        }

        // Populate fields with extracted properties
        tvBook.setText(message);
        if(newMsg) {
            tvBook.setTextColor(Color.BLACK);
        }
        else {
            tvBook.setTextColor(Color.GRAY);
        }
        //tvShop.setText(shop);
        //tvOwner.setText(owner);
        //tvBorrower.setText(borrower);
        //tvAction.setText(action);
        tvTime.setText(time);
    }
}
