package com.compass.loco.homelibrary;

import android.graphics.Bitmap;

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

    public Bitmap getBitmap() {
        return bitmap;
    }

    public Boolean getState() { return state; }

    private String name;
    private String author;
    private String publisher;
    private String isbn;
    private String detail;
    private Bitmap bitmap;
    private Boolean state;

    public BookInfo(String name, String author, String publisher, String isbn, String detail, Bitmap bitmap, Boolean state) {
        this.name = name;
        this.author = author;
        this.publisher = publisher;
        this.isbn = isbn;
        this.detail = detail;
        this.bitmap = bitmap;
        this.state = state;
    }

}
