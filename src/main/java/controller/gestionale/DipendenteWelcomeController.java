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

        //TODO recupero clienti da db

        clientiInLocale.add(new Cliente("user", "marc", "gg", "scia", LocalDate.of(1990, 5, 14), "ciao", "ciaio"));
        return clientiInLocale;
    }

    public ArrayList<Cliente> getClientiInLocale() {
        return clientiInLocale;
    }

    public ArrayList<Cliente> ricercaClienti(String nome, String cognome, String username, int saldoMin, int saldoMax,
                                             double percentualeMin, double percentualeMax, int partiteMin, int partiteMax, String sospetto, String ban,
                                             boolean checkSaldo, boolean checkPartite, boolean checkPercentuale){

        ArrayList<Cliente> clientiRicercati = new ArrayList<>();

        for(Cliente c: clientiInLocale){

            if(!nome.isBlank()){
                if(!(nome.equals(c.getNome()))) continue;
            }

            if(!cognome.isBlank()){
                if(!(cognome.equals(c.getCognome()))) continue;
            }

            if(!username.isBlank()){
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

            if (!sospetto.equals("indifferente")) {
                if (sospetto.equals("si")) {
                    if (!c.isSospetto()) continue;
                } else {
                    if (c.isSospetto()) continue;
                }
            }
            if (!ban.equals("indifferente")) {
                if (ban.equals("si")) {
                    if (c.getBan() == null) continue;
                } else {
                    if (c.getBan() != null) continue;
                }
            }
            clientiRicercati.add(c);

        }
        return clientiRicercati;
    }
}
