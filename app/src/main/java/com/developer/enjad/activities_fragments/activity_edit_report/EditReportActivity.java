package com.developer.enjad.activities_fragments.activity_edit_report;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;


import com.developer.enjad.R;
import com.developer.enjad.adapter.ReportsAdapter;
import com.developer.enjad.databinding.ActivityEditReportBinding;
import com.developer.enjad.interfaces.Listeners;
import com.developer.enjad.language.LanguageHelper;
import com.developer.enjad.models.Reports_Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class EditReportActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityEditReportBinding binding;

    private String lang;
private List<Reports_Model.Data> data;
private ReportsAdapter reportsAdapter;
    private Reports_Model reports_model;


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", "ar")));

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_report);
        initView();

    }



    @SuppressLint("RestrictedApi")
    private void initView() {
        binding.toolbar.setTitle("");
        data = new ArrayList<>();
        reportsAdapter = new ReportsAdapter( this,data);
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());binding.setLang(lang);
        binding.setBackListener(this);
        binding.recView.setLayoutManager(new GridLayoutManager(this, 1));
        binding.recView.setAdapter(reportsAdapter);
getdata();


    }

    private void getdata() {
      if(getIntent().getSerializableExtra("data")!=null){
           reports_model= (Reports_Model) getIntent().getSerializableExtra("data");
           if(reports_model.getData()!=null&&reports_model.getData().size()>0){
               data.clear();
               data.addAll(reports_model.getData());
           }
      }
reportsAdapter.notifyDataSetChanged();
        }



    @Override
    public void back() {
        finish();
    }


}
