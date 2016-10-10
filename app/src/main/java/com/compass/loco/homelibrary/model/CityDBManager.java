package com.compass.loco.homelibrary.model;

/**
 * Created by efaqiyu on 2016/9/30.
 */


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.compass.loco.homelibrary.R;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

/**
 * write raw data file to local sqlite db
 */
public class CityDBManager {
    public static final String DB_NAME = "china_city_name.db";
    private static final String PACKAGE_NAME = "com.compass.loco.homelibrary";
    public static final String DB_PATH = "/data" + Environment.getDataDirectory().getAbsolutePath() + "/" + PACKAGE_NAME;
    private final int BUFFER_SIZE = 400000;
    private Context mContext;
    private SQLiteDatabase database;

    public CityDBManager(Context context) {
        this.mContext = context;
    }

    /**
     * 被调用方法
     */
    public void openDateBase() {
        this.database = this.openDateBase(DB_PATH + "/" + DB_NAME);

    }

    private SQLiteDatabase openDateBase(String dbFile) {
        File file = new File(dbFile);
        if (!file.exists()) {
            InputStream stream = this.mContext.getResources().openRawResource(R.raw.china_city_name);
            try {
                FileOutputStream outputStream = new FileOutputStream(dbFile);
                byte[] buffer = new byte[BUFFER_SIZE];
                int count = 0;
                while ((count = stream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, count);
                }
                outputStream.close();
                stream.close();
                SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
                return db;
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return database;
    }

    public void closeDatabase() {
        if (database != null && database.isOpen()) {
            this.database.close();
        }
    }
}
