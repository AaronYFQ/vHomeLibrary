package com.compass.loco.homelibrary;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;


/**
 * A simple {@link Fragment} subclass.
 */
public class MeFragment extends Fragment implements View.OnClickListener {

    private static final int REQUEST_CODE_TAKE_PHOTO = 1;
    private static final int REQUEST_CODE_SELECT_PICTURE = 2;
    private static final int REQUEST_CODE_CROP_IMAGE = 3;
    private static final int REQUEST_CODE_RETURN_FROM_LOGIN = 4;

    private Button mLoginBtn;
    private LinearLayout mLoginOut;
    private LinearLayout mChatMsg;
    private RelativeLayout mBorrowingHistory;
    private ImageView mAvatar;
    private Uri mAvatarUri;
    private File mAvatarFile;

    private boolean mUpdateAvatar;

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

        mLoginBtn.setOnClickListener(this);

        mLoginOut = (LinearLayout)view.findViewById(R.id.user_login_out);
        mLoginOut.setOnClickListener(this);

        mChatMsg  = (LinearLayout)view.findViewById(R.id.my_chat_msg);
        mChatMsg.setOnClickListener(this);

        mBorrowingHistory = (RelativeLayout)view.findViewById(R.id.my_borrowing_history);
        mBorrowingHistory.setOnClickListener(this);

        mAvatar = (ImageView) view.findViewById(R.id.avatar);
        mAvatar.setOnClickListener(this);

        mUpdateAvatar = true;

        updateBtn();

        return view;
    }

    private void updateBtn() {
        String username;
        String token;SharedPreferences sharedata = getContext().getSharedPreferences(GlobalParams.PREF_NAME, Context.MODE_PRIVATE);
        username = sharedata.getString("username", null);
        token = sharedata.getString("token", "");
        if (!token.equals("")) {
            mLoginBtn.setText(username);
            mAvatar.setClickable(true);

            UserInfo myInfo = JMessageClient.getMyInfo();
            if (myInfo != null) {
                if(mUpdateAvatar) {
                    myInfo.getAvatarBitmap(new GetAvatarBitmapCallback() {
                        @Override
                        public void gotResult(int i, String s, Bitmap bitmap) {
                            if (i == 0) {
                                mUpdateAvatar = false;
                                if (bitmap != null)
                                    mAvatar.setImageBitmap(bitmap);
                                else
                                    mAvatar.setImageResource(R.drawable.mine_head);
                            }
                        }
                    });
                }
            } else {
                mAvatar.setImageResource(R.drawable.mine_head);
            }
        } else
        {
            mLoginBtn.setText("登陆/注册");
            mAvatar.setClickable(false);
            mAvatar.setImageResource(R.drawable.mine_head);
        }
    }

    public void onUserLoginBtnClick() {
        SharedPreferences sharedPref = getContext().getSharedPreferences(GlobalParams.PREF_NAME, Context.MODE_PRIVATE);
        String userName = sharedPref.getString("username", "");
        if (!"".endsWith(userName)) {
            return;
        }
        Intent intent = new Intent(this.getContext(), LoginActivity.class);
        startActivityForResult(intent, REQUEST_CODE_RETURN_FROM_LOGIN);
    }

    private void takePhoto(){
        mAvatarFile = new File(Environment.getExternalStorageDirectory(), "/vbook/avatar.jpg");
        mAvatarUri = Uri.fromFile(mAvatarFile);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mAvatarUri);
        startActivityForResult(intent, REQUEST_CODE_TAKE_PHOTO);
    }

    private void selectFromGallay(){
        mAvatarFile = new File(Environment.getExternalStorageDirectory(), "/vbook/avatar.jpg");
        mAvatarUri = Uri.fromFile(mAvatarFile);

        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_SELECT_PICTURE);
    }

    private void onAvatarClick(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setItems(R.array.picture_choice, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
             switch(which)
             {
                 case 0:
                     takePhoto();
                     break;
                 case 1:
                     selectFromGallay();
                     break;
                 default:
                     break;
             }
            }
        });
        builder.show();
    }

    private void cropImage(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);

        intent.putExtra("outputFormat", "JPEG");// 图片格式
        intent.putExtra("return-data", true);

        startActivityForResult(intent, REQUEST_CODE_CROP_IMAGE);
    }

    public boolean saveBitmap2file(Bitmap bmp) {
        Bitmap.CompressFormat format = Bitmap.CompressFormat.JPEG;
        int quality = 100;
        OutputStream stream = null;
        try {
            stream = new FileOutputStream(mAvatarFile);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return bmp.compress(format, quality, stream);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != Activity.RESULT_OK)
            return;

        if(requestCode == REQUEST_CODE_TAKE_PHOTO){
           /* Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mAvatar.setImageBitmap(imageBitmap);*/
            cropImage(mAvatarUri);
        } else if (requestCode == REQUEST_CODE_SELECT_PICTURE) {
            Uri uri = data.getData();
            cropImage(uri);
        } else if (requestCode == REQUEST_CODE_CROP_IMAGE) {
            Bitmap bitmap = data.getParcelableExtra("data");
            mAvatar.setImageBitmap(bitmap);
            saveBitmap2file(bitmap);
            JMessageClient.updateUserAvatar(mAvatarFile, new BasicCallback() {
                @Override
                public void gotResult(int i, String s) {

                }
            });
        } else if (requestCode == REQUEST_CODE_RETURN_FROM_LOGIN) {
            updateBtn();
        }
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
            case R.id.avatar:
                onAvatarClick();
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
        mAvatar.setImageResource(R.drawable.mine_head);
        mAvatar.setClickable(false);
        mUpdateAvatar = true;
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
