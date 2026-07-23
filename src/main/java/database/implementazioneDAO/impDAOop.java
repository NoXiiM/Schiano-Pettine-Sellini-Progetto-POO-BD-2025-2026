package database.implementazioneDAO;

import database.ConnessioneDatabase;
import database.DAO.DAOop;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class impDAOop implements DAOop {
    @Override
    public boolean passwordDimenticata(String nome, String cognome, String username) throws SQLException {
        Connection connection = ConnessioneDatabase.getInstance().connection;

        //1 è meglio di * perchè ci basta una flag, non tutti i campi della tupla
        try (PreparedStatement query1 = connection.prepareStatement("select 1 " +
                "from cliente " +
                "where nome = ? AND cognome = ? AND username = ?")) {
            query1.setString(1, nome);
            query1.setString(2, cognome);
            query1.setString(3, username);

            try (ResultSet rs1 = query1.executeQuery()) {
                //query non vuota
                if ((rs1.next())) {
                    try (PreparedStatement updatePassword = connection.prepareStatement("UPDATE cliente " +
                            "set password = 'P@ssword!' " +
                            "where username = ?")) {
                        updatePassword.setString(1, username);

                        updatePassword.executeUpdate();
                    }

                    return true;
                }
            }
        }

        try (PreparedStatement query2 = connection.prepareStatement("select 1 " +
                "from Dipendente " +
                "where nome = ? AND cognome = ? AND username = ?")) {
            query2.setString(1, nome);
            query2.setString(2, cognome);
            query2.setString(3, username);

            try (ResultSet rs2 = query2.executeQuery()) {
                if ((rs2.next())) {
                    try (PreparedStatement updatePassword = connection.prepareStatement("UPDATE dipendente " +
                            "set password = 'P@ssword!' " +
                            "where username = ?")) {
                        updatePassword.setString(1, username);

                        updatePassword.executeUpdate();
                    }

                    return true;
                }
            }
        }
        return false;
    }

    //nel database c'è trigger function che si occupa di verificare all'inserimento che non ci siano duplicati di CF
    //o username sia nella tabella cliente che in quella dipendente

    @Override
    public void usernameTessereUtenti(ArrayList<String> usernames, ArrayList<String> codiciTessere) throws SQLException{

        Connection connection = ConnessioneDatabase.getInstance().connection;

        try (PreparedStatement query = connection.prepareStatement("select username, idCliente " +
                "from cliente ")) {

            try (ResultSet rs = query.executeQuery()) {
                while(rs.next()) {
                    usernames.add(rs.getString("username"));
                    codiciTessere.add(rs.getString("idCliente"));
                }
            }
        }
    }

    @Override
    public void registrazione(String id, String username, String nome, String cognome, String codiceFiscale,
                              LocalDate dataDiNascita, String password, int saldoIniziale) throws SQLException {
        Connection connection = ConnessioneDatabase.getInstance().connection;
        try (PreparedStatement inserimento = connection.prepareStatement("insert into cliente" +
                "(idCliente, username, nome, cognome, codiceFiscale, dataDiNascita, password, saldo) " +
                "VALUES(?,?,?,?,?,?,?,?)")) {
            inserimento.setString(1, id);
            inserimento.setString(2, username);
            inserimento.setString(3, nome);
            inserimento.setString(4, cognome);
            inserimento.setString(5, codiceFiscale);
            inserimento.setDate(6, java.sql.Date.valueOf(dataDiNascita));
            inserimento.setString(7, password);
            inserimento.setInt(8, saldoIniziale);

            inserimento.executeUpdate();
        }
    }

    @Override
    public String trovaTabella(String username, String password) throws SQLException {
        Connection connection = ConnessioneDatabase.getInstance().connection;

        try (PreparedStatement query = connection.prepareStatement("select 1 " +
                "from cliente " +
                "where username = ? AND password = ?")) {
            query.setString(1, username);
            query.setString(2, password);

            try (ResultSet rs = query.executeQuery()) {
                if (rs.next()) return "Cliente";
            }
        }

        try (PreparedStatement query = connection.prepareStatement("select 1 " +
                "from dipendente " +
                "where username = ? AND password = ?")) {
            query.setString(1, username);
            query.setString(2, password);

            try (ResultSet rs = query.executeQuery()) {
                if (rs.next()) return "Dipendente";
            }
        }

        return null;
    }

    @Override
    public void loginCliente(int[] saldo, String[] tipo, double[] scontoPokerPercentuale, LocalDate[] dataDiBan,
                             String[] nome, String[] cognome, String[] codiceFiscale, LocalDate[] dataDiNascita,
                             String username, String password, String[] codiceTessera) throws SQLException {

        Connection connection = ConnessioneDatabase.getInstance().connection;

        try (PreparedStatement query = connection.prepareStatement("select saldo, tipo, scontoPokerPercentuale," +
                "dataDiBan, nome, cognome, codiceFiscale, dataDiNascita, username, password, idCliente " +
                "from cliente " +
                "where username = ? AND password = ?")) {

            query.setString(1, username);
            query.setString(2, password);

            try (ResultSet rs = query.executeQuery()) {

                if (rs.next()) {
                    saldo[0] = rs.getInt(1);
                    tipo[0] = rs.getString(2);
                    scontoPokerPercentuale[0] = rs.getDouble(3);

                    java.sql.Date ddb = rs.getDate(4);
                    dataDiBan[0] = (ddb != null) ? ddb.toLocalDate() : null;

                    nome[0] = rs.getString(5);
                    cognome[0] = rs.getString(6);
                    codiceFiscale[0] = rs.getString(7);

                    dataDiNascita[0] = rs.getDate(8).toLocalDate();

                    codiceTessera[0]= rs.getString(9);
                }
            }
        }
    }

    @Override
    public void loginDipendente(String[] identificativo, String[] nome, String[] cognome, String[] codiceFiscale, LocalDate[] dataDiNascita,
                                String[] ruolo, String username, String password) throws SQLException {
        Connection connection = ConnessioneDatabase.getInstance().connection;

        try (PreparedStatement query = connection.prepareStatement("select idDipendente, nome, cognome, codiceFiscale," +
                " dataDiNascita, ruolo " +
                "from dipendente " +
                "where username = ? AND password = ?")) {
            query.setString(1, username);
            query.setString(2, password);

            try (ResultSet rs = query.executeQuery()) {
                if (rs.next()) {
                    identificativo[0] = rs.getString(1);
                    nome[0] = rs.getString(2);
                    cognome[0] = rs.getString(3);
                    codiceFiscale[0] = rs.getString(4);
                    dataDiNascita[0] = rs.getDate(8).toLocalDate();
                    ruolo[0] = rs.getString(6);
                }
            }
        }
    }
}
