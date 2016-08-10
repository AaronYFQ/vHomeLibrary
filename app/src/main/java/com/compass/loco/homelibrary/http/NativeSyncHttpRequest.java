package com.compass.loco.homelibrary.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by esaabbh on 8/9/2016.
 */
public class NativeSyncHttpRequest {

    final static int GET = 0;
    final static int POST = 1;

    public String doSyncGetHttpRequest(String urlpath, Map<String, String> requestParamsMap) throws IOException {

        URL url= null;
        HttpURLConnection conn= null;
        String json = null;

        StringBuffer params = makeURLParmeter(GET, requestParamsMap);
        urlpath = urlpath+params.toString();

        try {
            url = new URL(urlpath);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        conn = (HttpURLConnection)url.openConnection();
        conn.setConnectTimeout(7000); //set the timeout in milliseconds

        conn.setRequestProperty("encoding","UTF-8"); //可以指定编码
        conn.setUseCaches(false);//不使用缓存
        try {
            conn.setRequestMethod("GET");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }

        if(conn.getResponseCode()==200)
        {
            InputStream inputStream=conn.getInputStream();
            byte[] data=readStream(inputStream);
            json = new String(data);
            System.out.println(json);
        }

        conn.disconnect();
        return json;
    }

    public String doSyncPostHttpRequest(String urlpath, Map<String, String> requestParamsMap ) throws IOException {

        URL url= null;
        HttpURLConnection conn= null;
        String json = null;

        StringBuffer params = makeURLParmeter(POST, requestParamsMap);

        try {
            url = new URL(urlpath);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        conn = (HttpURLConnection)url.openConnection();
        conn.setConnectTimeout(7000); //set the timeout in milliseconds

        // 设置通用的请求属性
        conn.setRequestMethod("POST");
        conn.setRequestProperty("accept", "*/*");
        conn.setRequestProperty("connection", "Keep-Alive");
        conn.setRequestProperty("Content-Length", String.valueOf(params.length()));
        // 发送POST请求必须设置如下两行
        conn.setDoOutput(true);
        conn.setDoInput(true);
        // 获取URLConnection对象对应的输出流
        PrintWriter printWriter = new PrintWriter(conn.getOutputStream());
        // 发送请求参数
        printWriter.write(params.toString());
        // flush输出流的缓冲
        printWriter.flush();
        // 根据ResponseCode判断连接是否成功
        int responseCode = conn.getResponseCode();
        if (responseCode == 200) {
            InputStream inputStream=conn.getInputStream();
            byte[] data=readStream(inputStream);
            json = new String(data);
            System.out.println(json);
        }

        conn.disconnect();
        if (printWriter != null) {
            printWriter.close();
        }

        return json;
    }

    private StringBuffer makeURLParmeter(int Type, Map<String, String> requestParamsMap) {
        StringBuffer params = new StringBuffer();
        Iterator it = requestParamsMap.entrySet().iterator();
        if((Type == GET) && it.hasNext()) params.append("?");
        while (it.hasNext()) {
            Map.Entry element = (Map.Entry) it.next();
            params.append(element.getKey());
            params.append("=");
            try {
                params.append(URLEncoder.encode(String.valueOf(element.getValue()),"UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            params.append("&");
        }
        if (params.length() > 0) {
            params.deleteCharAt(params.length() - 1);
        }

        return params;
    }

    private byte[] readStream(InputStream inputStream) throws IOException {

        byte[] buffer=new byte[1024];
        int len=-1;
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();

        while((len=inputStream.read(buffer))!=-1)
        {
            byteArrayOutputStream.write(buffer,0,len);
        }

        inputStream.close();
        byteArrayOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }
}
