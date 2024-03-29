package com.kapok.brianramirez.kapok;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Ref;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class ShowLogActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    Map<String, Object> log;
    int logPos;
    float floatval;
    boolean result;
    TextView notesText;
    String note;
    RatingBar Rating;
    private ArrayList<String> teamMates = new ArrayList<String>(1);;
    private ArrayList<String> teamEmails;
    private boolean isAdmin;
    private String member;
    private String usrName;
    private String assignName;
    private String logStatus;
    private String status;
    private boolean breakout = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Theme set
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }
        setContentView(R.layout.activity_show_log);

        //Setting values of the team and admin and getting other user related stuff(and intent)

        getTeam();
        isAdmin();
        Intent intent = getIntent();
        logPos = intent.getIntExtra("Log Position", 0);
        mAuth = Database.mAuth;
        usrName = mAuth.getCurrentUser().getEmail();

        //View setup
        TextView locationText = findViewById(R.id.location_txt_display);
        TextView categoryText = findViewById(R.id.category_txt_display);
        notesText = findViewById(R.id.notes_txt_display);
        TextView creatorText = findViewById(R.id.creator_txt_display);
        TextView statusText = findViewById(R.id.status_txt_display);
        Rating = findViewById(R.id.ratingBar);


        //Database values setup
        FirebaseUser currentUser = mAuth.getCurrentUser();
        FirebaseFirestore db = Database.db;
        can_edit(logPos);


        DocumentReference docRef = db.collection("Profiles").document(currentUser.getEmail());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            //getting to teams(We need to get there because logs are there)
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                ArrayList<String> location = new ArrayList<>();
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        //get your username here
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
                                        String val = (log.get("Log Rating").toString());
                                        logStatus = log.get("status").toString();
                                        floatval = Float.parseFloat(val);
                                        status = log.get("status").toString();
                                        assignName = log.get("assignment").toString();

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
                                                        note = notes;
                                                        notesText.setText(notes);
                                                        Rating.setRating(floatval);
                                                        statusText.setText(status);
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

        Button complete = (Button) findViewById(R.id.complete_task);
        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                                DocumentReference teamRef = db.collection("Teams").document(TeamCode);
                                teamRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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
                                                log2.put("info", log.get("info").toString());
                                                log2.put("Log Rating", log.get("Log Rating"));
                                                log2.put("time", log.get("time").toString());
                                                log2.put("point", log.get("point"));
                                                log2.put("assignment", "No Assignment");
                                                log2.put("status","complete");
                                                teamRef.update("logs", FieldValue.arrayRemove(log));
                                                teamRef.update("logs", FieldValue.arrayUnion(log2));
                                                //Finding the name of the  assignee
                                                ArrayList<String> teamMembers = (ArrayList<String>) document.getData().get("members");
                                                //Add a breakout statement in the given loop to make more efficient
                                                for(String mem: teamMembers) {
                                                    DocumentReference teriRef = db.collection("Profiles").document(mem);
                                                    teriRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                            if(task.isSuccessful()){
                                                                DocumentSnapshot document = task.getResult();
                                                                if(document.exists()){
                                                                    String userName = document.get("name").toString();
                                                                    if(userName.equals(assignName)){
                                                                        teriRef.update("assignments", FieldValue.arrayRemove(log));
                                                                        teriRef.update("assignments", FieldValue.arrayUnion(log2));
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    });
                                                }

                                                //change assignment

                                                //On completing exit out of the activyty, polish notes
                                                //add assignment removal on completeion
                                                //refreshing the page when staus updates
                                                //add this to assigned log
                                                //you can only complete assigned task
                                                //put status checks around complete
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    }
                });
                //Finished activity so that the status could refresh
                Toast.makeText(ShowLogActivity.this, "Marked as Complete!.",
                        Toast.LENGTH_SHORT).show();
                finish();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.log_menu, menu);

        if(!Database.isAdmin){
            menu.findItem(R.id.menu_delete).setVisible(false);
            menu.findItem(R.id.menu_edit_priority).setVisible(false);
           menu.findItem(R.id.assign_task).setVisible(false);

        }
        if(!Database.isAdmin && !usrName.equals(assignName)){
            menu.findItem(R.id.menu_add_notes).setVisible(false);
        }
        return true;
    }

    //working of the hamburger menu and each option in it
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;
            if (id == R.id.menu_add_notes) {
                if(result == true) {
                    if (Database.isAdmin) {
                        editnotes(note);
                    } else {
                        Toast.makeText(ShowLogActivity.this, "You are not the Administrator", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(ShowLogActivity.this, "You are not the Administrator or Creator", Toast.LENGTH_SHORT).show();
                }
            }

        if (id == R.id.menu_delete) {
            if(result == true) {

                    Toast.makeText(ShowLogActivity.this, "YAAAAS", Toast.LENGTH_SHORT).show();
                    deletelog(logPos);

            }
            else{
                Toast.makeText(ShowLogActivity.this, "You are not the Administrator or Creator", Toast.LENGTH_SHORT).show();
            }
        }

        if (id == R.id.menu_edit_priority) {
            if(result == true) {


                    float curr = Rating.getRating();
                    ShowDialog(curr);

            }
            else{
                Toast.makeText(ShowLogActivity.this, "You are not the Administrator or Creator", Toast.LENGTH_SHORT).show();
            }
        }

        if(id == R.id.assign_task){
            //Checking if the given task is completed or not
            if(logStatus.equals("complete")){
                Toast.makeText(ShowLogActivity.this, "The task has been completed", Toast.LENGTH_SHORT).show();
            }
            else if (Database.isAdmin) {
                    final Dialog dialog = new Dialog(this);
                    dialog.setContentView(R.layout.log_view_alert);
                    Spinner spinner = dialog.findViewById(R.id.assignee);
                    Button assign = dialog.findViewById(R.id.alert_assign);
                    Button cancel = dialog.findViewById(R.id.alert_cancel);

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, teamMates);
                    spinner.setAdapter(adapter);
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            member = teamEmails.get(i);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                            Toast.makeText(ShowLogActivity.this, "Please Select Something!", Toast.LENGTH_SHORT).show();
                        }
                    });

                    assign.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
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
                                            DocumentReference teamRef = db.collection("Teams").document(TeamCode);
                                            teamRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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
                                                            log2.put("info", log.get("info").toString());
                                                            log2.put("Log Rating", log.get("Log Rating"));
                                                            log2.put("time", log.get("time").toString());
                                                            log2.put("point", log.get("point"));
                                                            log2.put("assignment", String.valueOf(spinner.getSelectedItem()));
                                                            log2.put("status",log.get("status").toString());
                                                            teamRef.update("logs", FieldValue.arrayRemove(log));
                                                            teamRef.update("logs", FieldValue.arrayUnion(log2));
                                                            DocumentReference teriRef = db.collection("Profiles").document(member);
                                                            teriRef.update("assignments", FieldValue.arrayUnion(log2));
                                                            dialog.dismiss();
                                                        }
                                                    }
                                                }
                                            });
                                        }
                                    }
                                }
                            });
                        }
                    });

                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();

            } else{
                Toast.makeText(ShowLogActivity.this, "You are not the Administrator or Creator", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

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
                                if(task.isSuccessful()){
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()){
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

//update the log after returning to this activity
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        FirebaseFirestore db = Database.db;
        if (requestCode == 2) {
            String message = data.getStringExtra("MESSAGE");
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
                                            log2.put("Log Rating", log.get("Log Rating")).toString();
                                            log2.put("time", log.get("time").toString());
                                            log2.put("point", log.get("point"));
                                            log2.put("assignment",log.get("assignment"));
                                            log2.put("status",log.get("status").toString());
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
//this shows a dialog box for editing
    public void ShowDialog(float curr)
    {

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_custom_with_rating);
        RatingBar ratingBar2 = dialog.findViewById(R.id.ratingBar2);
        ratingBar2.setRating(curr);
        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        Button dialogButton2 = (Button) dialog.findViewById(R.id.dialogButtonOK2);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                                                log = locations.get(logPos);
                                                Map<String, Object> log2 = new HashMap<>();
                                                log2.put("creator", log.get("creator").toString());
                                                log2.put("location", log.get("location").toString());
                                                log2.put("category", log.get("creator").toString());
                                                log2.put("info", log.get("info").toString());
                                                log2.put("Log Rating", String.valueOf(ratingBar2.getRating()));
                                                log2.put("time", log.get("time").toString());
                                                log2.put("point", log.get("point"));
                                                log2.put("assignment",log.get("assignment"));
                                                log2.put("status",log.get("status").toString());
                                                docRef.update("logs", FieldValue.arrayRemove(log));
                                                docRef.update("logs", FieldValue.arrayUnion(log2));
                                                Rating.setRating(ratingBar2.getRating());
                                                dialog.dismiss();
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    }
                });

            }
        });

        dialogButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void editnotes(String notes){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.activity_edit_notes);
        EditText editBar = dialog.findViewById(R.id.currentnotes);
        editBar.setText(notes);
        Button dialogButton = (Button) dialog.findViewById(R.id.editLogBtn);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                                                log = locations.get(logPos);
                                                Map<String, Object> log2 = new HashMap<>();
                                                log2.put("creator", log.get("creator").toString());
                                                log2.put("location", log.get("location").toString());
                                                log2.put("category", log.get("creator").toString());
                                                log2.put("info", editBar.getText().toString());
                                                log2.put("Log Rating", log.get("Log Rating"));
                                                log2.put("time", log.get("time").toString());
                                                log2.put("point", log.get("point"));
                                                log2.put("assignment",log.get("assignment"));
                                                log2.put("status",log.get("status").toString());
                                                docRef.update("logs", FieldValue.arrayRemove(log));
                                                docRef.update("logs", FieldValue.arrayUnion(log2));
                                                notesText.setText(editBar.getText());
                                                dialog.dismiss();
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    }
                });

            }
        });

        dialog.show();
    }

    public void deletelog(int logPos) {
        FirebaseFirestore db = Database.db;
        mAuth = Database.mAuth;
        String currentUser = mAuth.getCurrentUser().getEmail();
        DocumentReference userProf = db.collection("Profiles").document(currentUser);
        AlertDialog.Builder a = new AlertDialog.Builder(ShowLogActivity.this,R.style.AlertDialog);
        a.setMessage("Are you sure you want to delete this log").setCancelable(true)
                .setPositiveButton("Accept", new DialogInterface.OnClickListener() {

                    //If user accepts the request
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        userProf.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        ArrayList<String> team = (ArrayList<String>) document.getData().get("team");
                                        DocumentReference teamRef = db.collection("Teams").document(team.get(0));
                                        teamRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                DocumentSnapshot doc = task.getResult();
                                                if (task.isSuccessful()){
                                                    ArrayList<HashMap<String,Object>> logs = (ArrayList<HashMap<String, Object>>) doc.getData().get("logs");
                                                    teamRef.update("logs", FieldValue.arrayRemove(logs.get(logPos)));
                                                }

                                            }
                                        });
                                    }
                                }
                                Intent intent = new Intent(ShowLogActivity.this, LogListViewActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                });
        a.create();
        a.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            Intent intent = new Intent(ShowLogActivity.this, LogListViewActivity.class);
            startActivity(intent);
            finish();
            // do something on back.
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
// only creator or admin can edit the log, this ensures that
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
//get team value
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

}