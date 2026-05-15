package model.giochi;

import model.gestionale.Gioco;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Sabot
{
    private int numeroDiCarte;
    private Integer cuttingCard;
    private int numeroDiMazzi;
    private Gioco gioco;
    private ArrayList<Carta> listaCarte;

    public Sabot(int numeroDiMazzi, Gioco gioco)
    {
        this.gioco = gioco;
        listaCarte = new ArrayList<>();
        this.numeroDiMazzi = numeroDiMazzi;
        numeroDiCarte = numeroDiMazzi*52;
        if(gioco == Gioco.BlackJack) cuttingCard = (int)(numeroDiCarte * (((Math.random() * 10) + 75)/100));
        else cuttingCard = null;
        inizializzaSabot(numeroDiMazzi);
    }

    public Integer getCuttingCard() {
        return cuttingCard;
    }

    public void inizializzaSabot(int numeroMazzi)
    {
        Numero[] numeri = {Numero.uno, Numero.due, Numero.tre, Numero.quattro, Numero.cinque,
                Numero.sei, Numero.sette, Numero.otto, Numero.nove, Numero.dieci, Numero.jack, Numero.queen,
                Numero.king};
        Seme[] semi = {Seme.cuore, Seme.quadro, Seme.fiore, Seme.picche};

        for(int z = 0; z < numeroMazzi; z++)
        {
            for(int i = 0; i < numeri.length; i++)
            {
                for(int j = 0; j < semi.length; j++)
                {
                    listaCarte.add(new Carta(numeri[i], semi[j]));
                }
            }
        }
    }

    public void stampaCarte()
    {
        for(Carta i : listaCarte)
        {
            System.out.println(i.getNumero() + " " + i.getSeme());
        }
    }

    public void mischiaMazzo()
    {
        //algoritmo di mischiata fisher yates
        Random random = new Random();
        for(int i = listaCarte.size() - 1; i != 0; i--)
        {
            Collections.swap(listaCarte, i, random.nextInt(i+1));
        }
    }
}
