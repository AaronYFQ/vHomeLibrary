package com.compass.loco.homelibrary;

/**
 * Created by elanywa on 2016/8/1.
 */
public class BorrowedBookInfo {

    private String bookName;
    private String shopName;
    private String shopOwner;
    private String shopAddress;
    private String requested_Time;
    private String accepted_Time;
    private String finish_Time;
    private String state;
    private String bitmap;

    public BorrowedBookInfo(String name, String shop, String owner, String address, String requested_time, String accepted_time,String finish_time,String imageUrl, String state) {
        this.bookName = name;
        this.shopName = shop;
        this.shopOwner = owner;
        this.shopAddress = address;
        this.requested_Time = requested_time;
        this.accepted_Time = accepted_time;
        this.finish_Time = finish_time;
        this.state = state;
        this.bitmap = imageUrl;
    }

    public String  getBookName() {
        return bookName;
    }

    public String getShopName() {
        return shopName;
    }

    public String getShopOwner() { return shopOwner;}

    public String getState() {
        return state;
    }

    public String  getShopAddress() {
        return shopAddress;
    }

    public String getRequestTime() {
        return requested_Time;
    }
    public String getAcceptTime() {
        return accepted_Time;
    }
    public String getFinishTime() {
        return finish_Time;
    }

    public String getBitmap() {
        return bitmap;
    }

}