package com.kapok.brianramirez.kapok;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


public class TeamWelcomeActivity extends AppCompatActivity {

    Button cteamBtn;
    Button jteamBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_welcome);

        cteamBtn = (Button) findViewById(R.id.cteam_btn);
        jteamBtn = (Button) findViewById(R.id.jteam_btn);



        cteamBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { openCreateTeam(); }
        });



        jteamBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openJoinTeam();
            }
        });

    }

    public  void openJoinTeam(){
        Intent intent = new Intent(this, JoinTeamActivity.class);
        startActivity(intent);
    }

        public void openCreateTeam(){
            Intent i = new Intent(this, CreateTeamActivity.class);
            startActivity(i);
        }


}
