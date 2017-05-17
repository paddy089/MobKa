package com.example.paddy.mobka;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_liste:
                    mTextMessage.setText(R.string.title_liste);
                    changeColors(R.color.colorListViewPrimary, R.color.colorListViewSecondary);
                    return true;
                case R.id.navigation_karte:
                    mTextMessage.setText(R.string.title_karte);
                    changeColors(R.color.colorMapViewPrimary, R.color.colorMapViewSecondary);
                    //setContentView(R.layout.map_view);
                    return true;
                case R.id.navigation_favoriten:
                    mTextMessage.setText(R.string.title_favoriten);
                    changeColors(R.color.colorfavoritesViewPrimary, R.color.colorfavoritesViewSecondary);
                    //setContentView(R.layout.map_view);
                    return true;
                case R.id.navigation_optionen:
                    mTextMessage.setText(R.string.title_optionen);
                    changeColors(R.color.coloroptionsViewPrimary, R.color.coloroptionsViewSecondary);
                    //setContentView(R.layout.favorites_view);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        changeColors(R.color.colorListViewPrimary, R.color.colorListViewSecondary);

        mTextMessage = (TextView) findViewById(R.id.message);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setTitle(R.string.title_favoriten);



    }

    private void changeColors(int primaryColor, int secondaryColor){
        //getWindow().setNavigationBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorMapViewSecondary));

        getWindow().setNavigationBarColor(ContextCompat.getColor(getApplicationContext(), secondaryColor));
        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), secondaryColor));
        //navigation.setBackgroundColor(primaryColor);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), primaryColor));

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), primaryColor));

    }

}
