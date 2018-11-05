package com.kapok.brianramirez.kapok;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import io.realm.ObjectServerError;
import io.realm.Realm;
import io.realm.SyncCredentials;
import io.realm.SyncUser;

public class WelcomeActivity extends AppCompatActivity {

    Button signInBtn;
    Button registerBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Realm.init(this);


        if (SyncUser.current() != null) {
            Intent i = new Intent(WelcomeActivity.this, TeamWelcomeActivity.class);
            startActivity(i);
        }

       else{
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
