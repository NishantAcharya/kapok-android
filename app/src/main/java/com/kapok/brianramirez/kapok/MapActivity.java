package com.kapok.brianramirez.kapok;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import static com.mapbox.mapboxsdk.style.expressions.Expression.toNumber;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.geojson.Feature;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.expressions.Expression;
import com.mapbox.mapboxsdk.style.layers.CircleLayer;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.layers.TransitionOptions;
import com.mapbox.mapboxsdk.style.sources.GeoJsonOptions;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;


import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.sql.Ref;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

import static android.graphics.Color.rgb;

import static com.mapbox.mapboxsdk.style.expressions.Expression.all;
import static com.mapbox.mapboxsdk.style.expressions.Expression.division;
import static com.mapbox.mapboxsdk.style.expressions.Expression.exponential;
import static com.mapbox.mapboxsdk.style.expressions.Expression.get;
import static com.mapbox.mapboxsdk.style.expressions.Expression.gte;
import static com.mapbox.mapboxsdk.style.expressions.Expression.has;
import static com.mapbox.mapboxsdk.style.expressions.Expression.interpolate;
import static com.mapbox.mapboxsdk.style.expressions.Expression.literal;
import static com.mapbox.mapboxsdk.style.expressions.Expression.lt;
import static com.mapbox.mapboxsdk.style.expressions.Expression.rgb;
import static com.mapbox.mapboxsdk.style.expressions.Expression.stop;
import static com.mapbox.mapboxsdk.style.expressions.Expression.toNumber;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.circleColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.circleRadius;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconSize;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textField;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textSize;

/**
 * Display map property information for a clicked map feature
 */
