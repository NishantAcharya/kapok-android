package com.kapok.brianramirez.kapok;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import io.realm.SyncUser;

public class ProfileSetupActivity extends AppCompatActivity {
    EditText fullNameField;
    EditText occupationField;
    EditText contactInfoField;
    EditText aboutMeField;
    Button finishProfileBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup);
        fullNameField = findViewById(R.id.full_name_text_field);
        occupationField = findViewById(R.id.occupation_text_field);
        contactInfoField = findViewById(R.id.contact_info_text_field);
        aboutMeField = findViewById(R.id.about_me_text_field);
        finishProfileBtn = findViewById(R.id.finish_profile_btn);


        finishProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Person newP = new Person(SyncUser.current().getIdentity(), fullNameField.getText().toString(), occupationField.getText().toString(), contactInfoField.getText().toString(), aboutMeField.getText().toString());
               RealmManager.add(newP);
               goToTeamWelcome();
            }
        });
    }

    public void goToTeamWelcome(){
        Intent goToTeamIntent = new Intent(this, TeamWelcomeActivity.class);
        startActivity(goToTeamIntent);
    }



}
