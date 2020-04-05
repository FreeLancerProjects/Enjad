package com.developer.enjad.activities_fragments.activity_home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.developer.enjad.R;
import com.developer.enjad.activities_fragments.activity_edit_report.EditReportActivity;
import com.developer.enjad.activities_fragments.activity_login.LoginActivity;
import com.developer.enjad.activities_fragments.activity_new_communication.NewCommunicationActivity;
import com.developer.enjad.databinding.ActivityHomeBinding;
import com.developer.enjad.language.LanguageHelper;
import com.developer.enjad.models.UserModel;
import com.developer.enjad.preferences.Preferences;
import com.google.firebase.auth.FirebaseAuth;

import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;
    private String lang;
    private Preferences preferences;
    private UserModel userModel;
    private FirebaseAuth mAuth;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        initView();


    }

    private void initView() {
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        mAuth = FirebaseAuth.getInstance();

        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        binding.setUserModel(userModel);
        binding.flNewComm.setOnClickListener(view -> {
            Intent intent = new Intent(this, NewCommunicationActivity.class);
            startActivity(intent);


        });

        binding.flEditReport.setOnClickListener(view -> {
            Intent intent = new Intent(this, EditReportActivity.class);
            startActivity(intent);


        });

        binding.cardLogout.setOnClickListener(view -> {
            mAuth.signOut();
            preferences.clear(this);
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

    }






}
