package com.compass.loco.homelibrary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by ehoixie on 7/27/2016.
 */
public class DBAdapter {
    static final String KEY_ID = "_id";
    static final String KEY_NEW = "new";
    static final String KEY_USER = "user";
    static final String KEY_BOOK_NAME = "book";
    static final String KEY_SHOP_NAME = "shop";
    static final String KEY_ACTION = "action";
    static final String KEY_OWNER = "owner";
    static final String KEY_BORROWER = "borrower";
    static final String KEY_TIME = "time";
    static final String TAG = "DBAdapter";

    static final String DATABASE_NAME = "MessageDB";
    static final String DATABASE_TABLE = "Messages";
    static final int DATABASE_VERSION = 2;

    static final String DATABASE_CREATE =
            "create table Messages (_id integer primary key autoincrement," +
                    "new integer," +
                    "user text," +
                    "book text," +
                    "shop text, " +
                    "owner text," +
                    "borrower text," +
                    "action text," +
                    "time text);";

    final Context context;

    DatabaseHelper DBHelper;
    SQLiteDatabase db;

    public DBAdapter(Context ctx)
    {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(DATABASE_CREATE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS Messages");
            onCreate(db);
        }
    }

    //---opens the database---
    public DBAdapter open() throws SQLException
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    //---closes the database---
    public void close()
    {
        DBHelper.close();
    }

    //---insert a message into the database---
    public long insertMessage(MessageInfo message, String username)
    {
        if(username == null)
            return -1;

        Cursor cursor =db.query(DATABASE_TABLE, null, "user=? AND time=?", new String[]{username,message.getTime()}, null, null, KEY_ID + " DESC");
        if(cursor.getCount() > 0)
        {
            Log.v("Get duplicated item", username);
            return 0;
        }

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NEW,1);
        initialValues.put(KEY_BOOK_NAME, message.getBook());
        initialValues.put(KEY_SHOP_NAME, message.getShop());
        initialValues.put(KEY_OWNER, message.getOwner());
        initialValues.put(KEY_BORROWER, message.getBorrower());
        initialValues.put(KEY_ACTION, message.getAction());
        initialValues.put(KEY_TIME, message.getTime());
        initialValues.put(KEY_USER,username);
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    //---deletes a particular message---
    public boolean deleteMessage(long id)
    {
        return db.delete(DATABASE_TABLE, KEY_ID + "=" + id, null) > 0;
    }

    public void deleteTable()
    {
        db.execSQL("DROP Messages IF EXISTS Messages");
    }
    //---deletes a particular message---
/*    public boolean deleteMessage(String str)
    {
        return db.delete(DATABASE_TABLE, KEY_CONTENT +"='" + str+"'", null) > 0;
    }*/

    public boolean cleanMessage(String username)
    {
        return db.delete(DATABASE_TABLE, "user=?", new String[]{username}) > 0;
    }

    //---retrieves all the messages---
    public Cursor getAllMessages(String username)
    {
        Cursor cursor =db.query(DATABASE_TABLE, null, "user=?", new String[]{username}, null, null, KEY_ID + " DESC");

        Log.v(".........get item: ", "total:" + cursor.getCount());
        return cursor;
    }

    //---retrieves a particular message---
    public Cursor getMessage(long id) throws SQLException
    {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, null, KEY_ID + "=" + id, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }

        return mCursor;
    }

    public boolean resetState(long id)
    {
        ContentValues args = new ContentValues();
        args.put(KEY_NEW, 0);

        Boolean result = db.update(DATABASE_TABLE, args, KEY_ID + "=" + id, null) > 0;
        return result;
    }

    //---updates a message---
/*    public boolean updateMessage(long id, String message)
    {
        ContentValues args = new ContentValues();
        args.put(KEY_CONTENT, message);
//        return db.update(DATABASE_TABLE, args, KEY_ID + "=" + id, null) > 0;
        return db.update(DATABASE_TABLE, args, KEY_CONTENT + "='" + message+"'", null) > 0;
    }*/


/*    public Cursor getMessage(String string)throws SQLException  {

        // select * from message where content="蛋挞"
        // TODO Auto-generated method stub
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, null, KEY_CONTENT + "='" + string+"'", null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }*/
}
