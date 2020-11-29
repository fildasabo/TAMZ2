package com.example.tamz2aplikace.Model;

/**
 * Vyjmenovani urovni, pote toto pouzivam např. u seek baru
 * Díky úrovně si uživatel zvolí počet otázek
 */

public class Urovne {
    public static final int LEHKA_UROVEN_POC = 5;
    public static final int STREDNI_UROVEN_POC = 10;
    public static final int TEZKA_UROVEN_POC = 15;
    public static final int LEGENDARNI_UROVEN_POC = 20;

    public enum UROVEN{
        LEHKÁ,
        STŘEDNÍ,
        TĚŽKÁ,
        LEGENDÁRNÍ
    }
}
