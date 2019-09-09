package com.kapok.brianramirez.kapok;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class Database {
    public static FirebaseAuth mAuth;
    public static FirebaseFirestore db;
    private FirebaseUser currentUser;
    private String currentTeam;
    Database(){
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        final String[] team = {"nouser"};
        if (currentUser != null) {

            DocumentReference docRef = db.collection("Profiles").document(currentUser.getEmail());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            ArrayList<String> userCurrentTeam = (ArrayList<String>) document.getData().get("team");
                            team[0] = userCurrentTeam.get(0);
                        }

                    }

                }
            });

        }
        currentTeam = team[0];
    }

    public String getUserTeam(){
        return currentTeam;
    }
    public void updateProfile(String key, String value){

    }

}
