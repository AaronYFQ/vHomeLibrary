package com.compass.loco.homelibrary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;

import java.util.List;


public class HomeFragment extends Fragment implements View.OnClickListener{
    private final int REQUEST_CODE = 88;

    private String mLocatedCity = "定位中";
    private String mSelectedCity = "定位中";
    private Button mSelectCityBtn;
    private Button mSelectSearchBtn;
    private ImageButton mBorrowedBookBtn;
    private ImageButton mMylibraryBtn;
    private LocationClient mLocationClient = null;
    private BDLocationListener myListener = new MyLocationListener();

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
                mSelectedCity = cityName;
            }
        }
        mSelectCityBtn.setText(mSelectedCity);



        mBorrowedBookBtn = (ImageButton) view.findViewById(R.id.borrowed_book_btn);
        mBorrowedBookBtn.setOnClickListener(this);

        mMylibraryBtn = (ImageButton) view.findViewById(R.id.my_library_btn);
        mMylibraryBtn.setOnClickListener(this);

        //trigger locating
        //enable baidu location sdk
        mLocationClient = new LocationClient(getActivity().getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener( myListener );    //注册监听函数
        initLocation();
        mLocationClient.start();

        return view;
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        //就是这个方法设置为true，才能获取当前的位置信息
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("gcj02");//可选，默认gcj02，设置返回的定位结果坐标系
//        int span = 1000;
//        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        mLocationClient.setLocOption(option);
    }
    public void onSelectCityBtn(View view) {
        Intent intent = new Intent(this.getContext(), CityListActivity.class);
        intent.putExtra("locatedCity", mLocatedCity);
        startActivityForResult(intent,REQUEST_CODE);
    }
    public void onSelectSearchBtn(View view) {
        Intent intent = new Intent(this.getContext(), SearchMainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                mSelectedCity = data.getStringExtra("selectedCity");
                Log.i("HomeFragment", "cityName:"+mSelectedCity);
                if(mSelectCityBtn != null){
                    mSelectCityBtn.setText(mSelectedCity);
                }
            }
        }
    }

    public void onBorrowedBookBtnClick(View view) {
        Intent intent = new Intent(this.getContext(), Borrowed_Activity.class);
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
            case R.id.borrowed_book_btn:
                onBorrowedBookBtnClick(view);
                break;
            case R.id.my_library_btn:
                onMyLibraryBtnClick(view);
                break;
            default:
                // do nothing
                break;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        FragmentActivity activity = getActivity();
        if(activity == null)
            return;

        if (hidden) {
            ImageButton homeBtn = (ImageButton) activity.findViewById(R.id.menu_1);
            Drawable homeBtnGray = getResources().getDrawable(R.drawable.mainmenu_home_gray);
            homeBtn.setBackgroundDrawable(homeBtnGray);
        } else {
            ImageButton homeBtn = (ImageButton) activity.findViewById(R.id.menu_1);
            Drawable homeBtnGreen = getResources().getDrawable(R.drawable.mainmenu_home);
            homeBtn.setBackgroundDrawable(homeBtnGreen);
        }
    }

    @Override
    public void onStop() {
        super.onStop();

/*        ImageButton homeBtn = (ImageButton) getActivity().findViewById(R.id.menu_1);
        Drawable homeBtnGray = getResources().getDrawable(R.drawable.mainmenu_home_gray);
        homeBtn.setBackgroundDrawable(homeBtnGray);*/
    }

    @Override
    public void onResume() {
        super.onResume();

/*        ImageButton homeBtn = (ImageButton) getActivity().findViewById(R.id.menu_1);
        Drawable homeBtnGreen = getResources().getDrawable(R.drawable.mainmenu_home);
        homeBtn.setBackgroundDrawable(homeBtnGreen);*/
    }

    //method to find height of the status bar
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public class MyLocationListener implements BDLocationListener{
        @Override
        public void onReceiveLocation(BDLocation location) {
            //Receive Location
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation){// GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());// 单位：公里每小时
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\nheight : ");
                sb.append(location.getAltitude());// 单位：米
                sb.append("\ndirection : ");
                sb.append(location.getDirection());// 单位度
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");
                mLocatedCity = location.getCity();
                // mSelectCityBtn.setText(mCityName);

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){// 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\ncity : ");
                mLocatedCity = location.getCity();
                sb.append(mSelectedCity);
                //运营商信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");

                if(mSelectCityBtn == null)
                {
                    Log.i("homeFragment", "mSelectCityBtn is null!!!!");
                }
                else
                {
                    mSelectCityBtn.setText(mLocatedCity);
                }

            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
                mSelectedCity = location.getCity();
                //mSelectCityBtn.setText(mCityName);
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());// 位置语义化信息
            List<Poi> list = location.getPoiList();// POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }
            Log.i("BaiduLocationApiDem", sb.toString());
        }
    }


}
