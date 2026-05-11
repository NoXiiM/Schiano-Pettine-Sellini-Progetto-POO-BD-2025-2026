package model;

import model.utenteEFigli.Cliente;

public class Giocatore
{
    private int fiches;

    //attributi da associazioni
    private Cliente clienteAssociato;

    public Giocatore(Cliente clienteAssociato, int soldi)
    {
        //magari stesso nel costruttore possiamo applicare il tasso di conversione
        fiches = soldi;
        this.clienteAssociato = clienteAssociato;
    }
}
