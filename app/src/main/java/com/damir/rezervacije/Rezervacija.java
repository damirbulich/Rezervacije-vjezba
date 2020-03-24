package com.damir.rezervacije;

public class Rezervacija {

    private int pin;
    private String restoran;
    private String datum;
    private String vrijeme;
    private String br_osoba;
    private String ime;

    /* Konstruktor za kreiranje novih rezervacija */
    public Rezervacija(String restoran, String datum, String vrijeme, String br_osoba, String ime) {
        setRestoran(restoran);
        setDatum(datum);
        setVrijeme(vrijeme);
        setBr_osoba(br_osoba);
        setIme(ime);
    }


    /*! Dodati provjeru postoji li vec */
    public void setPin() {
        this.pin = (int)(Math.random()*9000+1000);
    }

    public void setRestoran(String restoran) {
        this.restoran = restoran;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public void setVrijeme(String vrijeme) {
        this.vrijeme = vrijeme;
    }

    public void setBr_osoba(String br_osoba) {
        this.br_osoba = br_osoba;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public int getPin() {
        return pin;
    }

    public String getRestoran() {
        return restoran;
    }

    public String getDatum() {
        return datum;
    }

    public String getVrijeme() {
        return vrijeme;
    }

    public String getBr_osoba() {
        return br_osoba;
    }

    public String getIme() {
        return ime;
    }
}
