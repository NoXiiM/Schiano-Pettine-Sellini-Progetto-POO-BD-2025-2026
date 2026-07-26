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
                  String identificativoDipendente, Gioco giocoDoveServe){

        super(username, nome, cognome, codiceFiscale, dataDiNascita, password, identificativoDipendente);
        giochiDoveServe.add(giocoDoveServe);
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
        if(giochiDoveServe==null){
            if(giochiDoveServe.get(0)==Blackjack ||giochiDoveServe.get(0)==Poker){
                if(giochiDoveServe.get(0)==Blackjack){
                    risultato = "Blackjack ";
            }   else{
              risultato = "Poker ";
            }
            if(giochiDoveServe.get(1)==Blackjack ||giochiDoveServe.get(1)==Poker) {
                if (giochiDoveServe.get(1) == Blackjack) {
                    risultato += "Blackjack ";
                } else {
                  risultato += "Poker ";
                }
            }

            }
            if (risultato.isEmpty()) {
             return "Nessuno";
            }
        }
        return risultato;
    }
}
