package com.compass.loco.homelibrary;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ShowMessagesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private DBAdapter db;
    private MessageCursorAdapter cursorAdapter;
    private ListView messageList;
    private Button clearButton;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View view;
    private TextView countTxView;
    private int numOfNewMsg = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_show_messages, container, false);

        Log.v("...............onCreate", "enter................");
        db = new DBAdapter(view.getContext());
        db.open();

        countTxView = (TextView)view.findViewById(R.id.num_view);
        countTxView.setVisibility(View.INVISIBLE);

        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipeViewMessages);
        swipeRefreshLayout.setOnRefreshListener(this);

        messageList=(ListView)view.findViewById(R.id.listViewMessages);
        clearButton = (Button)view.findViewById(R.id.clear_btn);

        cursorAdapter = new MessageCursorAdapter(view.getContext(), null, 0);
        messageList.setAdapter(cursorAdapter);
        messageList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long arg3) {
                Cursor cur = (Cursor) cursorAdapter.getItem(position);
                cur.moveToPosition(position);
                int id = cur.getInt(cur.getColumnIndexOrThrow("_id"));

/*                Toast.makeText(getContext(),
                        "Clicked item on position " + position + ", DB item ID " + id,
                        Toast.LENGTH_SHORT).show();*/

                Boolean bUpdate = false;
                if(cur.getInt(cur.getColumnIndexOrThrow("new")) > 0) {
                    db.resetState(id);
                    Log.v("..............Message", "Clicked item " + position + " " + id);
                    bUpdate = true;
                }

                String action = cur.getString(cur.getColumnIndexOrThrow("action"));
                handleAction(action, cur);

                if(bUpdate)
                {
                    DisplayMessages();
                }
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref = view.getContext().getSharedPreferences(GlobalParams.PREF_NAME, Context.MODE_PRIVATE);
                String username = sharedPref.getString("username", null);
                Boolean result = db.cleanMessage(username);
                Log.v("Clean message result: ", result.toString());
                DisplayMessages();
            }
        });

        SharedPreferences sharedPref = view.getContext().getSharedPreferences(GlobalParams.PREF_NAME, Context.MODE_PRIVATE);
        String token = sharedPref.getString("token", null);
        if(token != null) {
            DisplayMessages();
            getNewMessages(token);
        }
        else
        {
            Toast.makeText(getContext(),
                    "Please login first",
                    Toast.LENGTH_SHORT).show();
        }

        //MessageIntentService.startActionPoll(getContext());
        return view;
    }

    private void handleAction(String action, Cursor cursor)
    {
        if(action.equals("borrow") || action.equals("accept"))
        {
            Intent intent = new Intent(view.getContext(), ManageBookActivity.class);
            intent.putExtra("bookname", cursor.getString(cursor.getColumnIndexOrThrow("book")));
            intent.putExtra("shopname", cursor.getString(cursor.getColumnIndexOrThrow("shop")));
            intent.putExtra("user", cursor.getString(cursor.getColumnIndexOrThrow("owner")));
            if(action.equals("borrow")) {
                intent.putExtra("borrower", cursor.getString(cursor.getColumnIndexOrThrow("borrower")));
                intent.putExtra("request", "borrow");
            }

            startActivity(intent);
        }
    }

    private void getNewMessages(String token)
    {
        final Handler handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                String json = msg.getData().getString("responseBody");
                Log.v("handleMessage", json);
                try {
                    // handler item from Json
                    JSONObject jsonObj = new JSONObject(json);
                    JSONArray jsonArray = jsonObj.getJSONArray("messages");

                    Log.v("number of Messages: ", new Integer(jsonArray.length()).toString());

                    for (int i = 0; i < jsonArray.length(); ++i) {
                        jsonObj = jsonArray.getJSONObject(i);

                        //Log.v("Message: ", jsonObj.toString());

                        MessageInfo msgInfo = new MessageInfo(
                                jsonObj.getString("book"),
                                jsonObj.getString("shop"),
                                jsonObj.getString("owner"),
                                jsonObj.getString("borrower"),
                                jsonObj.getString("action"),
                                jsonObj.getString("time"));

                        SharedPreferences sharedPref = view.getContext().getSharedPreferences(GlobalParams.PREF_NAME, Context.MODE_PRIVATE);
                        String username = sharedPref.getString("username", null);
                        if(db.insertMessage(msgInfo, username) < 0)
                        {
                            Log.v("Insert error", "Insert error");
                        }
                    }

                    DisplayMessages();
                    swipeRefreshLayout.setRefreshing(false);

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };

        HttpUtil httptd = new HttpUtil();
        httptd.submitAsyncHttpClientGetMessage(token, handler);
    }

    private void DisplayMessages()
    {
        //MessageInfo msgInfo = new MessageInfo("Book" + count, "Shop", "Owner", "Borrower", "Action", "Time");
        //db.insertMessage(msgInfo);

        SharedPreferences sharedPref = view.getContext().getSharedPreferences(GlobalParams.PREF_NAME, Context.MODE_PRIVATE);
        String username = sharedPref.getString("username", null);

        if(username != null) {
            Cursor cursor = db.getAllMessages(username);
            cursor.moveToFirst();
            Log.v(".........message", "display item " + cursor.getCount());
            if(cursor.getCount() > 0) {
                numOfNewMsg = getNewMessageNumber(cursor);
                cursor.moveToFirst();
                if(numOfNewMsg > 0)
                {
                    countTxView.setVisibility(View.VISIBLE);
                    countTxView.setText(Integer.toString(numOfNewMsg));
                }
                else
                {
                    countTxView.setVisibility(View.INVISIBLE);
                }
            }
            else
            {
                numOfNewMsg = 0;
                countTxView.setVisibility(View.INVISIBLE);

            }

            cursorAdapter.changeCursor(cursor);
        }
    }

    private int getNewMessageNumber(Cursor cursor)
    {
        int num = 0;
        int count = cursor.getCount();
        for(int i = 0; i<cursor.getCount(); i++)
        {
            if(cursor.getInt(cursor.getColumnIndexOrThrow("new")) > 0)
            {
                ++num;
            }
            cursor.moveToNext();
        }

        return num;
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        SharedPreferences sharedPref = view.getContext().getSharedPreferences(GlobalParams.PREF_NAME, Context.MODE_PRIVATE);
        String token = sharedPref.getString("token", null);
        if(token != null) {
            getNewMessages(token);
        }
        else {
            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(getContext(),
                    "Please login first",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
