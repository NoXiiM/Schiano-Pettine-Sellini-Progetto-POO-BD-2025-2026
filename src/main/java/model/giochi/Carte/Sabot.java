package model.giochi.Carte;

import controller.blackjack.DeckOut;
import model.gestionale.Gioco;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Sabot, insieme di mazzi di carte francesi, per il poker si tratta del mazzo di carte francesi senza alcune carte, per il
 * blackjack invece il sabot può essere composto da 1 a 16 mazzi interi
 */
public class Sabot
{
    private final int numeroDiCarte;
    private final Integer cuttingCard;
    private final int numeroDiMazzi;
    private final Gioco gioco;
    private final ArrayList<Carta> listaCarte;

    /**
     * Istanzia un nuovo sabot con mazzi interi, la cutting card si trova tra il 65% e il 75% del mazzo, quando si arriva
     * alla cutting card al prossimo turno si rimischia il sabot
     *
     * @param numeroDiMazzi the numero di mazzi
     * @param gioco         the gioco
     */
    public Sabot(int numeroDiMazzi, Gioco gioco)
    {
        this.gioco = gioco;
        listaCarte = new ArrayList<>();
        this.numeroDiMazzi = numeroDiMazzi;
        numeroDiCarte = numeroDiMazzi*52;
        if(gioco == Gioco.Blackjack) cuttingCard = (int)(numeroDiCarte * (((Math.random() * 10) + 65)/100));
        else cuttingCard = null;
    }

    /**
     * Controlla se la cutting card è stata superata
     *
     * @return true: si rimischia; false: non si rimischia
     */
    public boolean controlloRimischiaMazzo()
    {
        return (cuttingCard + listaCarte.size()) < numeroDiCarte;
    }

    /**
     * Questa funzione inizializza il sabot con tutte le carte, i due cicli interni creano un mazzo di carte francesi intero,
     * il ciclo esterno determina di quanti mazzi di carte francesi sarà composto il sabot
     */
    public void inizializzaSabot()
    {
        Numero[] numeri = {Numero.uno, Numero.due, Numero.tre, Numero.quattro, Numero.cinque,
                Numero.sei, Numero.sette, Numero.otto, Numero.nove, Numero.dieci, Numero.jack, Numero.queen,
                Numero.king};
        Seme[] semi = {Seme.cuore, Seme.quadro, Seme.fiore, Seme.picche};

        for(int z = 0; z < numeroDiMazzi; z++)
        {
            for (Numero numero : numeri) {
                for (Seme seme : semi) {
                    listaCarte.add(new Carta(numero, seme));
                }
            }
        }
    }

    /**
     * L'inizializzazione del sabot per il poker invece è più particolare, viene messo solo un mazzo di carte francesi però
     * prendendo solo le carte >= jack - il numero di giocatori (minimo 2)
     *
     * @param nmani the nmani
     */
    public void inizializzaSabotPoker(int nmani)
    {
        Numero[] numeri = {Numero.due, Numero.tre, Numero.quattro, Numero.cinque,
                Numero.sei, Numero.sette, Numero.otto, Numero.nove, Numero.dieci, Numero.jack, Numero.queen,
                Numero.king, Numero.uno};
        Seme[] semi = {Seme.cuore, Seme.quadro, Seme.fiore, Seme.picche};

        for(int i = 10 - nmani; i < numeri.length; i++)
        {
            for (Seme seme : semi) {
                listaCarte.add(new Carta(numeri[i], seme));
            }
        }
    }

    /**
     * Funzione che sfrutta l'algoritmo di mischiata Fisher Yates: partendo dall'ultima carta del mazzo questa viene scambiata
     * in un posto casuale di indice <= al posto in cui si trova, si fa lo stesso per la 51esima e così via fino a effettuare
     * l'ultimo scambio, quello della 2a carta del mazzo
     */
    public void mischiaMazzo()
    {
        Random random = new Random();
        for(int i = listaCarte.size() - 1; i != 0; i--)
        {
            Collections.swap(listaCarte, i, random.nextInt(i+1));
        }
    }

    /**
     * Viene presa l'ultima carta da listaCarte e rimossa, poi viene restituita dalla funzione
     *
     * @return the carta
     * @throws DeckOut se le carte del sabot finiscono inaspettatamente viene lanciato un errore di DeckOut
     */
    public Carta serviCartaDaMazzo() throws DeckOut
    {
        if(listaCarte.isEmpty())
        {
            throw new DeckOut("rimischiata di emergenza, sono finite le carte nel mazzo");
        }
        Carta out = listaCarte.getLast();
        listaCarte.removeLast();
        return out;
    }

    /**
     * Gets numero di mazzi.
     *
     * @return the numero di mazzi
     */
    public int getNumeroDiMazzi() {
        return numeroDiMazzi;
    }

    /**
     * Gets gioco.
     *
     * @return the gioco
     */
    public Gioco getGioco() {
        return gioco;
    }
}
