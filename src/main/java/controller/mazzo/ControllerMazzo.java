package controller.mazzo;

import model.gestionale.Gioco;
import model.giochi.Carta;
import model.giochi.Numero;
import model.giochi.Sabot;
import model.giochi.Seme;

public class ControllerMazzo
{
    public static Sabot inizializzaSabot(int numeroMazzi)
    {
        Sabot mazzo = new Sabot(numeroMazzi);
        Numero[] numeri = {Numero.uno, Numero.due, Numero.tre, Numero.quattro, Numero.cinque,
            Numero.sei, Numero.sette, Numero.otto, Numero.nove, Numero.dieci, Numero.jack, Numero.queen,
            Numero.king};
        Seme[] semi = {Seme.cuore, Seme.quadro, Seme.fiore, Seme.picche};

        for(int z = 0; z < mazzo.getNumeroDiMazzi(); z++)
        {
            for(int i = 0; i < numeri.length; i++)
            {
                for(int j = 0; j < semi.length; j++)
                {
                    mazzo.addListaCarte(new Carta(numeri[i], semi[j]));
                }
            }
        }

        return mazzo;
    }
}
