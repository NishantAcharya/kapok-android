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
        ResLog TestLog1 = new ResLog(null, null, "FuckItPlace1", null, null, false);
        ResLog TestLog2 = new ResLog(null, null, "FuckItPlace1", null, null, false);
        ResLog TestLog3 = new ResLog(null, null, "FuckItPlace1", null, null, false);
        ResLog TestLog4 = new ResLog(null, null, "FuckItPlace1", null, null, false);
        ResLog TestLog5 = new ResLog(null, null, "FuckItPlace1", null, null, false);
        ResLog TestLog6 = new ResLog(null, null, "FuckItPlace1", null, null, false);

        ResLog[] resLogs1 = {TestLog1, TestLog2, TestLog3, TestLog4, TestLog5, TestLog6};
        String resLogs2 [] = {TestLog1.getLocation().concat(TestLog1.getCategory()), TestLog2.getLocation().concat(TestLog2.getCategory()), TestLog3.getLocation().concat(TestLog3.getCategory()), TestLog4.getLocation(), TestLog5.getLocation(), TestLog6.getLocation()};
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

