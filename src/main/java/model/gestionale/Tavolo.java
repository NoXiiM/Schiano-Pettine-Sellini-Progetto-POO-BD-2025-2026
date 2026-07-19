package model.gestionale;

import model.gestionale.utenteEFigli.Dealer;

import java.util.ArrayList;

public class Tavolo
{
    private int numero;
    private Gioco gioco;
    private int numeroPosti;

    //attributi da associazioni
    private Dealer dealer;
    //probabilmente non serve
    //private ArrayList<Giocatore> listaGiocatori;
    //private ArrayList<Sessione> sessioniAperteSulTavolo = new ArrayList<>();

    public Tavolo(int numero, Gioco gioco, int numeroPosti)
    {
        this.numero = numero;
        this.gioco = gioco;
        this.numeroPosti = numeroPosti;
        dealer = null;
    }

    public Tavolo(int numero, Gioco gioco, int numeroPosti, Dealer dealer)
    {
        this(numero, gioco, numeroPosti);
        this.dealer = dealer;
    }

    public int getNumeroPosti() {
        return numeroPosti;
    }
}
