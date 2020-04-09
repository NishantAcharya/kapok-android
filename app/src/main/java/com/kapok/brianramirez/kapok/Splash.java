package com.kapok.brianramirez.kapok;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Splash extends AppCompatActivity {

    private FirebaseAuth mAuth;

    //Initialized the data variables to default values
    protected void initialize(){
        Database.mAuth = FirebaseAuth.getInstance();
        Database.currentUser = Database.mAuth.getCurrentUser();
        Database.db = FirebaseFirestore.getInstance();
        if (Database.currentUser != null) {

            DocumentReference docRef = Database.db.collection("Profiles").document(Database.currentUser.getEmail());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Database.hasProfile = true;
                            ArrayList<String> userCurrentTeam = (ArrayList<String>) document.getData().get("team");
                            Database.currentStatus = (String) document.getData().get("status");
                            if(!userCurrentTeam.isEmpty())
                                Database.currentTeam = userCurrentTeam.get(0);
                        }
                        else{
                            Database.hasProfile = false;
                        }

                    }

                }
            });

        }
        else{
            Database.currentTeam = "noUser";
        }
    }

    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //Theme set
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }
        setContentView(R.layout.activity_splash);
        initialize();

//Determining where to go from the splash screen
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                mAuth = Database.mAuth;
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
                    String team = Database.currentTeam;
                    String status = Database.currentStatus;

                    if(!Database.hasProfile){
                        Intent i = new Intent(Splash.this, ProfileSetupActivity.class);
                        startActivity(i);
                    }

                    if (team == null && Database.hasProfile && Database.currentStatus.equalsIgnoreCase("none")){
                        Intent i = new Intent(Splash.this, TeamWelcomeActivity.class);
                        startActivity(i);
                    }


                    if (Database.hasProfile && status.equals("pending")){
                        Intent i = new Intent(Splash.this, JoinWaitActivity.class);
                        startActivity(i);
                    }

                    if (team != null && Database.hasProfile && status.equals("accepted")){
                        Intent i = new Intent(Splash.this, MapActivity.class);
                        startActivity(i);
                    }


                }

            }
        },2000);


    }
}
