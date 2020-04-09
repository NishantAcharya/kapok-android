package com.kapok.brianramirez.kapok;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;

//The wait screen for joining
public class JoinWaitActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Theme set
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }
        setContentView(R.layout.activity_waiting_to_join);

        //Getting data
        mAuth = Database.mAuth;
        String currentUser = mAuth.getCurrentUser().getEmail();
        FirebaseFirestore db = Database.db;

        //staying until request gets accepted else opening map activty
        DocumentReference docRef = db.collection("Profiles").document(currentUser);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {

                if (snapshot != null && snapshot.exists()) {
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    ArrayList<String> team = (ArrayList<String>) document.getData().get("team");
                                    if(!team.isEmpty()){
                                        openMaps();
                                    }
                                }
                            }
                        }
                    });
                }
            }
        });
    }

    //The back press is empty, this is for what need sto happen when you press back
    @Override
    public void onBackPressed() {
        //DO NOTHING
    }

    //opens map activty
    public void openMaps(){
        Intent intent = new Intent(this,MapActivity.class);
        startActivity(intent);
    }
}
