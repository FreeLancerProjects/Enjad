package com.developer.enjad.models;

import android.content.Context;
import android.text.TextUtils;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.developer.enjad.BR;
import com.developer.enjad.R;


public class NewReport extends BaseObservable {

    private String id;
    private String number;
    private String link;
    public ObservableField<String> error_number = new ObservableField<>();
    public ObservableField<String> error_link = new ObservableField<>();



    public NewReport(String id,String number,String link) {
        this.id=id;
        this.number = number;
        notifyPropertyChanged(BR.number);
        this.link=link;
        notifyPropertyChanged(BR.link);


    }

    public NewReport() {
        this.number ="";
        notifyPropertyChanged(BR.number);
        this.link="";
        notifyPropertyChanged(BR.link);


    }


    @Bindable
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
        notifyPropertyChanged(BR.number);

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Bindable
    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
        notifyPropertyChanged(BR.link);

    }


    public boolean isDataValid(Context context)
    {
        if (!TextUtils.isEmpty(number)&&
                !TextUtils.isEmpty(link)
        )
        {
            error_number.set(null);
            error_link.set(null);

            return true;
        }else
        {
            if (number.isEmpty())
            {
                error_number.set(context.getString(R.string.field_req));
            }else
            {
                error_number.set(null);
            }

            if (link.isEmpty())
            {
                error_link.set(context.getString(R.string.field_req));
            }else
            {
                error_link.set(null);
            }

            return false;
        }
    }


}
