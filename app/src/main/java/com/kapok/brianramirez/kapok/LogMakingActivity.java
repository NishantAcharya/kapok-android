package com.kapok.brianramirez.kapok;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
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
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import io.realm.SyncUser;

public class LogMakingActivity extends AppCompatActivity {

    EditText locationTxtField;
    EditText categoryTxtField;
    EditText infoTxtField;
    Switch sensitiveInfoBtn;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_log);
        locationTxtField = findViewById(R.id.location_txt);
        categoryTxtField= findViewById(R.id.category_txt);
        infoTxtField= findViewById(R.id.notes_txt);
        sensitiveInfoBtn = findViewById(R.id.sensitiveInfoBtn);
        mAuth = FirebaseAuth.getInstance();
        String currentUser = mAuth.getCurrentUser().getEmail();


        Button btn = (Button) findViewById(R.id.create_log);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ResLog resLog = new ResLog(null, null,  locationTxtField.getText().toString(), categoryTxtField.getText().toString(), infoTxtField.getText().toString(), sensitiveInfoBtn.isActivated());
//                RealmManager manager = new RealmManager();
//                manager.add(resLog);

                Map<String, Object> log = new HashMap<>();
                log.put("creator", currentUser);
                log.put("location", locationTxtField.getText().toString());
                log.put("category", categoryTxtField.getText().toString());
                log.put("info", infoTxtField.getText().toString());
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference userProf = db.collection("Profiles").document(currentUser);
                userProf.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
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
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                            }
                                        });

                            }
                        } else {
                        }
                    }
                });



            }
        });
    }
}


