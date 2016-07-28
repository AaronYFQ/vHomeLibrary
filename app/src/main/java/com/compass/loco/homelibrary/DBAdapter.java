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
    static final String KEY_CONTENT = "content";
    static final String TAG = "DBAdapter";

    static final String DATABASE_NAME = "MyDB";
    static final String DATABASE_TABLE = "messages";
    static final int DATABASE_VERSION = 2;

    static final String DATABASE_CREATE =
            "create table messages (_id integer primary key autoincrement, "
                    + "content text not null);";

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
        public void onCreate(SQLiteDatabase db)
        {
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
            db.execSQL("DROP TABLE IF EXISTS messages");
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
    public long insertMessage(String message)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_CONTENT, message);
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    //---deletes a particular message---
    public boolean deleteMessage(long id)
    {
        return db.delete(DATABASE_TABLE, KEY_ID + "=" + id, null) > 0;
    }

    //---deletes a particular message---
    public boolean deleteMessage(String str)
    {
        return db.delete(DATABASE_TABLE, KEY_CONTENT +"='" + str+"'", null) > 0;
    }

    public boolean cleanMessage()
    {
      return db.delete(DATABASE_TABLE, null, null) > 0;
    }

    //---retrieves all the messages---
    public Cursor getAllMessages()
    {
        return db.query(DATABASE_TABLE, new String[] {KEY_ID, KEY_CONTENT,
                }, null, null, null, null, null);
    }

    //---retrieves a particular message---
    public String getMessage(long id) throws SQLException
    {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[] {KEY_ID,
                                KEY_CONTENT}, KEY_ID + "=" + id, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }

        return mCursor.getString(mCursor.getColumnIndex(DBAdapter.KEY_CONTENT));
    }

    //---updates a message---
    public boolean updateMessage(long id, String message)
    {
        ContentValues args = new ContentValues();
        args.put(KEY_CONTENT, message);
//        return db.update(DATABASE_TABLE, args, KEY_ID + "=" + id, null) > 0;
        return db.update(DATABASE_TABLE, args, KEY_CONTENT + "='" + message+"'", null) > 0;
    }


    public Cursor getMessage(String string)throws SQLException  {

        // select * from message where content="蛋挞"
        // TODO Auto-generated method stub
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, null, KEY_CONTENT + "='" + string+"'", null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
}
