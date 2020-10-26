package com.kapok.brianramirez.kapok;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.widget.ImageView;
import android.widget.TextView;


public class WaitingScreenActivity extends AppCompatActivity {
    String teamCodeDisplay;
    AnimationDrawable logoani;
    ImageView logo;
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
        logo = findViewById(R.id.teamCodeAnim);

        //Creating the entering animation
        //logo.setBackgroundResource(R.drawable.kapok_animation_loop);
        //Animation removed for testing
        //logoani = (AnimationDrawable) logo.getBackground();

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

    //Starts the animation
   /* @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        logoani.start();
    }*/

    //Leads to ap avtivity
        public void openMaps(String teamCode){
        Intent intent = new Intent(this, MapActivity.class).putExtra("team_join_id", teamCode);
        startActivity(intent);
        }
}
