package database.implementazioneDAO;

import database.ConnessioneDatabase;
import database.DAO.DAOopd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class impDAOopd implements DAOopd {
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
                        "dataDiNascita, password, idCliente, premium, " +
                        "sconto_premium, sospetto, tempoDiGioco, " +
                        "fichesGiocate, saldo, partiteGiocate, " +
                        "dataBan, motiviBan " +
                        "FROM cliente"
        )) {

            ResultSet risultato = ricerca.executeQuery();


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
                        risultato.getBoolean("premium")
                );

                sconto_premium.add(
                        risultato.getDouble("sconto_premium")
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


                LocalDate data = risultato.getDate("dataBan").toLocalDate();

                if(data != null)
                    dataBan.add(data);
                else
                    dataBan.add(null);


                motiviBan.add(
                        risultato.getString("motiviBan")
                );
            }

        }

    }
}
