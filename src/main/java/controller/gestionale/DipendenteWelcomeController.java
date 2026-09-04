package controller.gestionale;

import database.implementazioneDAO.ImpDAOop;
import database.implementazioneDAO.ImpDAOopd;
import model.gestionale.Gioco;
import model.gestionale.Sessione;
import model.gestionale.Tavolo;
import model.gestionale.utenteEFigli.Cliente;
import model.gestionale.utenteEFigli.Dealer;
import model.gestionale.utenteEFigli.Dipendente;
import model.gestionale.utenteEFigli.Supervisore;

import javax.swing.*;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Controller che gestisce un Dipendente
 */
public class DipendenteWelcomeController extends WelcomeController {

    private final Dipendente dipendenteCorrente;
    private ArrayList<Cliente> clientiInLocale;
    private ArrayList<Dipendente> dipendentiInLocale;
    private ArrayList<Tavolo> tavoliInLocale;
    private ArrayList<String> usernames;

    /**
     * Costruttore che istanzia un nuovo DipendenteWelcomeController a partire dal WelcomeController, questo comporta
     * l'esistenza di 2 controller, questo tipo di logica comporta il dover gestire la pulizia dei dati di WelcomeController
     * in alcuni casi
     *
     * @param controller WelcomeController
     */
    public DipendenteWelcomeController(WelcomeController controller){
        super(controller.getCurrentUser(), controller.getUsernamesList());
        this.usernames= controller.getUsernamesList();

        dipendenteCorrente= (Dipendente) getCurrentUser();

        clientiInLocale = new ArrayList<>();
        dipendentiInLocale = new ArrayList<>();
        tavoliInLocale = new ArrayList<>();
    }

    /**
     * Carica tutti i clienti tramite la funzione del db nella lista clientiInLocale e poi questa lista viene anche ritornata
     * dalla funzione (in maniera tale che questa funzione viene chiamata direttamente nella funzione addAll per aggiungere
     * le istanze nel modello della JList)
     *
     * @return lista clientiInLocale
     * @throws SQLException the sql exception
     */
//admin
    public ArrayList<Cliente> getListaClientiDB() throws SQLException{
        clientiInLocale = new ArrayList<>();

        ArrayList<String> username = new ArrayList<>();
        ArrayList<String> nome = new ArrayList<>();
        ArrayList<String> cognome = new ArrayList<>();
        ArrayList<String> codiceFiscale = new ArrayList<>();
        ArrayList<LocalDate> dataDiNascita = new ArrayList<>();
        ArrayList<String> password = new ArrayList<>();
        ArrayList<String> codiceTesseraGiocatore = new ArrayList<>();
        ArrayList<Boolean> premium = new ArrayList<>();
        ArrayList<Double> sconto_premium = new ArrayList<>();
        ArrayList<Boolean> sospetto = new ArrayList<>();
        ArrayList<Long> tempoDiGiocoInSec = new ArrayList<>();
        ArrayList<Integer> fichesGiocate = new ArrayList<>();
        ArrayList<Integer> saldo = new ArrayList<>();
        ArrayList<Integer> partiteGiocate = new ArrayList<>();
        ArrayList<Double> vincitaPercentualeTotale = new ArrayList<>();
        ArrayList<LocalDate> dataBan = new ArrayList<>();
        ArrayList<String> motiviBan = new ArrayList<>();

        ImpDAOopd db = new ImpDAOopd();
        db.recuperaDatiClienti(username, nome, cognome, codiceFiscale, dataDiNascita, password,
                codiceTesseraGiocatore,premium,sconto_premium,sospetto,tempoDiGiocoInSec,fichesGiocate,saldo,partiteGiocate,
                vincitaPercentualeTotale, dataBan,motiviBan);

        for(int i = 0; i < username.size(); i++) {

            Cliente c = new Cliente(
                    username.get(i),
                    nome.get(i),
                    cognome.get(i),
                    codiceFiscale.get(i),
                    dataDiNascita.get(i),
                    password.get(i),
                    codiceTesseraGiocatore.get(i),
                    premium.get(i),
                    sconto_premium.get(i),
                    sospetto.get(i),
                    tempoDiGiocoInSec.get(i),
                    fichesGiocate.get(i),
                    saldo.get(i),
                    partiteGiocate.get(i),
                    vincitaPercentualeTotale.get(i),
                    dataBan.get(i),
                    motiviBan.get(i)
            );

            clientiInLocale.add(c);
        }
        return clientiInLocale;
    }