public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, PermissionsListener,
        //Listener for all things related to clicking on the map
        MapboxMap.OnMapClickListener {
    //All the variables needed in the map activity
    private PermissionsManager permissionsManager;
    private int position = -1;
    private ListView mDrawerList;
    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView navView;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;
    private String mActivityTitle;
    private MapView mapView;
    private Marker featureMarker;
    private MapboxMap mapboxMap;
    private boolean isAdmin;
    private FirebaseAuth mAuth;
    private String currentUser;
    private ArrayList<Marker> curMarkers;
    private String usrEmail;
    private String usrName;
    private ArrayList<String> teamEmails;
    private int positionNum;
    int numOfReq;
    private ArrayList<String> teamMates = new ArrayList<String>(1);
    final Context context = this;
    String teamcode;
    private JsonObject logs;
    private JsonArray features;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Getting the current version of the map
        Mapbox.getInstance(this, "pk.eyJ1Ijoia2Fwb2stZGV2ZWxvcGVyIiwiYSI6ImNqbzFscjE2ejBjd2Mza210amdtN252OXYifQ.0gR_XnITpdJF-RquzFfIcQ");
       //setting the theme according to the option selected
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }
        setContentView(R.layout.activity_map);
        //Getting user data
        mAuth = Database.mAuth;
        currentUser = mAuth.getCurrentUser().getEmail();

        //getting database data of the user
        FirebaseFirestore db = Database.db;
        DocumentReference docRef = db.collection("Profiles").document(currentUser);

        //Initializing log related objects
        logs = new JsonObject();
        logs.addProperty("type", "FeatureCollection");
        features = new JsonArray();
        logs.add("features", new JsonArray());

        //Getting data from the service for notifications
        Intent i = new Intent(this, DatabaseListener.class);
        startService(i);

        //Initializing user's team data in respective variables
        getTeam();


        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    //Getting a version of the Profiles
                    DocumentSnapshot document = task.getResult();
                    //Doing  team checks and filling data
                    if (document.exists()) {
                       teamcode = ((ArrayList<String>)document.get("team")).get(0);
                    }
                }
            }
        });

        //*No Idea what this is
        logs.remove("features");
        features = new JsonArray();

        //Displaying the List view and refresh buttons
        Button displayListViewBtn = findViewById(R.id.listView);
        FloatingActionButton refresh = findViewById(R.id.refreshButton);

        //The working of the refresh button is defined here
        refresh.setOnClickListener(new View.OnClickListener() {
        @Override
        //Restarting the given activity on clicking
        public void onClick(View view) {
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
        });

        //The working of the listView button, starts the log list view activity
        displayListViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshMarkers();
                Intent intent = new Intent(MapActivity.this, LogListViewActivity.class);
                startActivity(intent);
            }
        });



       //Getting Profiles table in the database
      DocumentReference userProf = db.collection("Profiles").document(currentUser);




        //This is the section related to the navigation drawer, the place where navigation menu will go
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close);

        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dl = (DrawerLayout)findViewById(R.id.drawer_layout);
        t = new ActionBarDrawerToggle(this, dl,R.string.drawer_open, R.string.drawer_close);

        dl.addDrawerListener(t);
        t.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //This is the navigation menu setup
        navView = (NavigationView)findViewById(R.id.navListAdmin);
        //Admin check for hamburger menu

        View header = navView.getHeaderView(0);
        TextView userName = header.findViewById(R.id.nav_header_name);
        TextView userMail = header.findViewById(R.id.nav_header_email);

        userProf.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            //This is set of statements that define the username and email on the header of nav menu
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        usrName = document.getData().get("name").toString();
                        usrEmail = currentUser;

                        if(document.getData().get("isAdmin").toString().equals("true")){
                            Database.isAdmin = true;
                        }
                        else{
                            Database.isAdmin = false;
                        }

                        //Admin check here to find to add admin and responder here
                        String nameText = usrName;
                        if(!Database.isAdmin){
                            nameText += "(Responder)";
                        }
                        else{
                            nameText += "(Administrator)";
                        }
                        userName.setText(nameText);
                        userMail.setText(usrEmail);
                        //Map activity add on leave team
                    }
                }
            }
        });
        if(!Database.isAdmin) {
            Menu nav_Menu = navView.getMenu();
            nav_Menu.findItem(R.id.navRequests).setVisible(false);
            nav_Menu.findItem(R.id.navAssigned).setVisible(false);
        }
        //This is the functions related to each option in the nav view
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch (id)
                {
                    case R.id.navAssigned:
                        goToAssignedView();
                        break;
                    case R.id.navTeam:
                        goToTeamDisplay();
                        break;

                    case R.id.navTeamCode:
                        goToTeamCodeDisplay();
                        break;

                    case R.id.navChangeTheme:
                        changeTheme();
                        break;

                    case R.id.navLogOut:
                        logOutOption();
                        break;

                    case R.id.navRequests:
                        goToTeamJoinRequest();
                        break;
                    //Add Assigned here.
                    //This is navAssigned(Need to put in a function)
                    case R.id.navMyTask:
                            ArrayList<String> Assigned = new ArrayList<String>(1);
                            DocumentReference docRef = db.collection("Profiles").document(currentUser);

                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                //Getting to logs in Teams table
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            ArrayList<String> userCurrentTeam = (ArrayList<String>) document.getData().get("team");
                                            String TeamCode = userCurrentTeam.get(0);
                                            DocumentReference docRef = db.collection("Teams").document(TeamCode);
                                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                //Making a dialog box for the assigned task
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        DocumentSnapshot document = task.getResult();
                                                        if (document.exists()) {
                                                            //Filling up the array of assigned logs
                                                            ArrayList<Map<String, Object>> locations = (ArrayList<Map<String, Object>>) document.get("logs");
                                                            for (Map<String, Object> loc : locations) {
                                                                if (loc.get("assignment").equals(usrName)) {
                                                                    Assigned.add((String) loc.get("location"));
                                                                }
                                                            }
                                                            //Found logs were assigned leading to dialog box making
                                                            if(Assigned.size() > 0){

                                                                final Dialog dialog = new Dialog(MapActivity.this);
                                                                dialog.setContentView(R.layout.assign_show);
                                                                Spinner spinner = dialog.findViewById(R.id.assigned);
                                                                Button cancel = dialog.findViewById(R.id.alert_close);
                                                                Button open = dialog.findViewById(R.id.assign_open);

                                                                //Making Spinner using adapterview
                                                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MapActivity.this, android.R.layout.simple_spinner_dropdown_item, Assigned);

                                                                spinner.setAdapter(adapter);
                                                                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                                    //Choosing the spinner position for opening assigned log activity
                                                                    @Override
                                                                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                                                                        positionNum = position;
                                                                    }
                                                                    @Override
                                                                    public void onNothingSelected(AdapterView<?> adapterView) {
                                                                        positionNum = -1;
                                                                    }
                                                                });

                                                                //Button Operations for open and close , open leading to new activty
                                                                open.setOnClickListener(new View.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(View view) {
                                                                        if (positionNum != -1) {
                                                                            openAssignView(positionNum);
                                                                            dialog.dismiss();
                                                                        } else {
                                                                            Toast.makeText(MapActivity.this, "No Item selected", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }
                                                                });

                                                                cancel.setOnClickListener(new View.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(View v) {
                                                                        dialog.dismiss();
                                                                    }
                                                                });

                                                                dialog.show();
                                                            }
                                                            else{
                                                                Toast.makeText(MapActivity.this, "You Don't Have Any Tasks!", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    }
                                                }
                                            });
                                        }
                                    }
                                }
                            });
                        break;

                        //Alert box....
                        //This is the leave team option's working
                    case R.id.navLeaveTeam:
                        if(Database.isAdmin){
                            if(teamMates.size()>1) {

                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context,R.style.AlertDialog);


                                // set dialog message
                                alertDialogBuilder
                                        .setMessage("You are the admin of the team, you have to perform either one of the activity before proceeding to leave the team!")
                                        .setCancelable(false)
                                        .setPositiveButton("Close this activity and choose a new team admin", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                // if this button is clicked, close
                                                // current activity
                                                dialog.cancel();
                                            }
                                        })
                                        .setNegativeButton("I wish to dissolve the team regardless", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                // if this button is clicked, just close
                                                // the dialog box and do nothing
                                                removeFromTeam();
                                                //DISSOLVE IT....
                                            }
                                        });

                                // create alert dialog
                                AlertDialog alertDialog = alertDialogBuilder.create();

                                // show it
                                alertDialog.show();
                            }

                            else if (!hasMembers()){
                                removeFromTeam();

                            }

                        }
                        else {
                            removeFromTeam();
                        }
                        break;


                    default:
                        return true;

                }
                return true;
            }
        });
        //End of Navigation Drawer items


