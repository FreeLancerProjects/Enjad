package com.developer.enjad.activities_fragments.activity_splash;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.developer.enjad.activities_fragments.activity_home.HomeActivity;
import com.developer.enjad.R;
import com.developer.enjad.activities_fragments.activity_login.LoginActivity;
import com.developer.enjad.databinding.ActivitySplashBinding;
import com.developer.enjad.language.LanguageHelper;
import com.developer.enjad.preferences.Preferences;
import com.developer.enjad.tags.Tags;


import io.paperdb.Paper;

public class SplashActivity extends AppCompatActivity {
    private ActivitySplashBinding binding;
    private Animation animation;
    private Preferences preferences;
    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", "ar")));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = Preferences.newInstance();

        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        animation = AnimationUtils.loadAnimation(this,R.anim.fade);
        binding.logoIcon.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.logoIcon.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                String session = preferences.getSession(SplashActivity.this);
                if (session.equals(Tags.session_login))
                {
                    Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }else
                    {
                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
