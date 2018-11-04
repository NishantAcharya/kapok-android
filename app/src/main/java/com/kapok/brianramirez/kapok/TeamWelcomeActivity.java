package com.kapok.brianramirez.kapok;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class TeamWelcomeActivity extends AppCompatActivity {

    Button mapBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_welcome);


        mapBtn = (Button) findViewById(R.id.map_btn);

        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMaps();
            }
        });
    }
        public void openMaps(){
            Intent i = new Intent(this, MapActivity.class);
            startActivity(i);
        }


}
