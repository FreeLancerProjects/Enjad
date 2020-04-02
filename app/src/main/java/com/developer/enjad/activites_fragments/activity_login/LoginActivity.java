package com.developer.enjad.activites_fragments.activity_login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;

import com.developer.enjad.R;
import com.developer.enjad.databinding.ActivityLoginBinding;
import com.developer.enjad.language.LanguageHelper;
import com.developer.enjad.preferences.Preferences;


import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private int fragment_count = 0;
    private FragmentManager manager;
    private Fragment_Language fragment_language;
    private Preferences preferences;
    private Fragment_Sign_In fragment_sign_in;


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang","ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        manager = getSupportFragmentManager();
        preferences = Preferences.newInstance();
        if (savedInstanceState == null) {
            if (preferences.isLangSelected(this)) {
                displayFragmentSignIn();

            } else {
                displayFragmentLanguage();
            }
        }


    }

    private void displayFragmentLanguage()
    {
        fragment_language = Fragment_Language.newInstance();
        manager.beginTransaction().add(R.id.fragment_sign_in_container, fragment_language, "fragment_language").addToBackStack("fragment_language").commit();

    }
    public void displayFragmentSignIn()
    {
        fragment_count++;
        fragment_sign_in = Fragment_Sign_In.newInstance();
        manager.beginTransaction().add(R.id.fragment_sign_in_container, fragment_sign_in, "fragment_sign_in").addToBackStack("fragment_sign_in").commit();

    }

    public void refreshActivity(String lang)
    {
        Paper.init(this);
        Paper.book().write("lang", lang);
        preferences.saveSelectedLanguage(this);
        LanguageHelper.setNewLocale(this, lang);
        Intent intent = getIntent();
        finish();
        startActivity(intent);

    }

    @Override
    public void onBackPressed() {
        back();
    }

    public void back() {
        if (fragment_count > 1) {
            fragment_count--;
            super.onBackPressed();
        } else {
            finish();
        }
    }

}
