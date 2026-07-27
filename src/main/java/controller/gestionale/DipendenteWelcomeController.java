package controller.gestionale;

import database.implementazioneDAO.ImpDAOop;
import database.implementazioneDAO.ImpDAOopd;
import model.gestionale.Gioco;
import model.gestionale.utenteEFigli.Cliente;
import model.gestionale.utenteEFigli.Dealer;
import model.gestionale.utenteEFigli.Dipendente;
import model.gestionale.utenteEFigli.Supervisore;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class DipendenteWelcomeController extends WelcomeController {

    Dipendente dipendenteCorrente;
    ArrayList<Cliente> clientiInLocale;
    ArrayList<Dipendente> dipendentiInLocale;
    ArrayList<String> usernames;

    public DipendenteWelcomeController(WelcomeController controller){
        super(controller.getCurrentUser(), controller.getUsernamesList());
        this.usernames= controller.getUsernamesList();

        dipendenteCorrente= (Dipendente) getCurrentUser();

        clientiInLocale = new ArrayList<>();
        dipendentiInLocale = new ArrayList<>();
    }

    //admin
    public ArrayList<Cliente> getListaClientiDB() {

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
        ArrayList<LocalDate> dataBan = new ArrayList<>();
        ArrayList<String> motiviBan = new ArrayList<>();

        ImpDAOopd db = new ImpDAOopd();
        try {
            db.recuperaDatiClienti(username, nome, cognome, codiceFiscale, dataDiNascita, password,
                    codiceTesseraGiocatore,premium,sconto_premium,sospetto,tempoDiGiocoInSec,fichesGiocate,saldo,partiteGiocate,dataBan,motiviBan);
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
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
                    dataBan.get(i),
                    motiviBan.get(i)
            );

            clientiInLocale.add(c);
        }
        return clientiInLocale;
    }

    public ArrayList<Dipendente> getDipendentiDB(){
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
        try {
            db.recuperaDatiDipendenti(idDipendenti, nome, cognome, dataDiNascita, codiceFiscale,
                    username, password, ruolo, gioco);
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }

        Dealer d;
        Supervisore s;

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

    public ArrayList<Cliente> getClientiInLocale() {
        return clientiInLocale;
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

    public void registraDipendente(String username, String nome, String cognome, String codiceFiscale, LocalDate dataNascita, String password, String ruolo){

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
            throw new RuntimeException(e);
        }

        if(ruolo.equals("Supervisore")) dipendentiInLocale.add(new Supervisore(username, nome, cognome, codiceFiscale, dataNascita, password, idTesseraDip));
        if(ruolo.equals("Dealer")) dipendentiInLocale.add(new Dealer(username, nome, cognome, codiceFiscale, dataNascita, password, idTesseraDip));

        pulisciUsernames();
    }
}
