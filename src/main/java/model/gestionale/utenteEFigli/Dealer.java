package model.gestionale.utenteEFigli;

import model.gestionale.Gioco;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class Dealer extends Dipendente
{
    //soluzione assolutamente provvisoria
    private ArrayList<Gioco> giochiDoveServe = new ArrayList<>();

    public Dealer(String username, String nome, String cognome, String codiceFiscale,
                  LocalDate dataDiNascita, String password,
                  String identificativoDipendente, Gioco giocoDoveServe)
    {
        super(username, nome, cognome, codiceFiscale, dataDiNascita, password, identificativoDipendente);
        giochiDoveServe.add(giocoDoveServe);
    }

    public Dealer(String username, String nome, String cognome, String codiceFiscale,
                  LocalDate dataDiNascita, String password,
                  String identificativoDipendente)
    {
        super(username, nome, cognome, codiceFiscale, dataDiNascita, password, identificativoDipendente);
    }
}
