package com.kapok.brianramirez.kapok;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

class AboutKapokActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_about);

        TextView logCreation;
        TextView adminFeature;
        TextView navigation;
        TextView uninstallation;

        //Setting the link movement to allow clickable hyperlinks
        logCreation = (TextView)findViewById(R.id.link1);
        logCreation.setMovementMethod(LinkMovementMethod.getInstance());
        adminFeature = (TextView)findViewById(R.id.link2);
        adminFeature.setMovementMethod(LinkMovementMethod.getInstance());
        navigation = (TextView)findViewById(R.id.link3);
        navigation.setMovementMethod(LinkMovementMethod.getInstance());
        uninstallation = (TextView)findViewById(R.id.link4);
        uninstallation.setMovementMethod(LinkMovementMethod.getInstance());
    }

}
