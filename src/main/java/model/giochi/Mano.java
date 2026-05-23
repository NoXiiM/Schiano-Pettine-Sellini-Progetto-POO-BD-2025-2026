package model.giochi;

import model.gestionale.Gioco;

import java.util.ArrayList;

public class Mano
{
    private Gioco gioco;
    private Integer larghezzaMano;
    private ArrayList<Carta> listaMano;
    private int puntata;

    public Mano(Gioco gioco) {

        //considerare l eliminazione del codice sotto
        if(gioco == Gioco.PokerItaliano)
        {
            larghezzaMano = 5;
        }
        if(gioco == Gioco.BlackJack)
        {
            larghezzaMano = null;
        }
        this.gioco = gioco;
        puntata = 0;
        listaMano = new ArrayList<>();
    }

    public Carta getCarta(int indice)
    {
        return listaMano.get(indice);
    }

    public void riceviCarta(Carta nuova)
    {
        //aggiungere controllo su dimensione mano
        //controllo ogni pescata
        listaMano.add(nuova);
    }

    public Gioco getGioco() {
        return gioco;
    }

    public Integer getLarghezzaMano() {
        return larghezzaMano;
    }

    public ArrayList<Carta> getListaMano() {
        return listaMano;
    }

    public int getDimensioneMano(){return listaMano.size();}

    public void setPuntata(int puntata) {
        this.puntata = puntata;
    }
}
