package com.example.tamz2aplikace.GrafickeAktivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tamz2aplikace.DatabazeObsluha.DbObsluha;
import com.example.tamz2aplikace.Model.Otazky;
import com.example.tamz2aplikace.R;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class Hra extends AppCompatActivity implements View.OnClickListener{

    final static long INTERVAL = 1000; // 1 sekunda
    final static long KONEC = 15000; // 15 sekund

    // slouží pro Android tlačítko zpět
    private static final String TAG = "Zpet";
    private boolean _doubleBackToExitPressedOnce = false;

    int casProCasovac = 1;

    CountDownTimer countDownTimer; //odpočítavadlo pro casProCasovac

    List<Otazky> otazkyHra = new ArrayList<>(); // všechny otazky

    DbObsluha db;
    int index = 0, skore = 0, thisOtazka = 0, vsechnyOtazky, spravnaOdpoved;
    String uroven="";

    //Ovládání
    ProgressBar progressBar;
    Button btnA, btnB, btnC, btnD;
    TextView txtSkore, txtOtazka, txtZadani;

    //tlačítko zpět, při prvním stisknutí se zeptá, jestli chcete opravdu odejít z aplikace
    @Override
    public void onBackPressed() {

        Log.i(TAG, "vratitSeZpet--");
        if (_doubleBackToExitPressedOnce) {
            countDownTimer.cancel();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            return;
        }
        this._doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Opravdu chcete ukončit hru?", Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.activity_hra);

        //data z hlavní aktivity
        Bundle extra = getIntent().getExtras();

        if(extra != null)
            uroven = extra.getString("UROVEN");

        db = new DbObsluha(this);

        txtSkore = (TextView)findViewById(R.id.txtSkore);
        txtOtazka = (TextView)findViewById(R.id.txtOtazka);
        txtZadani = (TextView)findViewById(R.id.txtZadani);
        progressBar = (ProgressBar)findViewById(R.id.progessBar);
        btnA = (Button)findViewById(R.id.btnOdpovedA);
        btnB = (Button)findViewById(R.id.btnOdpovedB);
        btnC = (Button)findViewById(R.id.btnOdpovedC);
        btnD = (Button)findViewById(R.id.btnOdpovedD);

        btnA.setOnClickListener(this);
        btnB.setOnClickListener(this);
        btnC.setOnClickListener(this);
        btnD.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        otazkyHra = db.vsechnyOtazkyUroven(uroven);
        vsechnyOtazky = otazkyHra.size();

        countDownTimer = new CountDownTimer(KONEC, INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                ++casProCasovac;
                progressBar.setProgress(casProCasovac);
            }

            @Override
            public void onFinish() {
                countDownTimer.cancel();
                ukazOtazku(++index);
            }
        };
        ukazOtazku(index);
    }

    private void ukazOtazku(int index) {
        if(index < vsechnyOtazky)
        {
            thisOtazka++;
            txtOtazka.setText(String.format("%d/%d", thisOtazka, vsechnyOtazky));
            progressBar.setProgress(0);
            casProCasovac = 0;

            txtZadani.setText(otazkyHra.get(index).getOtazka());
            btnA.setText(otazkyHra.get(index).getOdpovedA());
            btnB.setText(otazkyHra.get(index).getOdpovedB());
            btnC.setText(otazkyHra.get(index).getOdpovedC());
            btnD.setText(otazkyHra.get(index).getOdpovedD());

            countDownTimer.start();
        }
        else{
            Intent intent = new Intent(this,Vysledek.class);
            Bundle odeslaniDat = new Bundle();
            odeslaniDat.putInt("SKORE",skore);
            odeslaniDat.putInt("VSECHNYOTAZKY",vsechnyOtazky);
            odeslaniDat.putInt("SPRAVNAODPOVED",spravnaOdpoved);
            intent.putExtras(odeslaniDat);
            intent.putExtra("UROVEN", uroven);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        countDownTimer.cancel();
        if(index < vsechnyOtazky){
            Button zmacknuteTlacitko = (Button)v;
            if(zmacknuteTlacitko.getText().equals(otazkyHra.get(index).getVysledek()))
            {
                skore+=10; //zvětšení skore
                spravnaOdpoved++; //zvětšená správné odpovědi
                ukazOtazku(++index);
            }
            else {
                ukazOtazku(++index); // pokud zvolí dobře, následuje další otázka
            }
            txtSkore.setText(String.format("%d", skore));
        }
    }
}
