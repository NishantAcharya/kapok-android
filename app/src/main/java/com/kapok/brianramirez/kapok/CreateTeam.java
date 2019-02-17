package com.kapok.brianramirez.kapok;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Random;

public class CreateTeam extends AppCompatActivity {

    Button confirmTeam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_team);

        confirmTeam = (Button) findViewById(R.id.create_NewTeam);

        confirmTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCodeDisplay();
            }
        });
    }
        public void openCodeDisplay(){

        Intent i = new Intent(this, CodeDisplay.class);
        startActivity(i);

    }
}
