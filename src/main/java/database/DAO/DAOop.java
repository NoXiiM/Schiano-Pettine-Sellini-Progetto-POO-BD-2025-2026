package database.DAO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public interface DAOop
{
    //OP1
    //se i dati sono corretti ritorna true
    boolean passwordDimenticata(String nome, String cognome, String username) throws SQLException;

    //OP2
    //se username e codicefiscale sono nuovi ritorna true sennò false
    void registrazione(String id, String username, String nome, String cognome, String codiceFiscale, LocalDate data,
                       String password, int saldo) throws SQLException;

    //OP3
    //restituisce Cliente o Dipendente o null,
    String trovaTabella(String username, String password) throws SQLException;

    //carica tutti i dati del cliente che effettivamente servono, gli array come parametri servono per effettuare
    //passaggio per riferimento, in questo caso di un singolo valore (nota: per valori multipli userò arraylist)
    void loginCliente(int[] saldo, String[] tipo, double[] scontoPokerPercentuale, LocalDate[] dataDiBan,
                      String[] nome, String[] cognome, String[] codiceFiscale, LocalDate[] dataDiNascita, String username,
                      String password, String[] codiceTessera) throws SQLException;

    void loginDipendente(String[] identificativo, String[] nome, String[] cognome, String[] codiceFiscale, LocalDate[] dataDiNascita,
                                String[] ruolo, String username, String password) throws SQLException;

    //operazione batch su username
    void usernameUtenti(ArrayList<String> usernames) throws SQLException;

}
