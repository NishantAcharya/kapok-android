package com.kapok.brianramirez.kapok;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import io.realm.ObjectServerError;
import io.realm.Realm;
import io.realm.SyncCredentials;
import io.realm.SyncUser;

public class LogInActivity extends AppCompatActivity {

    EditText emailField;
    EditText passwordField;
    Button logInBtn;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        mAuth = FirebaseAuth.getInstance();

        emailField = (EditText) findViewById(R.id.email_text_field);
        passwordField = (EditText) findViewById(R.id.password_text_field);
        logInBtn = (Button) findViewById(R.id.log_in_btn);

        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailField.getText().toString();
                String password = passwordField.getText().toString();

                if (isValidRequest(email, password)){
//                    SyncCredentials credentials = SyncCredentials.usernamePassword(email, password, false);
//                    SyncUser.logInAsync(credentials, Constants.AUTH_URL, new SyncUser.Callback<SyncUser>() {
//                        @Override
//                        public void onSuccess(SyncUser user) {
//                            // User is logged
//                            Toast.makeText(LogInActivity.this, "User Signed in successful", Toast.LENGTH_SHORT).show();
//                            Intent i = new Intent(LogInActivity.this, TeamWelcomeActivity.class);
//                            startActivity(i);
//                        }
//
//                        @Override
//                        public void onError(ObjectServerError error) {
//                            System.out.println(error);
//                            Toast.makeText(LogInActivity.this, "Log in failed....     :(", Toast.LENGTH_SHORT).show();
//                        }
//                    });

                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LogInActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        Toast.makeText(LogInActivity.this, "User Signed in successful", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(LogInActivity.this, TeamWelcomeActivity.class);
                                        startActivity(i);
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

    public static boolean isValidRequest(String email, String password){
        //TODO: Return true if both fields are not empty and are larget than length 5
        return true;
    }








}
