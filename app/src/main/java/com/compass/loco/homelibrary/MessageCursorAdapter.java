package com.compass.loco.homelibrary;

import android.content.Context;
import android.database.Cursor;
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
        TextView tvBook = (TextView) view.findViewById(R.id.message_book);
        TextView tvShop = (TextView) view.findViewById(R.id.message_shop);
        TextView tvOwner = (TextView) view.findViewById(R.id.message_owner);
        TextView tvBorrower = (TextView) view.findViewById(R.id.message_borrower);
        TextView tvAction = (TextView) view.findViewById(R.id.message_action);
        TextView tvTime = (TextView) view.findViewById(R.id.message_time);

        // Extract properties from cursor
        String book = cursor.getString(cursor.getColumnIndexOrThrow("book"));
        String shop = cursor.getString(cursor.getColumnIndexOrThrow("shop"));
        String owner = cursor.getString(cursor.getColumnIndexOrThrow("owner"));
        String borrower = cursor.getString(cursor.getColumnIndexOrThrow("borrower"));
        String action = cursor.getString(cursor.getColumnIndexOrThrow("action"));
        String time = cursor.getString(cursor.getColumnIndexOrThrow("time"));
        // Populate fields with extracted properties
        tvBook.setText(book);
        tvShop.setText(shop);
        tvOwner.setText(owner);
        tvBorrower.setText(borrower);
        tvAction.setText(action);
        tvTime.setText(time);
    }
}
