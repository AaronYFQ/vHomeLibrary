package com.compass.loco.homelibrary;

/**
 * Created by ehoixie on 7/29/2016.
 */
public class MessageInfo {
    public MessageInfo(String book, String shop, String owner, String borrower, String action, String time)
    {
        this.book = book;
        this.shop = shop;
        this.owner = owner;
        this.borrower = borrower;
        this.action = action;
        this.time = time;
    }

    public MessageInfo(){
    };

    public String getBook() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getBorrower() {
        return borrower;
    }

    public void setBorrower(String borrower) {
        this.borrower = borrower;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    private String book;
    private String shop;
    private String owner;
    private String borrower;
    private String action;
    private String time;
}

