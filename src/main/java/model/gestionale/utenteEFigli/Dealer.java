package model.gestionale.utenteEFigli;

import model.gestionale.Gioco;

import java.util.ArrayList;
import java.util.Date;

public class Dealer extends Dipendente
{
    //soluzione assolutamente provvisoria
    private ArrayList<Gioco> giochiDoveServe = new ArrayList<>();

    public Dealer(String nome, String cognome, String codiceFiscale,
                  Date dataDiNascita, String password,
                  String identificativoDipendente, Gioco giocoDoveServe)
    {
        super(nome, cognome, codiceFiscale, dataDiNascita, password, identificativoDipendente);
        giochiDoveServe.add(giocoDoveServe);
    }

    public Dealer(String nome, String cognome, String codiceFiscale,
                  Date dataDiNascita, String password,
                  String identificativoDipendente)
    {
        super(nome, cognome, codiceFiscale, dataDiNascita, password, identificativoDipendente);
    }
}
