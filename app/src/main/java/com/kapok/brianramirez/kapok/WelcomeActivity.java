package com.kapok.brianramirez.kapok;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


public class WelcomeActivity extends AppCompatActivity {

    Button signInBtn;
    Button registerBtn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }
        setContentView(R.layout.activity_welcome);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection("Profiles").document(currentUser.getEmail());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            //ArrayList<String> status = (ArrayList<String>) document.getData().get("status");
                            String status = (String) document.getData().get("status");
                            ArrayList<String> userCurrentTeam = (ArrayList<String>) document.getData().get("team");

                            if (userCurrentTeam.isEmpty()) {
                                if (status.equals("None")) {

                                    Intent i = new Intent(WelcomeActivity.this, TeamWelcomeActivity.class);
                                    startActivity(i);

                                }

                                if (status.equals("accepted")) {

                                    Intent i = new Intent(WelcomeActivity.this, MapActivity.class);
                                    startActivity(i);

                                }
                                if (status.equals("pending")) {
                                    Intent i = new Intent(WelcomeActivity.this, JoinWaitActivity.class);
                                    startActivity(i);
                                }

                            } else {
                                Intent i = new Intent(WelcomeActivity.this, MapActivity.class);
                                startActivity(i);
                            }
                        }
                    }
                }
            });
        } else {
            signInBtn = findViewById(R.id.sign_in_btn);
            registerBtn = findViewById(R.id.register_btn);

            signInBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent goToLogInIntent = new Intent(WelcomeActivity.this, LogInActivity.class);
                    startActivity(goToLogInIntent);
                }
            });

            registerBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent goToRegisterIntent = new Intent(WelcomeActivity.this, RegisterActivity.class);
                    startActivity(goToRegisterIntent);
                }
            });
        }
    }
}

