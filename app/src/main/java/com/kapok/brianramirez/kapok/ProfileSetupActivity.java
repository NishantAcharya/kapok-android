package com.kapok.brianramirez.kapok;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ProfileSetupActivity extends AppCompatActivity {
    EditText fullNameField;
    EditText occupationField;
    EditText contactInfoField;
    EditText aboutMeField;
    Button finishProfileBtn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup);

        fullNameField = findViewById(R.id.full_name_text_field);
        occupationField = findViewById(R.id.occupation_text_field);
        contactInfoField = findViewById(R.id.contact_info_text_field);
        aboutMeField = findViewById(R.id.about_me_text_field);
        finishProfileBtn = findViewById(R.id.finish_profile_btn);
        mAuth = Database.mAuth;
        String currentUser = mAuth.getCurrentUser().getEmail();


        finishProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> team = new ArrayList<String>(1);
                FirebaseFirestore db = Database.db;

                // Create a new user with a first and last name
                Map<String, Object> user = new HashMap<>();
                user.put("name", fullNameField.getText().toString());
                user.put("occupation", occupationField.getText().toString());
                user.put("contactInfo", contactInfoField.getText().toString());
                user.put("about", aboutMeField.getText().toString());
                user.put("isAdmin", false);
                user.put("team", team);
                user.put("status", "None");
                user.put("recentMapPoint", null);
                user.put("requests", new ArrayList<String>(1));

                db.collection("Profiles").document(currentUser)
                        .set(user)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Intent goToTeamIntent = new Intent(ProfileSetupActivity.this, TeamWelcomeActivity.class);
                                startActivity(goToTeamIntent);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ProfileSetupActivity.this, "Profile Setup failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}