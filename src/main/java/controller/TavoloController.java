package controller;

import database.implementazioneDAO.ImpDAOopc;
import model.gestionale.Gioco;
import model.gestionale.Tavolo;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Controller per la logica di tutte le schermate di selezione tavolo
 *
 * @see gui.gestionale.SelezioneTavoloPoker
 * @see gui.gestionale.SelezioneTavoloBlackJack
 * @see gui.gestionale.SelezioneTavoloSlotMachine
 */
public class TavoloController
{
    /**
     * Lista tavoli.
     */
    private final ArrayList<Tavolo> listaTavoli;

    /**
     * Instantiates a new Tavolo controller.
     */
    public TavoloController()
    {
        this.listaTavoli = new ArrayList<>();
    }

    /**
     * Funzione che istanzia tutti i tavoli di BlackJack con un dealer caricati da db e li mette in listaTavoli
     *
     * @throws SQLException the sql exception
     */
    public void popolaBlackJack() throws SQLException
    {
        ImpDAOopc db = new ImpDAOopc();

        ArrayList<Integer> idTavoli = new ArrayList<>();
        ArrayList<Integer> numeroPosti = new ArrayList<>();
        ArrayList<String> idDealer = new ArrayList<>();

        db.caricaTavoliGioco(Gioco.Blackjack, idTavoli, numeroPosti, idDealer);

        for(int i = 0; i < idTavoli.size(); i++)
        {
            String idDealerCorrente = idDealer.get(i);
            if(idDealerCorrente != null)
            {
                listaTavoli.add(new Tavolo(idTavoli.get(i), Gioco.Blackjack, numeroPosti.get(i)));
            }
        }
    }

    /**
     * Funzione che istanzia tutti i tavoli di SlotMachine caricati da db e li mette in listaTavoli
     *
     * @throws SQLException the sql exception
     */
    public void popolaSlotMachine() throws SQLException
    {
        ImpDAOopc db = new ImpDAOopc();

        ArrayList<Integer> idTavoli = new ArrayList<>();
        ArrayList<Integer> numeroPosti = new ArrayList<>();
        ArrayList<String> idDealer = new ArrayList<>();

        db.caricaTavoliGioco(Gioco.SlotMachine, idTavoli, numeroPosti, idDealer);
        //System.out.println(idTavoli.size());

        for(int i = 0; i < idTavoli.size(); i++)
        {
            String idDealerCorrente = idDealer.get(i);
            //System.out.println(idDealer.get(i).equals("null"));

            if(idDealerCorrente == null)
            {
                listaTavoli.add(new Tavolo(idTavoli.get(i), Gioco.SlotMachine, numeroPosti.get(i)));
            }
        }
    }

    /**
     * Funzione che istanzia tutti i tavoli di Poker con un dealer caricati da db e li mette in listaTavoli
     *
     * @throws SQLException the sql exception
     */
    public void popolaPoker() throws SQLException
    {
        ImpDAOopc db = new ImpDAOopc();

        ArrayList<Integer> idTavoli = new ArrayList<>();
        ArrayList<Integer> numeroPosti = new ArrayList<>();
        ArrayList<String> idDealer = new ArrayList<>();

        db.caricaTavoliGioco(Gioco.Poker, idTavoli, numeroPosti, idDealer);

        for(int i = 0; i < idTavoli.size(); i++)
        {
            String idDealerCorrente = idDealer.get(i);
            if(idDealerCorrente != null)
            {
                listaTavoli.add(new Tavolo(idTavoli.get(i), Gioco.Blackjack, numeroPosti.get(i)));
            }
        }
    }

    /**
     * Visto che nelle JList nelle gui di selezione dei tavoli passiamo un ArrayList di stringhe relative a info sui tavoli
     * piuttosto che istanze della classe Tavolo stessa, è stato creato questo metodo per desumere l'id dei tavoli partendo
     * dalle stringhe passate. Le info che analizza questa funzione sono stringhe generate da 'getTavoliId()' che è anch'essa
     * situata in questo controller
     *
     * @param idTavolo info da JList
     * @return id del tavolo
     */
    public int getIdFromList(String idTavolo)
    {
        return Integer.parseInt(idTavolo.replaceAll("[^0-9]", ""));
    }

    /**
     * Funzione che trova in listaTavoli il tavolo con l'id passato come parametro
     *
     * @param id id
     * @return tavolo con l'id passato
     */
    public Tavolo getTavoloWithId(int id){
        for(Tavolo i : listaTavoli)
        {
            if(i.getIdTavolo() == id) return i;
        }

        return null;
    }

    /**
     * Funzione che genera un ArrayList con info dei tavoli formattate in maniera tale da poter essere passate
     * alla JList. Soluzione puramente estetica, non volevamo mostrare troppe info già nel testo della JList, infatti poi
     * il toString viene chiamato quando viene selezionata una tupla della JList per mostrare più info nella JTextArea a
     * destra. Il toString di tavolo ritorna comodo per quando nella schermata dei supervisori passiamo alla JList direttamente
     * le istanze di tavolo, in quella JList è più comodo passare più informazioni perché poi la textArea dei supervisori
     * contiene anche info sui dipendenti assegnati.
     *
     * @return ArrayList di info formattate
     */
    public ArrayList<String> getTavoliId()
    {
        ArrayList<String> info = new ArrayList<>();

        for(Tavolo i : listaTavoli)
        {
            info.add("tavolo " + i.getIdTavolo());
        }

        return info;
    }

    /**
     * Funzione che calcola la somma che deve pagare un cliente per affittare un tavolo da Poker
     *
     * @param id     id del tavolo
     * @param sconto se è un utente premium viene applicato uno sconto sulla tassa da pagare
     * @return somma da pagare
     */
    public int pagaTavoloPoker(int id, Double sconto)
    {
        Tavolo tavoloScelto = getTavoloWithId(id);

        int somma = 20 + tavoloScelto.getNumeroPosti() * 10;

        return (int)(somma - (somma * sconto));
    }
}
