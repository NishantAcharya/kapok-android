package com.kapok.brianramirez.kapok;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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
    private ArrayList<String> teamMates = new ArrayList<String>(1);
    private ArrayList<String> teamEmails;
    ArrayList<Map<String, Object>> baseLog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Theme set
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }
        setContentView(R.layout.activity_log_list_view);
        //Filling in team values
        getTeam();

        lv = findViewById(R.id.LogListView);
        //Getting user data
        mAuth = Database.mAuth;
        FirebaseUser currentUser = mAuth.getCurrentUser();
        FirebaseFirestore db = Database.db;
        DocumentReference docRef = db.collection("Profiles").document(currentUser.getEmail());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            //Getting to teams table
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                ArrayList<String> location = new ArrayList<>();
                ArrayList<String> assignment = new ArrayList<>();
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        ArrayList<String> userCurrentTeam = (ArrayList<String>) document.getData().get("team");
                        String TeamCode = userCurrentTeam.get(0);
                        DocumentReference docRef = db.collection("Teams").document(TeamCode);
                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            //Getting data of the log
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        ArrayList<Map<String, Object>> locations = (ArrayList<Map<String, Object>>) document.get("logs");
                                        baseLog = (ArrayList<Map<String, Object>>) locations.clone();
                                        for (Map<String, Object> currLog : locations) {
                                            location.add((String) currLog.get("location"));
                                            assignment.add((String)currLog.get("assignment"));
                                        }
                                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(LogListViewActivity.this, android.R.layout.simple_list_item_1, location){
                                            //This is where we change the text color for listview
                                            @Override
                                            public View getView(int position, View convertView, ViewGroup parent){
                                                // Get the Item from ListView
                                                View view = super.getView(position, convertView, parent);

                                                // Initialize a TextView for ListView each Item
                                                TextView tv = (TextView) view.findViewById(android.R.id.text1);

                                                // Set the text color of TextView (ListView Item)
                                                tv.setTextColor(Color.WHITE);

                                                // Generate ListView Item using TextView
                                                return view;
                                            }
                                        };
                                        //Change the color of the logs here in the future

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

    //Sorting menu working, basically sorts the logs according to the item selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.sort_newest) {
            Comparator<Map<String, Object>> logComparator = new Comparator<Map<String, Object>>() {
                public int compare(Map<String, Object> log1, Map<String, Object> log2) {
                    long time1 = Long.parseLong((String)log1.get("time"));
                    long time2 = Long.parseLong((String)log1.get("time"));
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
                    long time1 = Long.parseLong((String)log1.get("time"));
                    long time2 = Long.parseLong((String)log2.get("time"));
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
            Comparator<Map<String, Object>> logComparator = new Comparator<Map<String, Object>>() {
                public int compare(Map<String, Object> log1, Map<String, Object> log2) {
                    float time1 = Float.parseFloat((String)log1.get("Log Rating"));
                    float time2 = Float.parseFloat((String)log2.get("Log Rating"));
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

        if (id == R.id.sort_category) {
            Comparator<Map<String, Object>> logComparator = new Comparator<Map<String, Object>>() {
                public int compare(Map<String, Object> log1, Map<String, Object> log2) {
                    String time1 = (String)log1.get("category");
                    String time2 = (String)log2.get("Log Rating");
                    if(time1.compareToIgnoreCase(time2) > 0)
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
        return super.onOptionsItemSelected(item);
    }

    //Fills in team data for the current user
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

    public void openLogView (int position){
        Intent i = new Intent(this, ShowLogActivity.class).putExtra("Log Position", position);
        startActivity(i);
    }



}




