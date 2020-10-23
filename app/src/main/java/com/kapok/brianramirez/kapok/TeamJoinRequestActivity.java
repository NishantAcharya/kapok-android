package com.kapok.brianramirez.kapok;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
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

public class TeamJoinRequestActivity extends AppCompatActivity {

    private ArrayList<String> allRequests;
    private ArrayList<String> allNames = new ArrayList<String>(1);
    private FirebaseAuth mAuth;
    private ListView lv;
    private boolean isAdmin;
    private ArrayList<String> teamId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//Theme set
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }

        //Setting the UI
        setContentView(R.layout.activity_team_join_request);
        lv = findViewById(R.id.listView);
        //Setting the UI

        //Firebase instance
        mAuth = Database.mAuth;
        FirebaseFirestore db = Database.db;

        //Current User
        String currentUser = mAuth.getCurrentUser().getEmail();
        isAdmin();

        DocumentReference userProf = db.collection("Profiles").document(currentUser);
        userProf.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        allRequests = (ArrayList<String>) document.getData().get("requests");
                        teamId = (ArrayList<String>) document.getData().get("team");

                        for(int i = 0; i < allRequests.size(); i++){
                            DocumentReference docRef = db.collection("Profiles").document(allRequests.get(i));
                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task){
                                    if(task.isSuccessful()){
                                        DocumentSnapshot document = task.getResult();
                                        if(document.exists()){
                                            allNames.add((String)document.getData().get("name"));
                                            //Array Adapter of requests
                                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(TeamJoinRequestActivity.this, android.R.layout.simple_list_item_1, allNames);
                                            lv.setAdapter(arrayAdapter);
                                            //On Request Click
                                            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                    if (Database.isAdmin){
                                                        AlertDialog.Builder a = new AlertDialog.Builder(TeamJoinRequestActivity.this, R.style.AlertDialog);
                                                    a.setMessage("This member wants to join the team").setCancelable(true)
                                                            .setPositiveButton("Accept", new DialogInterface.OnClickListener() {

                                                                //If user accepts the request
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    String requester = allRequests.get(position);

                                                                    //Reference for the Requester
                                                                    DocumentReference docRef = db.collection("Profiles").document(requester);

                                                                    //Change the status to Accepted
                                                                    docRef.update("status", "accepted")
                                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                @Override
                                                                                public void onSuccess(Void aVoid) {
                                                                                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                            if (task.isSuccessful()) {
                                                                                                DocumentSnapshot document = task.getResult();
                                                                                                String name = (String) document.getData().get("name");
                                                                                                DocumentReference teamRef = db.collection("Teams").document(teamId.get(0));
                                                                                                teamRef.update("membersn", FieldValue.arrayUnion(name));
                                                                                            }

                                                                                        }
                                                                                    });
                                                                                    DocumentReference teamRef = db.collection("Teams").document(teamId.get(0));

                                                                                    //Removing from the requests
                                                                                    teamRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                            if (task.isSuccessful()) {
                                                                                                DocumentSnapshot document = task.getResult();
                                                                                                String admin = (String) document.getData().get("admin");
                                                                                                DocumentReference docRefAdmin = db.collection("Profiles").document(admin);
                                                                                                docRefAdmin.update("requests", FieldValue.arrayRemove(requester));
                                                                                            }
                                                                                        }
                                                                                    });

                                                                                    //Adding the requester as a member in the team document
                                                                                    teamRef
                                                                                            .update("members", FieldValue.arrayUnion(requester))
                                                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                @Override
                                                                                                public void onSuccess(Void aVoid) {
                                                                                                    Toast.makeText(TeamJoinRequestActivity.this, "Request accepted!",
                                                                                                            Toast.LENGTH_SHORT).show();
                                                                                                }
                                                                                            });
                                                                                    teamRef.update("membersn", FieldValue.arrayUnion());
                                                                                }
                                                                            })

                                                                            //If this fails
                                                                            .addOnFailureListener(new OnFailureListener() {
                                                                                @Override
                                                                                public void onFailure(@NonNull Exception e) {
                                                                                    Toast.makeText(TeamJoinRequestActivity.this, "Request failed!",
                                                                                            Toast.LENGTH_SHORT).show();

                                                                                }
                                                                            });

                                                                    //Adding the team to the requester document
                                                                    docRef.update("team", FieldValue.arrayUnion(teamId.get(0)))
                                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                @Override
                                                                                public void onSuccess(Void aVoid) {
                                                                                    DocumentReference teamRef = db.collection("Teams").document(teamId.get(0));
                                                                                    teamRef
                                                                                            .update("members", FieldValue.arrayUnion(requester));
                                                                                }
                                                                            });
                                                                    TeamJoinRequestActivity.this.recreate();
                                                                }

                                                            })

                                                            //If request is denied
                                                            .setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    String requester = allRequests.get(position);

                                                                    //Reference to the requester
                                                                    DocumentReference docRef = db.collection("Profiles").document(requester);

                                                                    //Change the status to none
                                                                    docRef.update("status", "None").addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void aVoid) {
                                                                            DocumentReference teamRef = db.collection("Teams").document(teamId.get(0));
                                                                            //Removing from the requests
                                                                            teamRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                    if (task.isSuccessful()) {
                                                                                        DocumentSnapshot document = task.getResult();
                                                                                        String admin = (String) document.getData().get("admin");
                                                                                        DocumentReference docRefAdmin = db.collection("Profiles").document(admin);
                                                                                        docRefAdmin.update("requests", FieldValue.arrayRemove(requester));
                                                                                    }
                                                                                }
                                                                            });
                                                                        }
                                                                    });
                                                                    Toast.makeText(TeamJoinRequestActivity.this, "Request denied!", Toast.LENGTH_SHORT).show();
                                                                    TeamJoinRequestActivity.this.recreate();
                                                                }
                                                            }).show();
                                                }
                                                    else {
                                                        Toast.makeText(TeamJoinRequestActivity.this, "You are not the Administrator!", Toast.LENGTH_SHORT).show();
                                                    }
                                            }
                                            });
                                        }
                                    }

                                }
                            });
                        }
                    }
                }
            }
        });

    }

    private boolean isAdmin() {
        mAuth = Database.mAuth;
        String currentUser = mAuth.getCurrentUser().getEmail();
        FirebaseFirestore db = Database.db;
        DocumentReference docRef = db.collection("Profiles").document(currentUser);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        ArrayList<String> userCurrentTeam = (ArrayList<String>) document.getData().get("team");
                        String TeamCode = userCurrentTeam.get(0);
                        DocumentReference docRef = db.collection("Teams").document(TeamCode);
                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()){
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()){
                                        String currentAdmin = document.getData().get("admin").toString();
                                        Database.isAdmin = currentUser.equals(currentAdmin);
                                    }
                                }
                            }
                        });

                    }
                }
            }
        });
        return isAdmin;
    }

}




