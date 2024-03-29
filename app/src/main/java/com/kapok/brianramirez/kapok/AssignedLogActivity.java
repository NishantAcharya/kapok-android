package com.kapok.brianramirez.kapok;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AssignedLogActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    Map<String, Object> log;
    int logPos;
    float floatval;
    boolean result;
    TextView notesText;
    String note;
    String status;
    RatingBar Rating;
    private ArrayList<String> teamMates = new ArrayList<String>(1);
    private ArrayList<String> teamEmails;
    private boolean isAdmin; //Value for the Admin Check

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Theme set and change
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }
        setContentView(R.layout.activity_assigned_log);

        //filling up admin and team data
        getTeam();
        isAdmin();

        //Getting data from map activity to locate the correct log
        Intent intent = getIntent();
        logPos = intent.getIntExtra("Log Position", 0);
        mAuth = Database.mAuth;

        //Initializing the activity page
        TextView locationText = findViewById(R.id.location_txt_display);
        TextView categoryText = findViewById(R.id.category_txt_display);
        TextView statusText = findViewById(R.id.status_txt_display);
        notesText = findViewById(R.id.notes_txt_display);
        TextView creatorText = findViewById(R.id.creator_txt_display);
        Rating = findViewById(R.id.ratingBar);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        FirebaseFirestore db = Database.db;
        can_edit(logPos);


        //Filling in the text view and other values from the database
        DocumentReference docRef = db.collection("Profiles").document(currentUser.getEmail());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            //getting to the team table
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
                                    //getting the values of the fields
                                    if (document.exists()) {
                                        ArrayList<Map<String, Object>> locations = (ArrayList<Map<String, Object>>) document.get("logs");
                                        log = locations.get(logPos);
                                        String creator = log.get("creator").toString();
                                        String category = log.get("category").toString();
                                        String notes = log.get("info").toString();
                                        Map<String, Object> currPoint = (Map<String, Object>) log.get("point");
                                        double lat = (double) currPoint.get("latitude");
                                        double lon = (double) currPoint.get("longitude");
                                        String val = (log.get("Log Rating").toString());
                                        floatval = Float.parseFloat(val);
                                        status = log.get("status").toString();

                                        DocumentReference docRef = db.collection("Profiles").document(creator);
                                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            //Filling in the text view with the previous values
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot document = task.getResult();
                                                    if (document.exists()) {
                                                        String creatorName = (String) document.getData().get("name");
                                                        locationText.setText(lat + "," + lon);
                                                        creatorText.setText(creatorName + "\n" + "Email:" + creator);
                                                        categoryText.setText(category);
                                                        statusText.setText(status);
                                                        note = notes;
                                                        notesText.setText(notes);
                                                        Rating.setRating(floatval);

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
            }
        });
    }


//Is Admin check
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
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
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

//Updating the database on editing the log by making a new log and replacing the old one with it
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        FirebaseFirestore db = Database.db;
        if (requestCode == 2) {
            String message = data.getStringExtra("MESSAGE");
            //Toast.makeText(ShowLogActivity.this, message, Toast.LENGTH_SHORT).show();
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
                                            log = locations.get(logPos);
                                            Map<String, Object> log2 = new HashMap<>();
                                            log2.put("creator", log.get("creator").toString());
                                            log2.put("location", log.get("location").toString());
                                            log2.put("category", log.get("category").toString());
                                            log2.put("info", message);
                                            log2.put("Log Rating", log.get("Log Rating")).toString();
                                            log2.put("time", log.get("time").toString());
                                            log2.put("point", log.get("point"));
                                            log2.put("assignment", log.get("assignment"));
                                            docRef.update("logs", FieldValue.arrayRemove(log));
                                            docRef.update("logs", FieldValue.arrayUnion(log2));
                                            notesText.setText(message);

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

//This tells us where to go back
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(AssignedLogActivity.this, MapActivity.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    //Checking who can edit the assigned log
    public void can_edit(int index) {
        mAuth = Database.mAuth;
        FirebaseUser currentUser = mAuth.getCurrentUser();
        FirebaseFirestore db = Database.db;
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
                                        ArrayList<Map<String, Object>> logs = (ArrayList<Map<String, Object>>) document.get("logs");
                                        log = logs.get(index);
                                        String creator = log.get("creator").toString();
                                        String admin = (String) document.get("admin");
                                        String currentUserEmail = currentUser.getEmail();
                                        if (currentUserEmail.equals(creator) || currentUserEmail.equals(admin))
                                            result = true;
                                        else
                                            result = false;

                                    }

                                }

                            }
                        });

                    }

                }
            }
        });
    }

    void getTeam() {
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
                                        for (int i = 0; i < teamEmails.size(); i++) {
                                            DocumentReference docRef = db.collection("Profiles").document(teamEmails.get(i));
                                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        DocumentSnapshot document = task.getResult();
                                                        if (document.exists()) {
                                                            teamMates.add((String) document.getData().get("name"));
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

}
