package com.example.tamz2aplikace.GrafickeAktivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.example.tamz2aplikace.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

public abstract class BaseActivity extends AppCompatActivity {

    // slouží pro Android tlačítko zpět
    protected static final String TAG = "Zpet";

    protected SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        boolean nocniPozadi = sharedPref.getBoolean(getString(R.string.pozadi), false);
        setActivityBackgroundColor(nocniPozadi);

    }

    @Override
    protected void onStart() {
        super.onStart();

        boolean nocniPozadi = sharedPref.getBoolean(getString(R.string.pozadi), false);
        setActivityBackgroundColor(nocniPozadi);
    }

    protected void setActivityBackgroundColor(boolean nocniPozadi) {
        int color;

        if(nocniPozadi)
        {
            color = ResourcesCompat.getColor(getResources(), R.color.pozadiStrankyNocni, null);
        }
        else {
            color = ResourcesCompat.getColor(getResources(), R.color.pozadiStranky, null);
        }

        getWindow().getDecorView().setBackgroundColor(color);
    }
}
