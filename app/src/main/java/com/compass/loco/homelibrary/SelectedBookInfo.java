package com.compass.loco.homelibrary;

/**
 * Created by EXIAOQU on 7/27/2016.
 */

public class SelectedBookInfo {

    private BookInfo bookInfo;
    private Boolean selected = false;

    public SelectedBookInfo(BookInfo bookinfo, Boolean selected) {
        this.bookInfo = bookinfo;
        this.selected = selected;
    }

    public BookInfo getBookInfo() {
        return bookInfo;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public Boolean isSelected() {
        return selected;
    }
}
