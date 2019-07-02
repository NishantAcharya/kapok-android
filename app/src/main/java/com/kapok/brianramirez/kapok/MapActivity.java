package com.kapok.brianramirez.kapok;


import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mapbox.geojson.Feature;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Display map property information for a clicked map feature
 */
public class MapActivity extends AppCompatActivity implements OnMapReadyCallback,
        MapboxMap.OnMapClickListener {
    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private NavigationView navView;
    private ActionBarDrawerToggle mDrawerToggle;

    private ArrayAdapter<String> mAdapter;
    private String mActivityTitle;
    private MapView mapView;
    private Marker featureMarker;
    private MapboxMap mapboxMap;

    private FirebaseAuth mAuth;
    private String currentUser;
    private ArrayList<Marker> curMarkers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Button displayListViewBtn = findViewById(R.id.listView);

        displayListViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapActivity.this, LogListViewActivity.class);
                startActivity(intent);
            }
        });

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser().getEmail();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close);

        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navView = (NavigationView)findViewById(R.id.navList);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch (id)
                {
                    case R.id.navTeam:
                        goToTeamDisplay();
                        break;

                    case R.id.navTeamCode:
                        goToTeamCodeDisplay();
                        break;

                    case R.id.navLogOut:
                        logOutOption();
                        break;

                    case R.id.navRequests:
                        goToTeamJoinRequest();
                        break;


                    default:
                        return true;

                }
                return true;
            }
        });



// Mapbox access token is configured here. This needs to be called either in your application
// object or in the same activity which contains the mapview.
        Mapbox.getInstance(this, "pk.eyJ1Ijoia2Fwb2stZGV2ZWxvcGVyIiwiYSI6ImNqbzFscjE2ejBjd2Mza210amdtN252OXYifQ.0gR_XnITpdJF-RquzFfIcQ");

// This contains the MapView in XML and needs to be called after the access token is configured.


        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

    }


    @Override
    public void onBackPressed() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mDrawerToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        MapActivity.this.mapboxMap = mapboxMap;
        mapboxMap.addOnMapClickListener(this);
        refreshMarkers();
    }

    @Override
    public void onMapClick(@NonNull LatLng point) {


        if (featureMarker != null) {
            mapboxMap.removeMarker(featureMarker);
        }


        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference userProf = db.collection("Profiles").document(currentUser);

        // Set the admin field of the current user to true
        userProf
                .update("recentMapPoint", point)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });

        final PointF pixel = mapboxMap.getProjection().toScreenLocation(point);
        List<Feature> features = mapboxMap.queryRenderedFeatures(pixel);

        if (features.size() > 0) {
            Feature feature = features.get(0);

            String property;

            StringBuilder stringBuilder = new StringBuilder();
            if (feature.properties() != null) {
//                for (int i = 0; i < 100; i++) {
//                    featureMarker = mapboxMap.addMarker(new MarkerOptions()
//                            .position(point)
//                            .title("Location:")
//                            .snippet(point.getLatitude() + "," + point.getLongitude())
//                    );
//
//                    //  openLogMaker();
//                }

                featureMarker = mapboxMap.addMarker(new MarkerOptions()
                        .position(point)
                        .title("Location:")
                        .snippet(point.getLatitude() + "," + point.getLongitude())
                );
            }
        }
        mapboxMap.selectMarker(featureMarker);
        refreshMarkers();
        startOpenLog();
    }



    void LoglistOpen(){
        Intent i = new Intent(this, LogListViewActivity.class);
        startActivity(i);
    }
    void startOpenLog(){
    Intent i = new Intent(this, LogMakingActivity.class);
    startActivity(i);
    }

    void openLogMaker(){
        Intent i = new Intent(this, LogMakingActivity.class);
        startActivity(i);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mapboxMap != null) {
            mapboxMap.removeOnMapClickListener(this);
        }
        mapView.onDestroy();
    }


    private void addDrawerItems() {
        String[] osArray = { "Team", "Team Code", "Requests", "Settings", "Log Out" };
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MapActivity .this, "Time for an upgrade!", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Feed");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }



    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

/*

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        switch (id)
        {
            case R.id.navLogOut:
                logOutOption();
                break;
        }

        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
*/
    public void logOutOption() {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            mAuth.signOut();
            Intent intent = new Intent(this, WelcomeActivity.class);
            startActivity(intent);
    }

    public void goToTeamDisplay() {
        Intent intent = new Intent(this, TeamDIsplayActivity.class);
        startActivity(intent);
    }

    public void goToTeamCodeDisplay() {
        Intent intent = new Intent(this, TeamCodeDisplayActivity.class);
        startActivity(intent);
    }

    public void goToTeamJoinRequest() {
        Intent intent = new Intent(this, TeamJoinRequestActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    private void refreshMarkers(){

        mapboxMap.clear();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        ArrayList<MarkerOptions> allMarkers = new ArrayList<MarkerOptions>(1);

        DocumentReference userProf = db.collection("Profiles").document(currentUser);
        userProf.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        ArrayList<String> team = (ArrayList<String>)document.getData().get("team");

                        DocumentReference docRef = db.collection("Teams").document(team.get(0));
//
                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot teamDoc = task.getResult();
                                    if (teamDoc.exists()) {
                                        ArrayList<Map<String, Object>> points = (ArrayList<Map<String, Object>>) teamDoc.get("logs");
                                        for (Map<String, Object> currLog : points){
                                            Map<String, Object> currPoint = (Map<String, Object>) currLog.get("point");
                                            double lat = (double) currPoint.get("latitude");
                                            double lon = (double) currPoint.get("longitude");
                                            String name = (String) currLog.get("location");
                                            Log.d("Lets", name+lat+lon);
//                                            allMarkers.add(new MarkerOptions().position(new LatLng(lat, lon))
//                                                    .title(name));
                                            mapboxMap.addMarker(new MarkerOptions()
                                                    .position(new LatLng(lat, lon))
                                                    .title(name)
                                                    .snippet(name)
                                            );
                                        }
                                    }
                                } else {
                                }
                            }
                        });


                    }
                } else {
                }
            }
        });
    }
}