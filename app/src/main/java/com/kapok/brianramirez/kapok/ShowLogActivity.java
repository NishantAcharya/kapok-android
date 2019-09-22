package com.kapok.brianramirez.kapok;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RatingBar;
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

public class ShowLogActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    Map<String, Object> log;
    int logPos;
    float floatval;
    boolean result;
    TextView notesText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_log);

        Intent intent = getIntent();
        logPos = intent.getIntExtra("Log Position", 0);
        mAuth = Database.mAuth;
        TextView locationText = findViewById(R.id.location_txt_display);
        TextView categoryText = findViewById(R.id.category_txt_display);
        notesText = findViewById(R.id.notes_txt_display);
        TextView creatorText = findViewById(R.id.creator_txt_display);
        RatingBar Rating = findViewById(R.id.ratingBar);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        FirebaseFirestore db = Database.db;
        can_edit(logPos);

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
                                        String val = (String)(log.get("Log Rating"));
                                        floatval = Float.parseFloat(val);




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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.log_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;
            if (id == R.id.menu_add_notes) {
            if (result==true) {
                intent = new Intent(ShowLogActivity.this, EditNotesActivity.class);
                ShowLogActivity.this.startActivityForResult(intent, 2);
            }
                else
                Toast.makeText(ShowLogActivity.this, "This sucks!", Toast.LENGTH_SHORT).show();
        }

        if (id == R.id.menu_delete) {
            if (result==true)
                Toast.makeText(ShowLogActivity.this, "YAAAAS", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(ShowLogActivity.this, "This sucks!", Toast.LENGTH_SHORT).show();
        }

        if (id == R.id.menu_edit_priority) {
            if (result==true) {
                Toast.makeText(ShowLogActivity.this, "YAAAAS", Toast.LENGTH_SHORT).show();
                ShowDialog();
            }

            else {
                Toast.makeText(ShowLogActivity.this, "This sucks!", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

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
                                            log2.put("category", log.get("creator").toString());
                                            log2.put("info", message);
                                            log2.put("Log Rating", log.get("Log Rating").toString());
                                            log2.put("time", log.get("time").toString());
                                            log2.put("point", log.get("point"));
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

    public void ShowDialog()
    {
        AlertDialog.Builder popDialog = new AlertDialog.Builder(this);
        final RatingBar rating = new RatingBar(this);
        rating.setRating(floatval);
        rating.setNumStars(5);
        float val = rating.getNumStars();
        popDialog.setIcon(android.R.drawable.btn_star_big_on);
        popDialog.setTitle("New rating:");
        popDialog.setView(rating);
// Button OK
        popDialog.setPositiveButton(android.R.string.ok,
        new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                double result = rating.getProgress(); //put this on the database
                dialog.dismiss();
            }

        })


// Button Cancel
                .setNegativeButton("Cancel",

        new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        popDialog.create();
        popDialog.show();
    }

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
}