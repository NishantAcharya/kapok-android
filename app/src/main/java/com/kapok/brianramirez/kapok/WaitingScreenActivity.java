package com.kapok.brianramirez.kapok;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;


public class WaitingScreenActivity extends AppCompatActivity {
    String teamCodeDisplay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_display);
        Intent intent = getIntent();
        teamCodeDisplay = intent.getStringExtra("Team Code");
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
        public void openMaps(String teamCode){
        Intent intent = new Intent(this, MapActivity.class).putExtra("team_join_id", teamCode);
        startActivity(intent);
        }
}
