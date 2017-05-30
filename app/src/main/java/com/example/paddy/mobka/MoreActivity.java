package com.example.paddy.mobka;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MoreActivity extends AppCompatActivity {


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {


        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_home:

                    Intent i0 = new Intent(MoreActivity.this, MainActivity.class);
                    startActivity(i0);

                    return true;
                case R.id.navigation_map:

                    Intent i1 = new Intent(MoreActivity.this, MapsActivity.class);
                    startActivity(i1);

                    return true;
                case R.id.navigation_more:


                    return true;
            }
            return false;
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);

        handleMenuStuff();



    }


    private void handleMenuStuff(){
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Menu menu = navigation.getMenu();
        MenuItem menuItem0 = menu.getItem(0);
        MenuItem menuItem1 = menu.getItem(1);
        MenuItem menuItem2 = menu.getItem(2);

        menuItem2.setChecked(true);
        menuItem2.setIcon(R.drawable.ic_nav_more_active);

        menuItem0.setIcon(R.drawable.ic_nav_home_inactive);
        menuItem1.setIcon(R.drawable.ic_nav_map_inactive);
    }
}