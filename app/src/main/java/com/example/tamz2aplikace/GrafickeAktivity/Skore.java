package com.example.tamz2aplikace.GrafickeAktivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.example.tamz2aplikace.DatabazeObsluha.DbObsluha;
import com.example.tamz2aplikace.Model.AdapterZebricek;
import com.example.tamz2aplikace.Model.Zebricek;
import com.example.tamz2aplikace.R;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class Skore extends AppCompatActivity {

    ListView lstView;

    // slouží pro Android tlačítko zpět
    private static final String TAG = "Zpet";

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
        setContentView(R.layout.activity_skore);

        lstView = (ListView)findViewById(R.id.lstSkore);
        DbObsluha db = new DbObsluha(this);
        List<Zebricek> lstZebricek = db.getZebricek();
        if(lstZebricek.size() > 0){
            AdapterZebricek adapter = new AdapterZebricek(this, lstZebricek);
            lstView.setAdapter(adapter);
        }
    }
}