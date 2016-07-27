package com.compass.loco.homelibrary;

/**
 * Created by EXIAOQU on 7/27/2016.
 */

public class SelectedBookInfo {

    private BookInfo myBookInfo;
    private Boolean  mySelected = false;

    public SelectedBookInfo(BookInfo bookinfo, Boolean selected) {
        myBookInfo = bookinfo;
        mySelected = selected;
    }

    public BookInfo getBookInfo() {
        return myBookInfo;
    }

    public void setSelected(Boolean selected) {
        mySelected = selected;
    }

    public Boolean isSelected() {
        return mySelected;
    }
}
