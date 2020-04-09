package com.kapok.brianramirez.kapok;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CreateTeamActivity extends AppCompatActivity {

    Button confirmTeam;
    EditText team_name;
    EditText team_location;
    String teamID;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Theme Change
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }
        setContentView(R.layout.activity_create_team);

        //Setting up the textviews
        team_name = findViewById(R.id.teamName);
        team_location = findViewById(R.id.location);
        confirmTeam = findViewById(R.id.create_NewTeam);

        mAuth = Database.mAuth;
        String currentUser = mAuth.getCurrentUser().getEmail();

    //Working of the button
        confirmTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseFirestore db = Database.db;

                //create list containing the user
                ArrayList<String> members = new ArrayList<String>(1);
                ArrayList<String> membersn = new ArrayList<String>(1);
                members.add(currentUser);
                DocumentReference teamRef = db.collection("Profiles").document(currentUser);

                //Removing from the requests
                teamRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            String admin = (String) document.getData().get("name");
                            membersn.add(admin);
                            teamID = genTeamCode(db);
                            ArrayList<Map<String, Object>> logs = new ArrayList<Map<String, Object>>(1);

                            // Create a new user with a first and last name
                            Map<String, Object> team = new HashMap<>();
                            team.put("name", team_name.getText().toString());
                            team.put("location", team_location.getText().toString());
                            team.put("members", members);
                            team.put("membersn", membersn);
                            team.put("id", teamID);
                            team.put("logs", logs);
                            team.put("admin",currentUser);

                            db.collection("Teams").document(teamID)
                                    .set(team)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            DocumentReference userProf = db.collection("Profiles").document(currentUser);
                                            // Set the admin field of the current user to true
                                            userProf
                                                    .update("isAdmin", true);
                                            userProf
                                                    .update("team", FieldValue.arrayUnion(teamID));
                                            userProf
                                                    .update("status", "accepted");
                                            openCodeDisplay();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(CreateTeamActivity.this, "Team Setup failed.",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                });

                }
            });
        }
        //Leads to wait screen
    public void openCodeDisplay(){
        Intent i = new Intent(this, WaitingScreenActivity.class).putExtra("Team Code", teamID);
        startActivity(i);
    }

    //gets the team code for the newly created team
    private String genTeamCode(FirebaseFirestore db){
        do{
            Random rand = new Random();
            int number = rand.nextInt(1000000)+100000;
            final String[] code = {Integer.toString(number)};
            DocumentReference docRef = db.collection("Teams").document(code[0]);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            code[0] = null;
                        }
                    } else {
                    }
                }
            });
            if(code[0] != null) {
                return code[0];
            }
        }
        while(true);
    }
}
