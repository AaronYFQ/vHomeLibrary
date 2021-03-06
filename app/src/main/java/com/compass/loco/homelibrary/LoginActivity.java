package com.compass.loco.homelibrary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;

import com.compass.loco.homelibrary.chatting.BaseActivity;
import com.compass.loco.homelibrary.chatting.utils.HandleResponseCode;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;

public class LoginActivity extends BaseActivity {

    String TAG = LoginActivity.class.getSimpleName();
    TabHost tabHost;
    // UI references.
    private EditText mLoginRegUsernameView;
    private EditText mLoginRegPasswordView;
    private View mProgressView;
    private View mLoginRegisterFormView;
    String mToken;
    String mShopname;
    enum ActionType {LOGIN, REGISTER};
    Button mButtonView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


       /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

        tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();
        tabHost.addTab(tabHost.newTabSpec("登陆").setIndicator("登陆")
                .setContent(R.id.login_form));
        TextView x = (TextView) tabHost.getTabWidget().getChildAt(0).findViewById(android.R.id.title);
        x.setTextSize(17);

        tabHost.addTab(tabHost.newTabSpec("注册").setIndicator("注册")
                .setContent(R.id.register_form));
        x = (TextView) tabHost.getTabWidget().getChildAt(1).findViewById(android.R.id.title);
        x.setTextSize(17);

        tabHost.setCurrentTabByTag("登陆");

