package com.kapok.brianramirez.kapok;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import io.realm.SyncUser;

public class TeamWelcomeActivity extends AppCompatActivity {

    Button mapBtn;
    Button cteamBtn;
    Button LogOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_welcome);


        cteamBtn = (Button) findViewById(R.id.cteam_btn);
        mapBtn = (Button) findViewById(R.id.map_btn);
        LogOut =  (Button) findViewById(R.id.logOut);

        LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            SyncUser syncUser = SyncUser.current();
            if (syncUser != null){
                syncUser.logOut();
                logOutOption();
            }
            }
        });
        cteamBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { opencteam(); }
        });


        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMaps();
            }
        });

    }

        public void logOutOption(){
            Intent intent = new Intent(this, WelcomeActivity.class);
            startActivity(intent);
        }
        public void openMaps(){
            Intent i = new Intent(this, MapActivity.class);
            startActivity(i);
        }

        public void opencteam(){
            Intent i = new Intent(this, CreateTeamActivity.class);
            startActivity(i);
        }


}
