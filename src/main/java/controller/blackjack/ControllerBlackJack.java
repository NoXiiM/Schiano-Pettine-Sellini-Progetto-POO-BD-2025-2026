package controller.blackjack;

import controller.mazzo.ControllerMazzo;
import model.gestionale.Gioco;
import model.giochi.*;

import java.util.ArrayList;
import java.util.Map;

public class ControllerBlackJack extends ControllerMazzo
{
    private ManoBlackJack banco;

    public ControllerBlackJack(int nmazzi, int nmani) {
        super(nmazzi, nmani, Gioco.BlackJack);

        banco = new ManoBlackJack(Gioco.BlackJack);
    }

    @Override
    public Mano creaMano(Gioco gioco) {
        return new ManoBlackJack(gioco);
    }

    public int getPoints(Mano mano)
    {
        int acc = 0;
        int aceCounter = 0;
        for(Carta i : mano.getListaMano())
        {
            if(getValoreNumero(i) == 1) aceCounter += 1;
            else acc += getValoreNumero(i);
        }

        for(int i = 0; i < aceCounter; i++)
        {
            if(i == aceCounter - 1 && acc <= 10) acc += 11;
            else acc += 1;
        }

        return acc;
    }

    public static int getValoreNumero(Carta carta) {
        Numero valCarta = carta.getNumero();
        //in blackjack l'1 può valere 1 o 11
        if(valCarta.equals(Numero.uno)) return 1;
        if(valCarta.equals(Numero.due)) return 2;
        if(valCarta.equals(Numero.tre)) return 3;
        if(valCarta.equals(Numero.quattro)) return 4;
        if(valCarta.equals(Numero.cinque)) return 5;
        if(valCarta.equals(Numero.sei)) return 6;
        if(valCarta.equals(Numero.sette)) return 7;
        if(valCarta.equals(Numero.otto)) return 8;
        if(valCarta.equals(Numero.nove)) return 9;
        return 10;
    }

    public int serviCarte()
    {
        for(Mano i: listaMani)
        {
            this.serviCarta(i);
            this.serviCarta(i);
        }

        this.serviCarta(banco);
        this.serviCarta(banco);

        return listaMani.size();
    }

    public int getNumMani()
    {
        return listaMani.size();
    }

    public int getManoSize(int index)
    {
        return listaMani.get(index).getDimensioneMano();
    }

    public String displayCardDealer(int icarta)
    {
        String path = "/Carte2/";
        int num = 0;

        Carta carta = banco.getCarta(icarta);
        Seme seme = banco.getCarta(icarta).getSeme();


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

        num += getValoreNumero(carta);
        String numString = String.format("%02d", num);

        path += numString + "_kerenel_Cards.png";

        return path;
    }

    public Mano getManoBanco()
    {
        return banco;
    }

    public StatiGioco statoPartitaIniziale(int iManoGiocatore)
    {
        Mano corrente = listaMani.get(iManoGiocatore);

        if(getValoreNumero(banco.getCarta(0)) == 1 && getPoints(corrente) == 21) return StatiGioco.evenmoney;
        if(getValoreNumero(banco.getCarta(0)) == 1) return StatiGioco.assicurabile;
        if(getPoints(corrente) == 21) return StatiGioco.blackjack;

        return StatiGioco.normale;
    }

    public boolean isSplittable(int i)
    {
        Mano mano = getMano(i);
        if(mano.getListaMano().size() == 2 &&
                mano.getCarta(0).getNumero().equals(mano.getCarta(1).getNumero())) return true;
        return false;
    }

    public int getManoBancoSize()
    {
        return banco.getDimensioneMano();
    }

    public String displayCardBanco(int icarta)
    {
        String path = "/Carte2/";
        int num = 0;

        Carta carta = banco.getCarta(icarta);
        Seme seme = banco.getCarta(icarta).getSeme();


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

        num += getValoreNumero(carta);
        String numString = String.format("%02d", num);

        path += numString + "_kerenel_Cards.png";

        return path;
    }

    public boolean algoritmoPescataBanco()
    {
        if(getPoints(banco) < 17)
        {
            serviCarta(banco);
            return true;
        }
        else return false;
    }

    public int calcolaVincita(int indexMano)
    {
        int punteggioBanco = getPoints(banco);
        ManoBlackJack manoCorrente = (ManoBlackJack) listaMani.get(indexMano);
        int punteggioGiocatore = getPoints(manoCorrente);

        int vincita;

        if(manoCorrente.getFlag() == HandStateBJ.bj && punteggioBanco != 21)
            return (int)(((float)(manoCorrente.getPuntata())) * (5/2));
        if(manoCorrente.getFlag() == HandStateBJ.assicurazione && punteggioBanco == 21)
            return manoCorrente.getPuntata();
        if(manoCorrente.getFlag() == HandStateBJ.evenmoney) return manoCorrente.getPuntata();
        if(punteggioGiocatore > 21) return 0;
        if(punteggioBanco > 21) return manoCorrente.getPuntata() * 2;

        if(punteggioBanco > punteggioGiocatore) return 0;
        else if(punteggioGiocatore > punteggioBanco) return manoCorrente.getPuntata() * 2;
        else return manoCorrente.getPuntata();
    }

    public void resetAll(int nmani)
    {
        listaMani = new ArrayList<>();
        for(int i = 0; i < nmani; i++)
        {
            this.addMano(creaMano(Gioco.BlackJack));
        }

        banco = new ManoBlackJack(Gioco.BlackJack);
    }

    public void divisione(int index)
    {
        ManoBlackJack manoCorrente = (ManoBlackJack) listaMani.get(index);

        listaMani.add(new ManoBlackJack(Gioco.BlackJack));

        ManoBlackJack nuovaMano = new ManoBlackJack(Gioco.BlackJack);

        nuovaMano.setPuntata(manoCorrente.getPuntata());

        Carta cartaTrasferita = manoCorrente.traslaCarta();
        nuovaMano.addCarta(cartaTrasferita);

        listaMani.add(index + 1, nuovaMano);
    }

    public boolean bancoHaAsso()
    {
        return (banco.getCarta(0).getNumero() == Numero.uno);
    }
}
