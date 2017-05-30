package com.example.paddy.mobka;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mapbox.directions.service.models.DirectionsRoute;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.views.MapView;


public class MapsActivity extends AppCompatActivity {

    MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Create a mapView and give it some properties.
        mapView = (MapView) findViewById(R.id.mapview);

        // set MapBox streets style.
        mapView.setStyleUrl(Style.MAPBOX_STREETS);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(41.885, -87.679)) // Sets the center of the map to Chicago
                .zoom(11)                            // Sets the zoom
                .build();

        mapView.moveCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));

        mapView.onCreate(savedInstanceState);
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