        //标签切换事件处理，setOnTabChangedListener
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (tabId.equals("登陆")) {   //第二个标签
                    tabHost.setCurrentTabByTag("登陆");
                }
                if (tabId.equals("注册")) {   //第三个标签
                    tabHost.setCurrentTabByTag("注册");
                }
            }
        });

    }

    //cancel tag handler
    public void onClickCancel(View view) {
        backToMainActivity(false, "Guest");
    }

    // login button handler
    public void onClickLogin(View view) {

        mLoginRegUsernameView = (EditText) findViewById(R.id.login_username);
        mLoginRegPasswordView = (EditText) findViewById(R.id.login_password);
        mLoginRegisterFormView = findViewById(R.id.login_form);
        //mProgressView = findViewById(R.id.login_progress);
        mButtonView = (Button)findViewById(R.id.sign_in_button);
        // Store values at the time of the login attempt.
        String username = mLoginRegUsernameView.getText().toString();
        String password = mLoginRegPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mLoginRegPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mLoginRegPasswordView;
            cancel = true;
        }
        // Check for a valid username.
        if (TextUtils.isEmpty(username)) {
            mLoginRegUsernameView.setError(getString(R.string.error_field_required));
            focusView = mLoginRegUsernameView;
            cancel = true;
        } else if (!isUsernameValid(username)) {
            mLoginRegUsernameView.setError(getString(R.string.error_invalid_username));
            focusView = mLoginRegUsernameView;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            //showProgress(true);
            connectJmessageServer(username, password, ActionType.LOGIN);

        }
    }

    private boolean isUsernameValid(String username) {
        //TODO: Replace this with your own logic
        // return username.length() > 4;
        return true;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return true;
    }


    // register button handler
    public void onClickRegister(View view) {

        mLoginRegUsernameView = (EditText) findViewById(R.id.register_username);
        mLoginRegPasswordView = (EditText) findViewById(R.id.register_password);
        mLoginRegisterFormView = findViewById(R.id.register_form);
       // mProgressView = findViewById(R.id.register_progress);
        mButtonView = (Button)findViewById(R.id.register_button);
        // Store values at the time of the login attempt.
        String username = mLoginRegUsernameView.getText().toString();
        String password = mLoginRegPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mLoginRegPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mLoginRegPasswordView;
            cancel = true;
        }
        // Check for a valid username.
        if (TextUtils.isEmpty(username)) {
            mLoginRegUsernameView.setError(getString(R.string.error_field_required));
            focusView = mLoginRegUsernameView;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            // showProgress(true);
            connectJmessageServer(username, password, ActionType.REGISTER);
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public void backToMainActivity(boolean isSuccess, String username) {
        //store Username
        //back to user profile pages
        Intent intent = new Intent(this, MainActivity.class);
        if (isSuccess) {
            SharedPreferences sharedPref = getSharedPreferences(GlobalParams.PREF_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor sharedata = sharedPref.edit();
            sharedata.putString("username", username);
            sharedata.putString("token", mToken);
            sharedata.putString("shopname", mShopname);
            intent.putExtra(MainActivity.INTENT_KEY_USER_NAME, username);
            sharedata.commit();

            sendRegisterIDToServer(getApplicationContext());
            SendLoginBroadcast(getApplicationContext());
        }
        //intent.putExtra(MainActivity.INTENT_KEY_LOGIN_RESULT, isSuccess);
        //startActivity(intent);
        setResult(Activity.RESULT_OK);
        finish();
    }


    private void connectHttpServer(final String username, String password, final ActionType type) {
        //handle server connect  response
        mToken = "";
        mShopname = "";
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String json = msg.getData().getString("responseBody");
                Log.v(TAG,"connectHttpServer :"+ json);
                try {
                    // handler item from Json
                    JSONObject item = new JSONObject(json);
                    String comment = item.getString("result");
                    if (comment.equals("success")) {
                        mToken = item.getString("token");
                        if (type == ActionType.LOGIN) {
                            mShopname = item.getString("shopname");
                        }
                        Log.v(TAG,"connectHttpServer result"+ "token" + ": " + mToken + "shop name" + ":" + mShopname);
                        backToMainActivity(true, username);
                    } else {

                        Log.v(TAG,"connectHttpServer :"+ "login/register error");
                        RecoverLoginRegbutton(type);
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();

                }
            }
        };
        HttpUtil httptd = new HttpUtil();

        if (type == ActionType.LOGIN) {
            //login  action
            httptd.submitAsyncHttpClientPostLogin(username, password, handler);
            Log.v(TAG,"connectHttpServer: " + "Login  request");
        } else if (type == ActionType.REGISTER) {
            //register action
            httptd.submitAsyncHttpClientPostRegisterUser(username, password, handler);
            Log.v(TAG,"connectHttpServer: " + "Register request");
        }

    }

    private void connectJmessageServer(final String username, final String password, final ActionType type) {

        Log.i("JMessageApplication", "connect Jmessage server " + username);
        DisableLoginRegButton();
        if (type == ActionType.REGISTER) {
            JMessageClient.register(username, GlobalParams.JCHAT_USER_PASSWORD, new BasicCallback() {
                @Override
                public void gotResult(int status, String desc) {
                    if (status == 0) {
                        Log.v(TAG,"connectJmessageServer: "+ "register success");
                        JMessageClient.login(username, GlobalParams.JCHAT_USER_PASSWORD, new BasicCallback() {
                            @Override
                            public void gotResult(int status, String desc) {
                                if (status == 0) {
                                    Log.v(TAG,"connectJmessageServer: "+ "login sucess");
                                    connectHttpServer(username, password, ActionType.REGISTER);
                                } else {
                                    Log.v(TAG,"connectJmessageServer: "+ "login failure");
                                    HandleResponseCode.onHandle(getApplicationContext(), status, false);
                                    RecoverLoginRegbutton(type);
                                }
                            }
                        });
                    } else {
                        Log.v(TAG,"connectJmessageServer: "+ "register failure");
                        HandleResponseCode.onHandle(getApplicationContext(), status, false);
                        RecoverLoginRegbutton(type);
                    }
                }
            });
        } else {
            JMessageClient.login(username, GlobalParams.JCHAT_USER_PASSWORD, new BasicCallback() {
                @Override
                public void gotResult(int status, String desc) {
                    if (status == 0) {
                        Log.v(TAG,"connectJmessageServer: "+ "login success");
                        connectHttpServer(username, password, ActionType.LOGIN);
                    } else {
                        Log.v(TAG,"connectJmessageServer: "+ "login failure");
                        HandleResponseCode.onHandle(getApplicationContext(), status, false);
                        RecoverLoginRegbutton(type);
                    }
                }
            });
        }

    }

    private void RecoverLoginRegbutton(final ActionType type)
    {
        if (type == ActionType.REGISTER) {
            mLoginRegUsernameView.setError(getString(R.string.error_existed_username));
        } else {
            mLoginRegUsernameView.setError(getString(R.string.error_username_or_password));
        }
        mLoginRegUsernameView.requestFocus();
        //recover button clickable and background resource
        mButtonView.setClickable(true);
        mButtonView.setBackgroundResource(R.color.colorBackgroundGreen);
        mButtonView.setTextColor(Color.WHITE);
    }

    private void DisableLoginRegButton() {
        mButtonView.setClickable(false);
        mButtonView.setBackgroundResource(R.color.colorBackgroundGray);
        mButtonView.setTextColor(Color.BLACK);
    }

    private void sendRegisterIDToServer(Context context)
    {
        String regid = JPushInterface.getRegistrationID(context);

        if(!mToken.isEmpty()) {
            HttpUtil httptd = new HttpUtil();
            httptd.submitAsyncHttpClientPostRegisterID(mToken, regid, null);

            Log.v(TAG,"sendRegisterIDToServer:" + "Send register ID.");
        }
    }

    private void SendLoginBroadcast(Context context){

        Intent intent = new Intent();
        intent.setAction(GlobalParams.LOGIN_ACTION);
        context.sendBroadcast(intent);
    }

}



