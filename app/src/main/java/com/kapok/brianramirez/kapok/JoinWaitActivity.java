package com.kapok.brianramirez.kapok;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;

//The wait screen for joining
public class JoinWaitActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    AnimationDrawable logoani;
    ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Theme set
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }
        setContentView(R.layout.activity_waiting_to_join);

        //Getting data
        mAuth = Database.mAuth;
        String currentUser = mAuth.getCurrentUser().getEmail();
        FirebaseFirestore db = Database.db;

        Intent intent = getIntent();
        String team_id = intent.getStringExtra("teamid");

        logo = findViewById(R.id.imageView2);

        //Creating the entering animation
       // logo.setBackgroundResource(R.drawable.kapok_animation_loop);
        //Animation disabled for testing
        //logoani = (AnimationDrawable) logo.getBackground();

        TextView teamText = findViewById(R.id.teamText);
        Button cancel = findViewById(R.id.cancel_request);
        DocumentReference docRef = db.collection("Profiles").document(currentUser);
        DocumentReference teamRef = db.collection("Teams").document(team_id);
        teamRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        teamText.setText(document.getData().get("name").toString());
                    }
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                teamRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if(document.exists()){
                                ArrayList<String> members = (ArrayList<String>) document.get("members");
                                String admin = members.get(0);
                                DocumentReference adminRef = db.collection("Profiles").document(admin);
                                adminRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if(task.isSuccessful()){
                                            DocumentSnapshot document = task.getResult();
                                            if(document.exists()){
                                                docRef.update("status","none");
                                                docRef.update("requests", FieldValue.arrayRemove(currentUser));

                                            }
                                        }
                                    }
                                });
                            }
                        }
                    }
                });
                finish();
            }
        });

        //staying until request gets accepted else opening map activty

        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {

                if (snapshot != null && snapshot.exists()) {
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    ArrayList<String> team = (ArrayList<String>) document.getData().get("team");
                                    if(!team.isEmpty()){
                                        openMaps();
                                    }
                                }
                            }
                        }
                    });
                }
            }
        });
    }

    //Starts the animation
    /*@Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        logoani.start();
    }*/

    //The back press is empty, this is for what need sto happen when you press back
    @Override
    public void onBackPressed() {
        //DO NOTHING
    }

    //opens map activty
    public void openMaps(){
        Intent intent = new Intent(this,MapActivity.class);
        startActivity(intent);
    }
}
