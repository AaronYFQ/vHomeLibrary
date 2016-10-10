package com.compass.loco.homelibrary.chatting;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.compass.loco.homelibrary.GlobalParams;
import com.compass.loco.homelibrary.R;
import com.compass.loco.homelibrary.chatting.utils.HandleResponseCode;
import com.compass.loco.homelibrary.controller.ConversationListController;
import com.compass.loco.homelibrary.controller.MenuItemController;
import com.compass.loco.homelibrary.entity.Event;
import com.compass.loco.homelibrary.view.ConversationListView;
import com.compass.loco.homelibrary.view.MenuItemView;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.enums.ConversationType;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.GroupInfo;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;
import de.greenrobot.event.EventBus;

/*
 * 会话列表界面
 */
public class ConversationListFragment extends BaseFragment {

    private static String TAG = ConversationListFragment.class.getSimpleName();
    private View mRootView;
    private ConversationListView mConvListView;
    private ConversationListController mConvListController;
    private PopupWindow mMenuPopWindow;
    private View mMenuView;
    private MenuItemView mMenuItemView;
    private MenuItemController mMenuController;
    private NetworkReceiver mReceiver;
    private Activity mContext;
    private BackgroundHandler mBackgroundHandler;
    private HandlerThread mThread;
    private static final int REFRESH_CONVERSATION_LIST = 0x3000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onCreate !");
        mContext = this.getActivity();
        EventBus.getDefault().register(this);
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        mRootView = layoutInflater.inflate(R.layout.fragment_conv_list,
                (ViewGroup) getActivity().findViewById(R.id.main_view), false);


        mConvListView = new ConversationListView(mRootView, this.getActivity());
        mConvListView.initModule();

