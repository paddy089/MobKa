package com.example.paddy.mobka;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapFragment;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HereMapsActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {


        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent i1 = new Intent(HereMapsActivity.this, MainActivity.class);
                    startActivity(i1);
                    return true;

                case R.id.navigation_map:
                    return true;

                case R.id.navigation_more:
                    Intent i2 = new Intent(HereMapsActivity.this, MoreActivity.class);
                    startActivity(i2);
                    return true;
            }
            return false;
        }

    };


    /**
     * permissions request code
     */
    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;

    /**
     * Permissions that need to be explicitly requested from end user.
     */
    private static final String[] REQUIRED_SDK_PERMISSIONS = new String[] {
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE };

    // map embedded in the map fragment
    private Map map = null;

    // map fragment embedded in this activity
    private MapFragment mapFragment = null;

    // Initial map scheme, initialized in onCreate() and accessed in goHome()
    private static String initial_scheme = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermissions();
    }


    /*private void initialize() {
        setContentView(R.layout.activity_here_maps);
        handleMenuStuff();
        handleUIStuff();

        // Search for the map fragment to finish setup by calling init().
        mapFragment = (MapFragment)getFragmentManager().findFragmentById(
                R.id.mapfragment);

        mapFragment.init(new OnEngineInitListener() {
            @Override
            public void onEngineInitializationCompleted(
                    OnEngineInitListener.Error error)
            {
                if (error == OnEngineInitListener.Error.NONE) {
                    // retrieve a reference of the map from the map fragment
                    map = mapFragment.getMap();
                    // Set the map center to the Vancouver region (no animation)
                    map.setCenter(new GeoCoordinate(49.196261, -123.004773, 0.0),
                            Map.Animation.NONE);
                    // Set the zoom level to the average between min and max
                    map.setZoomLevel(
                            (map.getMaxZoomLevel() + map.getMinZoomLevel()) / 5);
                } else {
                    System.out.println("ERROR: Cannot initialize Map Fragment");
                }
            }
        });
    }*/

    private void initialize() {
        setContentView(R.layout.activity_here_maps);
        handleMenuStuff();
        handleUIStuff();

        // Search for the map fragment to finish setup by calling init().
        mapFragment = (MapFragment)getFragmentManager().findFragmentById(
                R.id.mapfragment);

        mapFragment.init(new OnEngineInitListener() {
            @Override
            public void onEngineInitializationCompleted(
                    OnEngineInitListener.Error error)
            {
                if (error == OnEngineInitListener.Error.NONE) {

                    onMapFragmentInitializationCompleted();

                } else {
                    System.out.println("ERROR: Cannot initialize Map Fragment");
                }
            }
        });
    }

    private void onMapFragmentInitializationCompleted() {
        // retrieve a reference of the map from the map fragment
        map = mapFragment.getMap();
        map.setCenter(new GeoCoordinate(48.146893, 11.570602, 0.0),
                Map.Animation.NONE);
        // Set the map zoom level to the average between min and max (no
        // animation)
        map.setZoomLevel((map.getMaxZoomLevel() + map.getMinZoomLevel()) / 1);
    }

    // Functionality for taps of the "Change Map Scheme" button
    public void changeScheme(View view) {
        if (map != null) {
            // Local variable representing the current map scheme
            String current = map.getMapScheme();
            // Local array containing string values of available map schemes
            List<String> schemes = map.getMapSchemes();
            // Local variable representing the number of available map schemes
            int total = map.getMapSchemes().size();

            if (initial_scheme.isEmpty())
            {
                //save the initial scheme
                initial_scheme = current;
            }

            // If the current scheme is the last element in the array, reset to
            // the scheme at array index 0
            if (schemes.get(total - 1).equals(current))
                map.setMapScheme(schemes.get(0));
            else {
                // If the current scheme is any other element, set to the next
                // scheme in the array
                for (int count = 0; count < total - 1; count++) {
                    if (schemes.get(count).equals(current))
                        map.setMapScheme(schemes.get(count + 1));
                }
            }
        }
    }


    /**
     * Checks the dynamically-controlled permissions and requests missing permissions from end user.
     */
    protected void checkPermissions() {
        final List<String> missingPermissions = new ArrayList<>();
        // check all required dynamic permissions
        for (final String permission : REQUIRED_SDK_PERMISSIONS) {
            final int result = ContextCompat.checkSelfPermission(this, permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(permission);
            }
        }
        if (!missingPermissions.isEmpty()) {
            // request all missing permissions
            final String[] permissions = missingPermissions
                    .toArray(new String[missingPermissions.size()]);
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_ASK_PERMISSIONS);
        } else {
            final int[] grantResults = new int[REQUIRED_SDK_PERMISSIONS.length];
            Arrays.fill(grantResults, PackageManager.PERMISSION_GRANTED);
            onRequestPermissionsResult(REQUEST_CODE_ASK_PERMISSIONS, REQUIRED_SDK_PERMISSIONS,
                    grantResults);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                for (int index = permissions.length - 1; index >= 0; --index) {
                    if (grantResults[index] != PackageManager.PERMISSION_GRANTED) {
                        // exit the app if one permission is not granted
                        Toast.makeText(this, "Required permission '" + permissions[index]
                                + "' not granted, exiting", Toast.LENGTH_LONG).show();
                        finish();
                        return;
                    }
                }
                // all permissions were granted
                initialize();
                break;
        }
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

    private void handleUIStuff(){
        // Hide the status bar.
        /*View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);*/

        // Translucent status bar.
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

    }

}
