package com.compass.loco.homelibrary.adapter;

import android.content.Context;

import com.compass.loco.homelibrary.R;
import com.compass.loco.homelibrary.model.ShopBean;
import com.compass.loco.homelibrary.util.CommonAdapter;
import com.compass.loco.homelibrary.util.ViewHolder;

import java.util.List;

/**
 * Created by elanywa on 2016/9/27.
 */
/*public class BookshopPushAdapter extends CommonAdapter<ShopBean>{

        public BookshopPushAdapter(Context context, List<ShopBean> data, int layoutId) {
            super(context, data, layoutId);
        }



        @Override
        public void convert(ViewHolder holder, int position) {

            //holder.setImageResource(R.id.item_search_iv_book_icon,mData.get(position).getIconId())
            holder.setText(R.id.bookshop_name,mData.get(position).getShopName())
                    .setText(R.id.bookshop_bookcnt,mData.get(position).getBookCounts())
                    .setText(R.id.bookshop_addr,mData.get(position).getVillage())
                    .setText(R.id.bookshop_borrowcnt,mData.get(position).getBorrowCounts());
        }
}*/
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class BookshopPushAdapter extends RecyclerView.Adapter<BookshopPushAdapter.MyViewHolder> {
    List<ShopBean> mData;
    private LayoutInflater inflater;

    public BookshopPushAdapter(Context context, List<ShopBean> data) {
        inflater = LayoutInflater.from(context);
        this.mData = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.content_bookshop_push, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ShopBean current = mData.get(position);
        holder.setData(current, position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView shopname, shopaddr,bookcnt, borrowcnt;
        int position;
        ShopBean current;

        public MyViewHolder(View itemView) {
            super(itemView);
            shopname       = (TextView)  itemView.findViewById(R.id.bookshop_name);
            shopaddr       = (TextView)  itemView.findViewById(R.id.bookshop_addr);
            bookcnt        = (TextView)  itemView.findViewById(R.id.bookshop_bookcnt);
            borrowcnt      = (TextView)  itemView.findViewById(R.id.bookshop_borrowcnt);
        }

        public void setData(ShopBean current, int position) {
            String bookCntText = "藏书量:" + current.getBookCounts();
            String borrowCntText = "借书次数:" + current.getBorrowCounts();
            this.shopname.setText(current.getShopName());
            this.shopaddr.setText(current.getVillage());
            this.bookcnt.setText(bookCntText);
            this.borrowcnt.setText(borrowCntText);
            this.position = position;
            this.current = current;
        }
    }
}

