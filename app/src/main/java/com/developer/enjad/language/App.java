package com.developer.enjad.language;

import android.content.Context;

import androidx.multidex.MultiDexApplication;

import com.developer.enjad.share.TypefaceUtil;


public class App extends MultiDexApplication {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LanguageHelper.updateResources(base,"ar"));
    }

  public void onCreate() {
        super.onCreate();

        TypefaceUtil.setDefaultFont(this, "DEFAULT", "fonts/ar_font.ttf");
        TypefaceUtil.setDefaultFont(this, "MONOSPACE", "fonts/ar_font.ttf");
        TypefaceUtil.setDefaultFont(this, "SERIF", "fonts/ar_font.ttf");
        TypefaceUtil.setDefaultFont(this, "SANS_SERIF", "fonts/ar_font.ttf");



    }
}
