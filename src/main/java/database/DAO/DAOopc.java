package database.DAO;

import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;

public interface DAOopc
{
    //OPC1
    //svolge update nella tabella cliente nell'id corretto per tutti questi parametri
    void salvataggioCliente(String codiceTessera, int saldo, Duration tempoDiGioco, int fichesGiocate,
                            double vincitaPercentualeTot, int partiteGiocate, String tipo, double scontoPokerPercentuale,
                            boolean sospetto, LocalDate dataDiBan, String motiviBan, String password) throws SQLException;
    //campi che mancano perchè semplicemente non possono cambiare: nome, cognome, codiceFiscale, dataDiNascita

    //OPC2
    //salva sessione, non c'è bisogno di creare l'id di sessione in java, è serial in db
    void salvaSessione(String idCliente, String idTavolo, Duration durata, double vincitaPercentuale, int partiteSvolte)
            throws SQLException;

    //OPC3
    //il cambio username in operazione separata rispetto a salvataggio cliente perché username deve essere unico
    //se uno cambia l'username cambia anche il codice della tessera
    void cambioUsername(String vecchioCodiceTessera, String username, String nuovoCodiceTessera) throws SQLException;
}
