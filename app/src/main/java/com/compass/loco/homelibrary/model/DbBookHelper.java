package com.compass.loco.homelibrary.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eweilzh on 8/16/2016.
 */
public class DbBookHelper {

    private DatabaseHelper dbHelper;

    static final String KEY_ID = "_id";

    static final String KEY_BOOK_NAME = "book";
    static final String KEY_BOOK_STATE = "state";
    static final String KEY_ISBN = "isbn";
    static final String KEY_AUTHOR = "author";

    static final String KEY_SHOP_NAME = "shop";
    static final String KEY_SHOP_ADDR = "addr";
    static final String KEY_TIME = "time";
    static final String TAG = "bookTb";

    static final String DATABASE_NAME = "vbook";
    static final String DATABASE_TABLE = "bookTb";
    static final int DATABASE_VERSION = 2;


    static final String DATABASE_CREATE =
            "CREATE TABLE IF NOT EXISTS bookTb (_id integer primary key autoincrement," +
                    "isbn text," +
                    "book text," +
                    "state text," +
                    "author text," +
                    "shop text, " +
                    "addr text," +
                    "time text);";

    // private Context context;  B

    public DbBookHelper(Context context) {
        // this.context = context;
        dbHelper = new DatabaseHelper(context);
    }
    private static class DatabaseHelper extends SQLiteOpenHelper {
        // 类没有实例化,是不能用作父类构造器的参数,必须声明为静态


        public DatabaseHelper(Context context) {
            // 第一个参数是应用的上下文
            // 第二个参数是应用的数据库名字
            // 第三个参数CursorFactory指定在执行查询时获得一个游标实例的工厂类,设置为null,代表使用系统默认的工厂类
            // 第四个参数是数据库版本，必须是大于0的int（即非负数）
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            // TODO Auto-generated constructor stub
        }

        public DatabaseHelper(Context context, String name,
                            SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(DATABASE_CREATE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            //db.execSQL("CREATE TABLE IF NOT EXISTS bookDb (id integer primary key autoincrement, name varchar(20), age INTEGER)");
        }

        // onUpgrade()方法在数据库版本每次发生变化时都会把用户手机上的数据库表删除，然后再重新创建。
        // 一般在实际项目中是不能这样做的，正确的做法是在更新数据库表结构时，还要考虑用户存放于数据库中的数据不会丢失,从版本几更新到版本几。
        @Override
        public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE );
            onCreate(db);
        }

    }



    public  long insertBook(Bean book) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();

        long ret = 0;

        if(book == null )
            return -1;
        // public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy)
        try {
            Cursor cursor =db.query(DATABASE_TABLE, null, "isbn=? ", new String[]{book.getIsbn() }, null, null, KEY_ID + " DESC");
            if(cursor.getCount() > 0)
            {
                Log.v("Get duplicated item", book.getBookName());
                db.setTransactionSuccessful();
                db.endTransaction();
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ISBN, book.getIsbn());
        initialValues.put(KEY_BOOK_NAME, book.getBookName());
        initialValues.put(KEY_BOOK_STATE, book.getBookState());
        initialValues.put(KEY_SHOP_NAME, book.getShopName());
        initialValues.put(KEY_AUTHOR, book.getBookAuthor());
        initialValues.put(KEY_SHOP_ADDR, book.getBookAddr());

        try {
            ret =  db.insert(DATABASE_TABLE, null, initialValues);
        } catch (SQLException e) {
            e.printStackTrace();
        }


       /* db.execSQL("insert into book(name,age)values(?,?)",
                new Object[] { book.getName(), book.getAge() });*/
        // database.close();可以不关闭数据库，他里面会缓存一个数据库对象，如果以后还要用就直接用这个缓存的数据库对象。但通过
        // context.openOrCreateDatabase(arg0, arg1, arg2)打开的数据库必须得关闭
        db.setTransactionSuccessful();
        db.endTransaction();

        return  ret;

    }
   /*
   * 应该只考虑书本信息
   * */
    public void update(Bean book) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.execSQL(
                "update bookTb set state=? where isbn=?",
                new Object[] { book.getBookState(),   book.getIsbn() });
    }

    public Bean find(String isbn) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery(
                "select * from bookTb where isbn=?",
                new String[] { isbn });
        if (cursor.moveToFirst()) {
            String bookName = cursor.getString(cursor.getColumnIndex("book"));
            return new Bean(bookName,"","","","","","","");
        }
        return null;
    }

    public List<Bean> getBookListByName(String name) {
        List<Bean> bookList = new ArrayList<Bean>();
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery(
                "select * from bookTb where book like '%?%'",
                new String[] { name });

        if (cursor != null) {
            while (cursor.moveToNext()) {
            // 拿到每一行name 与hp的数值
                String bookName = cursor.getString(cursor.getColumnIndex("book"));
                //....
                Log.v("info", "book 是 " + bookName);

                bookList.add( new Bean(
                        bookName,"","","","","","",""
                        //(jsonObj.getBoolean("state")) ? "在库" : "借出",
                        //jsonObj.getString("author"),
                       // jsonObj.getString("publisher"),
                        //jsonObj.getString("shopname"),
                        //jsonObj.getString("shopaddr"),
                        //jsonObj.getString("isbn")
                ));
            }
            cursor.close();
        }
      /*  if (cursor.moveToFirst()) {
            String bookName = cursor.getString(cursor.getColumnIndex("book"));
            return new Bean(bookName,"","","","","","");
        }*/
        return bookList;
    }

    public void delete(String isbn) {
        if (isbn != null && !isbn.equals("")) {

            SQLiteDatabase database = dbHelper.getWritableDatabase();
            database.execSQL(
                    "delete from bookTb where isbn=?",
                    new String[] { isbn });
        }
 }

    public List<Bean> getScrollData(int startResult, int maxResult) {
       /* List<Person> persons = new ArrayList<Person>();
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery(
                "select * from person limit ?,?",
                new String[] { String.valueOf(startResult),
                        String.valueOf(maxResult) });
        while (cursor.moveToNext()) {
            persons.add(new Person(cursor.getInt(0), cursor.getString(1),
                    cursor.getShort(2)));
        }
        return persons;*/
        return  null;
    }

    // 获取分页数据，提供给SimpleCursorAdapter使用。
    /*public Cursor getRawScrollData(int startResult, int maxResult) {
        List<Person> persons = new ArrayList<Person>();
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        return database.rawQuery(
                "select personid as _id ,name,age from person limit ?,?",
                new String[] { String.valueOf(startResult),
                        String.valueOf(maxResult) });

    }

    public long getCount() {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("select count(*) from person", null);
        if (cursor.moveToNext()) {
            return cursor.getLong(0);
        }
        return 0;
    }*/

}


