package nl.hu.domain;

import java.time.LocalDate;

public class OVChipkaart {

    private int kaart_nummer;
    private LocalDate geldigTot;
    private int klasse;
    private double saldo;
    private Reiziger reiziger;

    public OVChipkaart(int kaart_nummer, LocalDate geldigTot, int klasse, double saldo, Reiziger reiziger) {
        this.kaart_nummer = kaart_nummer;
        this.geldigTot = geldigTot;
        this.klasse = klasse;
        this.saldo = saldo;
        this.reiziger = reiziger;
    }

    public OVChipkaart(int kaart_nummer, LocalDate geldigTot, int klasse, double saldo) {
        this.kaart_nummer = kaart_nummer;
        this.geldigTot = geldigTot;
        this.klasse = klasse;
        this.saldo = saldo;
    }

    public int getKaart_nummer() {
        return kaart_nummer;
    }

    public void setKaart_nummer(int kaart_nummer) {
        this.kaart_nummer = kaart_nummer;
    }

    public LocalDate getGeldigTot() {
        return geldigTot;
    }

    public void setGeldigTot(LocalDate geldigTot) {
        this.geldigTot = geldigTot;
    }

    public int getKlasse() {
        return klasse;
    }

    public void setKlasse(int klasse) {
        this.klasse = klasse;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public Reiziger getReiziger() {
        return reiziger;
    }

    public void setReiziger(Reiziger reiziger) {
        this.reiziger = reiziger;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OVChipkaart{kaart_nummer=");
        builder.append(kaart_nummer);
        builder.append(", geldigTot=");
        builder.append(geldigTot);
        builder.append(", klasse=");
        builder.append(klasse);
        builder.append(", saldo=");
        builder.append(saldo);
        if (reiziger != null) {
            builder.append(", reiziger=");
            builder.append(reiziger);
        }
        builder.append('}');
        return builder.toString();
    }
}
