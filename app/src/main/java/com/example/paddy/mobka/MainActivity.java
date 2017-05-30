package com.example.paddy.mobka;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
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

                    Intent i2 = new Intent(MainActivity.this, MapsActivity.class);
                    startActivity(i2);

                    return true;
                case R.id.navigation_more:
                    Intent i3 = new Intent(MainActivity.this, MoreActivity.class);
                    startActivity(i3);

                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);



        //Window window = activity.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        //getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        //getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));

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
