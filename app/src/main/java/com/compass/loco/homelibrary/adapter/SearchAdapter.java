package com.compass.loco.homelibrary.adapter;

import android.content.Context;
import android.view.View;
import android.app.Activity;


import com.compass.loco.homelibrary.R;
import com.compass.loco.homelibrary.model.Bean;
import com.compass.loco.homelibrary.util.CommonAdapter;
import com.compass.loco.homelibrary.util.ViewHolder;

import java.util.List;

/**
 * Created by eweilzh on 2016-07-27
 */

public class SearchAdapter extends CommonAdapter<Bean>{

    public SearchAdapter(Context context, List<Bean> data, int layoutId) {
        super(context, data, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, int position) {

        //holder.setImageResource(R.id.item_search_iv_book_icon,mData.get(position).getIconId())
          holder.setText(R.id.textViewBookName,mData.get(position).getBookName())
                .setText(R.id.textViewBookState,mData.get(position).getBookState())
                .setText(R.id.textViewBookAuthor,mData.get(position).getBookAuthor())
                .setText(R.id.textViewBookPublisher,mData.get(position).getBookPublisher())
                .setText(R.id.textViewBookShop,mData.get(position).getShopName())
                .setText(R.id.textViewBookAddr,mData.get(position).getBookAddr());
    }
}
