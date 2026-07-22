package controller.gestionale;

import model.gestionale.utenteEFigli.Cliente;
import model.gestionale.utenteEFigli.Utente;

import java.util.ArrayList;

public class DipendenteWelcomeController extends WelcomeController {

    public DipendenteWelcomeController(WelcomeController controller){
        super(controller.getLista_utenti(), controller.getCurrentUser());
    }

    //admin
    public ArrayList<Cliente> getLista_clienti() {
        ArrayList<Cliente> onlyClients= new ArrayList<>();

        for(Utente i : getLista_utenti()){
            if(i instanceof Cliente temp){
                onlyClients.add(temp);
            }
        }
        return onlyClients;
    }
}
