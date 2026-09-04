package model.gestionale;

import model.gestionale.utenteEFigli.Dealer;
import model.gestionale.utenteEFigli.Supervisore;

import java.util.ArrayList;

/**
 * UN Tavolo, al tavolo può essere associato un dealer e più supervisori
 */
public class Tavolo
{
    private final int idTavolo;
    private Gioco gioco;
    private final int numeroPosti;

    //attributi da associazioni

    private Dealer dealer;
    private String idDealer;
    private ArrayList<Supervisore> supervisori;

    /**
     * Istanzia UN Tavolo senza informazioni sui dealer, serve in casi come la selezione dei tavoli da parte dei Clienti
     * per giocare a un gioco, ma anche quando si crea UN nuovo Tavolo dalla schermata dei supervisori
     *
     * @param idTavolo    the id tavolo
     * @param gioco       the gioco
     * @param numeroPosti the numero posti
     */
    public Tavolo(int idTavolo, Gioco gioco, int numeroPosti)
    {
        this.idTavolo = idTavolo;
        this.gioco = gioco;
        this.numeroPosti = numeroPosti;
        dealer = null;
        supervisori = new ArrayList<>();
    }

    /**
     * Istanzia UN Tavolo con id del dealer, serve per i tavoli caricati dal db
     *
     * @param idTavolo    the id tavolo
     * @param gioco       the gioco
     * @param numeroPosti the numero posti
     * @param idDealer    the id dealer
     */
    public Tavolo(int idTavolo, Gioco gioco, int numeroPosti, String idDealer)
    {
        this(idTavolo, gioco, numeroPosti);
        this.idDealer = idDealer;
        supervisori = new ArrayList<>();
    }

    /**
     * Instantiates a new Tavolo.
     * @deprecated
     *
     * @param idTavolo    the id tavolo
     * @param gioco       the gioco
     * @param numeroPosti the numero posti
     * @param dealer      the dealer
     */
    public Tavolo(int idTavolo, Gioco gioco, int numeroPosti, Dealer dealer)
    {
        this(idTavolo, gioco, numeroPosti);
        this.dealer = dealer;
    }

    /**
     * Gets numero posti.
     *
     * @return the numero posti
     */
    public int getNumeroPosti() {
        return numeroPosti;
    }

    //Necessari alla selezione del tavolo

    @Override
    public String toString() {
        return "Il tavolo " + idTavolo +
                " ha " + numeroPosti +
                " posti";
    }

    /**
     * Get id tavolo int.
     *
     * @return the int
     */
    public int getIdTavolo(){
        return idTavolo;
        }

    /**
     * Gets gioco.
     *
     * @return the gioco
     */
    public Gioco getGioco() {
        return gioco;
    }

    /**
     * Gets id dealer.
     *
     * @return the id dealer
     */
    public String getIdDealer() {
        return idDealer;
    }

    /**
     * Sets dealer.
     *
     * @param dealer the dealer
     */
    public void setDealer(Dealer dealer) {
        this.dealer = dealer;
    }

    /**
     * Sets supervisori.
     *
     * @param supervisori the supervisori
     */
    public void setSupervisori(ArrayList<Supervisore> supervisori) {
        this.supervisori = supervisori;
    }

    /**
     * Gets dealer.
     *
     * @return the dealer
     */
    public Dealer getDealer() {
        return dealer;
    }

    /**
     * Gets supervisori.
     *
     * @return the supervisori
     */
    public ArrayList<Supervisore> getSupervisori() {
        return supervisori;
    }

    /**
     * Sets gioco.
     *
     * @param gioco the gioco
     */
    public void setGioco(Gioco gioco) {
        this.gioco = gioco;
    }
}

