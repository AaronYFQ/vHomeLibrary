package com.compass.loco.homelibrary;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONException;
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

    private static final int POLL_TIME = 5000;

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
    public static void startActionPoll(Context context, String token) {
        Intent intent = new Intent(context, MessageIntentService.class);
        intent.setAction(ACTION_POLL);
        intent.putExtra(USER_TOKEN, token);
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
                final String token = intent.getStringExtra(USER_TOKEN);
                handleActionPoll(token);
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
    private void handleActionPoll(String token) {
        // TODO: Handle action Poll
        while(true)
        {
            try {
                Thread.sleep(POLL_TIME);

                checkNewMessageOnServer(token);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //throw new UnsupportedOperationException("Not yet implemented");
    }

    private void checkNewMessageOnServer(String token)
    {
        SyncHttpClient client = new SyncHttpClient();
        String url = HttpUtil.CHECK_MESSAGE_URL + "?" + "token=" + token;

        client.get(url, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                String jsonString = new String(responseBody);
                JSONObject item = null;
                try {
                    item = new JSONObject(jsonString);
                    boolean hasNewMsg = item.getBoolean("hasNewMessage");
                    if(hasNewMsg)
                    {
                        sendNotification();
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

    private void sendNotification()
    {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("My notification")
                        .setContentText("Receive new messages!");

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(ShowMessagesActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        Intent intent = new Intent(this, ShowMessagesActivity.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(1, mBuilder.build());
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
