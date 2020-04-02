package com.developer.enjad.interfaces;


import com.developer.enjad.models.NewReport;

public interface Listeners {


    interface LoginListener {
        void checkDataLogin();
    }
    interface SkipListener
    {
        void skip();
    }
    interface BackListener
    {
        void back();
    }
    interface ShowCountryDialogListener
    {
        void showDialog();
    }


    interface SignUpListener {
        void checkDataSignUp();
    }

    interface NewRepListener {
        void checkNewReportData(NewReport newReport);
    }

    interface UpdateProfileListener
    {
        void updateProfile();
    }

   /* interface ShowCountryDialogListener
    {
        void showDialog();
    }*/
    /*interface ContactListener
    {
        void sendContact(ContactUsModel contactUsModel);
    }*/
}
