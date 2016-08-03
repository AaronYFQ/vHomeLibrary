package com.compass.loco.homelibrary.adapter;

import android.content.Context;

import com.compass.loco.homelibrary.R;
import com.compass.loco.homelibrary.model.Bean;
import com.compass.loco.homelibrary.model.ShopBean;
import com.compass.loco.homelibrary.util.CommonAdapter;
import com.compass.loco.homelibrary.util.ViewHolder;

import java.util.List;

/**
 * Created by eweilzh on 2016-07-27
 */

public class SearchAearAdapter extends CommonAdapter<ShopBean>{

   public SearchAearAdapter(Context context, List<ShopBean> data, int layoutId) {
        super(context, data, layoutId);
    }



    @Override
    public void convert(ViewHolder holder, int position) {

        //holder.setImageResource(R.id.textViewBookCounters,mData.get(position).getIconId())
          holder.setText(R.id.textViewShopName,mData.get(position).getShopName())
                .setText(R.id.textViewShopAddr,mData.get(position).getVillage());
    }
}
