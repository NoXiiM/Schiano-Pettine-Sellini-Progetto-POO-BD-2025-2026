package model.gestionale.utenteEFigli;

import model.gestionale.Gioco;

import java.time.LocalDate;
import java.util.ArrayList;

import static model.gestionale.Gioco.*;

public class Dealer extends Dipendente
{
    //soluzione assolutamente provvisoria
    private ArrayList<Gioco> giochiDoveServe = new ArrayList<>();

    public Dealer(String username, String nome, String cognome, String codiceFiscale,
                  LocalDate dataDiNascita, String password,
                  String identificativoDipendente, ArrayList<Gioco> giochiDoveServe){

        super(username, nome, cognome, codiceFiscale, dataDiNascita, password, identificativoDipendente);
        this.giochiDoveServe = giochiDoveServe;
    }

    public Dealer(String username, String nome, String cognome, String codiceFiscale,
                  LocalDate dataDiNascita, String password,
                  String identificativoDipendente){

        super(username, nome, cognome, codiceFiscale, dataDiNascita, password, identificativoDipendente);
    }

    @Override
    public String toString() {
        return username + " " + nome + " " + cognome+ " " + "Dealer" ;
    }

    public String getGiochiDoveServeString() {
        String risultato = "";
        if(giochiDoveServe!=null){

            if (giochiDoveServe.contains(Blackjack)) {
                risultato += Blackjack.name()+" ";
            }
            if (giochiDoveServe.contains(Poker)){
                risultato += Poker.name()+" ";
            }
            if (risultato.isBlank()){
                risultato = "Nessuno ";
            }
        }
        else{
            risultato = "Nessuno ";
        }
        return risultato;
    }
}
