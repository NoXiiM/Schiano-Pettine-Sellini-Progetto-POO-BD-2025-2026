package database.DAO;

import model.gestionale.Gioco;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public interface DAOopd {
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

    void recuperaDatiDipendenti(ArrayList<String> idDipendenti, ArrayList<String> nome, ArrayList<String> cognome, ArrayList<LocalDate> dataDiNascita, ArrayList<String> codiceFiscale, ArrayList<String> username, ArrayList<String> password, ArrayList<String> ruolo, ArrayList<String> gioco)throws SQLException;

    void caricaTavoli(Gioco gioco, ArrayList<Integer> idTavolo, ArrayList<Integer> numeroPosti,
                      ArrayList<String> idDealer) throws SQLException;

    void salvataggioBan(String idCliente,LocalDate dataBan, String motivoBan) throws SQLException;

    void eliminaDipendente(String idDipendente) throws SQLException;

    void aggiungiTavolo(int idTavolo, String gioco, int numeroPosti, String idDealer) throws SQLException;

    void assegnaDipendenteATavolo(String idDipendente, String ruolo, int idTavolo) throws SQLException;

    void aggiungiGiocoDealer(String idDealer, ArrayList<Gioco> giochi) throws  SQLException;
}
