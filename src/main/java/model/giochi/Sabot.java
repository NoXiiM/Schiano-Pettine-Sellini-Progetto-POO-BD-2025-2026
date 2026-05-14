package model.giochi;

import model.gestionale.Gioco;

import java.util.ArrayList;

public class Sabot
{
    private int numeroDiCarte;
    private Integer cuttingCard;
    private int numeroDiMazzi;
    private Gioco gioco;
    private ArrayList<Carta> listaCarte;

    public Sabot(int numeroDiMazzi)
    {
        listaCarte = new ArrayList<>();
        this.numeroDiMazzi = numeroDiMazzi;
        numeroDiCarte = numeroDiMazzi*52;
        if(gioco == Gioco.BlackJack) cuttingCard = (int)(numeroDiCarte * (((Math.random() * 10) + 75)/100));
        else cuttingCard = null;
        inizializzaSabot(numeroDiMazzi);
    }

    public ArrayList<Carta> getListaCarte() {
        return listaCarte;
    }

    public void addListaCarte(Carta carta) {
        listaCarte.add(carta);
    }

    public int getNumeroDiMazzi() {
        return numeroDiMazzi;
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
}
