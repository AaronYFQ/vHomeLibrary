package com.compass.loco.homelibrary;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.compass.loco.homelibrary.chatting.ConversationListFragment;
import com.compass.loco.homelibrary.chatting.utils.DialogCreator;

import cn.jpush.im.android.api.event.UserLogoutEvent;


public class MainActivity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "com.compass.loco.homelibrary.MainActivity.EXTRA_MESSAGE";
    public final static String INTENT_KEY_CITY_NAME = MainActivity.class.getName() + ".CITY_NAME";
    public final static String INTENT_KEY_USER_NAME = MainActivity.class.getName() + ".USER_NAME";
    public final static String INTENT_KEY_LOGIN_RESULT = MainActivity.class.getName() + ".LOGIN_RESULT";
    public final static String INTENT_KEY_NOTIFICATION = MainActivity.class.getName() + ".NOTIFY_RESULT";

    public static String NEW_MESSAGE_ACTION = "com.compass.loco.homelibrary.NEW_MESSAGE_ACTION";

    private final HomeFragment mHomeFragment = new HomeFragment();
    private final ShowMessagesFragment mMessageFragment = new ShowMessagesFragment();
    private final MeFragment mMeFragment = new MeFragment();

    private  final  ConversationListFragment mCovFragment = new ConversationListFragment();;

    private BadgeView badge;
    private Dialog mDialog; //for Jmessage

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_my);

       

        // get application private shared preference



        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout

        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
/*            if (savedInstanceState != null) {
                return;
            }*/

            if (getIntent() != null && getIntent().getExtras() != null && getIntent().getExtras().containsKey(INTENT_KEY_LOGIN_RESULT)) {
                mMeFragment.setArguments(getIntent().getExtras());
                if(!mMeFragment.isAdded()) {
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.fragment_container, mHomeFragment)
                            .add(R.id.fragment_container, mMessageFragment)
                            .add(R.id.fragment_container, mCovFragment)
                            .add(R.id.fragment_container, mMeFragment).commit();
                }

                getSupportFragmentManager().beginTransaction()
                        .hide(mMessageFragment)
                        .hide(mHomeFragment)
                        .hide(mCovFragment)
                        .show(mMeFragment).commit();

                ImageButton meBtn = (ImageButton) findViewById(R.id.menu_4);
                Drawable meBtnGreen = getResources().getDrawable(R.drawable.mainmenu_me);
                meBtn.setBackgroundDrawable(meBtnGreen);
            }
            else if(getIntent() != null && getIntent().getExtras() != null && getIntent().getExtras().containsKey(INTENT_KEY_NOTIFICATION)){
/*                disableAllMainMenuBtn();
                ImageButton messageBtn = (ImageButton) findViewById(R.id.menu_2);
                Drawable messageBtnGreen = getResources().getDrawable(R.drawable.mainmenu_message);
                messageBtn.setBackgroundDrawable(messageBtnGreen);*/

                mMessageFragment.setArguments(getIntent().getExtras());
                if(!mMessageFragment.isAdded()) {
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.fragment_container, mMeFragment)
                            .add(R.id.fragment_container, mHomeFragment)
                            .add(R.id.fragment_container, mCovFragment)
                            .add(R.id.fragment_container, mMessageFragment).commit();
                }

                getSupportFragmentManager().beginTransaction()
                        .hide(mMeFragment)
                        .hide(mHomeFragment)
                        .hide(mCovFragment)
                        .show(mMessageFragment).commit();

                ImageButton messageBtn = (ImageButton) findViewById(R.id.menu_2);
                Drawable messageBtnGreen = getResources().getDrawable(R.drawable.mainmenu_message);
                messageBtn.setBackgroundDrawable(messageBtnGreen);
            }
            else {
                mHomeFragment.setArguments(getIntent().getExtras());

                if(!mHomeFragment.isAdded()) {
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.fragment_container, mMeFragment)
                            .add(R.id.fragment_container, mMessageFragment)
                            .add(R.id.fragment_container, mCovFragment)
                            .add(R.id.fragment_container, mHomeFragment).commit();
                }

                getSupportFragmentManager().beginTransaction()
                        .hide(mMeFragment)
                        .hide(mMessageFragment)
                        .hide(mCovFragment)
                        .show(mHomeFragment).commit();

                ImageButton homeBtn = (ImageButton) findViewById(R.id.menu_1);
                Drawable homeBtnGreen = getResources().getDrawable(R.drawable.mainmenu_home);
                homeBtn.setBackgroundDrawable(homeBtnGreen);
            }
        }

        badge = new BadgeView(this);
        badge.setTargetView(findViewById(R.id.menu_2));
        badge.setHideOnNull(true);
        badge.setBadgeCount(0);

        //badge.setVisibility(View.INVISIBLE);

        //registerBroadcastReceiver();

        //start bg service
        //MessageIntentService.startActionPoll(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