        mThread = new HandlerThread("Work on MainActivity");
        mThread.start();
        mBackgroundHandler = new BackgroundHandler(mThread.getLooper());
        mMenuView = getActivity().getLayoutInflater().inflate(R.layout.jmui_drop_down_menu, null);
        mConvListController = new ConversationListController(mConvListView, this, mWidth);
        mConvListView.setListener(mConvListController);
        mConvListView.setItemListeners(mConvListController);
        mConvListView.setLongClickListener(mConvListController);
        mMenuPopWindow = new PopupWindow(mMenuView, WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT, true);
        mMenuItemView = new MenuItemView(mMenuView);
        mMenuItemView.initModule();
        // mMenuController = new MenuItemController(mMenuItemView, this, mConvListController, mWidth);
        //mMenuItemView.setListeners(mMenuController);
        ConnectivityManager manager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeInfo = manager.getActiveNetworkInfo();
        if (null == activeInfo) {
            mConvListView.showHeaderView();
        } else {
            mConvListView.dismissHeaderView();
        }
        initReceiver();
        JMessageLogin();

    }

    private void initReceiver() {
        mReceiver = new NetworkReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction(GlobalParams.LOGIN_ACTION);
        filter.addAction(GlobalParams.LOGOUT_ACTION);
        mContext.registerReceiver(mReceiver, filter);
    }


    private void initController() {

        Log.v(TAG, "initController !");
        mConvListController.initConvListAdapter();
        mMenuController = new MenuItemController(mMenuItemView, this, mConvListController, mWidth);
        mMenuItemView.setListeners(mMenuController);

    }


    //监听网络状态的广播 &login and logout
    private class NetworkReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //for network state change
            if (intent != null && action.equals("android.net.conn.CONNECTIVITY_CHANGE")) {
                ConnectivityManager manager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeInfo = manager.getActiveNetworkInfo();
                if (null == activeInfo) {
                    mConvListView.showHeaderView();
                } else {
                    mConvListView.dismissHeaderView();
                }
            }
            // for login
            if (intent != null && GlobalParams.LOGIN_ACTION.equals(action)) {
                Log.v(TAG, "ConversationListFragment: Handle login in action");
                mConvListController.updateConversationsList();

            }
            // for logout
            if (intent != null && GlobalParams.LOGOUT_ACTION.equals(action)) {
                Log.v(TAG, "ConversationListFragment: Handle login out action");
                mConvListController.clearConversationsList();
            }
        }

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.v(TAG,"onActivityCreated");
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);

    }

    public void JMessageLogin() {

        if (JMessageClient.getMyInfo() == null) {
            SharedPreferences sharedPref = getContext().getSharedPreferences(GlobalParams.PREF_NAME, Context.MODE_PRIVATE);
            String username = sharedPref.getString("username", null);
            if((username != null) && (username != "")) {
                Log.v(TAG, "JMessageLogin: request login");
                JMessageClient.login(username, GlobalParams.JCHAT_USER_PASSWORD, new BasicCallback() {
                    @Override
                    public void gotResult(int status, String desc) {
                        if (status == 0) {
                            Log.v(TAG, "JMessageLogin: login success");
                            initController();
                        } else {
                            Log.v(TAG, "JMessageLogin: login failure");
                            Toast.makeText(getContext(),
                                    "连接JmessageServer 失败.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } else {
            Log.v(TAG, "User logined, init Controller directly");
            initController();
        }
    }

    //显示下拉菜单
    public void showMenuPopWindow() {
        mMenuPopWindow.setTouchable(true);
        mMenuPopWindow.setOutsideTouchable(true);
        mMenuPopWindow.setBackgroundDrawable(new BitmapDrawable(getResources(),
                (Bitmap) null));
        if (mMenuPopWindow.isShowing()) {
            mMenuPopWindow.dismiss();
        } else
            mMenuPopWindow.showAsDropDown(mRootView.findViewById(R.id.create_group_btn), -10, -5);
    }

    /**
     * 在会话列表中接收消息
     *
     * @param event 消息事件
     */
    public void onEvent(MessageEvent event) {
        Message msg = event.getMessage();
        Log.d(TAG, "收到消息：msg = " + msg.toString());
        ConversationType convType = msg.getTargetType();
        if (convType == ConversationType.group) {
            long groupID = ((GroupInfo) msg.getTargetInfo()).getGroupID();
            Conversation conv = JMessageClient.getGroupConversation(groupID);
            if (conv != null && mConvListController != null) {
                mBackgroundHandler.sendMessage(mBackgroundHandler.obtainMessage(REFRESH_CONVERSATION_LIST,
                        conv));
            }
        } else {
            final UserInfo userInfo = (UserInfo) msg.getTargetInfo();
            final String targetID = userInfo.getUserName();
            final Conversation conv = JMessageClient.getSingleConversation(targetID, userInfo.getAppKey());
            if (conv != null && mConvListController != null) {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //如果设置了头像
                        if (!TextUtils.isEmpty(userInfo.getAvatar())) {
                            //如果本地不存在头像
                            userInfo.getAvatarBitmap(new GetAvatarBitmapCallback() {
                                @Override
                                public void gotResult(int status, String desc, Bitmap bitmap) {
                                    if (status == 0) {
                                        mConvListController.getAdapter().notifyDataSetChanged();
                                    } else {
                                        HandleResponseCode.onHandle(mContext, status, false);
                                    }
                                }
                            });
                        }
                    }
                });
                mBackgroundHandler.sendMessage(mBackgroundHandler.obtainMessage(REFRESH_CONVERSATION_LIST,
                        conv));
            }
        }
    }

    private class BackgroundHandler extends Handler {
        public BackgroundHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REFRESH_CONVERSATION_LIST:
                    Conversation conv = (Conversation) msg.obj;
                    mConvListController.getAdapter().setToTop(conv);
                    break;
            }
        }
    }

    /**
     * 收到创建单聊的消息
     *
     * @param event 可以从event中得到targetID
     */
    public void onEventMainThread(Event.StringEvent event) {
        Log.d(TAG, "StringEvent execute");
        String targetId = event.getTargetId();
        String appKey = event.getAppKey();
        Conversation conv = JMessageClient.getSingleConversation(targetId, appKey);
        if (conv != null) {
            mConvListController.getAdapter().addNewConversation(conv);
        }
    }

    /**
     * 收到创建或者删除群聊的消息
     *
     * @param event 从event中得到groupID以及flag
     */
    public void onEventMainThread(Event.LongEvent event) {
        long groupId = event.getGroupId();
        Conversation conv = JMessageClient.getGroupConversation(groupId);
        if (conv != null && event.getFlag()) {
            mConvListController.getAdapter().addNewConversation(conv);
        } else {
            mConvListController.getAdapter().deleteConversation(groupId);
        }
    }

    /**
     * 收到保存为草稿事件
     *
     * @param event 从event中得到Conversation Id及草稿内容
     */
    public void onEventMainThread(Event.DraftEvent event) {
        String draft = event.getDraft();
        String targetId = event.getTargetId();
        String targetAppKey = event.getTargetAppKey();
        Conversation conv;
        if (targetId != null) {
            conv = JMessageClient.getSingleConversation(targetId, targetAppKey);
        } else {
            long groupId = event.getGroupId();
            conv = JMessageClient.getGroupConversation(groupId);
        }
        //如果草稿内容不为空，保存，并且置顶该会话
        if (!TextUtils.isEmpty(draft)) {
            mConvListController.getAdapter().putDraftToMap(conv.getId(), draft);
            mConvListController.getAdapter().setToTop(conv);
            //否则删除
        } else {
            mConvListController.getAdapter().delDraftFromMap(conv.getId());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v(TAG, "onCreateView !");
        // TODO Auto-generated method stub
        //mRootView = inflater.inflate(R.layout.fragment_conv_list, container, false);
        ViewGroup p = (ViewGroup) mRootView.getParent();
        if (p != null) {
            p.removeAllViewsInLayout();
        }
        return mRootView;
    }

    @Override
    public void onResume() {
        Log.v(TAG, "onResume !");
        dismissPopWindow();
        super.onResume();

    }

    public void dismissPopWindow() {
        if (mMenuPopWindow.isShowing()) {
            mMenuPopWindow.dismiss();
        }
    }

    @Override
    public void onStart()
    {
        Log.v(TAG, "onStart !");
        super.onStart();
        if (JMessageClient.getMyInfo() != null) {
        mConvListController.updateConversationsList();
    }


    }

    @Override
    public void onPause()
    {
        super.onPause();
        Log.v(TAG, "onPause !");

    }

    @Override
    public void onStop()
    {
        super.onStop();
        Log.v(TAG, "onStop !");

    }

    @Override
    public void onDestroy() {
        Log.v(TAG, "onDestroy !");
        EventBus.getDefault().unregister(this);
        mContext.unregisterReceiver(mReceiver);
        mBackgroundHandler.removeCallbacksAndMessages(null);
        mThread.getLooper().quit();
        super.onDestroy();
    }


    /*public void StartCreateGroupActivity() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), CreateGroupActivity.class);
        startActivity(intent);
    }*/

    public void sortConvList() {
        if (mConvListController != null) {
            mConvListController.getAdapter().sortConvList();
        }
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        Log.v(TAG, "onHiddenChanged !");
        super.onHiddenChanged(hidden);
        FragmentActivity activity = getActivity();
        if (hidden) {
            if (activity != null) {
                ImageButton messageBtn = (ImageButton) getActivity().findViewById(R.id.menu_3);
                Drawable messageBtnGray = getResources().getDrawable(R.drawable.mainmenu_chat_gray);
                messageBtn.setBackgroundDrawable(messageBtnGray);
            }
        } else {
            if (activity != null) {
                ImageButton messageBtn = (ImageButton) getActivity().findViewById(R.id.menu_3);
                Drawable messageBtnGreen = getResources().getDrawable(R.drawable.mainmenu_chat_green);
                messageBtn.setBackgroundDrawable(messageBtnGreen);
            }

        }
    }



   /* @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        SharedPreferences sharedPref = view.getContext().getSharedPreferences(GlobalParams.PREF_NAME, Context.MODE_PRIVATE);
        String token = sharedPref.getString("token", null);
        if(!token.isEmpty()) {
            getNewMessages(token);
        }
        else {
            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(getContext(),
                    "请先登录.",
                    Toast.LENGTH_SHORT).show();
        }
    }*/
}
