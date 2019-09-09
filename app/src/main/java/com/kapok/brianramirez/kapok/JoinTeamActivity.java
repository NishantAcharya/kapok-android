package com.kapok.brianramirez.kapok;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class JoinTeamActivity extends AppCompatActivity {
    Button joinTeam;
    EditText edit_team_code;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_team);
        edit_team_code = findViewById(R.id.teamCode);
        joinTeam = findViewById(R.id.joinRequest);

        mAuth = Database.mAuth;
        String currentUser = mAuth.getCurrentUser().getEmail();

        joinTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore db = Database.db;
                DocumentReference docRef = db.collection("Teams").document(edit_team_code.getText().toString());
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String admin = (String) document.getData().get("admin");
                                DocumentReference adminRef = db.collection("Profiles").document(admin);
                                adminRef.update("requests", FieldValue.arrayUnion(currentUser));
                                DocumentReference docRef2 = db.collection("Profiles").document(currentUser);
                                docRef2.update("status","pending");
                            }
                            else {
                                Toast.makeText(JoinTeamActivity.this, "Team Does not exist",
                                        Toast.LENGTH_SHORT).show();
                            }
                            openJoinWait(edit_team_code.getText().toString());
                        }
                    }
                });
            }
        });
    }
    public void openJoinWait(String team){
        Intent i = new Intent(this, JoinWaitActivity.class).putExtra("teamid", team);
        startActivity(i);
    }
}
