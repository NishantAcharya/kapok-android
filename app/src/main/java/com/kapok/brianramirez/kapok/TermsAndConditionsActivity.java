package com.kapok.brianramirez.kapok;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TermsAndConditionsActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Checking for accepting of terms before movig forwards
        SharedPreferences settings = getSharedPreferences("Terms", Context.MODE_PRIVATE);
        boolean acceptedTerms = settings.getBoolean("Terms", false);
        

        if (acceptedTerms) {
            launchHomeScreen();
            finish();
        }
        setContentView(R.layout.activity_terms_and_conditions);

        Button Accepting = (Button)findViewById(R.id.terms);
        Accepting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences settings = getSharedPreferences("Terms", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("Terms",true);
                // Commit the edits!
                editor.commit();
                launchHomeScreen();
            }
        });
    }
    private void launchHomeScreen() {

        startActivity(new Intent(TermsAndConditionsActivity.this, Splash.class));
        finish();
    }
}
