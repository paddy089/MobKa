package com.example.paddy.mobka;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

public class Einzelbildactivity extends Activity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_einzelbildactivity);

        if (getIntent() != null) {
            Bundle daten = getIntent().getExtras();
            if (daten != null) {
                int resid = daten.getInt("resId");

                ImageView bild = (ImageView) findViewById(R.id.imageView1);
                bild.setImageResource(resid);
            }
        }
    }
}
