package com.compass.loco.homelibrary.widge;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;

/**
 * Created by eweilzh on 8/22/2016.
 */
public  class CacheBookImages extends AsyncTask<Void, Void,Bitmap> {
    private String url;
    private String isbn;
    private static String filePath = Environment.getExternalStorageDirectory().toString() + "/vbook/imgCache/";

    public CacheBookImages(String url,  String isbn) {
        this.url = url;
        this.isbn = isbn;
    }

    public Bitmap getBitmap() throws IOException {
        /*URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
       *//* if(conn.getResponseCode() == 200){
            InputStream inputStream = conn.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        }*//*

        InputStream inputStream = conn.getInputStream();
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        return bitmap;
        //return null;*/
        Log.i("CCCCDDDD","cache" + url);
        URL urlConnection = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) urlConnection
                .openConnection();
        connection.setDoInput(true);
        connection.connect();
        InputStream input = connection.getInputStream();
        Bitmap myBitmap = BitmapFactory.decodeStream(input);
        return myBitmap;

    }

    public void saveBitmap() {
        Log.e("ddd", "保存图片");

        makeRootDir(filePath);

        String cacheImg = filePath + isbn;
        File file = null;
        try {
            file = new File(cacheImg);
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {
            Log.i("error:", e + "");
        }

        File f = new File(cacheImg);
        if (f.exists()) {
            f.delete();
        }
        try {
            Bitmap bm = getBitmap();
            if(bm == null)
            {
                Log.e("ddd","error");
                return;
            }
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.JPEG, 50, out);
            out.flush();
            out.close();
            Log.i("ddd", "已经保存");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    // 生成文件夹
    public void  makeRootDir(String filePath)
    {
        File file=null;
        String newPath=null;
        String[] path=filePath.split("/");
        for(int i=0;i<path.length;i++)
        {
            if(newPath==null){
                newPath=path[i];
            }else{
                newPath=newPath+"/"+path[i];
            }
            file=new File(newPath);
            if (!file.exists()) {
                file.mkdir();

            }
        }
    }


    /**
     * 运行在UI线程中，在调用doInBackground()之前执行
     */
    @Override
    protected void onPreExecute() {
        //Toast.makeText(context,"开始执行",Toast.LENGTH_SHORT).show();
    }
    /**
     * 后台运行的方法，可以运行非UI线程，可以执行耗时的方法
     */
    @Override
    protected Bitmap doInBackground(Void... params) {
        try {
            saveBitmap();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 运行在ui线程中，在doInBackground()执行完毕后执行
     */
 /*   @Override
    protected void onPostExecute(Integer integer) {
        //Toast.makeText(context,"执行完毕",Toast.LENGTH_SHORT).show();
    }*/



}
