package com.kapok.brianramirez.kapok;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

//Going over this
public class LogInActivity extends AppCompatActivity {

    EditText emailField;
    EditText passwordField;
    Button logInBtn;
    Button resetBtn;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Theme set
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }
        setContentView(R.layout.activity_log_in);

        mAuth = Database.mAuth;

        //Setting up views
        emailField = findViewById(R.id.email_text_field);
        passwordField = findViewById(R.id.password_text_field);
        logInBtn = findViewById(R.id.log_in_btn);
        resetBtn = findViewById(R.id.resetBtn);

        //Reset button leads to forgot password activity
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LogInActivity.this, ForgotPasswordActivity.class);
                startActivity(i);

            }
        });

        //Login button working
        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailField.getText().toString();
                String password = passwordField.getText().toString();
                if (isValidRequest(email, password)) {
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LogInActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        // Sign in success, update UI with the signed-in user's information
                                        mAuth = FirebaseAuth.getInstance();
                                        FirebaseUser currentUser = mAuth.getCurrentUser();
                                        Toast.makeText(LogInActivity.this, "User sign in successful", Toast.LENGTH_SHORT).show();
                                        FirebaseFirestore db = Database.db;
                                        DocumentReference docRef = db.collection("Profiles").document(currentUser.getEmail());
                                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot document = task.getResult();
                                                    if (document.exists()) {
                                                        ArrayList<String> userCurrentTeam = (ArrayList<String>) document.getData().get("team");

                                                        if (!userCurrentTeam.isEmpty()) {
                                                            Intent i = new Intent(LogInActivity.this, MapActivity.class);
                                                            startActivity(i);
                                                        }

                                                    } else {
                                                        Intent i = new Intent(LogInActivity.this, TeamWelcomeActivity.class);
                                                        startActivity(i);
                                                    }

                                                }

                                            }
                                        });
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(LogInActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }

                                    // ...
                                }
                            });
                } else {
                    //TODO: show error saying that username and pass are too short or empty
                }

            }
        });

    }

    public static boolean isValidRequest(String email, String password) {
        //TODO: Return true if both fields are not empty and are larget than length 5

        return true;
    }




}
