package com.kapok.brianramirez.kapok;

import android.os.Bundle;
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

        TextView myText = (TextView)findViewById(R.id.teamCodeDisplay);
        String myString = String.valueOf(number);
        myText.setText(myString);
    }

        Random rand = new Random();
        int number = rand.nextInt(1000000)+100000;
}
