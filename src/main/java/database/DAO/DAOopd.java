package database.DAO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public interface DAOopd {
    //OPD1 Recupero clienti dal DB
    void recuperaDatiClienti(ArrayList<String> username, ArrayList<String> nome, ArrayList<String> cognome,
                             ArrayList<String> codiceFiscale, ArrayList<LocalDate> dataDiNascita, ArrayList<String> password,
                             ArrayList<String> codiceTesseraGiocatore,ArrayList<Boolean> premium,
                             ArrayList<Double> sconto_premium,
                             ArrayList<Boolean> sospetto,
                             ArrayList<Long> tempoDiGiocoInSec,
                             ArrayList<Integer> fichesGiocate,
                             ArrayList<Integer> saldo,
                             ArrayList<Integer> partiteGiocate,
                             ArrayList<LocalDate> dataBan,
                             ArrayList<String> motiviBan)throws SQLException;

    //OPD2 registrazione del ban
    void salvataggioBan(String idCliente,LocalDate dataBan, String motivoBan) throws SQLException;

    //OPD3
    void aggiungiDipendente(String idDipendente, String nome, String cognome, LocalDate dataDiNascita,
                            String codiceFiscale, String username, String password, String ruolo) throws SQLException;

    //OPD4
    void eliminaDipendente(String idDipendente) throws SQLException;

    //OPD5
    void aggiungiTavolo(int idTavolo, String gioco, int numeroPosti, String idDealer) throws SQLException;
}
