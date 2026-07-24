package controller.gestionale;

import model.gestionale.utenteEFigli.Cliente;
import model.gestionale.utenteEFigli.Utente;

import java.util.ArrayList;

public class DipendenteWelcomeController extends WelcomeController {


    public DipendenteWelcomeController(WelcomeController controller){
        super(controller.getCurrentUser(), controller.getUsernamesList());
    }

    //admin
    public ArrayList<Cliente> getLista_clienti() {
        ArrayList<Cliente> onlyClients= new ArrayList<>();
        //TODO recupero clienti da super
        return onlyClients;
    }
    public ArrayList<Cliente> ricercaClienti(String nome, String cognome, String username, int saldo,
                                             double percentuale,String piumenoPe, int partite, String piumenoPa,String sospetto, String ban){
        ArrayList<Cliente> clientiRicercati = new ArrayList<>();
        ArrayList<Cliente> clientiLista = new ArrayList<>();
        //clientiLista.addAll(recuperoDao);
        boolean trovatoUtente;
        for(Cliente c: clientiLista){
            trovatoUtente=true;
            if(nome!=null){
                if(!(nome.equals(c.getNome())))trovatoUtente=false;
            }
            if(cognome!=null){
                if(!(cognome.equals(c.getCognome())))trovatoUtente=false;
            }
            if(username!=null){
                if(!(username.equals(c.getUsername())))trovatoUtente=false;
            }
            if(saldo!=0){
                if(!(saldo==c.getSaldo()))trovatoUtente=false;
            }
            if(piumenoPe!=null){
                if(piumenoPe.equals("piu")){
                    if(!(c.getVincitaPercentualeTot()>percentuale))trovatoUtente=false;
                }
                else{
                    if(!(c.getVincitaPercentualeTot()<percentuale))trovatoUtente=false;
                }
            }
            if(piumenoPa!=null){
                if(piumenoPa.equals("piu")){
                    if(!(c.getPartiteGiocate()>partite))trovatoUtente=false;
                }
                else{
                    if(!(c.getPartiteGiocate()<partite))trovatoUtente=false;
                }
            }
            if(!(sospetto.equals("indifferente"))){
                if(sospetto.equals("si")){
                    if(!(c.isSospetto()))trovatoUtente=false;
                }else{
                    if(c.isSospetto())trovatoUtente=false;
                }
            }
            if(!(ban.equals("indifferente"))){
                if(ban.equals("si")){
                    if(!(c.getBan()==null))trovatoUtente=false;
                }else{
                    if(c.getBan()==null)trovatoUtente=false;
                }
            }
            if(trovatoUtente)clientiRicercati.add(c);


        }
        return clientiRicercati;
    }
}
