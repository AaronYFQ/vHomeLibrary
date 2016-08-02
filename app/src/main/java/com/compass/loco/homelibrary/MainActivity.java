package com.compass.loco.homelibrary;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;


public class MainActivity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "com.compass.loco.homelibrary.MainActivity.EXTRA_MESSAGE";
    public final static String INTENT_KEY_CITY_NAME = MainActivity.class.getName() + ".CITY_NAME";
    public final static String INTENT_KEY_USER_NAME = MainActivity.class.getName() + ".USER_NAME";
    public final static String INTENT_KEY_LOGIN_RESULT = MainActivity.class.getName() + ".LOGIN_RESULT";
    public final static String INTENT_KEY_NOTIFICATION = MainActivity.class.getName() + ".NOTIFY_RESULT";

    private final HomeFragment mHomeFragment = new HomeFragment();
    private final ShowMessagesFragment mMessageFragment = new ShowMessagesFragment();
    private final MeFragment mMeFragment = new MeFragment();

    final  GlobalParams mGlobal = new GlobalParams();
    /*public MainActivity() {
        mGlobal.initSharedPreferences(getSharedPreferences("com.compass.loco.homelibrary.data", Context.MODE_PRIVATE));
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            if (getIntent() != null && getIntent().getExtras() != null && getIntent().getExtras().containsKey(INTENT_KEY_LOGIN_RESULT)) {
                mMeFragment.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, mMeFragment).commit();
            }
            else if(getIntent() != null && getIntent().getExtras() != null && getIntent().getExtras().containsKey(INTENT_KEY_NOTIFICATION)){
                disableAllMainMenuBtn();
                ImageButton messageBtn = (ImageButton) findViewById(R.id.menu_2);
                Drawable messageBtnGreen = getResources().getDrawable(R.drawable.mainmenu_message);
                messageBtn.setBackgroundDrawable(messageBtnGreen);

                mMessageFragment.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, mMessageFragment).commit();
            }
            else {
                mHomeFragment.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, mHomeFragment).commit();
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Called when the user clicks the Send button
     */
    public void sendMessage(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, CreateLibActivity.class);
//        EditText editText = (EditText) findViewById(R.id.name1);

        SharedPreferences sharedPref = getSharedPreferences("com.compass.loco.homelibrary.data", Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedata = sharedPref.edit();
        sharedata.putString("item", "hello");
        sharedata.commit();
        startActivity(intent);
    }

    public void onClickHomeBtn(View view) {
        if (mHomeFragment.isVisible()) {
            return;
        }

        disableAllMainMenuBtn();
        ImageButton homeBtn = (ImageButton) findViewById(R.id.menu_1);
        Drawable homeBtnGreen = getResources().getDrawable(R.drawable.mainmenu_home);
        homeBtn.setBackgroundDrawable(homeBtnGreen);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_container, mHomeFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    public void onClickMessageBtn(View view) {
        if (mMessageFragment.isVisible()) {
            return;
        }

        disableAllMainMenuBtn();
        ImageButton messageBtn = (ImageButton) findViewById(R.id.menu_2);
        Drawable messageBtnGreen = getResources().getDrawable(R.drawable.mainmenu_message);
        messageBtn.setBackgroundDrawable(messageBtnGreen);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_container, mMessageFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    public void onClickMeBtn(View view) {
        if (mMeFragment.isVisible()) {
            return;
        }

        disableAllMainMenuBtn();
        ImageButton meBtn = (ImageButton) findViewById(R.id.menu_4);
        Drawable meBtnGray = getResources().getDrawable(R.drawable.mainmenu_me);
        meBtn.setBackgroundDrawable(meBtnGray);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_container, mMeFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    public void disableAllMainMenuBtn() {
        ImageButton homeBtn = (ImageButton) findViewById(R.id.menu_1);
        Drawable homeBtnGray = getResources().getDrawable(R.drawable.mainmenu_home_gray);
        homeBtn.setBackgroundDrawable(homeBtnGray);

        ImageButton messageBtn = (ImageButton) findViewById(R.id.menu_2);
        Drawable messageBtnGray = getResources().getDrawable(R.drawable.mainmenu_message_gray);
        messageBtn.setBackgroundDrawable(messageBtnGray);

        ImageButton meBtn = (ImageButton) findViewById(R.id.menu_4);
        Drawable meBtnGray = getResources().getDrawable(R.drawable.mainmenu_me_gray);
        meBtn.setBackgroundDrawable(meBtnGray);
    }
}
