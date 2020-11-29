package com.example.tamz2aplikace.DatabazeObsluha;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Zde se nachází veškerá obsluha databáze
 * Zahrnuje otevření, zavření, vytvoření, vše co by mělo být potřeba
 * Dále bude spolupracovat s dalšíma třídama
 */

public class DbObsluha extends SQLiteOpenHelper {

    private static String DB_JMENO = "Databaze.db";
    private static String DB_CESTA ="";
    private static final int DB_VERZE = 27;
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
}
