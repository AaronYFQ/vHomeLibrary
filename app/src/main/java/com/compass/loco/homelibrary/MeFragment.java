package com.compass.loco.homelibrary;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class MeFragment extends Fragment implements View.OnClickListener {

    private Button mLoginBtn;
    private LinearLayout mLoginOut;

    public MeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        mLoginBtn = (Button)view.findViewById(R.id.user_login_btn);
        if (getArguments() != null && getArguments().containsKey(MainActivity.INTENT_KEY_LOGIN_RESULT)) {
            mLoginBtn.setText(getArguments().getString(MainActivity.INTENT_KEY_USER_NAME));
        } else {
            mLoginBtn.setOnClickListener(this);
        }

        mLoginOut = (LinearLayout)view.findViewById(R.id.user_login_out);
        mLoginOut.setOnClickListener(this);
        return view;
    }

    public void onUserLoginBtnClick(View view) {
        Intent intent = new Intent(this.getContext(), LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.user_login_btn:
                onUserLoginBtnClick(view);
                break;
            case R.id.user_login_out:
                onUserClickLoginout(view);
                break;
            default:
                // do nothing
                break;
        }
    }
    public void onUserClickLoginout(View view) {
            mLoginBtn.setText("登陆/注册");
            SharedPreferences sharedPref = getContext().getSharedPreferences(GlobalParams.PREF_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor sharedata = sharedPref.edit();
            sharedata.putString("username", "");
            sharedata.putString("token", "");
            sharedata.putString("shopname", "");
            sharedata.commit();
            return;
    }
}
