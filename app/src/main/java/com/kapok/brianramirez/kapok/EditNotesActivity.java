package com.kapok.brianramirez.kapok;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditNotesActivity extends AppCompatActivity {
    String log;
    EditText currentNotes;
    String result ;
    Button editlogBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_notes);
        currentNotes = findViewById(R.id.currentnotes);
        result = getIntent().getStringExtra("prev");
        currentNotes.setText(result);
        editlogBtn = findViewById(R.id.editLogBtn);

        editlogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String notestoAdd = currentNotes.getText().toString().trim();
                Intent intent = getIntent();
                intent.putExtra("MESSAGE", notestoAdd);
                EditNotesActivity.this.setResult(2, intent);
                EditNotesActivity.this.finish();
            }
        });




    }

    @Override
    public void onBackPressed() {
        //DO NOTHING
    }
}