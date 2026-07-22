package database.implementazioneDAO;

import database.ConnessioneDatabase;
import database.DAO.DAOop;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class impDAOop implements DAOop
{
    @Override
    public boolean passwordDimenticata(String nome, String cognome, String username) throws SQLException {
        Connection connection = ConnessioneDatabase.getInstance().connection;

        //1 è meglio di * perchè ci basta una flag, non tutti i campi della tupla
        try(PreparedStatement query1 = connection.prepareStatement("select 1 " +
                "from cliente " +
                "where nome = ? AND cognome = ? AND username = ?")) {
            query1.setString(1, nome);
            query1.setString(2, cognome);
            query1.setString(3, username);

            try(ResultSet rs1 = query1.executeQuery())
            {
                //query non vuota
                if ((rs1.next())) {
                    try(PreparedStatement updatePassword = connection.prepareStatement("UPDATE cliente " +
                            "set password = 'P@ssword!' " +
                            "where username = ?")) {
                        updatePassword.setString(1, username);

                        updatePassword.executeUpdate();
                    }

                    return true;
                }
            }
        }

        try(PreparedStatement query2 = connection.prepareStatement("select 1 " +
                "from Dipendente " +
                "where nome = ? AND cognome = ? AND username = ?")) {
            query2.setString(1, nome);
            query2.setString(2, cognome);
            query2.setString(3, username);

            try(ResultSet rs2 = query2.executeQuery()) {
                if ((rs2.next())) {
                    try(PreparedStatement updatePassword = connection.prepareStatement("UPDATE dipendente " +
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
    public void registrazione(String id, String username, String nome, String cognome, String codiceFiscale,
                              Date dataDiNascita, String password) throws SQLException {
        Connection connection = ConnessioneDatabase.getInstance().connection;
        try(PreparedStatement inserimento = connection.prepareStatement("insert into cliente" +
                    "(idCliente, username, nome, cognome, codiceFiscale, dataDiNascita, password) " +
                    "VALUES(?,?,?,?,?,?,?)"))
        {
            inserimento.setString(1, id);
            inserimento.setString(2, username);
            inserimento.setString(3, nome);
            inserimento.setString(4, cognome);
            inserimento.setString(5, codiceFiscale);
            inserimento.setDate(6, new java.sql.Date(dataDiNascita.getTime()));
            inserimento.setString(7, password);

            inserimento.executeUpdate();
        }
    }

    @Override
    public String trovaTabella(String username, String password) {
        return "";
    }

    @Override
    public void loginCliente(int[] saldo, String[] tipo, String[] scontoPokerPercentuale, String[] dataDiBan,
                             String[] nome, String[] cognome, String[] codiceFiscale, String[] dataDiNascita,
                             String[] username, String[] password) throws SQLException {

    }

    @Override
    public void loginDipendente(String[] identificativo, String[] nome, String[] cognome, String[] codiceFiscale, String[] dataDiNascita,
                                String[] username, String[] password, String[] ruolo) throws SQLException {

    }
}
