package controller.gestionale;

import model.gestionale.utenteEFigli.Cliente;

import java.time.LocalDate;
import java.util.ArrayList;

public class DipendenteWelcomeController extends WelcomeController {

    ArrayList<Cliente> clientiInLocale;

    public DipendenteWelcomeController(WelcomeController controller){
        super(controller.getCurrentUser(), controller.getUsernamesList());
        clientiInLocale= new ArrayList<>();
    }

    //admin
    public ArrayList<Cliente> getListaClientiDB() {

        ArrayList<Cliente> onlyClients= new ArrayList<>();
        //TODO recupero clienti da db

        onlyClients.add(new Cliente("user", "marc", "gg", "scia", LocalDate.of(1990, 5, 14), "ciao", "ciaio"));
        return onlyClients;
    }

    public ArrayList<Cliente> ricercaClienti(String nome, String cognome, String username, int saldoMin, int saldoMax,
                                             double percentualeMin, double percentualeMax, int partiteMin, int partiteMax, String sospetto, String ban,
                                             boolean checkSaldo, boolean checkPartite, boolean checkPercentuale){

        ArrayList<Cliente> clientiRicercati = new ArrayList<>();
        ArrayList<Cliente> clientiLista = new ArrayList<>();

        clientiLista.addAll(getListaClientiDB());

        for(Cliente c: clientiLista){

            if(nome != null){
                if(!(nome.equals(c.getNome()))) continue;
            }

            if(cognome != null){
                if(!(cognome.equals(c.getCognome()))) continue;
            }

            if(username!=null){
                if(!(username.equals(c.getUsername()))) continue;
            }

            if(checkSaldo){
                if(!(c.getSaldo() >= saldoMin && c.getSaldo() <= saldoMax)) continue;
            }

            if(checkPercentuale){
                if(!(c.getVincitaPercentualeTot() >= percentualeMin && c.getVincitaPercentualeTot() <= percentualeMax)) continue;
            }

            if(checkPartite){
                if(!(c.getPartiteGiocate() >= partiteMin && c.getPartiteGiocate() <= partiteMax)) continue;
            }

            if(!(sospetto.equals("indifferente"))){
                if(sospetto.equals("si")){
                    if(!(c.isSospetto())) continue;
                } else{
                    if(c.isSospetto()) continue;
                }
            }
            if(!(ban.equals("indifferente"))){
                if(ban.equals("si")){
                    if(!(c.getBan()==null)) continue;
                } else{
                    if(c.getBan()==null) continue;
                }
            }

            clientiRicercati.add(c);

        }

        return clientiRicercati;
    }
}
