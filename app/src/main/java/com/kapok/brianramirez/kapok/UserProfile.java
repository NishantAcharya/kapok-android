package com.kapok.brianramirez.kapok;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Ref;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserProfile extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private String currentUser;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Theme set
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }
        setContentView(R.layout.activity_show_members);

        //user data get
        FirebaseFirestore db = Database.db;
        mAuth = Database.mAuth;

        //View set
        TextView nameText = findViewById(R.id.full_name_text_field);
        TextView occupationText = findViewById(R.id.occupation_text_field);
        TextView contactInfoText = findViewById(R.id.contact_info_text_field);
        TextView aboutText = findViewById(R.id.about_me_text_field);
        TextView registeredEmailText = findViewById(R.id.registered_email_text_field);

        //Profile varialbles get
        currentUser = mAuth.getCurrentUser().getEmail();


        DocumentReference memberRef = db.collection("Profiles").document(currentUser);
        //Showing user values here by setting in the texxt view
        memberRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String name = (String) document.getData().get("name");
                        String occupation = (String) document.getData().get("occupation");
                        String contactInfo = (String) document.getData().get("contactInfo");
                        String about = (String) document.getData().get("about");
                        String registeredEmail = (String) currentUser;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_edit_menu, menu);
        return true;
    }

    @Override
    //Profile Editing here
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.editName){
            Toast.makeText(UserProfile.this, "YAAAAS", Toast.LENGTH_SHORT).show();
            editName();
        }
        if(id == R.id.editInfo){
            Toast.makeText(UserProfile.this, "YAAAAS", Toast.LENGTH_SHORT).show();
            editInfo();
        }
        if(id == R.id.editContact){
            Toast.makeText(UserProfile.this, "YAAAAS", Toast.LENGTH_SHORT).show();
            editContact();
        }
        if(id == R.id.editOccupation){
            Toast.makeText(UserProfile.this, "YAAAAS", Toast.LENGTH_SHORT).show();
            editOccupation();
        }
        return super.onOptionsItemSelected(item);
    }
    //The above used methods
    private void editName(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_edit_profile);
        EditText editBar = dialog.findViewById(R.id.currentEdit);
        Button dialogButton = (Button) dialog.findViewById(R.id.editProfBtn);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseUser currentUser = mAuth.getCurrentUser();
                FirebaseFirestore db = Database.db;

                DocumentReference docRef = db.collection("Profiles").document(currentUser.getEmail());
                docRef.update("name",editBar.getText().toString());
                dialog.dismiss();
                //Refreshing activity
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
            }
        });

        dialog.show();
    }

    private void editInfo(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_edit_profile);
        EditText editBar = dialog.findViewById(R.id.currentEdit);
        Button dialogButton = (Button) dialog.findViewById(R.id.editProfBtn);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseUser currentUser = mAuth.getCurrentUser();
                FirebaseFirestore db = Database.db;

                DocumentReference docRef = db.collection("Profiles").document(currentUser.getEmail());
                docRef.update("about",editBar.getText().toString());
                dialog.dismiss();
                //Refreshing activity
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
            }
        });

        dialog.show();
    }

    private void editContact(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_edit_profile);
        EditText editBar = dialog.findViewById(R.id.currentEdit);
        Button dialogButton = (Button) dialog.findViewById(R.id.editProfBtn);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseUser currentUser = mAuth.getCurrentUser();
                FirebaseFirestore db = Database.db;

                DocumentReference docRef = db.collection("Profiles").document(currentUser.getEmail());
                docRef.update("contactInfo",editBar.getText().toString());
                dialog.dismiss();
                //Refreshing activity
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
            }
        });

        dialog.show();
    }
    private void editOccupation(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_edit_profile);
        EditText editBar = dialog.findViewById(R.id.currentEdit);
        Button dialogButton = (Button) dialog.findViewById(R.id.editProfBtn);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseUser currentUser = mAuth.getCurrentUser();
                FirebaseFirestore db = Database.db;

                DocumentReference docRef = db.collection("Profiles").document(currentUser.getEmail());
                docRef.update("occupation",editBar.getText().toString());
                dialog.dismiss();
                //Refreshing activity
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
            }
        });

        dialog.show();
    }

}
