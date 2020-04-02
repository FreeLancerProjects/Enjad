package com.developer.enjad.activites_fragments.activity_main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.developer.enjad.R;
import com.developer.enjad.activites_fragments.activity_edit_report.EditReportActivity;
import com.developer.enjad.activites_fragments.activity_new_comunication.NewCommunicationActivity;
import com.developer.enjad.activites_fragments.activity_sign_up.SignUpActivity;
import com.developer.enjad.databinding.ActivityLoginBinding;
import com.developer.enjad.databinding.ActivityMainBinding;
import com.developer.enjad.language.LanguageHelper;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private String lang;


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang","ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        initView();
    }

    private void initView() {
        lang = Paper.book().read("lang","ar");
        binding.setLang(lang);
        binding.flNewComm.setOnClickListener(view -> {
            Intent intent = new Intent(this, NewCommunicationActivity.class);
            startActivity(intent);


        });

        binding.flEditReport.setOnClickListener(view -> {
            Intent intent = new Intent(this, EditReportActivity.class);
            startActivity(intent);


        });

    }
}
