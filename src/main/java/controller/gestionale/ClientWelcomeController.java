package controller.gestionale;

import model.gestionale.Giocatore;
import model.gestionale.Sessione;
import model.gestionale.Tavolo;
import model.gestionale.utenteEFigli.Cliente;
import model.gestionale.utenteEFigli.Utente;
import database.implementazioneDAO.impDAOop;

import javax.swing.*;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Random;

public class ClientWelcomeController extends WelcomeController {

    private ArrayList<String> usernames;
    private Cliente cliente;
    private Sessione sessione;

    public ClientWelcomeController(WelcomeController controller) {
        super(controller.getLista_utenti(), controller.getCurrentUser());
        cliente = (Cliente) getCurrentUser();
        usernames = new ArrayList<>();
    }

    //client
    public void registrati(String username, String nome, String cognome, String codiceFiscale,
                           LocalDate dataNascita, String password, int importo) throws RuntimeException {

        if (username.isBlank() || nome.isBlank() || cognome.isBlank() || codiceFiscale.isBlank() || password.isBlank())
            throw new RuntimeException("Compila tutti i campi!");

        if (!isEta18(dataNascita)) throw new RuntimeException("Devi avere almeno 18 anni per registrarti.");
        if (importo < 50) throw new RuntimeException("Deposito minimo obbligatorio di 50 euro");

//        for (Utente i :getLista_utenti()) {
//            if (i.getUsername().equals(username)) throw new RuntimeException("Username non disponibile");
//        }

        //check locale
        for (String user : usernames) {
            if (username.equals(user)){
                throw new RuntimeException("Username non disponibile");
            }
        }

        String codiceTessera = generaCodiceTessera();

        try {
            new impDAOop().registrazione(codiceTessera, username, nome, cognome, codiceFiscale,
                    dataNascita, password, importo);
        } catch (SQLException e) {
            aggiornaUsernamesTessere();
            throw new RuntimeException(e);
        }

//       getLista_utenti().add(new Cliente(username, nome, cognome, codiceFiscale, dataNascita, password, codiceTessera));
    }

    //client
    public void depositaSaldoCliente(int deposito) {
        cliente.deposita(deposito);
    }

    //client
    public boolean prelevaSaldoCliente(int prelievo) {
        if (!cliente.preleva(prelievo)) {
            return false;
        }
        return true;
    }

    //client
    private String generaCodiceTessera()
    {
        String user = cliente.getUsername();
        Random random = new Random();
        String numero = String.format("%03d", random.nextInt(0, 1000));
        int taglio = random.nextInt(0, user.length());

        return user.substring(0, taglio) + numero + user.substring(taglio);
    }

    //client
    private boolean isEta18(LocalDate dataNascita) {
        return Period.between(dataNascita, LocalDate.now()).getYears() >= 18;
    }

    //client
    public int getSaldoCliente() {
        return cliente.getSaldo();
    }

    //solo client, un admin non puo cancellare il profilo, un superadmin puo cancellare altri profili
    public boolean deleteUser(String username, String pass, String conferma) throws RuntimeException {
        if (username.isBlank() || pass.isBlank() || conferma.isBlank())
            throw new RuntimeException("Compila tutti i campi!");

        if (getCurrentUser().getUsername().equals(username) && getCurrentUser().getPassword().equals(pass) && conferma.equals("CONFERMA")) {
            getLista_utenti().remove(getCurrentUser());
            setCurrentUserNull();
            return true;
        }
        return false;
    }

    //client
    public void resetPass(String nome, String cognome, String username) throws RuntimeException {

        if (username.isBlank() || nome.isBlank() || cognome.isBlank())
            throw new RuntimeException("Compila tutti i campi!");


        for (Utente i : getLista_utenti()) {
            if (i.getUsername().equals(username) && i.getNome().equals(nome) && i.getCognome().equals(cognome)) {
                i.setPassword("P@ssw0rd!");
                JOptionPane.showMessageDialog(null, "Password resettata ! Al prossimo accesso usa: \"P@ssword!\", non dimenticare di modificarla !");
                return;
            }
        }
        throw new RuntimeException("Utente non trovato!");
    }

    public void pulisciUsernamesTessere() {
        usernames.clear();
    }

    public boolean isBanned() {
        return cliente.getBan() != null;
    }

    public void aggiornaUsernamesTessere() {

        impDAOop db = new impDAOop();

        try {
            db.usernameUtenti(usernames);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void creaNuovaSessioneDiGioco(Tavolo tavoloSelezionato)
    {
        Giocatore giocatoreCorrente = new Giocatore(cliente, cliente.getSaldo());
        sessione = new Sessione(giocatoreCorrente, tavoloSelezionato);
        sessione.startTimer();
    }

    public int getSaldoGiocatore(){return sessione.getSaldoGiocatore();}
    public void decrementaSaldoGiocatore(int creditoInserito) throws RuntimeException{sessione.decrementaSaldoGiocatore(creditoInserito);}
    public void incrementaSaldoGiocatore(int creditoInserito){sessione.incrementaSaldoGiocatore(creditoInserito);}
    public void terminaSessione(){sessione.terminaSessione();}
    public void aggiornaVincitaPercentuale(boolean v){ sessione.aggiornaVincitaPercentuale(v);}
    public int getPostiTavolo(){return sessione.getPostiTavolo();}
    public int getTimeSecondi(){return sessione.getTimeSecondi();}
    public Time getTime(){return sessione.getTime();}
    public String stringaPercentuale(){return sessione.stringaPercentuale();}
    public Cliente getClienteCorrente(){return cliente;}
}