    /**
     * Carica tutti i dipendenti tramite la funzione del db nella lista dipendentiInLocale e poi questa lista viene anche ritornata
     * dalla funzione (in maniera tale che questa funzione viene chiamata direttamente nella funzione addAll per aggiungere
     * le istanze nel modello della JList). Visto che la funzione che prende i dati da db restituisce la tabella di dipendenti
     * joinata con GiochiDealer (l'ordine è order by idDipendente), c'è un ciclo for che per ogni dealer incontrato fra i dipendenti
     * controlla nelle tuple successive tramite un while se hanno lo stesso id in maniera tale che nell'ArrayList del dealer
     * si aggiungano tutti i giochi che sa fare e che queste linee non instanzino dipendenti duplicati ma con giochi diversi
     *
     * @return lista dipendentiInLocale
     * @throws SQLException the sql exception
     */
    public ArrayList<Dipendente> getDipendentiDB() throws SQLException{
        dipendentiInLocale = new ArrayList<>();

        ArrayList<String> idDipendenti = new ArrayList<>();
        ArrayList<String> nome = new ArrayList<>();
        ArrayList<String> cognome = new ArrayList<>();
        ArrayList<LocalDate> dataDiNascita = new ArrayList<>();
        ArrayList<String> codiceFiscale = new ArrayList<>();
        ArrayList<String> username = new ArrayList<>();
        ArrayList<String> password = new ArrayList<>();
        ArrayList<String> ruolo = new ArrayList<>();
        ArrayList<String> gioco = new ArrayList<>();

        ImpDAOopd db = new ImpDAOopd();

        db.recuperaDatiDipendenti(idDipendenti, nome, cognome, dataDiNascita, codiceFiscale,
                username, password, ruolo, gioco);

        Dipendente d;

        //System.out.println(idDipendenti.size());

        for(int i = 0; i < idDipendenti.size(); i++) {
            if(ruolo.get(i).equals("Dealer")){
                ArrayList<Gioco> listaGiochi = new ArrayList<>();
                if(gioco.get(i) != null) listaGiochi.add(Gioco.valueOf(gioco.get(i)));
                String idCorrente = idDipendenti.get(i);

                while(i < (idDipendenti.size() - 1) && idDipendenti.get(i+1).equals(idCorrente))
                {
                    i++;
                    listaGiochi.add(Gioco.valueOf(gioco.get(i)));
                }

                //for(Gioco j : listaGiochi) System.out.println(j);

                d = new Dealer(username.get(i), nome.get(i), cognome.get(i), codiceFiscale.get(i), dataDiNascita.get(i),
                        password.get(i), idDipendenti.get(i), listaGiochi);
                dipendentiInLocale.add(d);
            }
            else{
                d = new Supervisore(username.get(i), nome.get(i), cognome.get(i), codiceFiscale.get(i),
                        dataDiNascita.get(i), password.get(i), idDipendenti.get(i));
                dipendentiInLocale.add(d);
            }

        }
        return dipendentiInLocale;
    }

    /**
     * Carica tutti i tavoli tramite la funzione del db nella lista tavoliInLocale e poi questa lista viene anche ritornata
     * dalla funzione (in maniera tale che questa funzione viene chiamata direttamente nella funzione addAll per aggiungere
     * le istanze nel modello della JList). Inoltre questa funzione sfrutta altre due funzione per assegnare ai tavoli tutti
     * i supervisori e il dealer rispettivi
     *
     * @return lista tavoliInLocale
     * @throws SQLException the sql exception
     */
    public ArrayList<Tavolo> getTavoliDB() throws SQLException
    {
        tavoliInLocale = new ArrayList<>();

        ArrayList<Integer> idTavolo = new ArrayList<>();
        ArrayList<Gioco> gioco = new ArrayList<>();
        ArrayList<Integer> numeroPosti = new ArrayList<>();
        ArrayList<String> idDealer = new ArrayList<>();

        ImpDAOopd db = new ImpDAOopd();

        db.recuperaDatiTavoli(idTavolo, gioco, numeroPosti, idDealer);

        for(int i = 0; i < idDealer.size(); i++)
        {
            Tavolo temp = new Tavolo(idTavolo.get(i), gioco.get(i), numeroPosti.get(i), idDealer.get(i));
            assegnaDealerDelTavolo(temp);
            fetchDadbAssegnaSupervisoriDelTavolo(temp);
            tavoliInLocale.add(temp);
        }

        return tavoliInLocale;
    }

