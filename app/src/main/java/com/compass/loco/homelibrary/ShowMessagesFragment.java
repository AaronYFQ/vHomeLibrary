package com.compass.loco.homelibrary;

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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ShowMessagesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_messages, container, false);

        Log.v("...............onCreate", "enter................");
        db = new DBAdapter(view.getContext());
        db.open();

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
                if(cur.getInt(cur.getColumnIndexOrThrow("new")) > 0) {
                    db.resetState(id);
                    Log.v("..............Message", "Clicked item " + position + " " + id);
                    Toast.makeText(getContext(),
                            "Clicked item on position " + position + ", DB item ID " + id,
                            Toast.LENGTH_SHORT).show();
                    DisplayMessages();
                }
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.cleanMessage();
                DisplayMessages();
            }
        });

        getNewMessages();
        DisplayMessages();
        MessageIntentService.startActionPoll(getContext(), "Test");
        return view;
    }

    private DBAdapter db;
    private MessageCursorAdapter cursorAdapter;
    private ListView messageList;
    private Button clearButton;
    private SwipeRefreshLayout swipeRefreshLayout;

    public void getNewMessages()
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

                    if(jsonArray.length() == 0)
                        return;

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

                        if(db.insertMessage(msgInfo) < 0)
                        {
                            Log.v("Insert error", "Insert error");
                        }
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };

        HttpUtil httptd = new HttpUtil();
        httptd.submitAsyncHttpClientGetMessage("zhong", handler);
    }

    public void DisplayMessages()
    {
        //MessageInfo msgInfo = new MessageInfo("Book" + count, "Shop", "Owner", "Borrower", "Action", "Time");
        //db.insertMessage(msgInfo);

        Cursor cursor = db.getAllMessages();
        cursor.moveToFirst();
        Log.v(".........message", "display item " + cursor.getCount());
        cursorAdapter.swapCursor(cursor);
        cursorAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        getNewMessages();
        DisplayMessages();
        swipeRefreshLayout.setRefreshing(false);
    }
}
