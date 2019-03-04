package com.kapok.brianramirez.kapok;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Random;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmAsyncTask;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.SyncUser;

public class CreateTeam extends AppCompatActivity {

    Button confirmTeam;
    EditText team_name;
    private RealmAsyncTask asyncTransaction;
    EditText team_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_team);

        team_name = (EditText) findViewById(R.id.teamName);
        team_location = (EditText) findViewById(R.id.location);
        confirmTeam = (Button) findViewById(R.id.create_NewTeam);
        Realm realm = Realm.getDefaultInstance();


        confirmTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RealmList<Person> b  = new RealmList<>();
                Team newTeam = new Team(0, team_name.getText().toString(), team_location.getText().toString());
                RealmManager.add(newTeam);
               // String currentUserAboutme = realm.where(Person.class).equalTo("id", SyncUser.current().getIdentity()).findFirstAsync().getAboutMe();
                //realm.commitTransaction();
              //  int twen=0;
                String currentId = SyncUser.current().getIdentity();
                final Person[] currentUser = null;


/*              Person currentUser = realm.where(Person.class).equalTo("id", currentId).findFirst();
                currentUser.setTeam(newTeam);
                currentUser.setAdmin(true);
                currentUser.setStatus("joined");
                realm.commitTransaction();
                realm.close();
                openCodeDisplay();*/
            }
        });
    }

    public void openCodeDisplay(){
        Intent i = new Intent(this, CodeDisplay.class);
        startActivity(i);
    }
}
