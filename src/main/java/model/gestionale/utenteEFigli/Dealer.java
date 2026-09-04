package model.gestionale.utenteEFigli;

import model.gestionale.Gioco;

import java.time.LocalDate;
import java.util.ArrayList;

import static model.gestionale.Gioco.*;

/**
 * Il Dealer può vedere il tavolo a cui è assegnato e tutte le sessioni del tavolo
 */
public class Dealer extends Dipendente
{
    //soluzione assolutamente provvisoria
    private ArrayList<Gioco> giochiDoveServe = new ArrayList<>();

    /**
     * Istanzia un nuovo dealer con anche i giochi a cui serve, quando il supervisore fa il login, e può gestire anche i giochi
     * a cui serve il dealer oltre a poterli assegnare ai tavoli, serve tenere conto anche dei dati sui giochi relativi al
     * dealer
     *
     * @param username                 the username
     * @param nome                     the nome
     * @param cognome                  the cognome
     * @param codiceFiscale            the codice fiscale
     * @param dataDiNascita            the data di nascita
     * @param password                 the password
     * @param identificativoDipendente the identificativo dipendente
     * @param giochiDoveServe          the giochi dove serve
     */
    public Dealer(String username, String nome, String cognome, String codiceFiscale,
                  LocalDate dataDiNascita, String password,
                  String identificativoDipendente, ArrayList<Gioco> giochiDoveServe){

        super(username, nome, cognome, codiceFiscale, dataDiNascita, password, identificativoDipendente);
        this.giochiDoveServe = giochiDoveServe;
    }

    /**
     * Istanzia un nuovo dealer senza i giochi a cui serve, quando il dealer fa il login non è necessario che venga conservata
     * l'informazione dei giochi a cui serve
     *
     * @param username                 the username
     * @param nome                     the nome
     * @param cognome                  the cognome
     * @param codiceFiscale            the codice fiscale
     * @param dataDiNascita            the data di nascita
     * @param password                 the password
     * @param identificativoDipendente the identificativo dipendente
     */
    public Dealer(String username, String nome, String cognome, String codiceFiscale,
                  LocalDate dataDiNascita, String password,
                  String identificativoDipendente){

        super(username, nome, cognome, codiceFiscale, dataDiNascita, password, identificativoDipendente);
    }

    @Override
    public String toString() {
        return username + " " + nome + " " + cognome+ " " + "Dealer" ;
    }

    /**
     * Funzione che formatta stringa di messaggio per giochi a cui serve un dealer
     *
     * @return messaggio formattato
     */
    public String getGiochiDoveServeString() {
        String risultato = "";
        if(giochiDoveServe!=null){

            if (giochiDoveServe.contains(Blackjack)) {
                risultato += Blackjack.name()+" ";
            }
            if (giochiDoveServe.contains(Poker)){
                risultato += Poker.name()+" ";
            }
            if (risultato.isBlank()){
                risultato = "Nessuno ";
            }
        }
        else{
            risultato = "Nessuno ";
        }
        return risultato;
    }

    /**
     * Get giochi dealer array list.
     *
     * @return the array list
     */
    public ArrayList<Gioco> getGiochiDealer(){
        return giochiDoveServe;
    }

    /**
     * Aggiungi giochi.
     *
     * @param giochi the giochi
     */
    public void aggiungiGiochi(ArrayList<Gioco> giochi)
    {
        giochiDoveServe.addAll(giochi);
    }
}