// Mapbox access token is configured here. This needs to be called either in your application
// object or in the same activity which contains the mapview.


// This contains the MapView in XML and needs to be called after the access token is configured.

        //Making/Displaying the actual map
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        //Assigning the find me button here
        FloatingActionButton findMe = findViewById(R.id.findmeButton);
        findMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            onMapReady(MapActivity.this.mapboxMap);
            }
        });

    }


//This checks for isAdmin in the database in the teams
    private boolean isAdmin() {
        FirebaseFirestore db = Database.db;
        String currentUse = mAuth.getCurrentUser().getEmail();
        DocumentReference docRef = db.collection("Profiles").document(currentUser);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        ArrayList<String> userCurrentTeam = (ArrayList<String>) document.getData().get("team");
                        String TeamCode = userCurrentTeam.get(0);
                        DocumentReference docRef = db.collection("Teams").document(TeamCode);
                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()){
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()){
                                        String currentAdmin = document.getData().get("admin").toString();
                                        Database.isAdmin = currentUse.equals(currentAdmin);
                                    }
                                }
                            }
                        });

                    }
                }
            }
        });
        return isAdmin;
    }



    @Override
    public void onBackPressed() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(t.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }


    //MAPBOX METHODS///////////////////////////////////////////////////////////////

    @Override
    //Ready the mapbox map
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        MapActivity.this.mapboxMap = mapboxMap;
        mapboxMap.setStyle(new Style.Builder().fromUri("mapbox://styles/mapbox/cjerxnqt3cgvp2rmyuxbeqme7"),
                new Style.OnStyleLoaded() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        style.setTransition(new TransitionOptions(0, 0, false));

                        mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
                                12.099, -79.045), 3));


                        addClusteredGeoJsonSource(style);

                        Toast.makeText(MapActivity.this, R.string.zoom_map_in_and_out_instruction,
                                Toast.LENGTH_SHORT).show();

                        enableLocationComponent(style);
                    }
                });

        mapboxMap.addOnMapClickListener(this);

        refreshMarkers();
    }


    //Clustering happens here
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void addClusteredGeoJsonSource(@NonNull Style loadedMapStyle) {


        FirebaseFirestore db = Database.db;
        DocumentReference docRef = db.collection("Profiles").document(currentUser);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        //isAdmin = isAdmin();
                        teamcode = ((ArrayList<String>)document.get("team")).get(0);
                        DocumentReference teamRef = db.collection("Teams").document(teamcode);
                        teamRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        ArrayList<Map<String, Object>> locations = (ArrayList<Map<String, Object>>) document.get("logs");
                                        for(Map<String, Object> log: locations){
                                            JsonObject logJson = new JsonObject();
                                            logJson.addProperty("type", "Feature");
                                            JsonObject geo = new JsonObject();
                                            geo.addProperty("type", "Point");
                                            //Go here p
                                            JsonArray coor = new JsonArray();
                                            HashMap<String, Float> point = (HashMap<String, Float>) log.get("point");
                                            coor.add(point.get("longitude"));
                                            coor.add(point.get("latitude"));
                                            geo.add("coordinates", coor);
                                            JsonObject prop = new JsonObject();
                                            prop.addProperty("time", log.get("time").toString());
                                            prop.addProperty("Log Rating", (String)log.get("Log Rating"));
                                            prop.addProperty("category", (String)log.get("category"));
                                            prop.addProperty("creator", (String)log.get("creator"));
                                            prop.addProperty("info", (String)log.get("info"));
                                            prop.addProperty("location", (String)log.get("location"));
                                            logJson.add("properties", prop);
                                            logJson.add("geometry", geo);
                                            features.add(logJson.deepCopy());
                                        }
                                        logs.add("features", features);
                                        Gson gson = new Gson();
                                        loadedMapStyle.addSource(
                                                // Point to GeoJSON data. This example visualizes all M1.0+ earthquakes from
                                                // 12/22/15 to 1/21/16 as logged by USGS' Earthquake hazards program.
                                                new GeoJsonSource("earthquakes",
                                                        gson.toJson(logs),
                                                        new GeoJsonOptions()
                                                                .withCluster(true)
                                                                .withClusterMaxZoom(14)
                                                                .withClusterRadius(50)
                                                )
                                        );
                                        SymbolLayer count = new SymbolLayer("count", "earthquakes");
                                        count.setProperties(
                                                textField(Expression.toString((Expression) get("point_count"))),
                                                textSize(20f),
                                                textColor(ContextCompat.getColor(context, R.color.colorBlack)),
                                                textIgnorePlacement(true),
                                                textAllowOverlap(true)
                                        );
                                        loadedMapStyle.addLayer(count);
                                    }
                                }
                            }
                        });
                    }
                }
            }
        });

    }


    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
// Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {

// Get an instance of the component
            LocationComponent locationComponent = mapboxMap.getLocationComponent();

// Activate with options
            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions.builder(this, loadedMapStyle).build());

// Enable to make component visible
            locationComponent.setLocationComponentEnabled(true);

// Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);

// Set the component's render mode
            locationComponent.setRenderMode(RenderMode.COMPASS);
            locationComponent.zoomWhileTracking(20);
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    enableLocationComponent(style);
                }
            });
        } else {
            Toast.makeText(this, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    //What to do when there is a click on the map
    public boolean onMapClick(@NonNull LatLng point) {
        Map<String,Object> log;

        if (featureMarker != null) {
            mapboxMap.removeMarker(featureMarker);
        }


        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference userProf = db.collection("Profiles").document(currentUser);

        // Set the admin field of the current user to true
        userProf
                .update("recentMapPoint", point);

        final PointF pixel = mapboxMap.getProjection().toScreenLocation(point);
        List<Feature> features = mapboxMap.queryRenderedFeatures(pixel);

        if (features.size() > 0) {
            Feature feature = features.get(0);

            String property;

            StringBuilder stringBuilder = new StringBuilder();
            if (feature.properties() != null) {
                featureMarker = mapboxMap.addMarker(new MarkerOptions()
                        .position(point)
                        .title("Location:")
                        .snippet(point.getLatitude() + "," + point.getLongitude())
                );
            }
        }
        mapboxMap.selectMarker(featureMarker);
        refreshMarkers();
        //Make a method that loads up the logs and opens a log page if the longlat value matches
        startOpenLog();
        return true;
    }


    //One off methods
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
    @SuppressWarnings( {"MissingPermission"})
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

    //Checking for leave team for admin, if the team has more than 1 memeber
    public boolean member_check = false;

    public boolean hasMembers(){

        FirebaseFirestore db = Database.db;
        DocumentReference userProf = db.collection("Profiles").document(currentUser);
        userProf.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task)
            {
                if (task.isSuccessful())
                {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists())
                    {
                        ArrayList<String> team = (ArrayList<String>) document.getData().get("team");

                        DocumentReference docRef = db.collection("Teams").document(team.get(0));
//
                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot teamDoc = task.getResult();
                                    if (teamDoc.exists()) {
                                        ArrayList<String> members = (ArrayList<String>) teamDoc.get("members");
                                        if (members.size() > 1) {
                                            member_check = true;

                                        }
                                    } else {
                                    }
                                }
                            }
                        });
                    }
                }
            }
    });
        return member_check;
    }

    //Leading to different activty methods
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

    public void goToAssignedView(){
        Intent intent = new Intent(this, AssignedLogAllActivity.class);
        startActivity(intent);
    }

    public void changeTheme(){
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        finish();
        startActivity(new Intent(MapActivity.this, MapActivity.this.getClass()));
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
    //Mapview method
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    //Method to remove from team uses hasMemebers
    public void removeFromTeam() {
        FirebaseFirestore db = Database.db;
        DocumentReference userProf = db.collection("Profiles").document(currentUser.toString());
        AlertDialog.Builder a = new AlertDialog.Builder(MapActivity.this,R.style.AlertDialog);
        a.setMessage("Are you sure you want to leave the team").setCancelable(true)
                .setPositiveButton("Accept", new DialogInterface.OnClickListener() {

                    //If user accepts the request
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(!Database.isAdmin) {
                            userProf.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            ArrayList<String> team = (ArrayList<String>) document.getData().get("team");
                                            teamcode = team.get(0);
                                            DocumentReference teamRef = db.collection("Teams").document(team.get(0));
                                            teamRef.update("members", FieldValue.arrayRemove(currentUser));
                                        }
                                    }
                                    userProf.update("status", "none");
                                    userProf.update("isAdmin", "false");
                                    userProf.update("team", FieldValue.arrayRemove(teamcode));
                                    userProf.update("team",FieldValue.arrayUnion(null));
                                    Intent intent = new Intent(MapActivity.this, TeamWelcomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        }
                        else{
                            userProf.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            ArrayList<String> team = (ArrayList<String>) document.getData().get("team");
                                            teamcode = team.get(0);
                                            DocumentReference teamRef = db.collection("Teams").document(team.get(0));
                                            teamRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if(task.isSuccessful()){
                                                        DocumentSnapshot document = task.getResult();
                                                        if(document.exists()){
                                                            ArrayList<String> members = (ArrayList<String>) document.get("members");
                                                            for(String member: members){
                                                                teamRef.update("members", FieldValue.arrayRemove(member));
                                                                DocumentReference usrPref = db.collection("Profiles").document(member);
                                                                usrPref.update("status", "none");
                                                                usrPref.update("isAdmin", "false");
                                                                usrPref.update("team", FieldValue.arrayRemove(teamcode));
                                                                usrPref.update("team",FieldValue.arrayUnion(null));
                                                            }
                                                        }
                                                    }
                                                }
                                            });
                                        }
                                    }
                                }
                            });
                            Intent intent = new Intent(MapActivity.this, TeamWelcomeActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
        a.create();
        a.show();
    }

    //Dunno what this does,didn't remove
    private void updateJson(String team){
        mAuth = Database.mAuth;
        currentUser = mAuth.getCurrentUser().getEmail();
        FirebaseFirestore db = Database.db;
        DocumentReference docRef = db.collection("Profiles").document(currentUser);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                       // isAdmin = isAdmin();
                        teamcode = ((ArrayList<String>)document.get("team")).get(0);
                    }
                }
            }
        });
        logs.remove("features");
        features = new JsonArray();

        docRef = db.collection("Teams").document(team);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        ArrayList<Map<String, Object>> locations = (ArrayList<Map<String, Object>>) document.get("logs");
                        for(Map<String, Object> log: locations){
                            JsonObject logJson = new JsonObject();
                            logJson.addProperty("type", "Feature");
                            JsonObject geo = new JsonObject();
                            geo.addProperty("type", "Point");
                            JsonArray coor = new JsonArray();
                            HashMap<String, Float> point = (HashMap<String, Float>) log.get("point");
                            coor.add(point.get("longitude"));
                            coor.add(point.get("latitude"));
                            geo.add("coordinates", coor);
                            JsonObject prop = new JsonObject();
                            prop.addProperty("time", log.get("time").toString());
                            prop.addProperty("Log Rating", (String)log.get("Log Rating"));
                            prop.addProperty("category", (String)log.get("category"));
                            prop.addProperty("creator", (String)log.get("creator"));
                            prop.addProperty("info", (String)log.get("info"));
                            prop.addProperty("location", (String)log.get("location"));
                            logJson.add("properties", prop);
                            logJson.add("geometry", geo);
                            features.add(logJson.deepCopy());
                        }
                        logs.add("features", features);
                    }
                }
            }
        });
    }

    //Method to refresh markers
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

    //This gets the user's team
    void getTeam(){
        mAuth = Database.mAuth;
        FirebaseUser currentUser = mAuth.getCurrentUser();
        FirebaseFirestore db = Database.db;
        DocumentReference docRef = Database.db.collection("Profiles").document(currentUser.getEmail());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        ArrayList<String> userCurrentTeam = (ArrayList<String>) document.getData().get("team");
                        String TeamCode = userCurrentTeam.get(0);
                        DocumentReference docRef = Database.db.collection("Teams").document(TeamCode);
                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {

                                        teamEmails = ((ArrayList<String>) document.getData().get("members"));
                                        for(int i = 0; i < teamEmails.size(); i++){
                                            DocumentReference docRef = db.collection("Profiles").document(teamEmails.get(i));
                                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if(task.isSuccessful()){
                                                        DocumentSnapshot document = task.getResult();
                                                        if(document.exists()){
                                                            teamMates.add((String)document.getData().get("name"));
                                                        }
                                                    }
                                                }
                                            });
                                        }
                                    }
                                }
                            }
                        });
                    }
                }
            }
        });
    }

//Method leading to assgin view
    public void openAssignView (int position){
        Intent i = new Intent(this, AssignedLogActivity.class).putExtra("Log Position", position);
        startActivity(i);
    }
}
