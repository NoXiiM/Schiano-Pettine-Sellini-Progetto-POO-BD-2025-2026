package controller.gestionale;

import database.implementazioneDAO.impDAOopc;
import model.gestionale.Giocatore;
import model.gestionale.Sessione;
import model.gestionale.Tavolo;
import model.gestionale.utenteEFigli.Cliente;
import database.implementazioneDAO.impDAOop;

import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Random;

public class ClientWelcomeController extends WelcomeController {

    private Cliente cliente;
    private Sessione sessione;
    private ArrayList<String> usernames;

    public ClientWelcomeController(WelcomeController controller) {
        super(controller.getCurrentUser(), controller.getUsernamesList());
        this.usernames = controller.getUsernamesList();
        cliente = (Cliente) getCurrentUser();
    }

    //client
    public void registrati(String username, String nome, String cognome, String codiceFiscale,
                           LocalDate dataNascita, String password, int importo) throws RuntimeException {

        if (username.isBlank() || nome.isBlank() || cognome.isBlank() || codiceFiscale.isBlank() || password.isBlank())
            throw new RuntimeException("Compila tutti i campi!");

        if (!isEta18(dataNascita)) throw new RuntimeException("Devi avere almeno 18 anni per registrarti.");
        if (importo < 50) throw new RuntimeException("Deposito minimo obbligatorio di 50 euro");

        //check locale
        for (String user : usernames) {
            if (username.equals(user)){
                throw new RuntimeException("Username non disponibile");
            }
        }

        String codiceTessera = generaCodiceTessera(username);

        try {
            new impDAOop().registrazione(codiceTessera, username, nome, cognome, codiceFiscale,
                    dataNascita, password, importo);
        } catch (SQLException e) {
            aggiornaUsernamesTessere();
            throw new RuntimeException(e);
        }
        pulisciUsernames();
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
    private String generaCodiceTessera(String username)
    {
        Random random = new Random();
        String numero = String.format("%03d", random.nextInt(0, 1000));
        int taglio = random.nextInt(0, username.length());

        return username.substring(0, taglio) + numero + username.substring(taglio);
    }

    //client
    private boolean isEta18(LocalDate dataNascita) {
        return Period.between(dataNascita, LocalDate.now()).getYears() >= 18;
    }

    public boolean changeUsername(String newUser, String pass1, String pass2) throws RuntimeException, SQLException{
        if(newUser.isBlank() || pass1.isBlank() || pass2.isBlank()) throw new RuntimeException("Compila tutti i campi!");

        impDAOop db_fetch_user= new impDAOop();

        if(!pass1.equals(pass2)) throw new RuntimeException("Le 2 password non coincidono");
        if(db_fetch_user.trovaTabella(cliente.getUsername(), pass1) == null) throw new RuntimeException("password errata");

        impDAOopc db= new impDAOopc();

        db_fetch_user.usernameUtenti(usernames);

        for(String i : usernames){
            if(i.equals(newUser)) throw new RuntimeException("username già preso");
        }

        String newCodiceTessera= generaCodiceTessera(newUser);

        db.cambioUsername(cliente.getCodiceTesseraGiocatore(), newUser, newCodiceTessera);

        return true;
    }

    //solo client, un admin non puo cancellare il profilo, un superadmin puo cancellare altri profili
    public boolean deleteUser(String username, String pass, String conferma) throws RuntimeException {
        if (username.isBlank() || pass.isBlank() || conferma.isBlank())
            throw new RuntimeException("Compila tutti i campi!");

        return false;
    }

    //client
    public void resetPass(String nome, String cognome, String username) throws RuntimeException {

        if (username.isBlank() || nome.isBlank() || cognome.isBlank())
            throw new RuntimeException("Compila tutti i campi!");


    }

    public void pulisciUsernames() {
        usernames.clear();
    }

    public boolean isBanned() {

        //TODO fetch status isbanned pre partita da parte di client ( non urgente )
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

    public int getSaldoGiocatore(){
        return sessione.getSaldoGiocatore();
    }

    public void decrementaSaldoGiocatore(int creditoInserito) throws RuntimeException{
        sessione.decrementaSaldoGiocatore(creditoInserito);
    }

    public void incrementaSaldoGiocatore(int creditoInserito){
        sessione.incrementaSaldoGiocatore(creditoInserito);
    }

    public void terminaSessione() throws SQLException{
        sessione.terminaSessione();
        salvaDatiClienteUscitaDaGIoco();
    }

    //salvataggio dati sia al logout che a fine sessione
    public void salvaDatiClienteUscitaDaGIoco() throws SQLException{
        impDAOopc db= new impDAOopc();


        db.salvaSessione(cliente.getCodiceTesseraGiocatore(), sessione.getTavolo().getIdTavolo(),
                sessione.getDurataSessione(), sessione.getVincitaPercentuale(), sessione.getPartiteSvolte());

        String tipologiaCliente= (cliente.isPremium()) ? "Premium" : "Base";

        db.salvataggioCliente(cliente.getCodiceTesseraGiocatore(), cliente.getSaldo(), cliente.getTempoDiGioco(),
                cliente.getFichesGiocate(), cliente.getVincitaPercentualeTot(), cliente.getPartiteGiocate(), tipologiaCliente,
                cliente.getSconto_premium(), cliente.isSospetto());
    }

    public void salvaDatiClienteUscitaDaGestione() throws SQLException{
        impDAOopc db= new impDAOopc();

        String tipologiaCliente= (cliente.isPremium()) ? "Premium" : "Base";

        db.salvataggioCliente(cliente.getCodiceTesseraGiocatore(), cliente.getSaldo(), cliente.getTempoDiGioco(),
                cliente.getFichesGiocate(), cliente.getVincitaPercentualeTot(), cliente.getPartiteGiocate(), tipologiaCliente,
                cliente.getSconto_premium(), cliente.isSospetto());
    }

    public int getSaldoCliente(){
        return cliente.getSaldo();
    }

    public void aggiornaVincitaPercentuale(boolean v){
        sessione.aggiornaVincitaPercentuale(v);
    }

    public int getPostiTavolo(){
        return sessione.getPostiTavolo();
    }

    public Duration getTime(){
        return sessione.getDurataSessione();
    }

    public String stringaPercentuale(){
        return sessione.stringaPercentuale();
    }

    public Cliente getClienteCorrente(){
        return cliente;
    }
}