    /**
     * Gets tavoli in locale.
     *
     * @return the tavoli in locale
     */
    public ArrayList<Tavolo> getTavoliInLocale() {
        return tavoliInLocale;
    }

    /**
     * Funzione che ritorna una nuova lista che non è altro che il risultato dell'applicazione di tutti i filtri passati
     * come parametro su clientiInLocale
     *
     * @param nome             nome
     * @param cognome          cognome
     * @param username         username
     * @param saldoMin         saldo min
     * @param saldoMax         saldo max
     * @param percentualeMin   percentuale min
     * @param percentualeMax   percentuale max
     * @param partiteMin       partite min
     * @param partiteMax       partite max
     * @param sospetto         sospetto
     * @param ban              ban
     * @param checkSaldo       check saldo
     * @param checkPartite     check partite
     * @param checkPercentuale check percentuale
     * @return ArrayList con filtri applicati
     */
    public ArrayList<Cliente> ricercaClienti(String nome, String cognome, String username, int saldoMin, int saldoMax,
                                             double percentualeMin, double percentualeMax, int partiteMin, int partiteMax, String sospetto, String ban,
                                             boolean checkSaldo, boolean checkPartite, boolean checkPercentuale){

        ArrayList<Cliente> clientiRicercati = new ArrayList<>();

        for(Cliente c: clientiInLocale){

            if(!nome.isBlank()){
                if(!(nome.equals(c.getNome()))) continue;
            }

            if(!cognome.isBlank()){
                if(!(cognome.equals(c.getCognome()))) continue;
            }

            if(!username.isBlank()){
                if(!(username.equals(c.getUsername()))) continue;
            }

            if(checkSaldo){
                if(!(c.getSaldo() >= saldoMin && c.getSaldo() <= saldoMax)) continue;
            }

            if(checkPercentuale){
                if(!(c.getVincitaPercentualeTot() >= percentualeMin && c.getVincitaPercentualeTot() <= percentualeMax)) continue;
            }

            if(checkPartite){
                if(!(c.getPartiteGiocate() >= partiteMin && c.getPartiteGiocate() <= partiteMax)) continue;
            }

            if (!sospetto.equals("indifferente")) {
                if (sospetto.equals("si")) {
                    if (!c.isSospetto()) continue;
                } else {
                    if (c.isSospetto()) continue;
                }
            }
            if (!ban.equals("indifferente")) {
                if (ban.equals("si")) {
                    if (c.getBan() == null) continue;
                } else {
                    if (c.getBan() != null) continue;
                }
            }
            clientiRicercati.add(c);

        }
        return clientiRicercati;
    }

    /**
     * Funzione che ritorna una nuova lista che non è altro che il risultato dell'applicazione di tutti i filtri passati
     * come parametro su dipendentiInLocale
     *
     * @param nome       nome
     * @param cognome    cognome
     * @param username   username
     * @param checkRuolo check ruolo
     * @param ruolo      ruolo
     * @return ArrayList con filtri applicati
     */
    public ArrayList<Dipendente> ricercaDipendente(String nome, String cognome, String username,boolean checkRuolo, String ruolo){
        ArrayList<Dipendente> dipendentiRicercati = new ArrayList<>();

        for(Dipendente dipendente : dipendentiInLocale) {
            if (!nome.isBlank()) {
                if (!nome.equals(dipendente.getNome())) continue;
            }
            if (!cognome.isBlank()) {
                if (!cognome.equals(dipendente.getCognome())) continue;
            }
            if(!username.isBlank()){
                if (!username.equals(dipendente.getUsername())) continue;
            }
            if(checkRuolo &&!(ruolo.isBlank())){
                if((dipendente instanceof Dealer) && !(ruolo.equals("Dealer")))continue; //Da scrivere con lettere maiuscole iniziali
                if((dipendente instanceof Supervisore) && !(ruolo.equals("Supervisore"))) continue;
            }
            dipendentiRicercati.add(dipendente);
        }
        return dipendentiRicercati;
    }

