package controller.gestionale;

import database.implementazioneDAO.ImpDAOop;
import database.implementazioneDAO.ImpDAOopc;
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

public class DipendenteWelcomeController extends WelcomeController {

    Dipendente dipendenteCorrente;
    ArrayList<Cliente> clientiInLocale;
    ArrayList<Dipendente> dipendentiInLocale;
    ArrayList<Tavolo> tavoliInLocale;
    ArrayList<String> usernames;

    public DipendenteWelcomeController(WelcomeController controller){
        super(controller.getCurrentUser(), controller.getUsernamesList());
        this.usernames= controller.getUsernamesList();

        dipendenteCorrente= (Dipendente) getCurrentUser();

        clientiInLocale = new ArrayList<>();
        dipendentiInLocale = new ArrayList<>();
        tavoliInLocale = new ArrayList<>();
    }

    //admin
    public ArrayList<Cliente> getListaClientiDB() throws SQLException{

        if(clientiInLocale!=null) clientiInLocale.clear();
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

    public ArrayList<Dipendente> getDipendentiDB() throws SQLException{
        if(dipendentiInLocale!=null) dipendentiInLocale.clear();
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

        Dealer d;
        Supervisore s;

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
                s = new Supervisore(username.get(i), nome.get(i), cognome.get(i), codiceFiscale.get(i),
                        dataDiNascita.get(i), password.get(i), idDipendenti.get(i));
                dipendentiInLocale.add(s);
            }

        }
        return dipendentiInLocale;
    }

    public ArrayList<Tavolo> getTavoliDB() throws SQLException
    {
        if(tavoliInLocale!=null) tavoliInLocale.clear();

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

    public ArrayList<Cliente> getClientiInLocale() {
        return clientiInLocale;
    }

    public ArrayList<Tavolo> getTavoliInLocale() {
        return tavoliInLocale;
    }

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

    public void registraDipendente(String username, String nome, String cognome, String codiceFiscale,
                                   LocalDate dataNascita, String password, String ruolo, ArrayList<Gioco> gioco) throws SQLException{

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

    public Dipendente getDipendenteCorrente(){
        return dipendenteCorrente;
    }

    public ArrayList<Dipendente> getDipendentiInLocale() {
        return dipendentiInLocale;
    }

    public boolean isDealer(Object d){
        return d instanceof Dealer;
    }

    public void aggiungiGiochi(Dealer dealerSelezionato, ArrayList<Gioco> giochi) throws SQLException{

        ImpDAOopd db= new ImpDAOopd();

        dealerSelezionato.aggiungiGiochi(giochi);
        db.aggiungiGiocoDealer(dealerSelezionato.getIdentificativoDipendente(), giochi);
    }

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

    public void modficaGiocoTavolo(int idTavolo, Gioco gioco) throws SQLException
    {
        ImpDAOopd db = new ImpDAOopd();

        db.cambiaGiocoTavolo(idTavolo, gioco.name());
    }

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

    public void aggiornaInfoTavolodb(String idDipendente, String ruolo, int idTavolo) throws SQLException
    {
        ImpDAOopd db = new ImpDAOopd();

        db.assegnaDipendenteATavolo(idDipendente, ruolo, idTavolo);
    }

    public void dividiDealerSupervisore(ArrayList<Dealer> listaDealer, ArrayList<Supervisore> listaSupervisore)
    {
        for(Dipendente i : dipendentiInLocale)
        {
            if(i instanceof Dealer j) listaDealer.add(j);
            if(i instanceof Supervisore j) listaSupervisore.add(j);
        }
    }

    public void dividiDealerSupervisoreTavoloAtIndex(ArrayList<Dealer> listaDealer, ArrayList<Supervisore> listaSupervisore,
                                                     int indiceTavolo)
    {
        Tavolo temp = tavoliInLocale.get(indiceTavolo);

        listaDealer.add(temp.getDealer());
        listaSupervisore.addAll(temp.getSupervisori());
    }

    public int getIndexOfTavolo(Tavolo tavolo)
    {
        return tavoliInLocale.indexOf(tavolo);
    }

    public String infoTavoloAtIndex(int index)
    {
        Tavolo temp = tavoliInLocale.get(index);

        return "tavolo " + temp.getIdTavolo() + " gioco: " + temp.getGioco();
    }

    public int idTavoloAtIndex(int index)
    {
        Tavolo temp = tavoliInLocale.get(index);

        return temp.getIdTavolo();
    }

    public void aggiungiDealerAtIndex(Dealer dealer, int index)
    {
        tavoliInLocale.get(index).setDealer(dealer);
    }

    public void aggiungiSupervisoreAtIndex(Supervisore dealer, int index)
    {
        tavoliInLocale.get(index).getSupervisori().add(dealer);
    }

    public void rimuoviSupervisoreAtIndex(Supervisore dealer, int index)
    {
        tavoliInLocale.get(index).getSupervisori().remove(dealer);
    }

    public Gioco getGiocoAtIndex(int index)
    {
        return tavoliInLocale.get(index).getGioco();
    }

    public void eliminaSupervisore(int indiceTavolo, String idSupervisore) throws SQLException
    {
        ImpDAOopd db = new ImpDAOopd();

        db.eliminaSupervisoreTavolo(idTavoloAtIndex(indiceTavolo), idSupervisore);
    }

    public boolean idGiaPreso(int id) throws RuntimeException
    {
        if(id < 0) throw new RuntimeException("l'id del tavolo non può essere minore di 0");

        for(Tavolo i : tavoliInLocale)
        {
            if(id == i.getIdTavolo()) return true;
        }

        return false;
    }

    public void aggiungiTavolo(int numero, Gioco gioco, int id) throws SQLException
    {
        new ImpDAOopd().aggiungiTavolo(numero, gioco.toString(), id);

        tavoliInLocale.add(new Tavolo(id, gioco, numero));
    }

    public void cancellaTavolo(Tavolo tavolo) throws SQLException
    {
        new ImpDAOopd().eliminaTavolo(tavolo.getIdTavolo());

        tavoliInLocale.remove(tavolo);
    }
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
    public void updateSospetto(String username)throws SQLException{
        ImpDAOopd db = new ImpDAOopd();
        db.updateSospetto(username);
    }
}
