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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.here.android.mpa.common.GeoBoundingBox;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.GeoPosition;
import com.here.android.mpa.common.Image;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.common.PositioningManager;
import com.here.android.mpa.common.ViewObject;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapFragment;
import com.here.android.mpa.mapping.MapGesture;
import com.here.android.mpa.mapping.MapMarker;
import com.here.android.mpa.mapping.MapObject;
import com.here.android.mpa.mapping.MapRoute;
import com.here.android.mpa.routing.RouteManager;
import com.here.android.mpa.routing.RouteOptions;
import com.here.android.mpa.routing.RoutePlan;
import com.here.android.mpa.routing.RouteResult;

import java.io.IOException;
import java.lang.ref.WeakReference;
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
    private static final String[] REQUIRED_SDK_PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    // map embedded in the map fragment
    private Map map = null;

    // map fragment embedded in this activity
    private MapFragment mapFragment = null;

    // Initial map scheme, initialized in onCreate() and accessed in goHome()
    private static String initial_scheme = "";

    // TextView for displaying the current map scheme
    private TextView routingBarTitle = null;
    private TextView routingBarDesc = null;
    private LinearLayout routingBarLayout = null;

    // MapRoute for this activity
    private MapRoute mapRoute = null;

    private PositioningManager posManager;

    private boolean paused = false;
    private boolean routeIsActive = false;

    private MapMarker selectedMapMarker = null;
    private MapMarker lastSelectedMapMarker = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermissions();
    }

    private void initialize() {
        setContentView(R.layout.activity_here_maps);

        handleMenuStuff();
        handleUIStuff();

        // Search for the map fragment to finish setup by calling init().
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(
                R.id.mapfragment);

        mapFragment.init(new OnEngineInitListener() {
            @Override
            public void onEngineInitializationCompleted(
                    OnEngineInitListener.Error error) {
                if (error == OnEngineInitListener.Error.NONE) {

                    // Add listeners
                    initPosManager();
                    mapFragment.getMapGesture().addOnGestureListener(gestureListener);

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


        // My location test coordinates
        // 48.146915, 11.570623

        routingBarTitle = (TextView) findViewById(R.id.routeDestination);
        routingBarDesc = (TextView) findViewById(R.id.routeDescription);
        routingBarLayout = (LinearLayout) findViewById(R.id.routeBar);

        drawPOI();

        // Display position indicator
        map.getPositionIndicator().setVisible(true);
        // Set my location marker to custom image
        /*i.setImageResource(R.mipmap.ic_launcher);
        PositionIndicator.setMarker(Image).*/

        map.setZoomLevel((map.getMaxZoomLevel() + map.getMinZoomLevel()) / 1);
        //map.setZoomLevel(map.getMaxZoomLevel() - 2);


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

            if (initial_scheme.isEmpty()) {
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

    private void handleMenuStuff() {
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

    private void handleUIStuff() {
        // Hide the status bar.
        /*View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);*/

        // Translucent status bar.
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

    }

    private void drawPOI(){

        //48.147082, 11.571514

        try {
            Image poiImage = new Image();
            poiImage.setImageResource(R.mipmap.ic_poi_inactive);
            MapMarker m = new MapMarker(new GeoCoordinate(48.147082, 11.571514), poiImage);
            m.setTitle("Neue Pinakothek");
            map.addMapObject(m);

        } catch (IOException e) {
            finish();
            //e.printStackTrace();
        }

        try {
            Image poiImage = new Image();
            poiImage.setImageResource(R.mipmap.ic_poi_inactive);
            MapMarker m = new MapMarker(new GeoCoordinate(48.149689, 11.570581), poiImage);
            m.setTitle("Pinakothek d. Moderne");
            map.addMapObject(m);

        } catch (IOException e) {
            finish();
            //e.printStackTrace();
        }

    }

    private void initPosManager(){
        // Register positioning listener
        PositioningManager.getInstance().addListener(
                new WeakReference<>(positionListener));

        posManager = PositioningManager.getInstance();
        posManager.start(PositioningManager.LocationMethod.GPS_NETWORK);
    }

    private void checkGetDirections(){

        if (lastSelectedMapMarker == null)
            lastSelectedMapMarker = selectedMapMarker;

        if (!routeIsActive){
            routeIsActive = true;
            getDirections();
        } else {
            map.removeMapObject(mapRoute);

            if (lastSelectedMapMarker == selectedMapMarker){
                routeIsActive = false;
                selectedMapMarker = null;
                routingBarLayout.setVisibility(View.INVISIBLE);

            } else {
                lastSelectedMapMarker = selectedMapMarker;
                getDirections();

            }

//            map.setZoomLevel((map.getMaxZoomLevel() + map.getMinZoomLevel()) / 1, Map.Animation.LINEAR);
//
//            map.setCenter();

        }
    }


    private RouteManager.Listener routeManagerListener =
            new RouteManager.Listener() {
                public void onCalculateRouteFinished(RouteManager.Error errorCode,
                                                     List<RouteResult> result) {

                    if (errorCode == RouteManager.Error.NONE &&
                            result.get(0).getRoute() != null) {

                        // create a map route object and place it on the map
                        mapRoute = new MapRoute(result.get(0).getRoute());
                        map.addMapObject(mapRoute);

                        // Get the bounding box containing the route and zoom in
                        GeoBoundingBox gbb = result.get(0).getRoute().getBoundingBox();
                        map.zoomTo(gbb, Map.Animation.LINEAR,
                                Map.MOVE_PRESERVE_ORIENTATION);

                        routingBarLayout.setVisibility(View.VISIBLE);

                        routingBarTitle.setText(
                                String.format("%s",
                                        selectedMapMarker.getTitle()));

                        routingBarDesc.setText(
                                String.format("%d Meter",
                                        result.get(0).getRoute().getLength()));
                    } else {
                        routingBarTitle.setText(
                                String.format("Route calculation failed: %s",
                                        errorCode.toString()));
                    }
                }

                public void onProgress(int percentage) {
                    routingBarDesc.setText(
                            String.format("... %d percent done ...", percentage));
                }
            };

    public void getDirections() {

        routingBarDesc.setText("");
        if (map != null && mapRoute != null) {
            map.removeMapObject(mapRoute);
            mapRoute = null;
        }

        RouteManager routeManager = new RouteManager();

        RoutePlan routePlan = new RoutePlan();
        RouteOptions routeOptions = new RouteOptions();
        routeOptions.setTransportMode(RouteOptions.TransportMode.PEDESTRIAN);
        routeOptions.setRouteType(RouteOptions.Type.SHORTEST);
        routeOptions.setParksAllowed(true);
        routePlan.setRouteOptions(routeOptions);


        // Starting point:
        //GeoCoordinate myCoordinates = posManager.getPosition().getCoordinate();
        GeoCoordinate myCoordinates = posManager.getLastKnownPosition().getCoordinate();
        routePlan.addWaypoint(new GeoCoordinate(myCoordinates.getLatitude(), myCoordinates.getLongitude()));

        // End point:
        GeoCoordinate sMMCoords = selectedMapMarker.getCoordinate();
        routePlan.addWaypoint(new GeoCoordinate(sMMCoords.getLatitude(), sMMCoords.getLongitude()));

        // Calculate route
        RouteManager.Error error =
                routeManager.calculateRoute(routePlan, routeManagerListener);

        if (error != RouteManager.Error.NONE) {
            /*Toast.makeText(getApplicationContext(),
                    "Route calculation failed with: " + error.toString(),
                    Toast.LENGTH_SHORT)
                    .show();*/
            Toast.makeText(getApplicationContext(),
                    "Route calculation failed",
                    Toast.LENGTH_SHORT)
                    .show();
        }
    }


    // Define positioning listener
    private PositioningManager.OnPositionChangedListener positionListener = new
            PositioningManager.OnPositionChangedListener() {

                public void onPositionUpdated(PositioningManager.LocationMethod method,
                                              GeoPosition position, boolean isMapMatched) {
                    // set the center only when the app is in the foreground
                    // to reduce CPU consumption
                    if (!paused) {
                        map.setCenter(position.getCoordinate(),
                                Map.Animation.NONE);
                        map.setZoomLevel(map.getMaxZoomLevel() - 2, Map.Animation.LINEAR);
                        System.out.println("onPositionUpdated");
                    }
                }

                public void onPositionFixChanged(PositioningManager.LocationMethod method,
                                                 PositioningManager.LocationStatus status) {
                }
            };


    // Override onMapObjectsSelected gesture listener
    private MapGesture.OnGestureListener gestureListener = new MapGesture.OnGestureListener.OnGestureListenerAdapter() {
        @Override
        public boolean onMapObjectsSelected(List<ViewObject> objects) {
            // There are various types of map objects, but we only want
            // to handle the MapMarkers we have added
            for (ViewObject viewObj : objects) {
                if (viewObj.getBaseType() == ViewObject.Type.USER_OBJECT) {
                    if (((MapObject)viewObj).getType() == MapObject.Type.MARKER) {

                        // save the selected marker to use during route calculation
                        System.out.println("Selected Marker found!");
                        selectedMapMarker = ((MapMarker) viewObj);
                        checkGetDirections();

                        // If user has tapped multiple markers, just display one route
                        break;
                    }
                }
            }
            // return false to allow the map to handle this callback as well
            return false;
        }
    };


    public void onResume() {
        super.onResume();
        paused = false;
        if (posManager != null) {
            posManager.start(
                    PositioningManager.LocationMethod.GPS_NETWORK);
        }
        /*if (gestureListener != null){
            mapFragment.getMapGesture().addOnGestureListener(gestureListener);
        }*/
    }

    public void onPause() {
        if (posManager != null) {
            posManager.stop();
        }
        super.onPause();
        paused = true;
    }

    public void onDestroy() {
        if (posManager != null) {
            // Cleanup
            posManager.removeListener(
                    positionListener);
        }
        if (gestureListener != null){
            mapFragment.getMapGesture().removeOnGestureListener(gestureListener);
        }
        map = null;
        super.onDestroy();
    }


}
