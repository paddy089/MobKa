package com.example.paddy.mobka;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

class BildAdapter extends BaseAdapter {

    private final Context context;
    private final Integer[] bilderIDs = {
            /* added pictures
            R.drawable.bmw,
            R.drawable.lenbach_alte_villa,
            R.drawable.lenbach_neu,
            R.drawable.ns_doku,
            R.drawable.mvg,
            R.drawable.aegyptisches_museum,
            R.drawable.bayerisches_nm,
            R.drawable.de_museum,
            R.drawable.haus_der_kunst,
            R.drawable.pina_moderne
            */
    };

    public BildAdapter(Context c) {
        context = c;
    }

    @Override
    public int getCount() { return bilderIDs.length; }

    @Override
    public Object getItem(int position) { return bilderIDs[position]; }

    @Override
    public long getItemId(int position) { return 0; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView iv;
        if (convertView == null) {
            iv = new ImageView(context);
            iv.setLayoutParams(new GridView.LayoutParams(520, 520));  // changed from 50 to 520
            iv.setPadding(5, 0, 5, 0);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            iv = (ImageView) convertView;
        }

        iv.setImageResource(bilderIDs[position]);
        return iv;
    }
}


public class BildergalerieActivity extends Activity implements OnItemClickListener {
    BildAdapter adapter;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {


        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_home:

                   // Intent i0 = new Intent(MapsActivity.this, MainActivity.class);
                    //startActivity(i0);

                    return true;
                case R.id.navigation_map:


                    return true;
                case R.id.navigation_more:
                   // Intent i2 = new Intent(MapsActivity.this, MoreActivity.class);
                   // startActivity(i2);

                    return true;
            }
            return false;
        }

    };



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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bildergalerie);
        handleMenuStuff();

        GridView galerie = (GridView) findViewById(R.id.imageView1);
        adapter = new BildAdapter(this);
        galerie.setAdapter(adapter);

        galerie.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
        int resid = (Integer) adapter.getItem(pos);

        Intent intent = new Intent(this, Einzelbildactivity.class);
        intent.putExtra("resId", resid);
        startActivity(intent);
    }
}
