package com.example.tamz2aplikace.GrafickeAktivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.tamz2aplikace.DatabazeObsluha.DbObsluha;
import com.example.tamz2aplikace.R;

public class Vysledek extends BaseActivity {

    Button btnHrajZnovu, btnZpatkyDoMenu;
    TextView txtCelkoveSkore, txtVsechnyOtazky;
    ProgressBar progressBar;
    String uroven = "";

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
        setContentView(R.layout.activity_vysledek);
        getSupportActionBar().hide();

        DbObsluha db = new DbObsluha(this);

        btnHrajZnovu = (Button)findViewById(R.id.btnHrajZnovu);
        txtCelkoveSkore = (TextView)findViewById(R.id.txtVysledneSkore);
        txtVsechnyOtazky = (TextView)findViewById(R.id.txtVsechOtazky);
        progressBar = (ProgressBar)findViewById(R.id.vysledekProgressBar);
        btnZpatkyDoMenu = (Button)findViewById(R.id.btnZpetDoHlNabidky);

        btnHrajZnovu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Hra.class);
                //Pošle do Aktivity Hra herní úroven
                intent.putExtra("UROVEN", uroven);
                startActivity(intent);
                finish();
            }
        });

        btnZpatkyDoMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //dá data ze Hry
        Bundle extra = getIntent().getExtras();
        if(extra != null){
            int skore = extra.getInt("SKORE");
            int vsechnyOtazky = extra.getInt("VSECHNYOTAZKY");
            int spravnaOdpoved = extra.getInt("SPRAVNAODPOVED");
            txtCelkoveSkore.setText(String.format("Skóre : %d", skore));
            txtVsechnyOtazky.setText(String.format("Správně : %d/%d", spravnaOdpoved, vsechnyOtazky));

            uroven = extra.getString("UROVEN");

            progressBar.setMax(vsechnyOtazky);
            progressBar.setProgress(spravnaOdpoved);

            //vložení skore do databáze
            db.vlozeniSkore(skore);
        }
    }
}
