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
    public static FirebaseUser currentUser;
    public static String currentTeam;
    public static String currentStatus;
    public static boolean hasProfile;

}
