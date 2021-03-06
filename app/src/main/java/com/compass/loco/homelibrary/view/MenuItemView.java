package com.compass.loco.homelibrary.view;

import android.view.View;
import android.widget.LinearLayout;
import com.compass.loco.homelibrary.R;


/**
 * Created by Ken on 2015/1/26.
 */
public class MenuItemView {

    private View mView;
    private LinearLayout mCreateGroupLl;
    private LinearLayout mAddFriendLl;

    public MenuItemView(View view) {
        this.mView = view;
    }

    public void initModule() {
       // mCreateGroupLl = (LinearLayout) mView.findViewById(R.id.create_group_ll);
        mAddFriendLl = (LinearLayout) mView.findViewById(R.id.add_friend_ll);
    }

    public void setListeners(View.OnClickListener listener) {
      //  mCreateGroupLl.setOnClickListener(listener);
        mAddFriendLl.setOnClickListener(listener);
    }
}
