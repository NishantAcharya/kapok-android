package com.kapok.brianramirez.kapok;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

public class openLogMaking extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_log);

        Button btn=(Button) findViewById(R.id.create_log);
        btn.setOnClickListener(v -> finish());
    }
    }


