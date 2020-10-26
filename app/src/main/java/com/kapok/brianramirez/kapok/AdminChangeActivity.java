package com.kapok.brianramirez.kapok;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;

public class AdminChangeActivity extends AppCompatActivity {
//What's the point of this activity?
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //changing the theme
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }
        //code for changing theme ends

        //Layout set here from the xml files
        setContentView(R.layout.activity_admin_change);
    }
}
