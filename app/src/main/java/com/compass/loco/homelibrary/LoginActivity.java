package com.compass.loco.homelibrary;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;

public class LoginActivity extends Activity {

    TabHost tabHost;
    // UI references.
    private EditText mLoginUsernameView;
    private EditText mLoginPasswordView;
    private EditText mRegisterUsernameView;
    private EditText mRegisterPasswordView;
    private UserLoginTask mAuthTask = null;
    private UserRegisterTask mRegisterTask = null;
    private View mProgressView;
    private View mLoginRegisterView;

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
        backToMainActivity(false,"Guest");
    }

    public void onClickLogin(View view) {

        mLoginUsernameView = (EditText) findViewById(R.id.login_username);
        mLoginPasswordView = (EditText) findViewById(R.id.login_password);
        mLoginRegisterView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        // Store values at the time of the login attempt.
        String username = mLoginUsernameView.getText().toString();
        String password = mLoginPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mLoginPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mLoginPasswordView;
            cancel = true;
        }
        // Check for a valid username.
        if (TextUtils.isEmpty(username)) {
            mLoginUsernameView.setError(getString(R.string.error_field_required));
            focusView = mLoginUsernameView;
            cancel = true;
        } else if (!isUsernameValid(username)) {
            mLoginUsernameView.setError(getString(R.string.error_invalid_username));
            focusView = mLoginUsernameView;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(username, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isUsernameValid(String username) {
        //TODO: Replace this with your own logic
        return username.length() > 4;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUsername;
        private final String mPassword;

        UserLoginTask(String username, String password) {
            mUsername = username;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            try {
                // Simulate network access.
                Thread.sleep(300);
                if (mUsername.equals("lanying") && mPassword.equals("123456")) {
                    return true;
                } else {
                    return false;
                }
            } catch (InterruptedException e) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);
            if (success) {
                backToMainActivity(success,mUsername);
                finish();
            } else {
                mLoginPasswordView.setError(getString(R.string.error_incorrect_password));
                mLoginPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            showProgress(false);
            mAuthTask = null;
            backToMainActivity(false,"Guest");
        }
    }

    public void onClickRegister(View view) {

        mRegisterUsernameView = (EditText) findViewById(R.id.register_username);
        mRegisterPasswordView = (EditText) findViewById(R.id.register_password);
        mLoginRegisterView = findViewById(R.id.register_form);
        mProgressView = findViewById(R.id.register_progress);
        // Store values at the time of the login attempt.
        String username = mRegisterUsernameView.getText().toString();
        String password = mRegisterPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mRegisterUsernameView.setError(getString(R.string.error_invalid_password));
            focusView = mRegisterPasswordView;
            cancel = true;
        }
        // Check for a valid username.
        if (TextUtils.isEmpty(username)) {
            mRegisterUsernameView.setError(getString(R.string.error_field_required));
            focusView = mRegisterUsernameView;
            cancel = true;
        } else if (!isUsernameValid(username)) {
            mRegisterUsernameView.setError(getString(R.string.error_invalid_username));
            focusView = mRegisterUsernameView;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mRegisterTask = new UserRegisterTask(username, password);
            mRegisterTask.execute((Void) null);
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUsername;
        private final String mPassword;

        UserRegisterTask(String username, String password) {
            mUsername = username;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            try {
                // Simulate network access.
                Thread.sleep(300);
                if (mUsername.equals("lanying") && mPassword.equals("123456")) {
                    return true;
                } else {
                    return false;
                }
            } catch (InterruptedException e) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mRegisterTask = null;
            showProgress(false);
            if (success) {
                RegisterSuccess(success);
                finish();
            } else {
                mRegisterPasswordView.setError(getString(R.string.error_invalid_password));
                mRegisterPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mRegisterTask = null;
            showProgress(false);
            backToMainActivity(false,"Guest");
        }

        //new function : handlle register success
        protected void RegisterSuccess(boolean isSuccess) {
            //store Username
            backToMainActivity(isSuccess, mUsername);
        }
    }

    public void backToMainActivity(boolean isSuccess, String username) {
        //store Username
        //back to user profile pages
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.INTENT_KEY_USER_NAME, username);
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
            int shortAnimTime = getResources().getInteger(android.R.integer.config_longAnimTime);
            //mLoginRegisterView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginRegisterView.setVisibility(show ?  View.VISIBLE:View.GONE );
            mLoginRegisterView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    // mLoginRegisterView.setVisibility(show ? View.GONE : View.VISIBLE);
                    mLoginRegisterView.setVisibility(show ?  View.VISIBLE:View.GONE );

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
            mLoginRegisterView.setVisibility(show ?  View.VISIBLE:View.GONE );
        }
    }
}



