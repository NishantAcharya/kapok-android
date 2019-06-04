package com.kapok.brianramirez.kapok;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.SyncUser;

public class WaitingScreenActivity extends AppCompatActivity {

    Random rand = new Random();
    int number = rand.nextInt(1000000)+100000;
//    Realm realm = Realm.getDefaultInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_display);

//        realm.beginTransaction();
//        Person currentUser = realm.where(Person.class).equalTo("id", SyncUser.current().getIdentity()).findFirst();
//        currentUser.getTeam().setTeam_join_code(number);
//        realm.commitTransaction();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                openMaps(number);
            }
        }, 3000);
        TextView myText = (TextView)findViewById(R.id.teamCodeDisplay);
        String myString = String.valueOf(number);
        myText.setText(myString);
    }

        public void openMaps(int number){
        Intent intent = new Intent(this, MapActivity.class).putExtra("team_join_id", number);
        startActivity(intent);
        }




}
