package com.compass.loco.homelibrary;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends Activity {

    TabHost tabHost;
    // UI references.
    private EditText mLoginRegUsernameView;
    private EditText mLoginRegPasswordView;
    private View mProgressView;
    private View mLoginRegisterFormView;
    String mtoken;
    enum ActionType {LOGIN, REGISTER};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();
        //LayoutInflater.from(this).inflate(R.layout.activity_main,
        //   tabHost.getTabContentView(), true);
        //TextView loginTab = (TextView) LayoutInflater.from(this).inflate(R.layout.login_tab_bg, null);
        //loginTab.setText("登陆");
        tabHost.addTab(tabHost.newTabSpec("登陆").setIndicator("登陆")
                .setContent(R.id.login_form));
        TextView x = (TextView) tabHost.getTabWidget().getChildAt(0).findViewById(android.R.id.title);
        x.setTextSize(17);

        //TextView registerTab = (TextView) LayoutInflater.from(this).inflate(R.layout.login_tab_bg, null);
        //registerTab.setText("注册");
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

    public void onClickCancel(View view) {
        backToMainActivity(false, "Guest");
    }

    public void onClickLogin(View view) {

        mLoginRegUsernameView = (EditText) findViewById(R.id.login_username);
        mLoginRegPasswordView = (EditText) findViewById(R.id.login_password);
        mLoginRegisterFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
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
            connectServer(username, password, ActionType.LOGIN);
        }
    }
    private boolean isUsernameValid(String username) {
        //TODO: Replace this with your own logic
        return username.length() > 4;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return true;
    }
    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
   /* private boolean UserLoginRegisterTask(String username, String password, ActionType type) {

        connectServer(username, password, type);
        //showProgress(false);
        if (commentFromServer.equals("success")) {
            boolean isSuccess = true;
            backToMainActivity(isSuccess, username);
            return true;
        }
        return false;
    }*/


    public void onClickRegister(View view) {

        mLoginRegUsernameView = (EditText) findViewById(R.id.register_username);
        mLoginRegPasswordView = (EditText) findViewById(R.id.register_password);
        mLoginRegisterFormView = findViewById(R.id.register_form);
        mProgressView = findViewById(R.id.register_progress);
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
           connectServer(username, password, ActionType.REGISTER);
          //showProgress(false);
          //mLoginRegPasswordView.requestFocus();
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public void backToMainActivity(boolean isSuccess, String username) {
        //store Username
        //back to user profile pages
        //showProgress(false);
        Intent intent = new Intent(this, MainActivity.class);
        if (isSuccess) {
            SharedPreferences sharedPref = getSharedPreferences(GlobalParams.PREF_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor sharedata = sharedPref.edit();
            sharedata.putString("username", username);
            sharedata.putString("token", mtoken);
            intent.putExtra(MainActivity.INTENT_KEY_USER_NAME, username);
            sharedata.commit();
        }
        intent.putExtra(MainActivity.INTENT_KEY_LOGIN_RESULT, isSuccess);
        startActivity(intent);
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = 2000;
            //mLoginRegisterView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginRegisterFormView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginRegisterFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    // mLoginRegisterView.setVisibility(show ? View.GONE : View.VISIBLE);
                    mLoginRegisterFormView.setVisibility(show ? View.VISIBLE : View.GONE);

                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            // mLoginRegisterView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginRegisterFormView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    private void connectServer(final String username, String password, final ActionType type) {
        mtoken = "";
        Log.v("Lanying", "Connect to server request");
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String json = msg.getData().getString("responseBody");
                Log.v("Login_Register", json);
                Log.v("Login_Register", "username"+":" + username);
                try {
                    // showProgress(false)
                    // handler item from Json
                    Log.v("Login_Register", "username"+":" + username);
                    JSONObject item = new JSONObject(json);
                    String comment = item.getString("result");
                    if (comment.equals("success")) {
                        mtoken = item.getString("token");
                        Log.v("Login_Register", "token" + ": " + mtoken);
                        boolean isSuccess = true;
                        backToMainActivity(isSuccess, username);
                    }
                    else
                    {
                        Log.v("Login_Register",":" + "login/register error");
                        if(type == ActionType.REGISTER) {

                            mLoginRegUsernameView.setError(getString(R.string.error_existed_username));
                        }
                        else
                        {
                            mLoginRegUsernameView.setError(getString(R.string.error_username_or_password));
                        }
                        mLoginRegUsernameView.requestFocus();

                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();

                }
            }
        };
        HttpUtil httptd = new HttpUtil();

        if (type == ActionType.LOGIN) {
            //login in action
            httptd.submitAsyncHttpClientPostLogin(username, password, handler);
            Log.v("Login_Register", "Login  request");
        } else if (type == ActionType.REGISTER) {
            //register in action
            httptd.submitAsyncHttpClientPostRegisterUser(username, password, handler);
            Log.v("Login_Register", "Register request");
        }
    }
}



