package com.compass.loco.homelibrary;

/**
 * Created by elanywa on 2016/8/1.
 */
public class BorrowedBookInfo {

    private BookInfo bookInfo;
    private String shopName;
    private String state;

    public void BorrowedBookInfo(BookInfo bookinfo, String shopname, String state) {
        this.bookInfo = bookinfo;
        this.shopName = shopname;
        this.state = state;
        return;
    }

    public BookInfo getBookInfo() {
        return bookInfo;
    }

    public void setShopName(String shopname) {
        this.shopName = shopname;
    }

    public String getState() {
        return state;
    }

}
