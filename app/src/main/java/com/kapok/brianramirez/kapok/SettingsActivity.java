package com.kapok.brianramirez.kapok;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {
    public static boolean notificationcheck;
    public static boolean gravity;
    private FirebaseAuth mAuth;
    private String currentUser;
    private String currentAdmin;
    private boolean isAdmin;

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


        //View setup
        CompoundButton changeThemebtn = (CompoundButton) findViewById(R.id.theme_switch);
        CompoundButton notificationbtn = (CompoundButton) findViewById(R.id.notifcation_switch);
        CompoundButton menubtn = (CompoundButton) findViewById(R.id.drawer_switch);
        TextView adminLine1 = (TextView)findViewById(R.id.admin_text_1);
        View adminLine2 = (View)findViewById(R.id.admin_text_2);
        TextView changeAdminBtn = (TextView)findViewById(R.id.change_admin_button);
        TextView changeTeamNameBtn = (TextView)findViewById(R.id.change_team_name);
        TextView changeTeamLocBtn = (TextView)findViewById(R.id.change_team_location);

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

}
