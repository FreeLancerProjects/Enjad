package com.developer.enjad.models;

import android.content.Context;
import android.text.TextUtils;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.developer.enjad.BR;
import com.developer.enjad.R;


public class NewReport2 {

    private String id;
    private String number;
    private String link;


    public NewReport2(String id, String number, String link) {
        this.id=id;
        this.number = number;
        this.link=link;


    }

    public NewReport2() {


    }


    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;

    }



}
