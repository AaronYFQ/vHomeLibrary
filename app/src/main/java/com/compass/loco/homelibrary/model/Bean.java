package com.compass.loco.homelibrary.model;

/**
 * bean 类
 * Created by eweilzh on 2016-07-28
 */

public class Bean {
    //private int iconId;
    private String bookName;
    private String bookState;
    private String bookAuthor;
    private String bookPublisher;
    private String shopName;
    private String bookAddr;
    private String userName = "liangshui";

    private String isbn;
    private String time;

    public Bean(String bookName, String bookState, String bookAuthor, String bookPublisher, String shopName, String bookAddr, String userName, String isbn) {
        this.bookName = bookName;
        this.bookState = bookState;
        this.bookAuthor = bookAuthor;
        this.bookPublisher = bookPublisher;
        this.shopName = shopName;
        this.bookAddr = bookAddr;
        this.userName = userName;
        this.isbn = isbn;
        //this.time = time;
    }

   /* public int getBookIconId() {
        return iconId;
    }
    public void setBookIconId(int iconId) {
        this.iconId = iconId;
    }
*/
    public String getBookName() {
        return bookName;
    }
    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookState() {
        return bookState;
    }
    public void setBookState(String title) {
        this.bookState = bookState;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }
    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public String getBookPublisher() {
        return bookPublisher;
    }
    public void setBookPublisher(String comments) {
        this.bookPublisher = bookPublisher;
    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String content) {
        this.userName = userName;
    }

    public String getBookAddr() {
        return bookAddr;
    }
    public void setBookAddr(String bookAddr) {
        this.bookAddr = bookAddr;
    }

    public String getShopName() {
        return shopName;
    }
    public void setShopName(String content) {
        this.shopName = shopName;
    }

    public String getIsbn() {
        return isbn;
    }
    public void SetIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getDbString()
    {
        return "bookName="+ bookName + ", bookState=" + bookState + ", bookAuthor="+ bookAuthor + ", shopName="+ shopName + ", bookAddr="+ bookAddr ;
    }


}
