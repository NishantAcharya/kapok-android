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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import io.realm.Realm;
import io.realm.RealmResults;

public class JoinTeamActivity extends AppCompatActivity {
    Button joinTeam;
    EditText edit_team_code;
    Realm realm = Realm.getDefaultInstance();
    RealmResults<Team> teamCodes;
    int counter;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_team);
//        teamCodes = realm.where(Team.class).distinct("joinCode").findAll();
        edit_team_code = (EditText) findViewById(R.id.teamCode);
        joinTeam = (Button) findViewById(R.id.joinRequest);
        counter =0;
        mAuth = FirebaseAuth.getInstance();
        String currentUser = mAuth.getCurrentUser().getEmail();

        joinTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                StringBuilder stringBuilder = new StringBuilder();
//                for (int i = 0; i <teamCodes.size(); i++) {
//                    if (teamCodes.get(i).equals(edit_team_code)) {
//                        Toast.makeText(JoinTeamActivity.this, "Sending Request...", Toast.LENGTH_SHORT).show();
//                        counter = 1;
//
//                        openJoinWait();
//                    }
//                }
//
//                if (counter == 0)
//                {
//                    Toast.makeText(JoinTeamActivity.this, "Requesting failed", Toast.LENGTH_SHORT).show();
//
//                }
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference docRef = db.collection("Teams").document(edit_team_code.getText().toString());
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String admin = (String) document.getData().get("admin");
                                DocumentReference adminRef = db.collection("Profiles").document(admin);
                                adminRef.update("requests", FieldValue.arrayUnion(currentUser))
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                    }
                                })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                            }
                                        });
//                                docRef.update("members", FieldValue.arrayUnion(currentUser))
//                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                            @Override
//                                            public void onSuccess(Void aVoid) {
//                                                DocumentReference userProf = db.collection("Profiles").document(currentUser);
//                                                userProf
//                                                        .update("team", FieldValue.arrayUnion(edit_team_code.getText().toString()))
//                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                            @Override
//                                                            public void onSuccess(Void aVoid) {
//                                                                //TODO
//                                                            }
//                                                        })
//                                                        .addOnFailureListener(new OnFailureListener() {
//                                                            @Override
//                                                            public void onFailure(@NonNull Exception e) {
//                                                            }
//                                                        });
//                                            }
//                                        })
//                                        .addOnFailureListener(new OnFailureListener() {
//                                            @Override
//                                            public void onFailure(@NonNull Exception e) {
//                                            }
//                                        });

                            }
                            else {
                                Toast.makeText(JoinTeamActivity.this, "Team Does not exist",
                                        Toast.LENGTH_SHORT).show();
                            }
                            openJoinWait(edit_team_code.getText().toString());
                        }
                        else {

                        }
                    }
                });
            }
        });
    }
    public void openJoinWait(String team){

        Intent i = new Intent(this, JoinWaitActivity.class).putExtra("teamid", team);
        startActivity(i);

    }
}
