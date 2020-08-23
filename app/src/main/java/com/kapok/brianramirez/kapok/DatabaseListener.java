package com.kapok.brianramirez.kapok;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.sql.Ref;
import java.util.ArrayList;
import java.util.Map;

import static android.support.constraint.Constraints.TAG;

//This is a service manager responsible for notifications
public class DatabaseListener extends Service {
    private FirebaseAuth mAuth;
    private DocumentSnapshot prevUserSnap;
    private DocumentSnapshot prevTeamSnap;
    private String team;

    @Override
    public void onCreate() {
        mAuth = Database.mAuth;
        String currentUser = mAuth.getCurrentUser().getEmail();
        FirebaseFirestore db = Database.db;
        DocumentReference docRef = db.collection("Profiles").document(currentUser);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    // Document found in the offline cache
                    prevUserSnap = task.getResult();
                    team = ((ArrayList<String>) task.getResult().get("team")).get(0);
                    DocumentReference teamRef = db.collection("Teams").document(team);
                    teamRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                // Document found in the offline cache
                                prevTeamSnap = task.getResult();
                            }
                        }
                    });
                }
            }
        });



        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {

                if (snapshot != null && snapshot.exists()) {
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                //Fix the issue: The admin and user both get  notification, fix this
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    ArrayList<String> requests = (ArrayList<String>)document.get("requests");
                                    ArrayList<String> prevrequests = (ArrayList<String>)prevUserSnap.get("requests");
                                    String status = document.get("status").toString();
                                    String prevstatus = prevUserSnap.get("status").toString();
                                    Boolean isAdmin = (Boolean)document.get("isAdmin");
                                    Boolean previsAdmin = (Boolean)prevUserSnap.get("isAdmin");
                                    ArrayList<String> team = (ArrayList<String>)document.get("team");
                                    ArrayList<String> prevteam = (ArrayList<String>)prevUserSnap.get("team");
                                    ArrayList<Map<String, Object>> logs = (ArrayList<Map<String, Object>>)document.get("assignments");
                                    ArrayList<Map<String, Object>> prevlogs = (ArrayList<Map<String, Object>>)prevUserSnap.get("assignments");
                                    if(requests.size()>prevrequests.size()){
                                        //make notification
                                        createNotificationChannel();
                                        int notificationId = 10;
                                        NotificationCompat.Builder builder = new NotificationCompat.Builder(DatabaseListener.this, "Kapok")
                                                .setSmallIcon(R.drawable.logo)
                                                .setContentTitle("New Requests")
                                                .setContentText("New Request from someone to join team")
                                                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(DatabaseListener.this);

// notificationId is a unique int for each notification that you must define
                                        notificationManager.notify(notificationId, builder.build());

                                    }
                                    if(prevstatus.equals("pending") && status.equals("accepted")){
                                        //make notification
                                        createNotificationChannel();
                                        int notificationId = 12;
                                        NotificationCompat.Builder builder = new NotificationCompat.Builder(DatabaseListener.this, "Kapok")
                                                .setSmallIcon(R.drawable.logo)
                                                .setContentTitle("Request Accepted")
                                                .setContentText("Your request has been accepted")
                                                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(DatabaseListener.this);

// notificationId is a unique int for each notification that you must define
                                        notificationManager.notify(notificationId, builder.build());
                                    }
                                    if(isAdmin && !previsAdmin){
                                        //make notification
                                        createNotificationChannel();
                                        int notificationId = 13;
                                        NotificationCompat.Builder builder = new NotificationCompat.Builder(DatabaseListener.this, "Kapok")
                                                .setSmallIcon(R.drawable.logo)
                                                .setContentTitle("Administator Change")
                                                .setContentText("You are this team's new admin!")
                                                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(DatabaseListener.this);

// notificationId is a unique int for each notification that you must define
                                        notificationManager.notify(notificationId, builder.build());
                                    }
                                    if(!isAdmin && previsAdmin){
                                        //make notification
                                        createNotificationChannel();
                                        int notificationId = 14;
                                        NotificationCompat.Builder builder = new NotificationCompat.Builder(DatabaseListener.this, "Kapok")
                                                .setSmallIcon(R.drawable.logo)
                                                .setContentTitle("Administrator Change")
                                                .setContentText("You are no longer an Administartor!")
                                                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(DatabaseListener.this);

// notificationId is a unique int for each notification that you must define
                                        notificationManager.notify(notificationId, builder.build());
                                    }
                                    if(prevteam.size() > team.size()){
                                        //make notification
                                        createNotificationChannel();
                                        int notificationId = 15;
                                        NotificationCompat.Builder builder = new NotificationCompat.Builder(DatabaseListener.this, "Kapok")
                                                .setSmallIcon(R.drawable.logo)
                                                .setContentTitle("Team Left")
                                                .setContentText("You have left your current team")
                                                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(DatabaseListener.this);

// notificationId is a unique int for each notification that you must define
                                        notificationManager.notify(notificationId, builder.build());
                                    }
                                    if(logs.size()>prevlogs.size()){
                                        //Todo
                                        //Notification for log assigned
                                        createNotificationChannel();
                                        int notificationId = 11; //This unique
                                        NotificationCompat.Builder builder = new NotificationCompat.Builder(DatabaseListener.this, "Kapok")
                                                .setSmallIcon(R.drawable.logo)
                                                .setContentTitle("New Task assigned")
                                                .setContentText("New Tasks have been assigned to you")
                                                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(DatabaseListener.this);

// notificationId is a unique int for each notification that you must define
                                        notificationManager.notify(notificationId, builder.build());
                                    }
                                    prevUserSnap = document;
                                }
                            }
                        }
                    });
                }
            }
        });

    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String name = "Kapok Channel";
            String id = "Kapok";
            String description = "Notification Channel for Kapok Messages";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(id, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);


        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Service started by user.", Toast.LENGTH_LONG).show();
        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service destroyed by user.", Toast.LENGTH_LONG).show();
    }
}