    /**
     * Funzione che ritorna una nuova lista che non è altro che il risultato dell'applicazione di tutti i filtri passati
     * come parametro su tavoliInLocale
     *
     * @param gioco gioco
     * @return ArrayList con filtri applicati
     */
    public ArrayList<Tavolo> ricercaTavolo(Gioco gioco)
    {
        ArrayList<Tavolo> tavoliRicercati = new ArrayList<>();

        for(Tavolo i : tavoliInLocale)
        {
            if(gioco != null)
            {
                if(!(i.getGioco().equals(gioco))) continue;
            }

            tavoliRicercati.add(i);
        }

        return tavoliRicercati;
    }

    /**
     * Funzione per registrare un nuovo dipendente nel db, questa funzione è disponibile solo ai supervisori, solo i supervisori
     * possono registrare nuovi dipendenti
     *
     * @param username      username
     * @param nome          nome
     * @param cognome       cognome
     * @param codiceFiscale codice fiscale
     * @param dataNascita   data di nascita
     * @param password      password
     * @param ruolo         ruolo
     * @param gioco         gioco
     * @throws RuntimeException errore lanciato se non sono stati compilati tutti i campi, se l'username è già stato preso
     * @throws SQLException the sql exception
     */
    public void registraDipendente(String username, String nome, String cognome, String codiceFiscale,
                                   LocalDate dataNascita, String password, String ruolo, ArrayList<Gioco> gioco) throws RuntimeException, SQLException{

        if (username.isBlank() || nome.isBlank() || cognome.isBlank() || codiceFiscale.isBlank() || password.isBlank() || ruolo.isBlank())
            throw new RuntimeException("Compila tutti i campi!");

        //check locale
        for (String user : usernames) {
            if (username.equals(user)){
                throw new RuntimeException("Username non disponibile");
            }
        }

        String idTesseraDip= generaCodiceTessera(username);

        try {
            new ImpDAOop().registrazioneDipendente(idTesseraDip, username, nome, cognome, codiceFiscale,
                    dataNascita, password, ruolo);
        } catch (SQLException e) {
            aggiornaUsernames();
            throw new SQLException(e);
        }

        if(gioco != null)
        {
            new ImpDAOopd().aggiungiGiocoDealer(idTesseraDip, gioco);
        }

        if(ruolo.equals("Supervisore")) dipendentiInLocale.add(new Supervisore(username, nome, cognome, codiceFiscale, dataNascita, password, idTesseraDip));
        if(ruolo.equals("Dealer")) dipendentiInLocale.add(new Dealer(username, nome, cognome, codiceFiscale, dataNascita, password, idTesseraDip,gioco));

        pulisciUsernames();
    }

    /**
     * Funzione che si occupa dell'eliminazione di un dipendente, un dipendente può venire eliminato solo se licenziato
     * da un altro dipendente di rango superiore. Quando viene cancellato un dipendente in locale bisogna occuparsi anche
     * di cancellarlo dai riferimenti che mantengono i tavoli se ce ne sono
     *
     * @param dipendente the dipendente
     * @throws SQLException the sql exception
     */
    public void licenziaDipendente(Dipendente dipendente) throws SQLException{
        String idDipendente = dipendente.getIdentificativoDipendente();
        dipendentiInLocale.remove(dipendente);

        for(Tavolo i : tavoliInLocale)
        {
            if(dipendente instanceof Supervisore) i.getSupervisori().remove(dipendente);
            else if(dipendente.equals(i.getDealer())) i.setDealer(null);
        }

        ImpDAOopd db = new ImpDAOopd();

        db.eliminaDipendente(idDipendente);
    }

    /**
     * Gets dipendenti in locale.
     *
     * @return the dipendenti in locale
     */
    public ArrayList<Dipendente> getDipendentiInLocale() {
        return dipendentiInLocale;
    }

