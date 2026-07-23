package database.DAO;

import java.time.LocalDate;

public interface DAOopc
{
    //OPC1
    //svolge update nella tabella cliente nell'id corretto per tutti questi parametri
    void salvataggioCliente(String id, int saldo, String tipo, double scontoPercentuale, LocalDate dataDiBan,
                            String nome, String cognome, String codiceFiscale, LocalDate dataDiNascita,
                            String username, String password);

    //OPC2
}
