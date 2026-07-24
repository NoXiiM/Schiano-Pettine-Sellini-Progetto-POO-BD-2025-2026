package database.implementazioneDAO;

import database.ConnessioneDatabase;
import database.DAO.DAOopc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.time.Duration;
import java.time.LocalDate;

public class impDAOopc implements DAOopc
{
    @Override
    public void salvaSessione(String idCliente, int idTavolo, Duration durata, double vincitaPercentuale,
                              int partiteSvolte) throws SQLException {
        Connection connection = ConnessioneDatabase.getInstance().connection;

        try(PreparedStatement inserimento = connection.prepareStatement("insert into sessione(idCliente, idTavolo, " +
                "durata, vincitaPercentuale, partiteSvolte) " +
                "values(?,?,?,?,?)"))
        {
            inserimento.setString(1, idCliente);
            inserimento.setInt(2, idTavolo);
            inserimento.setLong(3, durata.getSeconds());
            inserimento.setDouble(4, vincitaPercentuale);
            inserimento.setInt(5, partiteSvolte);

            inserimento.executeUpdate();
        }
    }

    @Override
    public void salvataggioCliente(String codiceTessera, int saldo, Duration tempoDiGioco, int fichesGiocate,
                                   double vincitaPercentualeTot, int partiteGiocate, String tipo,
                                   double scontoPokerPercentuale, boolean sospetto) throws SQLException {
        Connection connection = ConnessioneDatabase.getInstance().connection;

        try(PreparedStatement inserimento = connection.prepareStatement("UPDATE cliente " +
                "SET saldo = ?, tempoDiGioco = ?, fichesGiocate = ?, vincitaPercentualeTot = ?, partiteGiocate = ?, " +
                "tipo = ?, scontoPokerPercentuale = ?, sospetto = ? " +
                "where  idCliente = ?"))
        {
            inserimento.setInt(1, saldo);
            inserimento.setLong(2, tempoDiGioco.getSeconds());
            inserimento.setInt(3, fichesGiocate);
            inserimento.setDouble(4, vincitaPercentualeTot);
            inserimento.setInt(5, partiteGiocate);
            inserimento.setString(6, tipo);
            inserimento.setDouble(7, scontoPokerPercentuale);
            inserimento.setBoolean(8, sospetto);
            inserimento.setString(9, codiceTessera);

            inserimento.executeUpdate();
        }
    }

    @Override
    public void cambioUsername(String vecchioCodiceTessera, String username, String nuovoCodiceTessera) throws SQLException {
        Connection connection = ConnessioneDatabase.getInstance().connection;

        try(PreparedStatement inserimento = connection.prepareStatement("update cliente " +
                "set username = ?, idCliente = ? " +
                "where idCliente = ?"))
        {
            inserimento.setString(1, username);
            inserimento.setString(2, nuovoCodiceTessera);
            inserimento.setString(3, vecchioCodiceTessera);

            inserimento.executeUpdate();
        }
    }

    @Override
    public void cambioPassword(String nuovaPassword, String username) throws SQLException {
        Connection connection = ConnessioneDatabase.getInstance().connection;

        try(PreparedStatement inserimento = connection.prepareStatement("update cliente " +
                "set password = ? " +
                "where username = ?"))
        {
            inserimento.setString(1, nuovaPassword);
            inserimento.setString(2, username);

            inserimento.executeUpdate();
        }
    }

    @Override
    public void resettaPassword(String nuovaPassword, String username, String nome, String cognome) throws SQLException {
        Connection connection = ConnessioneDatabase.getInstance().connection;

        try(PreparedStatement inserimento = connection.prepareStatement("update cliente " +
                "set password = ? " +
                "where username = ? and nome = ? and cognome = ?"))
        {
            inserimento.setString(1, nuovaPassword);
            inserimento.setString(2, username);
            inserimento.setString(3, nome);
            inserimento.setString(4, cognome);

            inserimento.executeUpdate();
        }
    }
}
