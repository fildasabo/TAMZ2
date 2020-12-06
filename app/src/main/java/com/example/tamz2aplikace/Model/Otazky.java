package com.example.tamz2aplikace.Model;

/**
 * Třída slouží pro práci s otázkama, odpověďma, atd...
 * Zde jsou pouze vytvoření getry, setry a konstruktor
 * otázky se berou z SqLite databáze
 */

public class Otazky {
    private int ID;
    private String Otazka;
    private String OdpovedA;
    private String OdpovedB;
    private String OdpovedC;
    private String OdpovedD;
    private String Vysledek;

    public Otazky(int ID, String otazka, String odpovedA, String odpovedB, String odpovedC, String odpovedD, String vysledek) {
        this.ID = ID;
        Otazka = otazka;
        OdpovedA = odpovedA;
        OdpovedB = odpovedB;
        OdpovedC = odpovedC;
        OdpovedD = odpovedD;
        Vysledek = vysledek;
    }

    public String getOtazka() {
        return Otazka;
    }

    public String getOdpovedA() {
        return OdpovedA;
    }

    public String getOdpovedB() {
        return OdpovedB;
    }

    public String getOdpovedC() {
        return OdpovedC;
    }

    public String getOdpovedD() {
        return OdpovedD;
    }

    public String getVysledek() {
        return Vysledek;
    }
}