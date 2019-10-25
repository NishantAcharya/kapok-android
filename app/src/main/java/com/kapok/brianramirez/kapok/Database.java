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
    public FirebaseUser currentUser;
    public String currentTeam;
    public static boolean hasProfile;
    Database(){
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        if (currentUser != null) {

            DocumentReference docRef = db.collection("Profiles").document(currentUser.getEmail());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            hasProfile = true;
                            ArrayList<String> userCurrentTeam = (ArrayList<String>) document.getData().get("team");
                            if(!userCurrentTeam.isEmpty())
                                currentTeam = userCurrentTeam.get(0);
                            else
                                currentTeam = null;
                        }
                        else{
                            hasProfile = false;
                        }

                    }

                }
            });

        }
        else{
            currentTeam = "noUser";
        }
    }

    public String getUserTeam(){
        return currentTeam;
    }
    public void updateProfile(String key, String value){

    }

}
