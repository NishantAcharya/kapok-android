package com.kapok.brianramirez.kapok;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Ref;
import java.util.ArrayList;

public class ShowMemberActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    String member;
    String teamcode;
    ArrayList<String> requests;
    private boolean isAdmin;
    private String currentUser;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }
        setContentView(R.layout.activity_show_members);


        Intent intent = getIntent();
        FirebaseFirestore db = Database.db;
        member = intent.getStringExtra("Member");
        mAuth = Database.mAuth;


        TextView nameText = findViewById(R.id.full_name_text_field);
        TextView occupationText = findViewById(R.id.occupation_text_field);
        TextView contactInfoText = findViewById(R.id.contact_info_text_field);
        TextView aboutText = findViewById(R.id.about_me_text_field);
        TextView registeredEmailText = findViewById(R.id.registered_email_text_field);

        currentUser = mAuth.getCurrentUser().getEmail();
        isAdmin();



        DocumentReference memberRef = db.collection("Profiles").document(member);
        memberRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String name = (String) document.getData().get("name");
                        String occupation = (String) document.getData().get("occupation");
                        String contactInfo = (String) document.getData().get("contactInfo");
                        String about = (String) document.getData().get("about");
                        String registeredEmail = (String) member;

                        nameText.setText(name);
                        occupationText.setText(occupation);
                        contactInfoText.setText(contactInfo);
                        aboutText.setText(about);
                        registeredEmailText.setText(registeredEmail);

                    }
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.member_kickout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.kickOut) {
            if(isAdmin()) {
                    removeFromTeam();
            }
            else{
                Toast.makeText(ShowMemberActivity.this, "You are not the Administrator", Toast.LENGTH_SHORT).show();
            }
        }

        if (id == R.id.makeAdmin) {
            if (isAdmin()) {
                FirebaseFirestore db = Database.db;
                    DocumentReference userRef = db.collection("Profiles").document(Database.currentUser.getEmail());
                    userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    teamcode = ((ArrayList<String>) document.get("team")).get(0);
                                    DocumentReference teamref = db.collection("Teams").document(teamcode);
                                    teamref.update("admin", member);
                                }
                            }
                        }
                    });
                    userRef.update("isAdmin", false);


                    DocumentReference userProf = db.collection("Profiles").document(member);
                    // Set the admin field of the current user to true
                    userProf.update("requests", FieldValue.arrayUnion(requests));
                    userProf.update("isAdmin", true);

                    userRef.update("requests", FieldValue.arrayRemove(requests));
            }
            else{
                Toast.makeText(ShowMemberActivity.this, "You are not the Administrator", Toast.LENGTH_SHORT).show();
            }
        }


        return super.onOptionsItemSelected(item);
    }


    public void removeFromTeam() {
        FirebaseFirestore db = Database.db;
        DocumentReference userProf = db.collection("Profiles").document(member);
        AlertDialog.Builder a = new AlertDialog.Builder(ShowMemberActivity.this);
        a.setMessage("Are you sure you want to kick this member out of the team?").setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    //If user accepts the request
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        userProf.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        ArrayList<String> team = (ArrayList<String>) document.getData().get("team");
                                        teamcode=team.get(0);
                                        DocumentReference teamRef = db.collection("Teams").document(team.get(0));
                                        teamRef.update("members", FieldValue.arrayRemove(member));
                                    }
                                }
                                userProf.update("status", "none");
                                userProf.update("team", FieldValue.arrayRemove(teamcode)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Intent intent = new Intent(ShowMemberActivity.this, TeamDIsplayActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });

                            }
                        });
                    }
                });
        a.create();
        a.show();

    }



    private boolean isAdmin() {
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
                                        isAdmin = currentUser.equals(currentAdmin);
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


    public  void openJoinTeam(){
        Intent intent = new Intent(this, JoinTeamActivity.class);
        startActivity(intent);
    }

}

