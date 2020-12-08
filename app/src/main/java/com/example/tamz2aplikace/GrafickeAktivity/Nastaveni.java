package com.example.tamz2aplikace.GrafickeAktivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.tamz2aplikace.DatabazeObsluha.DbObsluha;
import com.example.tamz2aplikace.Model.ZebricekXml;
import com.example.tamz2aplikace.R;
import com.example.tamz2aplikace.Xml.XmlParser;

import java.util.List;

import androidx.appcompat.widget.SwitchCompat;

public class Nastaveni extends BaseActivity {

    Button btnSmazZebricek, btnPridejSkore;
    SwitchCompat switchPozadi;

    DbObsluha dbObsluha;

    @Override
    public void onBackPressed() {
        Log.i(TAG, "vratitSeZpet--");
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_nastaveni);
        getSupportActionBar().hide();

        btnSmazZebricek = (Button) findViewById(R.id.btnSmazZebricek);
        btnPridejSkore = (Button) findViewById(R.id.btnPridejSkore);
        switchPozadi = (SwitchCompat) findViewById(R.id.switchPozadi);

        dbObsluha = new DbObsluha(this);

        //sharedPref = getPreferences(Context.MODE_PRIVATE);
        //sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean nocniPozadi = sharedPref.getBoolean(getString(R.string.pozadi), false);

        switchPozadi.setChecked(nocniPozadi);
        setActivityBackgroundColor(nocniPozadi);

        //Tlačítko smaz zebricek
        btnSmazZebricek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbObsluha.smazSkore();
                Toast.makeText(Nastaveni.this, "Žebříček byl smazaný", Toast.LENGTH_LONG).show();
            }
        });

        //Tlačítko pridej skore z XML
        btnPridejSkore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XmlParser xmlParser = new XmlParser();
                XmlResourceParser myXml = getApplicationContext().getResources().getXml(R.xml.zebricek);
                List<ZebricekXml> zebricekXmlList = xmlParser.parseToObjects(myXml);
                dbObsluha.vlozeniSkore(zebricekXmlList);
                Toast.makeText(Nastaveni.this, "Skóre bylo vloženo.", Toast.LENGTH_LONG).show();
            }
        });

        //Tlačítko switch - nocni pozadi
        switchPozadi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean(getString(R.string.pozadi), isChecked);
                editor.apply();

                //setActivityBackgroundColor
                setActivityBackgroundColor(isChecked);
            }
        });
    }
}