    /**
     * Is dealer boolean.
     *
     * @param d the d
     * @return the boolean
     */
    public boolean isDealer(Object d){
        return d instanceof Dealer;
    }

    /**
     * Funzione che permette di aggiungere a un dealer nuovi giochi che conosce, questa operazione deve essere effettuata
     * da un supervisore
     *
     * @param dealerSelezionato dealer selezionato
     * @param giochi            giochi
     * @throws SQLException the sql exception
     */
    public void aggiungiGiochi(Dealer dealerSelezionato, ArrayList<Gioco> giochi) throws SQLException{

        ImpDAOopd db= new ImpDAOopd();

        dealerSelezionato.aggiungiGiochi(giochi);
        db.aggiungiGiocoDealer(dealerSelezionato.getIdentificativoDipendente(), giochi);
    }

    /**
     * Funzione che prende i dati di tutte le sessioni svolte da un cliente dal db e le ritorna in un ArrayList di sessioni
     *
     * @param idCliente idCliente
     * @return ArrayList delle sessioni
     * @throws SQLException the sql exception
     */
    public ArrayList<Sessione> visualizzaSessioniCliente(String idCliente) throws SQLException
    {
        ArrayList<Sessione> sessioni = new ArrayList<>();

        ArrayList<Integer> idSessione = new ArrayList<>();
        ArrayList<Integer> idTavolo = new ArrayList<>();
        ArrayList<Duration> durata = new ArrayList<>();
        ArrayList<Double> vincitaPercentuale = new ArrayList<>();
        ArrayList<Integer> partiteSvolte = new ArrayList<>();

        ImpDAOopd db = new ImpDAOopd();

        db.ottieniSessioniDiCliente(idSessione, idCliente, idTavolo, durata, vincitaPercentuale, partiteSvolte);

        for(int i = 0; i < idSessione.size(); i++)
        {
            sessioni.add(new Sessione(idSessione.get(i), idTavolo.get(i), durata.get(i),
                    vincitaPercentuale.get(i), partiteSvolte.get(i)));
        }

        return sessioni;
    }

    /**
     * Funzione che prende i dati di tutte le sessioni svolte a un tavolo dal db e le ritorna in un ArrayList di sessioni.
     * In più visto che di Cliente c'è bisogno solo di due dati, piuttosto che istanziare la classe cliente in questo caso
     * abbiamo optato per l'utilizzo di due HashMap una per l'username e l'altra per il valore di sospetto
     *
     * @param identificativoTavoloDaPrendere identificativo tavolo da prendere
     * @param userSuspect                    the user suspect
     * @param userSessione                   the user sessione
     * @return the array list
     * @throws SQLException the sql exception
     */
    //userSuspect -> username : sospetto
    //userSessione -> idSessione : username
    public ArrayList<Sessione> visualizzaSessioniTavolo(int[] identificativoTavoloDaPrendere, HashMap<String,Boolean> userSuspect,
                                                        HashMap<Integer,String> userSessione) throws SQLException
    {
        ArrayList<Sessione> sessioni = new ArrayList<>();

        ArrayList<Integer> idSessione = new ArrayList<>();
        ArrayList<Integer> idTavolo = new ArrayList<>();
        ArrayList<Duration> durata = new ArrayList<>();
        ArrayList<Double> vincitaPercentuale = new ArrayList<>();
        ArrayList<Integer> partiteSvolte = new ArrayList<>();

        ArrayList<String> username = new ArrayList<>();
        ArrayList<Boolean> sospetto = new ArrayList<>();

        ImpDAOopd db = new ImpDAOopd();

        db.ottieniSessioniDiTavolo(idSessione, dipendenteCorrente.getIdentificativoDipendente(), idTavolo, durata,
                vincitaPercentuale, partiteSvolte, username, sospetto);

        userSuspect.clear();
        for(int i = 0; i < idSessione.size(); i++) {
            sessioni.add(new Sessione(idSessione.get(i), idTavolo.get(i), durata.get(i),
                    vincitaPercentuale.get(i), partiteSvolte.get(i)));
            userSuspect.put(username.get(i),sospetto.get(i));
            userSessione.put(idSessione.get(i),username.get(i));
        }

        if(identificativoTavoloDaPrendere != null && !idSessione.isEmpty())
            identificativoTavoloDaPrendere[0] = sessioni.getFirst().getIdTavolo();

        return sessioni;
    }

