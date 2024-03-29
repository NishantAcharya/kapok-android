package com.kapok.brianramirez.kapok;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.TimeUnit;

public class userVerifyActivity extends AppCompatActivity {
    Button verify;
    Button resend;
    private FirebaseAuth mAuth;
    FirebaseUser fu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//Theme set
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }
        setContentView(R.layout.activity_user_verify);



        verify = (Button) findViewById(R.id.login_btn);
        resend = (Button) findViewById(R.id.resend_btn);

        mAuth = Database.mAuth;
        String currentUser = mAuth.getCurrentUser().getEmail();
        fu = mAuth.getCurrentUser();
        //User verification is done here
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAuth.getCurrentUser().reload();
                        try
                        {
                            Thread.sleep(2000);
                        }
                        catch(InterruptedException ex)
                        {
                            Thread.currentThread().interrupt();
                        }
                    }
                });

                fu = mAuth.getCurrentUser();
//Leads to profilesetup activity
                if(fu.isEmailVerified()){
                    Intent goToProfileSetupIntent = new Intent(userVerifyActivity.this, ProfileSetupActivity.class);
                    startActivity(goToProfileSetupIntent);
                }
//Try again text
                else if(!fu.isEmailVerified()){
                    Toast.makeText(userVerifyActivity.this, "Verification failed.",
                            Toast.LENGTH_SHORT).show();
                }
            }

        });
        resend.setOnClickListener(new View.OnClickListener() {
            //Resends email to the given mail address
            @Override
            public void onClick(View view) {
                fu.sendEmailVerification()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(userVerifyActivity.this, "Email Sent",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

    }
}
