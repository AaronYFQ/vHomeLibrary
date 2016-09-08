package com.compass.loco.homelibrary;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import cn.jpush.im.android.api.JMessageClient;


/**
 * A simple {@link Fragment} subclass.
 */
public class MeFragment extends Fragment implements View.OnClickListener {

    private Button mLoginBtn;
    private LinearLayout mLoginOut;
    private LinearLayout mChatMsg;
    private RelativeLayout mBorrowingHistory;
    public MeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String username;
        String token="";
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        mLoginBtn = (Button)view.findViewById(R.id.user_login_btn);
        updateBtn();

        mLoginBtn.setOnClickListener(this);

        mLoginOut = (LinearLayout)view.findViewById(R.id.user_login_out);
        mLoginOut.setOnClickListener(this);

        mChatMsg  = (LinearLayout)view.findViewById(R.id.my_chat_msg);
        mChatMsg.setOnClickListener(this);

        mBorrowingHistory = (RelativeLayout)view.findViewById(R.id.my_borrowing_history);
        mBorrowingHistory.setOnClickListener(this);
        return view;
    }

    private void updateBtn() {
        String username;
        String token;SharedPreferences sharedata = getContext().getSharedPreferences(GlobalParams.PREF_NAME, Context.MODE_PRIVATE);
        username = sharedata.getString("username", null);
        token = sharedata.getString("token", null);
        if(token!=null)
        {
            if(!token.equals(""))
            {mLoginBtn.setText(username);}

        }
        else
        {
            mLoginBtn.setText("登陆/注册");
        }
    }

    public void onUserLoginBtnClick() {
        SharedPreferences sharedPref = getContext().getSharedPreferences(GlobalParams.PREF_NAME, Context.MODE_PRIVATE);
        String userName = sharedPref.getString("username", "");
        if (!"".endsWith(userName)) {
            return;
        }
        Intent intent = new Intent(this.getContext(), LoginActivity.class);
        startActivityForResult(intent, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        updateBtn();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.user_login_btn:
                onUserLoginBtnClick();
                break;
            case R.id.user_login_out:
                onUserClickLoginout();
                break;
            case R.id.my_chat_msg:
                onUserClickChatMsg();
                break;
            case R.id.my_borrowing_history:
                onUserClickBorrowingHistory();
                break;
            default:
                // do nothing
                break;
        }
    }
    public void onUserClickLoginout() {
            mLoginBtn.setText("登陆/注册");
            SharedPreferences sharedPref = getContext().getSharedPreferences(GlobalParams.PREF_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor sharedata = sharedPref.edit();
            sharedata.putString("username", "");
            sharedata.putString("token", "");
            sharedata.putString("shopname", "");
            sharedata.commit();
            JMessageClient.logout();
            broadcastLogoutMsg(getContext());
            return;
    }

    public void onUserClickChatMsg()
    {
        MainActivity activity = (MainActivity)getActivity();
        activity.showCovList();
    }

    public void onUserClickBorrowingHistory()
    {

        Intent intent = new Intent(this.getContext(), Borrowed_Activity.class);
        startActivity(intent);
    }


    private void broadcastLogoutMsg(Context context){

        Intent intent = new Intent();
        intent.setAction(GlobalParams.LOGOUT_ACTION);
        context.sendBroadcast(intent);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        FragmentActivity activity = getActivity();
        if(activity == null)
            return;

        if(hidden)
        {
            ImageButton meBtn = (ImageButton) getActivity().findViewById(R.id.menu_4);
            Drawable meBtnGray = getResources().getDrawable(R.drawable.mainmenu_me_gray);
            meBtn.setBackgroundDrawable(meBtnGray);
        }
        else
        {
            ImageButton meBtn = (ImageButton) getActivity().findViewById(R.id.menu_4);
            Drawable meBtnGreen = getResources().getDrawable(R.drawable.mainmenu_me);
            meBtn.setBackgroundDrawable(meBtnGreen);

            updateBtn();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

/*        ImageButton meBtn = (ImageButton) getActivity().findViewById(R.id.menu_4);
        Drawable meBtnGray = getResources().getDrawable(R.drawable.mainmenu_me_gray);
        meBtn.setBackgroundDrawable(meBtnGray);*/
    }

    @Override
    public void onResume() {
        super.onResume();

/*        ImageButton meBtn = (ImageButton) getActivity().findViewById(R.id.menu_4);
        Drawable meBtnGreen = getResources().getDrawable(R.drawable.mainmenu_me);
        meBtn.setBackgroundDrawable(meBtnGreen);*/
    }
}
