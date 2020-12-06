package com.example.tamz2aplikace.Model;

/**
 * Slouzi k zobrazování skore
 * Zatím se bude jednat pouze o žebříček pro jeden konkrétní mobil
 * do budoucna možné dodělat celosvětový žebříček
 */

public class Zebricek {
    private int Id;
    private double Score;
    private int Medaile;

    public Zebricek(int id, double score, int medaile) {
        Id = id;
        Score = score;
        Medaile = medaile;
    }

    public int getMedaile() {
        return Medaile;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public double getScore() {
        return Score;
    }
}
