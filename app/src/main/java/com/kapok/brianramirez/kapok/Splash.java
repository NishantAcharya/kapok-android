package com.kapok.brianramirez.kapok;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Timer;
import java.util.TimerTask;

public class Splash extends AppCompatActivity {

    private Database database;
    private FirebaseAuth mAuth;

    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        database = new Database();
        mAuth = Database.mAuth;


        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user == null){
                    Intent i = new Intent(Splash.this, WelcomeActivity.class);
                    startActivity(i);
                }
                else if(!user.isEmailVerified()){
                    Intent i = new Intent(Splash.this, userVerifyActivity.class);
                    startActivity(i);
                }

                else if (user.isEmailVerified()){
                    String team = database.getUserTeam();
                    String status = database.getUserStatus();

                    if(team == null && !Database.hasProfile){
                        Intent i = new Intent(Splash.this, ProfileSetupActivity.class);
                        startActivity(i);
                    }

                    if (team == null && Database.hasProfile){
                        Intent i = new Intent(Splash.this, TeamWelcomeActivity.class);
                        startActivity(i);
                    }


                    if (team != null && Database.hasProfile && status.equals("rejected")){
                        Intent i = new Intent(Splash.this, TeamWelcomeActivity.class);
                        startActivity(i);
                    }


                    if (team != null && Database.hasProfile && status.equals("pending")){
                        Intent i = new Intent(Splash.this, WaitingScreenActivity.class);
                        startActivity(i);
                    }

                    else if (team != null && Database.hasProfile && status.equals("accepted")){
                        Intent i = new Intent(Splash.this, MapActivity.class);
                        startActivity(i);
                    }


                }

            }
        },2000);


    }
}
