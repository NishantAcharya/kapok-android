package com.kapok.brianramirez.kapok;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.widget.TextView;


public class WaitingScreenActivity extends AppCompatActivity {
    String teamCodeDisplay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//Theme set
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }
        setContentView(R.layout.activity_code_display);
     //Getting intent data from the last activity
        Intent intent = getIntent();
        teamCodeDisplay = intent.getStringExtra("Team Code");

        //Team coed display waiting screen initialized here
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                openMaps(teamCodeDisplay);
            }
        }, 3000);
        TextView myText = (TextView)findViewById(R.id.teamCodeDisplay);
        myText.setText(teamCodeDisplay);
    }
    //Leads to ap avtivity
        public void openMaps(String teamCode){
        Intent intent = new Intent(this, MapActivity.class).putExtra("team_join_id", teamCode);
        startActivity(intent);
        }
}
