package com.kapok.brianramirez.kapok;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

class SettingsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Theme set
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }

        setContentView(R.layout.activity_settings);

        //View setup
        TextView changeThemebtn = (TextView)findViewById(R.id.change_theme_button);
        ToggleButton notificationbtn = (ToggleButton)findViewById(R.id.notifcation_switch);
        ToggleButton menubtn = (ToggleButton)findViewById(R.id.drawer_switch);

        //Setting the checked value of the slider according to the status of the service
        if(isMyServiceRunning(DatabaseListener.class)){
            notificationbtn.setChecked(true);
        }
        else {
            notificationbtn.setChecked(true);
        }

        //Working of the notification button
        notificationbtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    startService(new Intent(SettingsActivity.this, DatabaseListener.class));
                } else {
                    // The toggle is disabled
                    stopService(new Intent(SettingsActivity.this, DatabaseListener.class));
                }
            }
        });

        changeThemebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }

                finish();
                startActivity(new Intent(SettingsActivity.this, SettingsActivity.this.getClass()));
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
