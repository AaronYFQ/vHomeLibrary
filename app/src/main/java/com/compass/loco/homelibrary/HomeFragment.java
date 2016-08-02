package com.compass.loco.homelibrary;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private String mCityName = "北京";
    private Button mSelectCityBtn;
    private Button mSelectSearchBtn;
    private ImageButton mAddBookBtn;
    private ImageButton mMylibraryBtn;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mSelectCityBtn = (Button)view.findViewById(R.id.city_btn);
        mSelectCityBtn.setOnClickListener(this);

        mSelectSearchBtn = (Button)view.findViewById(R.id.search_btn);
        mSelectSearchBtn.setOnClickListener(this);

        if(getArguments() != null) {
            String cityName = getArguments().getString(MainActivity.INTENT_KEY_CITY_NAME);
            if(cityName != null) {
                mCityName = cityName;
            }
        }
        mSelectCityBtn.setText(mCityName);

        mAddBookBtn = (ImageButton) view.findViewById(R.id.add_book_btn);
        mAddBookBtn.setOnClickListener(this);

        mMylibraryBtn = (ImageButton) view.findViewById(R.id.my_library_btn);
        mMylibraryBtn.setOnClickListener(this);

        return view;
    }

    public void onSelectCityBtn(View view) {
        Intent intent = new Intent(this.getContext(), SelectCityActivity.class);
        startActivity(intent);
    }
    public void onSelectSearchBtn(View view) {
        Intent intent = new Intent(this.getContext(), SearchMainActivity.class);
        startActivity(intent);
    }


    public void onAddBookBtnClick(View view) {
        Intent intent = new Intent(this.getContext(), ScanBookActivity.class);
        startActivity(intent);
    }

    public void onMyLibraryBtnClick(View view) {

        SharedPreferences sharedPref = this.getContext().getSharedPreferences(GlobalParams.PREF_NAME,Context.MODE_PRIVATE);
        String username = sharedPref.getString("username", null);
        String shopname = sharedPref.getString("shopname", null);

        if (null != username && !username.equals(""))
        {
            //if shop exist, show the shop manage activity, otherwise show the create library actvity
            if(null != shopname && !shopname.equals(""))
            {
                Intent intent = new Intent(this.getContext(), ManageLibraryActivity.class);
                startActivity(intent);
            }
            else
            {
                Intent intent = new Intent(this.getContext(), CreateLibActivity.class);
                startActivity(intent);
            }

        }
        else
        {
            Intent intent = new Intent(this.getContext(), LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.city_btn:
                onSelectCityBtn(view);
                break;
            case R.id.search_btn:
                onSelectSearchBtn(view);
                break;
            case R.id.add_book_btn:
                onAddBookBtnClick(view);
                break;
            case R.id.my_library_btn:
                onMyLibraryBtnClick(view);
                break;
            default:
                // do nothing
                break;
        }
    }
}
