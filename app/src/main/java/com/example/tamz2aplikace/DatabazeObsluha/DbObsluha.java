package com.example.tamz2aplikace.DatabazeObsluha;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.tamz2aplikace.Model.Otazky;
import com.example.tamz2aplikace.Model.Urovne;
import com.example.tamz2aplikace.Model.Zebricek;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Zde se nachází veškerá obsluha databáze
 * Zahrnuje otevření, zavření, vytvoření, vše co by mělo být potřeba
 * Dále bude spolupracovat s dalšíma třídama
 */

public class DbObsluha extends SQLiteOpenHelper {

    private static String DB_JMENO = "Databaze.db";
    private static String DB_CESTA ="";
    private static final int DB_VERZE = 100;
    private SQLiteDatabase databaze;
    private Context mContext = null;

    public DbObsluha(Context context) {
        super(context, DB_JMENO, null, DB_VERZE);
        DB_CESTA = context.getApplicationInfo().dataDir + "/databases/";

        File file = new File(DB_CESTA + "Databaze.db");
        if(file.exists())
            otevreniDatabaze();

        this.mContext = context;
    }

    public void otevreniDatabaze() {
        String mojeCesta = DB_CESTA + DB_JMENO;
        databaze = SQLiteDatabase.openDatabase(mojeCesta, null, SQLiteDatabase.OPEN_READWRITE);
    }

    //Zkopíruje databázi ze složky assets do systémové složky

    public void kopirovaniDatabaze() throws IOException {
        try {
            InputStream mujVstup = mContext.getAssets().open(DB_JMENO);
            String vystupniNazevSouboru = DB_CESTA + DB_JMENO;
            OutputStream mujVystup = new FileOutputStream(vystupniNazevSouboru);

            byte[] buffer = new byte[1024];
            int velikost;
            while((velikost = mujVstup.read(buffer)) > 0)
                mujVystup.write(buffer, 0, velikost);

            mujVystup.flush();
            mujVystup.close();
            mujVstup.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private boolean overeniDataze()  {
        SQLiteDatabase tempDB = null;
        try {
            String mojeCesta = DB_CESTA + DB_JMENO;
            tempDB = SQLiteDatabase.openDatabase(mojeCesta, null, SQLiteDatabase.OPEN_READWRITE);

        }catch (SQLiteException e) {
            e.printStackTrace();
        }
        if(tempDB != null)
        {
            tempDB.close();
        }
        return tempDB != null ? true : false;
    }

    public void vytvoreniDatabaze() throws IOException {
        boolean existenceDatabaze = overeniDataze();
        if (existenceDatabaze) {

        }else  {
            this.getReadableDatabase();
            try {
                db_delete();
                kopirovaniDatabaze();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public synchronized void close() {
        if(databaze != null)
            databaze.close();

        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    public void db_delete()
    {
        File file = new File(DB_CESTA);
        if(file.exists())
        {
            file.delete();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion > oldVersion)
            try {

                kopirovaniDatabaze();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public List<Otazky> vsechnyOtazkyUroven(String uroven){
        List<Otazky> seznamOtazek = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        int konec = 0;

        if(uroven.equals(Urovne.UROVEN.LEHKÁ.toString()))
            konec = 5;
        else if(uroven.equals(Urovne.UROVEN.STŘEDNÍ.toString()))
            konec = 10;
        else if(uroven.equals(Urovne.UROVEN.TĚŽKÁ.toString()))
            konec = 15;
        else if(uroven.equals(Urovne.UROVEN.LEGENDÁRNÍ.toString()))
            konec = 20;
        Cursor c;
        try {
            c = db.rawQuery(String.format("SELECT DISTINCT id_z FROM Otazky ORDER BY RANDOM() LIMIT %d", konec), null);
            Cursor d;
            while(c.moveToNext()) {
                int Id = c.getInt(c.getColumnIndex("ID_z"));

                d = db.rawQuery(String.format("SELECT ot.ID_z, ot.Spravna, z.Zadani, od.ID_o, od.Odpovedi, " +
                        "(SELECT Odpovedi FROM Odpovedi os WHERE os.ID_o = ot.Spravna) Spravna FROM Otazky ot " +
                        "INNER JOIN Zadani z ON z.ID_z = ot.ID_z " +
                        "INNER JOIN odpovedi od ON od.ID_o = ot.ID_o WHERE ot.ID_z = %d", Id), null);
                int i = 0;
                String prehozeni[] = new String[4];
                String Otazka = "";
                String Vysledek = "";

                while(d.moveToNext()) {
                    Otazka = d.getString(d.getColumnIndex("z.Zadani"));
                    prehozeni[i++] = d.getString(d.getColumnIndex("od.Odpovedi"));
                    Vysledek = d.getString(d.getColumnIndex("Spravna"));
                }
                //přehození odpovědí
                List<String> strList = Arrays.asList(prehozeni);
                Collections.shuffle(strList);
                prehozeni = strList.toArray(new String[strList.size()]);
                String OdpovedA = prehozeni[0];
                String OdpovedB = prehozeni[1];
                String OdpovedC = prehozeni[2];
                String OdpovedD = prehozeni[3];
                Otazky otazky = new Otazky(Id, Otazka, OdpovedA, OdpovedB, OdpovedC, OdpovedD, Vysledek);
                seznamOtazek.add(otazky);
            }
            c.close();

        }catch (Exception e){
            e.printStackTrace();
        }

        db.close();
        return seznamOtazek;
    }

    //Vložení skore do žebříčku
    public void vlozeniSkore(double skore){
        String query = "INSERT INTO Zebricek(Skore) VALUES("+ skore +")";
        databaze.execSQL(query);
    }

    //skore - žebříček
    public List<Zebricek> getZebricek(){
        List<Zebricek> seznamZebricku = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c;
        int a = 1;
        try {
            c = db.rawQuery("SELECT * FROM Zebricek ORDER BY Skore DESC", null);
            if (c == null) return null;
            c.moveToFirst();
            do {
                int Id = c.getInt(c.getColumnIndex("ID"));
                double Skore = c.getDouble(c.getColumnIndex("Skore"));
                c.moveToNext();
                double pom = c.getDouble(c.getColumnIndex("Skore"));
                c.moveToPrevious();
                int medaile;

                medaile = a;

                Zebricek zebricek = new Zebricek(Id, Skore, medaile);
                seznamZebricku.add(zebricek);

                if(Skore != pom) {
                    a++;
                }

                if(a > 3) {
                    a = 4;
                }
            }while (c.moveToNext());
            c.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        db.close();

        return seznamZebricku;
    }
}
