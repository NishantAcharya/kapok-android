package com.kapok.brianramirez.kapok;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Random;
import java.util.UUID;

import io.realm.RealmList;

public class CreateTeam extends AppCompatActivity {

    Button confirmTeam;
    EditText team_name;
    EditText team_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_team);

        team_name = (EditText) findViewById(R.id.teamName);
        team_location = (EditText) findViewById(R.id.location);
        confirmTeam = (Button) findViewById(R.id.create_NewTeam);

        confirmTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RealmList<Person> b  = new RealmList<>();
                Team newTeam = new Team(0, team_name.getText().toString(), team_location.getText().toString());
                RealmManager.add(newTeam);
                openCodeDisplay();
            }
        });
    }
        public void openCodeDisplay(){

        Intent i = new Intent(this, CodeDisplay.class);
        startActivity(i);

    }
}
