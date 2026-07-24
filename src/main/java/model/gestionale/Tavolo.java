package model.gestionale;

import model.gestionale.utenteEFigli.Dealer;

import java.util.ArrayList;

public class Tavolo
{
    private int idTavolo;
    private Gioco gioco;
    private int numeroPosti;

    //attributi da associazioni
    private Dealer dealer;
    //probabilmente non serve
    //private ArrayList<Giocatore> listaGiocatori;
    //private ArrayList<Sessione> sessioniAperteSulTavolo = new ArrayList<>();

    public Tavolo(int idTavolo, Gioco gioco, int numeroPosti)
    {
        this.idTavolo = idTavolo;
        this.gioco = gioco;
        this.numeroPosti = numeroPosti;
        dealer = null;
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
}

