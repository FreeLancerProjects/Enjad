package com.developer.enjad.models;

import android.content.Context;
import android.util.Log;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;
import androidx.databinding.library.baseAdapters.BR;

import com.developer.enjad.R;

import java.io.Serializable;

public class SignUpModel extends BaseObservable implements Serializable {

    private String name;
    private String phone_code;
    private String phone;
    private int user_type;
    private String password;

    public ObservableField<String> error_name = new ObservableField<>();
    public ObservableField<String> error_phone = new ObservableField<>();
    public ObservableField<String> error_password = new ObservableField<>();

    public boolean isDataValid(Context context) {

        Log.e("name",name+"_");
        Log.e("phone_code",phone_code+"_");
        Log.e("phone",phone+"_");
        Log.e("user_type",user_type+"_");
        Log.e("password",password+"_");



        if (!name.trim().isEmpty() &&
                !phone_code.trim().isEmpty() &&
                !phone.trim().isEmpty() &&
                password.length() >= 6

        ) {
            error_name.set(null);
            error_phone.set(null);
            error_password.set(null);


            return true;
        } else {

            if (name.trim().isEmpty()) {
                error_name.set(context.getString(R.string.field_req));
            } else {
                error_name.set(null);

            }


            if (phone.trim().isEmpty()) {
                error_phone.set(context.getString(R.string.field_req));
            } else {
                error_phone.set(null);

            }





            if (password.trim().isEmpty()) {
                error_password.set(context.getString(R.string.field_req));
            } else if (password.trim().length() < 6) {
                error_password.set(context.getString(R.string.pass_short));

            }
            else {
                error_password.set(null);

            }


            return false;
        }
    }

    public SignUpModel() {
        setName("");
        setPhone("");
        setPhone_code("");
        setPassword("");
        setUser_type(0);

    }
    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }


    @Bindable
    public String getPhone_code() {
        return phone_code;
    }

    public void setPhone_code(String phone_code) {
        this.phone_code = phone_code;
        notifyPropertyChanged(BR.phone_code);

    }

    @Bindable
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        notifyPropertyChanged(BR.phone);

    }


    @Bindable
    public int getUser_type() {
        return user_type;
    }

    public void setUser_type(int user_type) {
        this.user_type = user_type;
    }

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);

    }
    }
