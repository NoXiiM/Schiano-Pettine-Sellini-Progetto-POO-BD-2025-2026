package controller;
import javax.swing.*;
import java.util.ArrayList;


public class WelcomeController {

    public void login(String username, String password, String userType) throws RuntimeException{
        if(username.isBlank() || password.isBlank()) throw new RuntimeException("Compila tutti i campi!");
         // JOptionPane.showMessageDialog(null, userType, "Errore", JOptionPane.ERROR_MESSAGE); // --> Per verificare se effettivamente restituisce Admin/Giocatore

        // da fare check delle credenziali in un db

        if(userType.equals("Admin")){
            //gestire ritorno/eccezione
        } else {
            //gestire ritorno/eccezione
        }
    }

    //funzione registrati

    //funzione password dimenticata
}
