package com.kapok.brianramirez.kapok;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

public class EditNotesActivity extends AppCompatActivity {
    private EditText inputEmail;
    private Button btnReset;
    private FirebaseAuth auth;
    int logid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        logid = intent.getIntExtra("Log Id", 0);



        setContentView(R.layout.activity_edit_notes);
        EditText currentNotes = findViewById(R.id.currentnotes);




    }
}