//        Intent intent = getIntent();
//        String selectedCity = intent.getStringExtra("selectedCity");
//        mHomeFragment.setSelectedCity(selectedCity);
//        Log.i("mainActivity", "selectedCity :"+selectedCity);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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


        getSupportFragmentManager().beginTransaction()
                .hide(mMeFragment)
                .hide(mMessageFragment)
                .hide(mCovFragment)
                .show(mHomeFragment).commit();


    }

    public void onClickMessageBtn(View view) {
        if (mMessageFragment.isVisible()) {
            return;
        }


        getSupportFragmentManager().beginTransaction()
                .hide(mMeFragment)
                .hide(mHomeFragment)
                .hide(mCovFragment)
                .show(mMessageFragment).commit();


    }

    public void onClickCovBtn(View view) {

       if (mCovFragment.isVisible()) {
            return;
        }


        getSupportFragmentManager().beginTransaction()
                .hide(mMeFragment)
                .hide(mHomeFragment)
                .hide(mMessageFragment)
                .show(mCovFragment).commit();


    }

    public void showCovList() {

        if (mCovFragment.isVisible()) {
            return;
        }


        getSupportFragmentManager().beginTransaction()
                .hide(mMeFragment)
                .hide(mHomeFragment)
                .hide(mMessageFragment)
                .show(mCovFragment).commit();


    }

    public void onClickMeBtn(View view) {
        if (mMeFragment.isVisible()) {
            return;
        }


        getSupportFragmentManager().beginTransaction()
                .hide(mHomeFragment)
                .hide(mMessageFragment)
                .hide(mCovFragment)
                .show(mMeFragment).commit();

    }

    public void setBadgeNumber(int num)
    {
            badge.setBadgeCount(num);
    }

/*    private void registerBroadcastReceiver(){
        NewMessageReceiver receiver = new NewMessageReceiver();
        IntentFilter filter = new IntentFilter(NEW_MESSAGE_ACTION);
        registerReceiver(receiver, filter);
    }

    private class NewMessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if(MainActivity.NEW_MESSAGE_ACTION.equals(action)){
                Bundle bundle = intent.getExtras();
                int numOfMsg = bundle.getInt("message_number");
                numOfMsg += badge.getBadgeCount();
                setBadgeNumber(numOfMsg);
            }
        }
    }*/

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


    public void onEventMainThread(UserLogoutEvent event) {
        String title = getApplicationContext().getString(R.string.jmui_user_logout_dialog_title);
        String msg = getApplicationContext().getString(R.string.jmui_user_logout_dialog_message);
        mDialog = DialogCreator.createBaseCustomDialog(getApplicationContext(), title, msg, onClickListener);
        mDialog.getWindow().setLayout((int) (0.8 * 200), WindowManager.LayoutParams.WRAP_CONTENT);
        mDialog.show();
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mDialog.dismiss();
        }
    };

}
