package com.kapok.brianramirez.kapok;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import io.realm.ObjectServerError;
import io.realm.Realm;
import io.realm.SyncCredentials;
import io.realm.SyncUser;

public class RegisterActivity extends AppCompatActivity {

    EditText emailField;
    EditText passwordField;
    EditText confirmPassField;
    Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Realm.init(this);

        emailField = (EditText)findViewById(R.id.email_text_field);
        passwordField = (EditText)findViewById(R.id.password_text_field);
        confirmPassField = (EditText) findViewById(R.id.confirm_pass_text_field);
        registerBtn = (Button)findViewById(R.id.register_btn);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SyncUser.current() != null){
                    SyncUser.current().logOut();
                }

                String email = emailField.getText().toString();
                String password = passwordField.getText().toString();
                String confirmPass = confirmPassField.getText().toString();

                if (isValidRequest(email, password, confirmPass)){
                    SyncCredentials credentials = SyncCredentials.usernamePassword(email, password, true);
                    SyncUser.logInAsync(credentials, Constants.AUTH_URL, new SyncUser.Callback<SyncUser>() {
                        @Override
                        public void onSuccess(SyncUser user) {
                            // User is logged
                            goToProfileSetup();
                        }

                        @Override
                        public void onError(ObjectServerError error) {
                            System.out.println(error.getLocalizedMessage());
                            Toast.makeText(RegisterActivity.this, "Registering  failed....     :(", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    //TODO: show error saying pass don't match or email is too short
                }
            }
        });
    }

    public void goToProfileSetup(){
        Intent goToProfileSetupIntent = new Intent(this , ProfileSetupActivity.class);
        startActivity(goToProfileSetupIntent);
    }

    public static boolean isValidRequest(String email, String password, String confirmPass){
        //TODO: Return true if passwords are the same, and email is longer than 5
        return true;
    }
}
