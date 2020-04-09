package com.kapok.brianramirez.kapok;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
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


public class LogMakingActivity extends AppCompatActivity {

    EditText locationTxtField;
    Spinner categoryTxtField;
    EditText infoTxtField;

    RatingBar LogPriority;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Theme set here
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }
        setContentView(R.layout.activity_create_log);

        //Setting up the views
        locationTxtField = findViewById(R.id.location_txt);
        categoryTxtField= findViewById(R.id.category_txt);
        infoTxtField= findViewById(R.id.notes_txt);
        LogPriority = findViewById(R.id.ratingBar);

        //getting user data
        mAuth = Database.mAuth;
        String currentUser = mAuth.getCurrentUser().getEmail();

        //Working of the log create button
        Button btn = (Button) findViewById(R.id.create_log);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            //Putting the values of the log in the categories in the database
            public void onClick(View v) {
                Map<String, Object> log = new HashMap<>();
                log.put("creator", currentUser);
                log.put("location", locationTxtField.getText().toString());
                log.put("category", String.valueOf(categoryTxtField.getSelectedItem()));
                log.put("info", infoTxtField.getText().toString());
                log.put("Log Rating", String.valueOf(LogPriority.getRating()));
                log.put("time", String.valueOf(System.currentTimeMillis()));
                log.put("assignment", "");
                FirebaseFirestore db = Database.db;
                DocumentReference userProf = db.collection("Profiles").document(currentUser);
                userProf.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    //Getting the latitude and longitude from the profiles tab;e
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                ArrayList<String> team = (ArrayList<String>)document.getData().get("team");
                                log.put("point", document.getData().get("recentMapPoint"));

                                DocumentReference docRef = db.collection("Teams").document(team.get(0));
//                                Toast.makeText(LogMakingActivity.this, team.get(0),
//                                        Toast.LENGTH_SHORT).show();
                                docRef
                                        .update("logs", FieldValue.arrayUnion(log))
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(LogMakingActivity.this, "Log created successfully!",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(LogMakingActivity.this, "Log creation failed!.",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        });

                            }
                        }
                    }
                });
                openMapDisplay();
            }
        });
    }

    //Opens Map activty
    public void openMapDisplay(){
        Intent i = new Intent(this, MapActivity.class);
        startActivity(i);
    }
}


