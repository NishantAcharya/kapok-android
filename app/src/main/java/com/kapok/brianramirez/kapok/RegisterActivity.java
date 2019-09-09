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

public class RegisterActivity extends AppCompatActivity {

    EditText emailField;
    EditText passwordField;
    EditText confirmPassField;
    Button registerBtn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();


        emailField = (EditText)findViewById(R.id.email_text_field);
        passwordField = (EditText)findViewById(R.id.password_text_field);
        confirmPassField = (EditText) findViewById(R.id.confirm_pass_text_field);
        registerBtn = (Button)findViewById(R.id.register_btn);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailField.getText().toString();
                String password = passwordField.getText().toString();
                String confirmPass = confirmPassField.getText().toString();

                if (isValidRequest(email, password, confirmPass)){
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                        // Sign in success
                                    } else {
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        goToProfileSetup();
                                    }
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
