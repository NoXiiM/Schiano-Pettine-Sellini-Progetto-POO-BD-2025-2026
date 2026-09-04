package model.gestionale;

import model.gestionale.utenteEFigli.Dealer;
import model.gestionale.utenteEFigli.Supervisore;

import java.util.ArrayList;

public class Tavolo
{
    private final int idTavolo;
    private Gioco gioco;
    private final int numeroPosti;

    //attributi da associazioni

    private Dealer dealer;
    private String idDealer;
    private ArrayList<Supervisore> supervisori;

    public Tavolo(int idTavolo, Gioco gioco, int numeroPosti)
    {
        this.idTavolo = idTavolo;
        this.gioco = gioco;
        this.numeroPosti = numeroPosti;
        dealer = null;
        supervisori = new ArrayList<>();
    }

    public Tavolo(int idTavolo, Gioco gioco, int numeroPosti, String idDealer)
    {
        this(idTavolo, gioco, numeroPosti);
        this.idDealer = idDealer;
        supervisori = new ArrayList<>();
    }

    public Tavolo(int idTavolo, Gioco gioco, int numeroPosti, Dealer dealer)
    {
        this(idTavolo, gioco, numeroPosti);
        this.dealer = dealer;
    }

    public int getNumeroPosti() {
        return numeroPosti;
    }

    //Necessari alla selezione del tavolo

    @Override
    public String toString() {
        return "Il tavolo " + idTavolo +
                " ha " + numeroPosti +
                " posti";
    }
    public int getIdTavolo(){
        return idTavolo;
        }

    public Gioco getGioco() {
        return gioco;
    }

    public String getIdDealer() {
        return idDealer;
    }

    public void setDealer(Dealer dealer) {
        this.dealer = dealer;
    }

    public void setSupervisori(ArrayList<Supervisore> supervisori) {
        this.supervisori = supervisori;
    }

    public Dealer getDealer() {
        return dealer;
    }

    public ArrayList<Supervisore> getSupervisori() {
        return supervisori;
    }

    public void setGioco(Gioco gioco) {
        this.gioco = gioco;
    }
}

