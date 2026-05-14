package model.giochi;

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
}
