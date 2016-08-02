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

    public Boolean getState() { return state; }

    private String name;
    private String author;
    private String publisher;
    private String isbn;
    private String detail;
    private String imageUrl;
    private Boolean state;

    public BookInfo(String name, String author, String publisher, String isbn, String detail, String imageUrl, Boolean state) {
        this.name = name;
        this.author = author;
        this.publisher = publisher;
        this.isbn = isbn;
        this.detail = detail;
        this.imageUrl = imageUrl;
        this.state = state;
    }

}
