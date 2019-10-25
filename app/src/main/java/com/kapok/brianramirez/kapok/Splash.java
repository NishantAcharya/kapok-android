package com.kapok.brianramirez.kapok;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;

import java.util.Timer;
import java.util.TimerTask;

public class Splash extends AppCompatActivity {

    private Database database;

    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        database = new Database();


        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                FirebaseUser user = database.currentUser;
                if (user == null){
                    Intent i = new Intent(Splash.this, WelcomeActivity.class);
                    startActivity(i);
                }
                String team = database.getUserTeam();
                if (team == null){
                    Intent i = new Intent(Splash.this, WelcomeActivity.class);
                    startActivity(i);
                }
                else if(team.equals("nouser")){
                    Intent i = new Intent(Splash.this, WelcomeActivity.class);
                    startActivity(i);
                }
                else{
                    Intent i = new Intent(Splash.this, MapActivity.class);
                    startActivity(i);
                }

            }
        },2000);


    }
}
