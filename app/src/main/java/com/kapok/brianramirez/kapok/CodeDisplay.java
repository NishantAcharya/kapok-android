package com.kapok.brianramirez.kapok;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class CodeDisplay extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_display);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                openMaps(number);
            }
        }, 3000);
        TextView myText = (TextView)findViewById(R.id.teamCodeDisplay);
        String myString = String.valueOf(number);
        myText.setText(myString);
    }

        public void openMaps(int number){
        Intent intent = new Intent(this,MapActivity.class).putExtra("team_join_id", number);
        startActivity(intent);
        }
        Random rand = new Random();
        int number = rand.nextInt(1000000)+100000;



}
