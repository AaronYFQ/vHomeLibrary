package com.compass.loco.homelibrary;

import android.graphics.Bitmap;

/**
 * Created by EXIAOQU on 7/27/2016.
 */
public class BookInfo {

    public String getName() {
        return myName;
    }

    public String getAuthor() {
        return myAuthor;
    }

    public String getPublisher() {
        return myPublisher;
    }

    public String getIsbn() {
        return myIsbn;
    }

    public String getDetail() {
        return myDetail;
    }

    public Bitmap getBitmap() {
        return myBitmap;
    }

    private String myName;
    private String myAuthor;
    private String myPublisher;
    private String myIsbn;
    private String myDetail;
    private Bitmap myBitmap;

    public BookInfo(String name, String author, String publisher, String isbn, String detail, Bitmap bitmap) {
        myName = name;
        myAuthor = author;
        myPublisher = publisher;
        myIsbn = isbn;
        myDetail = detail;
        myBitmap = bitmap;
    }
    
    
}
