package com.kapok.brianramirez.kapok;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.UUID;

public class LogListViewActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_list_view);
        ResLog TestLog1 = new ResLog(UUID.randomUUID().toString(), "FuckItPlace1", "Medicine", "None", true);
        ResLog TestLog2 = new ResLog(UUID.randomUUID().toString(), "FuckItPlace2", "Water", "None", false);
        ResLog TestLog3 = new ResLog(UUID.randomUUID().toString(), "FuckItPlace3", "Medicine", "None", false);
        ResLog TestLog4 = new ResLog(UUID.randomUUID().toString(), "FuckItPlace4", "Medicine", "None", false);
        ResLog TestLog5 = new ResLog(UUID.randomUUID().toString(), "FuckItPlace5", "Medicine", "None", false);
        ResLog TestLog6 = new ResLog(UUID.randomUUID().toString(), "FuckItPlace6", "Medicine", "None", false);
        ResLog TestLog7 = new ResLog(UUID.randomUUID().toString(), "FuckItPlace7", "Medicine", "None", false);
        ResLog TestLog8 = new ResLog(UUID.randomUUID().toString(), "FuckItPlace8", "Medicine", "None", false);
        ResLog TestLog9 = new ResLog(UUID.randomUUID().toString(), "FuckItPlace9", "Medicine", "None", true);
        ResLog TestLog10 = new ResLog(UUID.randomUUID().toString(),"FuckItPlace10", "Medicine", "None", false);
        ResLog TestLog11 = new ResLog(UUID.randomUUID().toString(), "FuckItPlace11", "Medicine", "None", false);


        ResLog[] resLogs1 = {TestLog1, TestLog2, TestLog3, TestLog4, TestLog5, TestLog6, TestLog7, TestLog8, TestLog9, TestLog10, TestLog11};
        String resLogs2 [] = {TestLog1.getLocation().concat(TestLog1.getCategory()), TestLog2.getLocation().concat(TestLog2.getCategory()), TestLog3.getLocation().concat(TestLog3.getCategory()), TestLog4.getLocation(), TestLog5.getLocation(), TestLog6.getLocation(), TestLog7.getLocation(), TestLog8.getLocation(), TestLog9.getLocation(), TestLog10.getLocation(), TestLog11.getLocation()};
        ListAdapter listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, resLogs2);
        ListView LogListView = (ListView) findViewById(R.id.LogListView);
        LogListView.setAdapter(listAdapter);

        LogListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        openLogView(position);
                    }
                }
        );
    }

            public void openLogView(int position){
                Intent i = new Intent(this, ShowLogActivity.class);
                startActivity(i);
            }





}

