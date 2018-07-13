package com.kapok.brianramirez.kapok;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import io.realm.ObjectServerError;
import io.realm.SyncCredentials;
import io.realm.SyncUser;

public class LogInActivity extends AppCompatActivity {

    EditText emailField;
    EditText passwordField;
    Button logInBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        emailField = (EditText) findViewById(R.id.email_text_field);
        passwordField = (EditText) findViewById(R.id.password_text_field);
        logInBtn = (Button) findViewById(R.id.log_in_btn);

        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailField.getText().toString();
                String password = passwordField.getText().toString();

                if (isValidRequest(email, password)){
                    SyncCredentials credentials = SyncCredentials.usernamePassword(email, password, false);
                    SyncUser.logInAsync(credentials, Constants.AUTH_URL, new SyncUser.Callback<SyncUser>() {
                        @Override
                        public void onSuccess(SyncUser user) {
                            // User is logged
                            Toast.makeText(LogInActivity.this, "User Signed in successful", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(ObjectServerError error) {
                            System.out.println(error);
                            Toast.makeText(LogInActivity.this, "Log in failed....     :(", Toast.LENGTH_SHORT).show();
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
