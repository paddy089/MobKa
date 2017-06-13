package com.example.paddy.mobka;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.google.android.gms.maps.SupportMapFragment;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.views.MapView;

import com.mapzen.android.lost.internal.LocationEngine;


public class MapsActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {


        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_home:

                    Intent i0 = new Intent(MapsActivity.this, MainActivity.class);
                    startActivity(i0);

                    return true;
                case R.id.navigation_map:


                    return true;
                case R.id.navigation_more:
                    Intent i2 = new Intent(MapsActivity.this, MoreActivity.class);
                    startActivity(i2);

                    return true;
            }
            return false;
        }

    };

    private MapView mapView;
    private FloatingActionButton floatingActionButton;
    private LocationEngine locationEngine;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        handleMenuStuff();




        // Hide the status bar.
        /*View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);*/

        // Translucent status bar.
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);


        // Create a mapView and give it some properties.
        mapView = (MapView) findViewById(R.id.mapview);

        // set MapBox streets style.
        mapView.setStyleUrl(Style.MAPBOX_STREETS);

        //Location myLocation = mapView.getMyLocation();
        //new LatLng(41.885, -87.679)
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(48.146944, 11.570489))
                .zoom(11)
                .build();

        mapView.moveCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));

        mapView.onCreate(savedInstanceState);

    }

    private void handleMenuStuff(){
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Menu menu = navigation.getMenu();
        MenuItem menuItem0 = menu.getItem(0);
        MenuItem menuItem1 = menu.getItem(1);
        MenuItem menuItem2 = menu.getItem(2);

        menuItem1.setChecked(true);
        menuItem1.setIcon(R.drawable.ic_nav_map_active);

        menuItem0.setIcon(R.drawable.ic_nav_home_inactive);
        menuItem2.setIcon(R.drawable.ic_nav_more_inactive);
    }

    private void handleStatusBarProps(){
        // clear FLAG_TRANSLUCENT_STATUS flag:
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
    }

    /**
     * Called when the activity is becoming visible to the user.
     */
    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    /**
     * Called when the activity is no longer visible to the user, because another activity has been resumed
     */
    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    /**
     * Called when the system is about to start resuming a previous activity.
     */
    @Override
    public void onPause()  {
        super.onPause();
        mapView.onPause();
    }

    /**
     * Called when the activity will start interacting with the use
     */
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * The final call you receive before your activity is destroyed.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

}

