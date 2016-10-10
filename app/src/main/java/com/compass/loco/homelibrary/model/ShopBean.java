package com.compass.loco.homelibrary.model;

/**
 * Created by eweilzh on 8/2/2016.
 */
public class ShopBean {
    private String shopName;
    private String village;
    private String bookCounts ;
    private String borrowCounts;

    private String username;

    public ShopBean(String shopName, String village, String username,String bookCounts, String borrowCounts) {
        this.shopName = shopName;
        this.village = village;
        this.username = username;
        this.bookCounts = bookCounts;
        this.borrowCounts =borrowCounts;
    }

    public String getBookCounts() {
        return bookCounts;
    }

    public void setBookCounts(String bookCounts) {
        this.bookCounts = bookCounts;
    }

    public String getShopName() {
        return shopName;
    }
    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getVillage() {
        return village;
    }
    public void setVillage(String village) {
        this.village = village;
    }

    public String getUserName() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getBorrowCounts() {
        return borrowCounts;
    }
    public void setBorrowCounts(String borrowCounts) {
        this.borrowCounts = borrowCounts;
    }
}
