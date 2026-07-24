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

        return onlyClients;
    }
}