    /**
     * Funzione che modifica in locale e nel db il gioco a cui è predisposto il tavolo
     *
     * @param idTavolo id tavolo
     * @param gioco    gioco
     * @throws SQLException the sql exception
     */
    public void modficaGiocoTavolo(int idTavolo, Gioco gioco) throws SQLException
    {
        ImpDAOopd db = new ImpDAOopd();

        db.cambiaGiocoTavolo(idTavolo, gioco.name());

        for(Tavolo i : tavoliInLocale)
        {
            if(i.getIdTavolo() == idTavolo)
            {
                i.setGioco(gioco);
            }
        }
    }

    /**
     * Funzione per assegnare in locale dealer al tavolo
     *
     * @param tavolo tavolo
     */
    private void assegnaDealerDelTavolo(Tavolo tavolo)
    {
        String idDealer = tavolo.getIdDealer();

        for(Dipendente i : dipendentiInLocale)
        {
            if(i instanceof Dealer j)
            {
                if((j.getIdentificativoDipendente()).equals(idDealer)) {
                    tavolo.setDealer(j);
                    break;
                }
            }
        }
    }

    /**
     * Funzione che prende da db le relazioni tra supervisore e tavolo e le riproduce in locale
     *
     * @param tavolo
     */
    private void fetchDadbAssegnaSupervisoriDelTavolo(Tavolo tavolo)
    {
        ImpDAOopd db = new ImpDAOopd();
        ArrayList<Supervisore> temp = new ArrayList<>();

        try {
            for(String i : db.tavoliSupervisori(tavolo.getIdTavolo()))
            {
                for(Dipendente j : dipendentiInLocale)
                {
                    if(j instanceof Supervisore k)
                    {
                        if(j.getIdentificativoDipendente().equals(i)) {
                            temp.add(k);
                            break;
                        }
                    }
                }
            }

            tavolo.setSupervisori(temp);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Funzione per cambiare username per i dipendenti, si occupa anche di assegnare il nuovo codice al dipendente generato a
     * partire dall'username
     *
     * @param newUser nuovo username
     * @param pass1   password
     * @param pass2   conferma password
     * @return the boolean
     * @throws RuntimeException the runtime exception
     * @throws SQLException     errore lanciato se non sono stati compilati tutti i campi, se le 2 password non coincidono
     * se le password coincidono ma sono errate, se l'username è già stato preso
     */
    public boolean changeUsername(String newUser, String pass1, String pass2) throws RuntimeException, SQLException{
        if(newUser.isBlank() || pass1.isBlank() || pass2.isBlank()) throw new RuntimeException("Compila tutti i campi!");

        ImpDAOop db_fetch_user= new ImpDAOop();

        if(!pass1.equals(pass2)) throw new RuntimeException("Le 2 password non coincidono");
        if(db_fetch_user.trovaTabella(dipendenteCorrente.getUsername(), pass1) == null) throw new RuntimeException("password errata");

        ImpDAOopd db= new ImpDAOopd();

        db_fetch_user.usernameUtenti(usernames);

        for(String i : usernames){
            if(i.equals(newUser)) throw new RuntimeException("username già preso");
        }

        String newCodiceTessera= generaCodiceTessera(newUser);
        db.cambioUsername(dipendenteCorrente.getIdentificativoDipendente(), newUser, newCodiceTessera);

        //dovuto al fatto che abbiamo dei duplicati dello stesso supervisore che è correntemente loggato
        for(Dipendente d : dipendentiInLocale){
            if(d.getIdentificativoDipendente().equals(dipendenteCorrente.getIdentificativoDipendente())){
                d.setUsername(newUser);
                d.setIdentificativoDipendente(newCodiceTessera);
                break;
            }
        }

        dipendenteCorrente.setIdentificativoDipendente(newCodiceTessera);
        dipendenteCorrente.setUsername(newUser);

        return true;
    }

    /**
     * Funzione che assegna dipendente a tavolo nel db
     *
     * @param idDipendente id dipendente
     * @param ruolo        ruolo
     * @param idTavolo     id tavolo
     * @throws SQLException the sql exception
     */
    public void aggiornaInfoTavolodb(String idDipendente, String ruolo, int idTavolo) throws SQLException
    {
        ImpDAOopd db = new ImpDAOopd();

        db.assegnaDipendenteATavolo(idDipendente, ruolo, idTavolo);
    }

    /**
     * Funzione che aggiunge in locale dealer a tavolo
     *
     * @param dealer dealer
     * @param index  indice
     */
    public void aggiungiDealerAtIndex(Dealer dealer, int index)
    {
        tavoliInLocale.get(index).setDealer(dealer);
    }

    /**
     * Funzione che aggiunge in locale supervisore a tavolo
     *
     * @param dealer dealer
     * @param index  indice
     */
    public void aggiungiSupervisoreAtIndex(Supervisore dealer, int index)
    {
        tavoliInLocale.get(index).getSupervisori().add(dealer);
    }

    /**
     * Funzione che divide la lista di dipendenti in una lista di dealer e una di supervisori
     *
     * @param listaDealer      lista dealer
     * @param listaSupervisore lista supervisori
     */
    public void dividiDealerSupervisore(ArrayList<Dealer> listaDealer, ArrayList<Supervisore> listaSupervisore)
    {
        for(Dipendente i : dipendentiInLocale)
        {
            if(i instanceof Dealer j) listaDealer.add(j);
            if(i instanceof Supervisore j) listaSupervisore.add(j);
        }
    }

    /**
     * Funzione che passa per riferimento il dealer e i supervisori assegnati al tavolo di indice indiceTavolo
     *
     * @param listaDealer      lista dealer
     * @param listaSupervisore lista supervisore
     * @param indiceTavolo     indice tavolo
     */
    public void dividiDealerSupervisoreTavoloAtIndex(ArrayList<Dealer> listaDealer, ArrayList<Supervisore> listaSupervisore,
                                                     int indiceTavolo)
    {
        Tavolo temp = tavoliInLocale.get(indiceTavolo);

        listaDealer.add(temp.getDealer());
        listaSupervisore.addAll(temp.getSupervisori());
    }

    /**
     * Gets index of tavolo.
     *
     * @param tavolo the tavolo
     * @return the index of tavolo
     */
    public int getIndexOfTavolo(Tavolo tavolo)
    {
        return tavoliInLocale.indexOf(tavolo);
    }

    /**
     * Funzione che ritorna info di tavolo formattate per la schermata AssegnaDipendentiTavolo
     *
     * @param index indice
     * @return info tavolo
     */
    public String infoTavoloAtIndex(int index)
    {
        Tavolo temp = tavoliInLocale.get(index);

        return "tavolo " + temp.getIdTavolo() + " gioco: " + temp.getGioco();
    }

    /**
     * Id tavolo at index int.
     *
     * @param index the index
     * @return the int
     */
    public int idTavoloAtIndex(int index)
    {
        Tavolo temp = tavoliInLocale.get(index);

        return temp.getIdTavolo();
    }

    private void rimuoviSupervisoreAtIndex(Supervisore supervisore, int index)
    {
        tavoliInLocale.get(index).getSupervisori().remove(supervisore);
    }

    /**
     * Gets gioco at index.
     *
     * @param index the index
     * @return the gioco at index
     */
    public Gioco getGiocoAtIndex(int index)
    {
        return tavoliInLocale.get(index).getGioco();
    }

    /**
     * Funzione che toglie il supervisore da un tavolo in db e in locale
     *
     * @param indiceTavolo  indice tavolo
     * @param supervisore supervisore
     * @throws SQLException the sql exception
     */
    public void eliminaSupervisore(int indiceTavolo, Supervisore supervisore) throws SQLException
    {
        ImpDAOopd db = new ImpDAOopd();

        db.eliminaSupervisoreTavolo(idTavoloAtIndex(indiceTavolo), supervisore.getIdentificativoDipendente());

        rimuoviSupervisoreAtIndex(supervisore, indiceTavolo);
    }

    /**
     * Controlla se l'id dato in input per la creazione del tavolo è già stato preso
     *
     * @param id id
     * @return true: id preso; id disponibile
     * @throws RuntimeException lancia errore se input < 0
     */
    public boolean idGiaPreso(int id) throws RuntimeException
    {
        if(id < 0) throw new RuntimeException("l'id del tavolo non può essere minore di 0");

        for(Tavolo i : tavoliInLocale)
        {
            if(id == i.getIdTavolo()) return true;
        }

        return false;
    }

    /**
     * Funzione che aggiunge un nuovo tavolo in db e in locale
     *
     * @param numero numero del tavolo (id)
     * @param gioco  gioco
     * @param numeroPosti     numero di posti
     * @throws SQLException the sql exception
     */
    public void aggiungiTavolo(int numero, Gioco gioco, int numeroPosti) throws SQLException
    {
        new ImpDAOopd().aggiungiTavolo(numero, gioco.toString(), numeroPosti);

        tavoliInLocale.add(new Tavolo(numero, gioco, numeroPosti));
    }

    /**
     * Funzione che rimuove tavolo da db ed in locale
     *
     * @param tavolo tavolo
     * @throws SQLException the sql exception
     */
    public void cancellaTavolo(Tavolo tavolo) throws SQLException
    {
        new ImpDAOopd().eliminaTavolo(tavolo.getIdTavolo());

        tavoliInLocale.remove(tavolo);
    }

    /**
     * Funzione che ritorna una nuova lista che non è altro che il risultato dell'applicazione di tutti i filtri passati
     * come parametro su tavoliInLocale
     *
     * @param usernameRicerca      the username ricerca
     * @param controllaUsername    the controlla username
     * @param percMin              the perc min
     * @param percMax              the perc max
     * @param controllaPercentuale the controlla percentuale
     * @param durMin               the dur min
     * @param durMax               the dur max
     * @param controllaDurata      the controlla durata
     * @param partMin              the part min
     * @param partMax              the part max
     * @param controllaPartite     the controlla partite
     * @return the array list
     */
    public ArrayList<Sessione>ricercaSessioni(String usernameRicerca, boolean controllaUsername, int percMin, int percMax,
                                              boolean controllaPercentuale, int durMin, int durMax, boolean controllaDurata,
                                              int partMin,int partMax,boolean controllaPartite){

        ArrayList<Sessione> sessioniLocali = new ArrayList<>();
        ArrayList<Sessione> sessioniRicercate = new ArrayList<>();
        HashMap<String,Boolean> userSuspect = new HashMap();
        HashMap<Integer,String> userSessioni = new HashMap<>();

        try {
            sessioniLocali.addAll(visualizzaSessioniTavolo(null,userSuspect,userSessioni));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "errore",
                    JOptionPane.ERROR_MESSAGE);
        }

        for(Sessione temp : sessioniLocali) {

            if (controllaUsername && !usernameRicerca.isBlank()) {
                if (!usernameRicerca.equals(userSessioni.get(temp.getIdSessione()))) continue;
            }

            if(controllaPercentuale){
                if(!(temp.getVincitaPercentuale() >= percMin && temp.getVincitaPercentuale() <= percMax)) continue;
            }

            if(controllaDurata){
                if(!(temp.getDurataSessione().getSeconds() >= durMin && temp.getDurataSessione().getSeconds() <= durMax)) continue;
            }

            if(controllaPartite){
                if(!(temp.getPartiteSvolte() >= partMin &&temp.getPartiteSvolte() <= partMax)) continue;
            }


            sessioniRicercate.add(temp);
            }


        return sessioniRicercate;
    }

    /**
     * Update sospetto.
     *
     * @param username the username
     * @throws SQLException the sql exception
     */
    public void updateSospetto(String username)throws SQLException{
        ImpDAOopd db = new ImpDAOopd();
        db.updateSospetto(username);
    }

    /**
     * Funzione che crea e assegna un ban al cliente
     *
     * @param cliente cliente
     * @param motivo motivo del ban
     * @throws SQLException the sql exception
     */
    public void creazioneBan(Cliente cliente, String motivo) throws RuntimeException, SQLException
    {
        cliente.creaBan(motivo);

        new ImpDAOopd().salvataggioBan(cliente.getCodiceTesseraGiocatore(), cliente.getDataBan(), cliente.getMotivoBan());
    }
}
