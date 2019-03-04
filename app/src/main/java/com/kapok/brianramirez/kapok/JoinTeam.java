package com.kapok.brianramirez.kapok;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class JoinTeam extends AppCompatActivity {
    Button joinTeam;
    EditText edit_team_code;
    Realm realm = Realm.getDefaultInstance();
    RealmResults<Team> teamCodes;
    int counter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_team);
        teamCodes = realm.where(Team.class).distinct("joinCode").findAll();
        edit_team_code = (EditText) findViewById(R.id.teamCode);
        joinTeam = (Button) findViewById(R.id.joinRequest);
        counter =0;

        joinTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i <teamCodes.size(); i++) {
                    if (teamCodes.get(i).equals(edit_team_code)) {
                        Toast.makeText(JoinTeam.this, "Sending Request...", Toast.LENGTH_SHORT).show();
                        counter = 1;
                        openJoinWait();
                    }
                }

                if (counter == 0)
                {
                    Toast.makeText(JoinTeam.this, "Requesting failed", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
    public void openJoinWait(){

        Intent i = new Intent(this, JoinWaitActivity.class);
        startActivity(i);

    }
}
