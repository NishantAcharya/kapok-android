package com.kapok.brianramirez.kapok;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

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
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        logPos = (int) intent.getIntExtra("Log Position", 0);
        TextView locationText = (TextView) findViewById(R.id.location_txt_display);
        TextView categoryText = (TextView) findViewById(R.id.category_txt_display);
        TextView notesText = (TextView) findViewById(R.id.notes_txt_display);
        TextView creatorText = (TextView) findViewById(R.id.creator_txt_display);

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


                                        DocumentReference docRef = db.collection("Profiles").document(creator);
                                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot document = task.getResult();
                                                    if (document.exists()) {
                                                        String creatorName = (String) document.getData().get("name");
                                                        locationText.setText(lat + "," + lon);
                                                        creatorText.setText(creatorName + "\n" + "Email:" + creator);
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
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.log_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_add_notes) {
            if (can_edit(logPos)==true)
                Toast.makeText(ShowLogActivity.this, "YAAAAS", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(ShowLogActivity.this, "This sucks!", Toast.LENGTH_SHORT).show();
        }

        if (id == R.id.menu_delete) {
            if (can_edit(logPos)==true)
                Toast.makeText(ShowLogActivity.this, "YAAAAS", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(ShowLogActivity.this, "This sucks!", Toast.LENGTH_SHORT).show();
        }

        if (id == R.id.menu_edit_priority) {
            if (can_edit(logPos)==true)
                Toast.makeText(ShowLogActivity.this, "YAAAAS", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(ShowLogActivity.this, "This sucks!", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean can_edit(int index) {
        final boolean[] result = {false};
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

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
                                            result[0] = true;

                                    }

                                }

                            }
                        });

                    }

                }
            }
        });
        return result[0];
    }
}