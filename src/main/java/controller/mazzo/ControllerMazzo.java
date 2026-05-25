package controller.mazzo;

import model.gestionale.Gioco;
import model.giochi.*;

import java.util.ArrayList;

public abstract class ControllerMazzo
{
    protected Sabot mazzo;
    protected ArrayList<Mano> listaMani = new ArrayList<>();

    public ControllerMazzo(int nmazzi, int nmani, Gioco gioco)
    {
        mazzo = new Sabot(nmazzi, gioco);
        mazzo.inizializzaSabot();
        mazzo.mischiaMazzo();

        for(int i = 0; i < nmani; i++)
        {
            this.addMano(creaMano(gioco));
        }
    }

    public boolean controlloCuttingCard()
    {
        return mazzo.controlloRimischiaMazzo();
    }

    public void reinizializzaMazzo()
    {
        mazzo = new Sabot(mazzo.getNumeroDiMazzi(), mazzo.getGioco());
        mazzo.mischiaMazzo();
    }

    public Mano creaMano(Gioco gioco) {
        return new Mano(gioco);
    }

    public void addMano(Mano nuova)
    {
        listaMani.add(nuova);
    }

    public void serviCarta(Mano ricevitore)
    {
        ricevitore.riceviCarta(mazzo.serviCartaDaMazzo());
    }

    //funzione di mapping delle carte generica
    public static int getValoreNumero(Carta carta) {
        Numero valCarta = carta.getNumero();

        if(valCarta.equals(Numero.uno)) return 1;
        if(valCarta.equals(Numero.due)) return 2;
        if(valCarta.equals(Numero.tre)) return 3;
        if(valCarta.equals(Numero.quattro)) return 4;
        if(valCarta.equals(Numero.cinque)) return 5;
        if(valCarta.equals(Numero.sei)) return 6;
        if(valCarta.equals(Numero.sette)) return 7;
        if(valCarta.equals(Numero.otto)) return 8;
        if(valCarta.equals(Numero.nove)) return 9;
        if(valCarta.equals(Numero.dieci)) return 10;
        if(valCarta.equals(Numero.jack)) return 11;
        if(valCarta.equals(Numero.queen)) return 12;
        if(valCarta.equals(Numero.king)) return 13;
        return -1;
    }

    //funzione display card per giocatori
    public String displayCard(int imano, int icarta)
    {
        String path = "/Carte2/";
        int num = 0;

        Carta carta = listaMani.get(imano).getCarta(icarta);
        Seme seme = listaMani.get(imano).getCarta(icarta).getSeme();


        switch(seme)
        {
            case Seme.cuore:
                num = 0;
                break;
            case Seme.picche:
                num = 14;
                break;
            case Seme.quadro:
                num = 28;
                break;
            case Seme.fiore:
                num = 42;
                break;
        }

        num += ControllerMazzo.getValoreNumero(carta);
        String numString = String.format("%02d", num);

        path += numString + "_kerenel_Cards.png";

        return path;
    }

    public Mano getMano(int index)
    {
        return listaMani.get(index);
    }
}
