package com.kapok.brianramirez.kapok;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;

public class ShowLogActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    Map<String, Object> log;
    int logPos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_log);

        Intent intent = getIntent();
        logPos = intent.getIntExtra("Log Position", 0);
        mAuth = FirebaseAuth.getInstance();
        TextView locationText = findViewById(R.id.location_txt_display);
        TextView categoryText = findViewById(R.id.category_txt_display);
        TextView notesText = findViewById(R.id.notes_txt_display);
        TextView creatorText = findViewById(R.id.creator_txt_display);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

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
                                        String creator = log.get("creator").toString();
                                        String category = log.get("category").toString();
                                        String notes = log.get("info").toString();
                                        Map<String, Object> currPoint = (Map<String, Object>) log.get("point");
                                        double lat = (double) currPoint.get("latitude");
                                        double lon = (double) currPoint.get("longitude");
                                        locationText.setText(lat+","+lon);
                                        creatorText.setText(creator);
                                        categoryText.setText(category);
                                        notesText.setText(notes);

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
