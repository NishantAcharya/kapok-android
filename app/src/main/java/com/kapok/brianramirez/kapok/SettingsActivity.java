package com.kapok.brianramirez.kapok;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

public class SettingsActivity extends AppCompatActivity {
    public static boolean notificationcheck;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Theme set
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }

        setContentView(R.layout.activity_settings);

        //View setup
        CompoundButton changeThemebtn = (CompoundButton) findViewById(R.id.theme_switch);
        CompoundButton notificationbtn = (CompoundButton) findViewById(R.id.notifcation_switch);
        CompoundButton menubtn = (CompoundButton) findViewById(R.id.drawer_switch);

        //Setting the checked value of the slider according to the status of the service
        if(notificationcheck){
            notificationbtn.setChecked(true);
        }
        else {
            notificationbtn.setChecked(false);
        }

        Intent notification = new Intent(SettingsActivity.this, DatabaseListener.class);;

        //Working of the notification button

        notificationbtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    notificationcheck = true;
                    startService(notification);
                } else {
                    // The toggle is disabled
                    notificationcheck = false;
                    stopService(notification);
                }
            }
        });


        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            changeThemebtn.setChecked(true);
        }
        else{
            changeThemebtn.setChecked(false);
        }

        changeThemebtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
                else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            }
        });

    }

    //Method to check if a service is running or not
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


}
