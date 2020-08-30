package com.kapok.brianramirez.kapok;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {
    public static boolean notificationcheck;
    public static boolean gravity;
    private FirebaseAuth mAuth;
    private String currentUser;
    private String currentAdmin;
    private boolean isAdmin;
    private ArrayList<String> teamMates = new ArrayList<String>(1);
    private ArrayList<String> teamEmails;
    final Context context = this;
    public boolean member_check = false;
    private String teamcode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Theme set
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }

        setContentView(R.layout.activity_settings);
        FirebaseFirestore db = Database.db;
        mAuth = Database.mAuth;
        currentUser = mAuth.getCurrentUser().getEmail();
        getAdmin();
        getTeam();


        //View setup
        CompoundButton changeThemebtn = (CompoundButton) findViewById(R.id.theme_switch);
        CompoundButton notificationbtn = (CompoundButton) findViewById(R.id.notifcation_switch);
        CompoundButton menubtn = (CompoundButton) findViewById(R.id.drawer_switch);
        TextView adminLine1 = (TextView)findViewById(R.id.admin_text_1);
        View adminLine2 = (View)findViewById(R.id.admin_text_2);
        TextView changeAdminBtn = (TextView)findViewById(R.id.change_admin_button);
        TextView changeTeamNameBtn = (TextView)findViewById(R.id.change_team_name);
        TextView changeTeamLocBtn = (TextView)findViewById(R.id.change_team_location);
        TextView leaveTeamBtn = (TextView)findViewById(R.id.leave_team);

        if(!Database.isAdmin){
            adminLine1.setVisibility(View.GONE);
            adminLine2.setVisibility(View.GONE);
            changeAdminBtn.setVisibility(View.GONE);
            changeTeamNameBtn.setVisibility(View.GONE);
            changeTeamLocBtn.setVisibility(View.GONE);
        }
        else{
            adminLine1.setVisibility(View.VISIBLE);
            adminLine2.setVisibility(View.VISIBLE);
            changeAdminBtn.setVisibility(View.VISIBLE);
            changeTeamNameBtn.setVisibility(View.VISIBLE);
            changeTeamLocBtn.setVisibility(View.VISIBLE);
        }



        //Setting the checked value of the slider according to the status of the service
        if(notificationcheck){
            notificationbtn.setChecked(true);
        }
        else {
            notificationbtn.setChecked(false);
        }

        Intent notification = new Intent(SettingsActivity.this, DatabaseListener.class);;

        //Working of the notification button

        notificationbtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    notificationcheck = true;
                    startService(notification);
                } else {
                    // The toggle is disabled
                    notificationcheck = false;
                    stopService(notification);
                }
            }
        });


        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            changeThemebtn.setChecked(true);
        }
        else{
            changeThemebtn.setChecked(false);
        }

        changeThemebtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
                else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
                finish();
                startActivity(getIntent());
            }
        });

        //Changing menu gravity
        if(gravity){
            changeThemebtn.setChecked(true);
        }
        else{
            changeThemebtn.setChecked(false);
        }

        menubtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    gravity = true;
                }
                else{
                    gravity = false;
                }

            }
        });

        leaveTeamBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Database.isAdmin){
                    if(teamMates.size()>1) {

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context,R.style.AlertDialog);


                        // set dialog message
                        alertDialogBuilder
                                .setMessage("You are the admin of the team, you have to perform either one of the activity before proceeding to leave the team!")
                                .setCancelable(false)
                                .setPositiveButton("Close this activity and choose a new team admin", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // if this button is clicked, close
                                        // current activity
                                        dialog.cancel();
                                    }
                                })
                                .setNegativeButton("I wish to dissolve the team regardless", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // if this button is clicked, just close
                                        // the dialog box and do nothing
                                        removeFromTeam();
                                        //DISSOLVE IT....
                                    }
                                });

                        // create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();

                        // show it
                        alertDialog.show();
                    }

                    else if (!hasMembers()){
                        removeFromTeam();

                    }

                }
                else {
                    removeFromTeam();
                }
            }
        });




    }

    private String getAdmin(){
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
                                        currentAdmin = document.getData().get("admin").toString();
                                    }
                                }
                            }
                        });

                    }
                }
            }
        });
        return currentAdmin;
    }

    void getTeam(){
        mAuth = Database.mAuth;
        FirebaseUser currentUser = mAuth.getCurrentUser();
        FirebaseFirestore db = Database.db;
        DocumentReference docRef = Database.db.collection("Profiles").document(currentUser.getEmail());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        ArrayList<String> userCurrentTeam = (ArrayList<String>) document.getData().get("team");
                        String TeamCode = userCurrentTeam.get(0);
                        DocumentReference docRef = Database.db.collection("Teams").document(TeamCode);
                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {

                                        teamEmails = ((ArrayList<String>) document.getData().get("members"));
                                        for(int i = 0; i < teamEmails.size(); i++){
                                            DocumentReference docRef = db.collection("Profiles").document(teamEmails.get(i));
                                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if(task.isSuccessful()){
                                                        DocumentSnapshot document = task.getResult();
                                                        if(document.exists()){
                                                            teamMates.add((String)document.getData().get("name"));
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
                }
            }
        });
    }

    public boolean hasMembers(){

        FirebaseFirestore db = Database.db;
        DocumentReference userProf = db.collection("Profiles").document(currentUser);
        userProf.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task)
            {
                if (task.isSuccessful())
                {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists())
                    {
                        ArrayList<String> team = (ArrayList<String>) document.getData().get("team");

                        DocumentReference docRef = db.collection("Teams").document(team.get(0));
//
                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot teamDoc = task.getResult();
                                    if (teamDoc.exists()) {
                                        ArrayList<String> members = (ArrayList<String>) teamDoc.get("members");
                                        if (members.size() > 1) {
                                            member_check = true;

                                        }
                                    } else {
                                    }
                                }
                            }
                        });
                    }
                }
            }
        });
        return member_check;
    }

    //Method to remove from team uses hasMemebers
    public void removeFromTeam() {
        FirebaseFirestore db = Database.db;
        DocumentReference userProf = db.collection("Profiles").document(currentUser.toString());
        AlertDialog.Builder a = new AlertDialog.Builder(SettingsActivity.this,R.style.AlertDialog);
        a.setMessage("Are you sure you want to leave the team").setCancelable(true)
                .setPositiveButton("Accept", new DialogInterface.OnClickListener() {

                    //If user accepts the request
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(!Database.isAdmin) {
                            userProf.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            ArrayList<String> team = (ArrayList<String>) document.getData().get("team");
                                            teamcode = team.get(0);
                                            DocumentReference teamRef = db.collection("Teams").document(team.get(0));
                                            teamRef.update("members", FieldValue.arrayRemove(currentUser));
                                        }
                                    }
                                    userProf.update("status", "none");
                                    userProf.update("isAdmin", false);
                                    userProf.update("team", FieldValue.arrayRemove(teamcode));

                                    Intent intent = new Intent(SettingsActivity.this, TeamWelcomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        }
                        else{
                            userProf.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            ArrayList<String> team = (ArrayList<String>) document.getData().get("team");
                                            teamcode = team.get(0);
                                            DocumentReference teamRef = db.collection("Teams").document(team.get(0));
                                            teamRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if(task.isSuccessful()){
                                                        DocumentSnapshot document = task.getResult();
                                                        if(document.exists()){
                                                            ArrayList<String> members = (ArrayList<String>) document.get("members");
                                                            for(String member: members){
                                                                teamRef.update("members", FieldValue.arrayRemove(member));
                                                                DocumentReference usrPref = db.collection("Profiles").document(member);
                                                                usrPref.update("status", "none");
                                                                usrPref.update("isAdmin", false);
                                                                usrPref.update("team", FieldValue.arrayRemove(teamcode));

                                                            }
                                                        }
                                                    }
                                                }
                                            });
                                        }
                                    }
                                }
                            });
                            Intent intent = new Intent(SettingsActivity.this, TeamWelcomeActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
        a.create();
        a.show();
    }

}
