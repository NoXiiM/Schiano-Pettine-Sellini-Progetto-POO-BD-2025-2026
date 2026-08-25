package database.implementazioneDAO;

import database.ConnessioneDatabase;
import database.DAO.DAOop;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class ImpDAOop implements DAOop {
    @Override
    public boolean passwordDimenticata(String nome, String cognome, String username) throws SQLException {
        Connection connection = ConnessioneDatabase.getInstance().connection;

        //1 è meglio di * perchè ci basta una flag, non tutti i campi della tupla
        try (PreparedStatement query1 = connection.prepareStatement("select 1 " +
                "from  Cliente " +
                "where nome = ? AND cognome = ? AND username = ?")) {
            query1.setString(1, nome);
            query1.setString(2, cognome);
            query1.setString(3, username);

            try (ResultSet rs1 = query1.executeQuery()) {
                //query non vuota
                if ((rs1.next())) {
                    try (PreparedStatement updatePassword = connection.prepareStatement("UPDATE cliente " +
                            "set password = 'P@ssw0rd!' " +
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
                            "set password = 'P@ssw0rd!' " +
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

    @Override
    public void registrazioneCliente(String idTessera, String username, String nome, String cognome, String codiceFiscale,
                                     LocalDate dataDiNascita, String password, int saldoIniziale) throws SQLException {
        Connection connection = ConnessioneDatabase.getInstance().connection;
        try (PreparedStatement inserimento = connection.prepareStatement("insert into cliente" +
                "(idCliente, username, nome, cognome, codiceFiscale, dataDiNascita, password, saldo) " +
                "VALUES(?,?,?,?,?,?,?,?)")) {
            inserimento.setString(1, idTessera);
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
    public void registrazioneDipendente(String idTessera, String username, String nome, String cognome, String codiceFiscale,
                              LocalDate dataDiNascita, String password, String ruolo) throws SQLException {

        Connection connection = ConnessioneDatabase.getInstance().connection;

        try (PreparedStatement inserimento = connection.prepareStatement("insert into dipendente" +
                "(username, nome, cognome, codiceFiscale, dataDiNascita, password, ruolo, idDipendente) " +
                "VALUES(?,?,?,?,?,?,?,?)")) {
            inserimento.setString(1, username);
            inserimento.setString(2, nome);
            inserimento.setString(3, cognome);
            inserimento.setString(4, codiceFiscale);
            inserimento.setDate(5, java.sql.Date.valueOf(dataDiNascita));
            inserimento.setString(6, password);
            inserimento.setString(7, ruolo);
            inserimento.setString(8, idTessera);

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
    public boolean loginCliente(String[] codiceTessera, int[] saldo, long[] tempoDiGioco, int[] fichesGiocate,
                             double[] vincitaPercentualeTot, int[] partiteGiocate, String[] tipo, double[] scontoPokerPercentuale,
                             boolean[] sospetto, LocalDate[] dataDiBan, String[] motiviBan, String[] nome, String[] cognome,
                             String[] codiceFiscale, LocalDate[] dataDiNascita, String username, String password) throws SQLException {

        Connection connection = ConnessioneDatabase.getInstance().connection;

        try (PreparedStatement query = connection.prepareStatement("select *" +
                "from cliente " +
                "where username = ? AND password = ?")) {

            query.setString(1, username);
            query.setString(2, password);

            try (ResultSet rs = query.executeQuery()) {

                if (rs.next()) {
                    codiceTessera[0] = rs.getString("idCliente");
                    saldo[0] = rs.getInt("saldo");

                    tempoDiGioco[0] = rs.getLong("tempoDiGioco");

                    fichesGiocate[0] = rs.getInt("fichesGiocate");
                    vincitaPercentualeTot[0] = rs.getDouble("vincitaPercentualeTot");
                    partiteGiocate[0] = rs.getInt("partiteGiocate");
                    tipo[0] = rs.getString("tipo");
                    scontoPokerPercentuale[0] = rs.getDouble("scontoPokerPercentuale");
                    sospetto[0] = rs.getBoolean("sospetto");

                    java.sql.Date ddb = rs.getDate("dataDiBan");
                    dataDiBan[0] = (ddb != null) ? ddb.toLocalDate() : null;

                    motiviBan[0] = rs.getString("motiviBan");
                    nome[0] = rs.getString("nome");
                    cognome[0] = rs.getString("cognome");
                    codiceFiscale[0] = rs.getString("codiceFiscale");

                    dataDiNascita[0] = rs.getDate("dataDiNascita").toLocalDate();

                    return true;
                }
                else return false;
            }
        }
    }

    @Override
    public boolean loginDipendente(String[] identificativo, String[] nome, String[] cognome, String[] codiceFiscale, LocalDate[] dataDiNascita,
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
                    dataDiNascita[0] = rs.getDate(5).toLocalDate();
                    ruolo[0] = rs.getString(6);
                    return true;
                }
                else return false;
            }
        }
    }

    //nel database c'è trigger function che si occupa di verificare all'inserimento che non ci siano duplicati di CF
    //o username sia nella tabella cliente che in quella dipendente
    @Override
    public void usernameUtenti(ArrayList<String> usernames) throws SQLException{
        //System.out.println("fetch locale eseguita");
        usernames.clear();

        Connection connection = ConnessioneDatabase.getInstance().connection;

        try (PreparedStatement query = connection.prepareStatement("select username " +
                "from cliente ")) {
            try (ResultSet rs = query.executeQuery()) {
                while(rs.next()) {
                    usernames.add(rs.getString("username"));
                }
            }
        }

        try (PreparedStatement query = connection.prepareStatement("select username " +
                "from dipendente ")) {
            try (ResultSet rs = query.executeQuery()) {
                while(rs.next()) {
                    usernames.add(rs.getString("username"));
                }
            }
        }
    }
    //Cambio Username e password, Reset password
    @Override
    public void cambioPassword(String nuovaPassword, String username,String ruolo) throws SQLException {
        Connection connection = ConnessioneDatabase.getInstance().connection;

        try(PreparedStatement inserimento = connection.prepareStatement("update "+ruolo+" "+
                "set password = ? " +
                "where username = ?"))
        {
            inserimento.setString(1, nuovaPassword);
            inserimento.setString(2, username);

            inserimento.executeUpdate();
        }
    }
}
