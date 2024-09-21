package nl.hu.domain;

import java.time.LocalDate;

public class Reiziger {
    private int id;
    private String voorletter;
    private String tussenvoegsel;
    private String achternaam;
    private LocalDate geboortedatum;
    private Adres adres;

    public Reiziger(int id, String voorletter, String tussenvoegsel, String achternaam, LocalDate geboortedatum) {
        this(id, voorletter, tussenvoegsel, achternaam, geboortedatum, null);
    }

    public Reiziger(int id, String voorletter, String tussenvoegsel, String achternaam, LocalDate geboortedatum, Adres adres) {
        this.id = id;
        this.voorletter = voorletter;
        this.tussenvoegsel = tussenvoegsel;
        this.achternaam = achternaam;
        this.geboortedatum = geboortedatum;
        this.adres = adres;
    }


    public Reiziger() {
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVoorletter() {
        return voorletter;
    }

    public void setVoorletter(String voorletter) {
        this.voorletter = voorletter;
    }

    public String getTussenvoegsel() {
        return tussenvoegsel;
    }

    public void setTussenvoegsel(String tussenvoegsel) {
        this.tussenvoegsel = tussenvoegsel;
    }

    public String getAchternaam() {
        return achternaam;
    }

    public void setAchternaam(String achternaam) {
        this.achternaam = achternaam;
    }

    public LocalDate getGeboortedatum() {
        return geboortedatum;
    }

    public void setGeboortedatum(LocalDate geboortedatum) {
        this.geboortedatum = geboortedatum;
    }

    public Adres getAdres() {
        return adres;
    }

    public void setAdres(Adres adres) {
        this.adres = adres;
    }

    @Override
    public String toString() {
        return "Reiziger{" +
                "id=" + id +
                ", voorletter='" + voorletter + '\'' +
                ", tussenvoegsel='" + tussenvoegsel + '\'' +
                ", achternaam='" + achternaam + '\'' +
                ", geboortedatum=" + geboortedatum +
                '}';
    }
}
