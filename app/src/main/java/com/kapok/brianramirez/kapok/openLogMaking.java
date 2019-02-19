package com.kapok.brianramirez.kapok;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import java.util.UUID;

import io.realm.SyncUser;

public class openLogMaking extends AppCompatActivity {

    EditText locationTxtField;
    EditText categoryTxtField;
    EditText infoTxtField;
    Switch sensitiveInfoBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_log);
        locationTxtField = findViewById(R.id.location_txt);
        categoryTxtField= findViewById(R.id.category_txt);
        infoTxtField= findViewById(R.id.notes_txt);
        sensitiveInfoBtn = findViewById(R.id.sensitiveInfoBtn);


        Button btn=(Button) findViewById(R.id.create_log);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResLog resLog = new ResLog(null, null,  locationTxtField.getText().toString(), categoryTxtField.getText().toString(), infoTxtField.getText().toString(), sensitiveInfoBtn.isActivated());
                RealmManager.add(resLog);
                Log.d("THIS IS  A TEST", UUID.randomUUID().toString() + " " + locationTxtField.getText().toString() + "  " + categoryTxtField.getText().toString() + "  " + infoTxtField.getText().toString() + "  " + sensitiveInfoBtn.isActivated());
                finish();
            }
        });
    }
    }


