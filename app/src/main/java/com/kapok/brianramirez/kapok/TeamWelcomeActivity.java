package com.kapok.brianramirez.kapok;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;


public class TeamWelcomeActivity extends AppCompatActivity {

    Button cteamBtn;
    Button jteamBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }
        setContentView(R.layout.activity_team_welcome);

        cteamBtn = findViewById(R.id.cteam_btn);
        jteamBtn = findViewById(R.id.jteam_btn);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.logOut) {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            mAuth.signOut();
            Intent intent = new Intent(TeamWelcomeActivity.this, WelcomeActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
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
