package com.kapok.brianramirez.kapok;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AssignedLogAllActivity extends AppCompatActivity {
    Button addLogListView;
    Map<String, Object> log;
    private ListView lv;
    private ArrayList<String> memberName = new ArrayList<String>(1);
    String clickedMember;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> members = new ArrayList<String>(1);
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //theme set
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }
        //Setting uo the UI
        setContentView(R.layout.activity_assigned_log_all);
        lv = findViewById(R.id.listView);
        addLogListView = findViewById(R.id.addLogListView);

        //FireBase Instance
        mAuth = Database.mAuth;
        FirebaseUser currentUser = mAuth.getCurrentUser(); //Current User
        FirebaseFirestore db = Database.db;

        //Putting the memebres in the listview activity here
        DocumentReference docRef = db.collection("Profiles").document(currentUser.getEmail());
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
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        ArrayList<Map<String, Object>> logs = new ArrayList<>(1);
                                        logs = (ArrayList<Map<String, Object>>)document.getData().get("logs");
                                        ArrayList<String> AssignedLogs = new ArrayList<>(1);
                                        for(int i = 0; i < logs.size(); i++){
                                            String assignment = logs.get(i).get("assignment").toString();
                                            String location = logs.get(i).get("location").toString();

                                            if(!assignment.equals("No Assignment")){
                                                AssignedLogs.add(location + " : " + assignment);
                                            }
                                        }
                                        arrayAdapter = new ArrayAdapter<String>(AssignedLogAllActivity.this, android.R.layout.simple_list_item_1, memberName);
                                        lv.setAdapter(arrayAdapter);
                                    }
                                }
                            }
                        });
                    }
                }
            }
        });


    }
}
