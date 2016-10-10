package com.compass.loco.homelibrary;

/**
 * Created by EXIAOQU on 7/27/2016.
 */
public class  BookInfo {

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getDetail() {
        return detail;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Boolean getState() { return (aVaiNum > 0); }

    public String getBorrower() {
        return borrower;
    }
    public int getaVaiNum() {
        return aVaiNum;
    }
    public int getBookNum() {
        return bookNum;
    }

    private String name;
    private String author;
    private String publisher;
    private String isbn;
    private String detail;
    private String imageUrl;
    private String borrower;
    private Boolean state;
    private int bookNum;
    private int aVaiNum;

    public BookInfo(String name, String author, String publisher, String isbn, String detail, String imageUrl, Boolean state,String borrower,int bookNum, int aVaiNum) {
        this.name = name;
        this.author = author;
        this.publisher = publisher;
        this.isbn = isbn;
        this.detail = detail;
        this.imageUrl = imageUrl;
        this.state = state;
        this.borrower = borrower;
        this.bookNum = bookNum;
        this.aVaiNum = aVaiNum;

    }

}
