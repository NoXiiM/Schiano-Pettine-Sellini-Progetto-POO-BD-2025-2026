package controller.blackjack;

import controller.mazzo.ControllerMazzo;
import model.giochi.*;

public class ControllerBlackJack extends ControllerMazzo implements ValoreNumero
{
    public ControllerBlackJack(Sabot mazzo) {
        super(mazzo);
    }

    public boolean isBlackJack(Mano mano)
    {
        if((getValoreNumero(mano.getCarta(0).getNumero()) == 1
        || getValoreNumero(mano.getCarta(1).getNumero()) == 1)
        && (getValoreNumero(mano.getCarta(0).getNumero()) == 10
        || (getValoreNumero(mano.getCarta(1).getNumero()) == 10))) return true;
        return false;
    }

    public boolean insurance(Mano mano)
    {
        if(mano.getListaMano().size() == 1 &&
                getValoreNumero(mano.getCarta(0).getNumero()) == 1)
            return true;
        return false;
    }

    public boolean isSplittable(Mano mano)
    {
        if(mano.getListaMano().size() == 2 &&
        mano.getCarta(0).getNumero().equals(mano.getCarta(1).getNumero())) return true;
        return false;
    }

    public int getPoints(Mano mano)
    {
        int acc = 0;
        int aceCounter = 0;
        for(Carta i : mano.getListaMano())
        {
            if(getValoreNumero(i.getNumero()) == 1) aceCounter += 1;
            else acc += getValoreNumero(i.getNumero());
        }

        for(int i = 0; i < aceCounter; i++)
        {
            if(i == aceCounter - 1 && acc <= 10) acc += 11;
            else acc += 1;
        }

        return acc;
    }

    @Override
    public int getValoreNumero(Numero valCarta) {
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
}
