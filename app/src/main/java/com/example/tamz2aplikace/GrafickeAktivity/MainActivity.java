package com.example.tamz2aplikace.GrafickeAktivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tamz2aplikace.DatabazeObsluha.DbObsluha;
import com.example.tamz2aplikace.Model.Urovne;
import com.example.tamz2aplikace.R;

import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    SeekBar seekBar;
    TextView txtUroven;
    Button btnSkore, btnHrej;
    DbObsluha dbObsluha;

    // slouží pro Android tlačítko zpět
    private boolean _doubleBackToExitPressedOnce = false;
    private static final String TAG = "Zpet";

    //tlačítko zpět, při prvním stisknutí se zeptá, jestli chcete opravdu odejít z aplikace
    @Override
    public void onBackPressed() {

        Log.i(TAG, "vratitSeZpet--");
        if (_doubleBackToExitPressedOnce) {
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory( Intent.CATEGORY_HOME );
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
            finish();
        }
        this._doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Opravdu chcete odejít?", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                _doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        seekBar = (SeekBar) findViewById(R.id.seekBar);
        txtUroven = (TextView) findViewById(R.id.txtUroven);
        btnSkore = (Button) findViewById(R.id.btnSkore);
        btnHrej = (Button) findViewById(R.id.btnHrej);

        dbObsluha = new DbObsluha(this);
        try{
            dbObsluha.vytvoreniDatabaze();
        }catch (IOException e){
            e.printStackTrace();
        }

        //Zmena -  seek bar
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                switch (progress)
                {
                    case 0:
                        txtUroven.setText(Urovne.UROVEN.LEHKÁ.toString());
                        break;
                    case 1:
                        txtUroven.setText(Urovne.UROVEN.STŘEDNÍ.toString());
                        break;
                    case 2:
                        txtUroven.setText(Urovne.UROVEN.TĚŽKÁ.toString());
                        break;
                    case 3:
                        txtUroven.setText(Urovne.UROVEN.LEGENDÁRNÍ.toString());
                        break;
                    default: txtUroven.setText(Urovne.UROVEN.LEHKÁ.toString());
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //Tlačítko hry - předání aktivity
        // kod

        //Tlačítko skore - předání aktivity
        // kod
    }

    private String getHerniUroven() {
        switch (seekBar.getProgress())
        {
            case 0:
                return Urovne.UROVEN.LEHKÁ.toString();
            case 1:
                return Urovne.UROVEN.STŘEDNÍ.toString();
            case 2:
                return Urovne.UROVEN.TĚŽKÁ.toString();
            case 3:
                return Urovne.UROVEN.LEGENDÁRNÍ.toString();
            default: return Urovne.UROVEN.LEHKÁ.toString();
        }
    }
}
