package com.kapok.brianramirez.kapok;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

public class LogListViewActivity extends AppCompatActivity {

    private ListView lv;
    private FirebaseAuth mAuth;
    ArrayList<Map<String, Object>> baseLog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_list_view);
        lv = findViewById(R.id.LogListView);
        mAuth = Database.mAuth;



        FirebaseUser currentUser = mAuth.getCurrentUser();
        FirebaseFirestore db = Database.db;
        DocumentReference docRef = db.collection("Profiles").document(currentUser.getEmail());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                ArrayList<String> location = new ArrayList<>();
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
                                        ArrayList<Map<String, Object>> locations = (ArrayList<Map<String, Object>>) document.get("logs");
                                        baseLog = (ArrayList<Map<String, Object>>) locations.clone();
                                        for (Map<String, Object> currLog : locations) {
                                            location.add((String) currLog.get("location"));

                                        }
                                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(LogListViewActivity.this, android.R.layout.simple_list_item_1, location);
                                        lv.setAdapter(arrayAdapter);
                                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                openLogView(position);

                                        }
                                    });
                                    }
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.log_sort_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.sort_newest) {
            Comparator<Map<String, Object>> logComparator = new Comparator<Map<String, Object>>() {
                public int compare(Map<String, Object> log1, Map<String, Object> log2) {
                    long time1 = (long) log1.get("time");
                    long time2 = (long) log2.get("time");
                    if(time1 >= time2)
                        return 1;
                    else
                        return -1;
                }
            };
            Collections.sort(baseLog, logComparator);
            ArrayList<String> location = new ArrayList<>();
            for (Map<String, Object> currLog : baseLog) {
                location.add((String) currLog.get("location"));
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(LogListViewActivity.this, android.R.layout.simple_list_item_1, location);
            lv.setAdapter(arrayAdapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    openLogView(position);
                }
            });
        }

        if (id == R.id.sort_oldest) {
            Comparator<Map<String, Object>> logComparator = new Comparator<Map<String, Object>>() {
                public int compare(Map<String, Object> log1, Map<String, Object> log2) {
                    long time1 = (long) log1.get("time");
                    long time2 = (long) log2.get("time");
                    if(time1 >= time2)
                        return -1;
                    else
                        return 1;
                }
            };
            Collections.sort(baseLog, logComparator);
            ArrayList<String> location = new ArrayList<>();
            for (Map<String, Object> currLog : baseLog) {
                location.add((String) currLog.get("location"));
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(LogListViewActivity.this, android.R.layout.simple_list_item_1, location);
            lv.setAdapter(arrayAdapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    openLogView(position);
                }
            });

        }

        if (id == R.id.sort_rating) {

        }
        return super.onOptionsItemSelected(item);
    }
    public void openLogView (int position){
        Intent i = new Intent(this, ShowLogActivity.class).putExtra("Log Position", position);
        startActivity(i);
    }


}




