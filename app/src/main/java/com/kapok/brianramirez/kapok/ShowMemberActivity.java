package com.kapok.brianramirez.kapok;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;

import io.realm.OrderedRealmCollectionSnapshot;

public class ShowMemberActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    String member;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_members);

        Intent intent = getIntent();
        member = intent.getStringExtra("Member");
        mAuth = FirebaseAuth.getInstance();

        TextView nameText = findViewById(R.id.full_name_text_field);
        TextView occupationText = findViewById(R.id.occupation_text_field);
        TextView contactInfoText = findViewById(R.id.contact_info_text_field);
        TextView aboutText = findViewById(R.id.about_me_text_field);
        TextView registeredEmailText = findViewById(R.id.registered_email_text_field);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection("Profiles").document(member);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String name = (String) document.getData().get("name");
                        String occupation = (String) document.getData().get("occupation");
                        String contactInfo = (String) document.getData().get("contactInfo");
                        String about = (String) document.getData().get("about");
                        String registeredEmail = (String) member;

                        nameText.setText(name);
                        occupationText.setText(occupation);
                        contactInfoText.setText(contactInfo);
                        aboutText.setText(about);
                        registeredEmailText.setText(registeredEmail);

                    }
                }
            }
        });
    }
}

