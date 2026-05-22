package model.giochi.carte;

import model.gestionale.Gioco;

import java.util.ArrayList;

public class Mano
{
    private Gioco gioco;
    private Integer larghezzaMano;
    private ArrayList<Carta> listaMano;

    public Mano(Gioco gioco) {
        if(gioco == Gioco.PokerItaliano)
        {
            larghezzaMano = 5;
        }
        if(gioco == Gioco.BlackJack)
        {
            larghezzaMano = null;
        }
        this.gioco = gioco;
        listaMano = new ArrayList<>();
    }

    public Carta getCarta(int indice)
    {
        return listaMano.get(indice);
    }

    public void riceviCarta(Carta nuova)
    {
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
}
