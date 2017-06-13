package com.example.paddy.mobka;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenu;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {


        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_home:

                    return true;
                case R.id.navigation_map:

                    //Intent i1 = new Intent(MainActivity.this, MapsActivity.class);
                    Intent i1 = new Intent(MainActivity.this, GMapsActivity.class);
                    startActivity(i1);

                    return true;
                case R.id.navigation_more:
                    Intent i2 = new Intent(MainActivity.this, MoreActivity.class);
                    startActivity(i2);

                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handleMenuStuff();


    }

    private void handleMenuStuff(){
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Menu menu = navigation.getMenu();
        MenuItem menuItem0 = menu.getItem(0);
        MenuItem menuItem1 = menu.getItem(1);
        MenuItem menuItem2 = menu.getItem(2);

        menuItem0.setChecked(true);
        menuItem0.setIcon(R.drawable.ic_nav_home_active);

        menuItem1.setIcon(R.drawable.ic_nav_map_inactive);
        menuItem2.setIcon(R.drawable.ic_nav_more_inactive);
    }



    /*private void changeColors(int primaryColor, int secondaryColor){
        //getWindow().setNavigationBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorMapViewSecondary));

        getWindow().setNavigationBarColor(ContextCompat.getColor(getApplicationContext(), secondaryColor));
        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), secondaryColor));
        //navigation.setBackgroundColor(primaryColor);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), primaryColor));

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), primaryColor));

    }*/

}
