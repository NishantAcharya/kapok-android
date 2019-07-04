package com.kapok.brianramirez.kapok;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;


public class TeamWelcomeActivity extends AppCompatActivity {

    Button cteamBtn;
    Button jteamBtn;
    Button logOutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_welcome);

        cteamBtn = findViewById(R.id.cteam_btn);
        jteamBtn = findViewById(R.id.jteam_btn);
        logOutBtn = findViewById(R.id.logOutBtn);


        logOutBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                Intent intent = new Intent(TeamWelcomeActivity.this, WelcomeActivity.class);
                startActivity(intent);
            }

        });
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

    @Override
    public void onBackPressed() {
        //DO NOTHING
    }
}
