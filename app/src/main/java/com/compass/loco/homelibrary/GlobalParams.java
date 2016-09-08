package com.compass.loco.homelibrary;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by elanywa on 7/29/2016.
 */
public class GlobalParams {
   public final static String PREF_NAME = "com.compass.loco.homelibrary";
   public final static String SHARED_KEY_ADVERTISEMENT = "advertisement";

   public final static String JCHAT_APP_KEY = "77f80ebef5571a87bc17f616";
   public final static String JCHAT_USER_PASSWORD = "123456";

   public static final String JCHAT_CONFIGS = "JChat_configs";
   public static final String TARGET_APP_KEY = "targetAppKey";
   public static final String TARGET_ID = "targetId";
   public static final String NAME = "name";
   public static final String NICKNAME = "nickname";
   public static final String GROUP_ID = "groupId";
   public static final String GROUP_NAME = "groupName";
   public static final String STATUS = "status";
   public static final String POSITION = "position";
   public static final String MsgIDs = "msgIDs";
   public static final String DRAFT = "draft";
   public static final String DELETE_MODE = "deleteMode";
   public static final String MEMBERS_COUNT = "membersCount";
   public static String PICTURE_DIR = "sdcard/vhomeJChat/pictures/";


   public static String LOGIN_ACTION = "com.compass.loco.homelibrary.LOGIN_ACTION";
   public static String LOGOUT_ACTION = "com.compass.loco.homelibrary.LOGOUT_ACTION";
}
