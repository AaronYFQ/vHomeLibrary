package com.compass.loco.homelibrary;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class MeFragment extends Fragment implements View.OnClickListener {

    private Button mLoginBtn;

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
            default:
                // do nothing
                break;
        }
    }
}
