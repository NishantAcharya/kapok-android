package com.kapok.brianramirez.kapok;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

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

    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
//        getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                if (currentUser != null) {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    DocumentReference docRef = db.collection("Profiles").document(currentUser.getEmail());
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    ArrayList<String> userCurrentTeam = (ArrayList<String>) document.getData().get("team");

                                    if (userCurrentTeam.get(0) != null){
                                        Intent i = new Intent(Splash.this, MapActivity.class);
                                        startActivity(i);
                                    }

                                }
                                else {
                                    Intent i = new Intent(Splash.this, JoinTeamActivity.class);
                                    startActivity(i);
                                }

                            }

                        }
                    });

                }

                else{
                        Intent i = new Intent(Splash.this, WelcomeActivity.class);
                        startActivity(i);
                }

            }
        },2000);


    }
}
