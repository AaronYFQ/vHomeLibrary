package com.compass.loco.homelibrary;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONObject;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class MessageIntentService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_POLL = "com.compass.loco.homelibrary.action.poll";

    // TODO: Rename parameters
    private static final String USER_TOKEN = "com.compass.loco.homelibrary.extra.TOKEN";

    private static final int POLL_TIME = 10000;

    public MessageIntentService() {
        super("MessageIntentService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionPoll(Context context) {
        Intent intent = new Intent(context, MessageIntentService.class);
        intent.setAction(ACTION_POLL);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
/*    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, MessageIntentService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }*/

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_POLL.equals(action)) {
                handleActionPoll();
            }
            /*else if (ACTION_BAZ.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionBaz(param1, param2);
            }*/
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionPoll() {
        // TODO: Handle action Poll
        while(true)
        {
            try {
                Thread.sleep(POLL_TIME);
                SharedPreferences sharedPref = getSharedPreferences(GlobalParams.PREF_NAME, Context.MODE_PRIVATE);
                String token = sharedPref.getString("token", "");
                if(!token.isEmpty()) {
                    Log.v("...check message token", token);
                    checkNewMessageOnServer(token);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //throw new UnsupportedOperationException("Not yet implemented");
    }

    private void checkNewMessageOnServer(String token)
    {
        SyncHttpClient client = new SyncHttpClient();
        String url = HttpUtil.GET_MESSAGE_URL + "?" + "token=" + token;
        //Log.v(".....get Message", "token: " + token);

        client.get(url, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                String jsonString = new String(responseBody);
                JSONObject item = null;
                Log.v("Receive message ", "result");
                try {
                    item = new JSONObject(jsonString);
                    int numOfNewMsg = item.getInt("count");
                    if(numOfNewMsg > 0)
                    {
                        Log.v("Receive message ", Integer.toString(numOfNewMsg));
                        //sendBroadcastToMainActivity(numOfNewMsg);
                        sendBroadcastToMessageFrag(jsonString);
                        sendNotification(numOfNewMsg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private void sendNotification(int numOfMessage)
    {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setTicker("v书房新消息")
                        .setSmallIcon(R.drawable.app_icon)
                        .setContentTitle("v书房")
                        .setContentText("收到" + numOfMessage +  "条新消息!")
                        .setDefaults(NotificationCompat.DEFAULT_ALL);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.INTENT_KEY_NOTIFICATION, numOfMessage);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(1, mBuilder.build());
    }

    private void sendBroadcastToMainActivity(int msgNum){
        Bundle bundle = new Bundle();
        bundle.putInt("message_number", msgNum);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        intent.setAction(ShowMessagesFragment.NEW_MESSAGE_ACTION);

        sendBroadcast(intent);
    }

    private void sendBroadcastToMessageFrag(String msgBody){
        Bundle bundle = new Bundle();
        bundle.putString("message_body", msgBody);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        intent.setAction(MainActivity.NEW_MESSAGE_ACTION);

        sendBroadcast(intent);
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
/*    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }*/
}
