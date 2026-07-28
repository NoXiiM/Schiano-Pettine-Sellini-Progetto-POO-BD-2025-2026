package database.implementazioneDAO;

import database.ConnessioneDatabase;
import database.DAO.DAOopd;
import model.gestionale.Gioco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class ImpDAOopd implements DAOopd {
    @Override
    public void recuperaDatiClienti(ArrayList<String> username, ArrayList<String> nome, ArrayList<String> cognome,
                                    ArrayList<String> codiceFiscale, ArrayList<LocalDate> dataDiNascita, ArrayList<String> password,
                                    ArrayList<String> codiceTesseraGiocatore,ArrayList<Boolean> premium,
                                    ArrayList<Double> sconto_premium,
                                    ArrayList<Boolean> sospetto,
                                    ArrayList<Long> tempoDiGiocoInSec,
                                    ArrayList<Integer> fichesGiocate,
                                    ArrayList<Integer> saldo,
                                    ArrayList<Integer> partiteGiocate,
                                    ArrayList<LocalDate> dataBan,
                                    ArrayList<String> motiviBan) throws SQLException {
        Connection connection = ConnessioneDatabase.getInstance().connection;


        try(PreparedStatement ricerca = connection.prepareStatement(
                "SELECT username, nome, cognome, codiceFiscale, " +
                        "dataDiNascita, password, idCliente, tipo, " +
                        "scontoPokerPercentuale, sospetto, tempoDiGioco, " +
                        "fichesGiocate, saldo, partiteGiocate, " +
                        "dataDiBan, motiviBan " +
                        "FROM Cliente"
        )) {

            try(ResultSet risultato = ricerca.executeQuery())
            {
                while(risultato.next()) {

                    username.add(risultato.getString("username"));

                    nome.add(risultato.getString("nome"));

                    cognome.add(risultato.getString("cognome"));

                    codiceFiscale.add(risultato.getString("codiceFiscale"));

                    dataDiNascita.add(
                            risultato.getDate("dataDiNascita").toLocalDate()
                    );

                    password.add(
                            risultato.getString("password")
                    );

                    codiceTesseraGiocatore.add(
                            risultato.getString("idCliente")
                    );

                    premium.add(
                            risultato.getString("tipo").equals("Premium")
                    );

                    sconto_premium.add(
                            risultato.getDouble("scontoPokerPercentuale")
                    );

                    sospetto.add(
                            risultato.getBoolean("sospetto")
                    );

                    tempoDiGiocoInSec.add(
                            risultato.getLong("tempoDiGioco")
                    );

                    fichesGiocate.add(
                            risultato.getInt("fichesGiocate")
                    );

                    saldo.add(
                            risultato.getInt("saldo")
                    );

                    partiteGiocate.add(
                            risultato.getInt("partiteGiocate")
                    );

                    java.sql.Date sqlDate = risultato.getDate("dataDiBan");
                    if (sqlDate != null) {
                        dataBan.add(sqlDate.toLocalDate());
                    } else {
                        dataBan.add(null);
                    }

                    motiviBan.add(
                            risultato.getString("motiviBan")
                    );
                }
            }
        }
    }

    @Override
    public void recuperaDatiDipendenti(ArrayList<String> idDipendenti, ArrayList<String> nome, ArrayList<String> cognome,
                                       ArrayList<LocalDate> dataDiNascita, ArrayList<String> codiceFiscale, ArrayList<String> username,
                                       ArrayList<String> password, ArrayList<String> ruolo, ArrayList<String> gioco) throws SQLException{
        Connection connection = ConnessioneDatabase.getInstance().connection;


        try(PreparedStatement ricerca = connection.prepareStatement(
                "SELECT idDipendente, nome, cognome, dataDiNascita, " +
                        "codiceFiscale, username, password, ruolo, idGioco " +
                        "FROM Dipendente as d " +
                        "LEFT JOIN giochiDealer as gd on d.idDipendente = gd.idDealer " +
                        "order by idDipendente"
        )) {

            try(ResultSet risultato = ricerca.executeQuery())
            {
                while (risultato.next()) {
                    idDipendenti.add(risultato.getString("idDipendente"));
                    nome.add(risultato.getString("nome"));
                    cognome.add(risultato.getString("cognome"));
                    dataDiNascita.add(risultato.getDate("dataDiNascita").toLocalDate());
                    codiceFiscale.add(risultato.getString("codiceFiscale"));
                    username.add(risultato.getString("username"));
                    password.add(risultato.getString("password"));
                    ruolo.add(risultato.getString("ruolo"));
                    gioco.add(risultato.getString("idGioco"));
                }
            }
        }
    }

    @Override
    public void caricaTavoli(Gioco gioco, ArrayList<Integer> idTavolo, ArrayList<Integer> numeroPosti,
                             ArrayList<String> idDealer) throws SQLException
    {
        Connection connection = ConnessioneDatabase.getInstance().connection;

        try(PreparedStatement query = connection.prepareStatement("select * " +
                "from tavolo as t " +
                "where gioco = ?"))
        {
            query.setString(1, gioco.name());

            try(ResultSet rs = query.executeQuery())
            {
                while (rs.next())
                {
                    idTavolo.add(rs.getInt("numero"));
                    numeroPosti.add(rs.getInt("numeroPosti"));
                    idDealer.add(rs.getString("idDealer"));
                }
            }
        }
    }

    @Override
    public void salvataggioBan(String idCliente,LocalDate dataDiBan, String motiviBan) throws SQLException
    {
        Connection connection = ConnessioneDatabase.getInstance().connection;

        try(PreparedStatement aggiornamento = connection.prepareStatement("update cliente " +
                "set dataDiBan = ?, motiviBan = ? " +
                "where idCliente = ?"))
        {
            aggiornamento.setDate(1, java.sql.Date.valueOf(dataDiBan));
            aggiornamento.setString(2, motiviBan);
            aggiornamento.setString(3, idCliente);

            aggiornamento.executeUpdate();
        }
    }

    @Override
    public void eliminaDipendente(String idDipendente) throws SQLException
    {
        Connection connection = ConnessioneDatabase.getInstance().connection;
        try(PreparedStatement cancellazione = connection.prepareCall("delete from dipendente " +
                "where idDipendente = ?"))
        {
            cancellazione.setString(1, idDipendente);

            cancellazione.executeUpdate();
        }
    }

    @Override
    public void aggiungiTavolo(int idTavolo, String gioco, int numeroPosti, String idDealer) throws SQLException
    {
        Connection connection = ConnessioneDatabase.getInstance().connection;

        try(PreparedStatement inserimento = connection.prepareStatement("insert into tavolo " +
                "values(?,?,?,?)"))
        {
            inserimento.setInt(1, idTavolo);
            inserimento.setString(2, gioco);
            inserimento.setInt(3, numeroPosti);
            inserimento.setString(4, idDealer);

            inserimento.executeUpdate();
        }
    }

    @Override
    public void assegnaDipendenteATavolo(String idDipendente, String ruolo, int idTavolo) throws SQLException
    {
        Connection connection = ConnessioneDatabase.getInstance().connection;

        if(ruolo.equals("Dealer"))
        {
            try(PreparedStatement aggiornamento = connection.prepareStatement("UPDATE tavolo " +
                    "set idDealer = ? " +
                    "where numero = ? "))
            {
                aggiornamento.setString(1, idDipendente);
                aggiornamento.setInt(2, idTavolo);

                aggiornamento.executeUpdate();
            }
        }
        else
        {
            try(PreparedStatement aggiornamento = connection.prepareStatement("insert into supervisoreTavolo(idSupervisore, idTavolo) " +
                    "values(?,?)"))
            {
                aggiornamento.setString(1, idDipendente);
                aggiornamento.setInt(2, idTavolo);

                aggiornamento.executeUpdate();
            }
        }
    }

    @Override
    public void aggiungiGiocoDealer(String idDealer, ArrayList<Gioco> giochi) throws  SQLException
    {
        Connection connection = ConnessioneDatabase.getInstance().connection;

        for(Gioco i : giochi)
        {
            try(PreparedStatement inserimento = connection.prepareStatement("insert into giochiDealer " +
                    "values(?,?) "))
            {
                inserimento.setString(1, idDealer);
                inserimento.setString(2, i.name());

                inserimento.executeUpdate();
            }
        }
    }
}