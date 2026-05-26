package model.gestionale;

import model.gestionale.utenteEFigli.Cliente;

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

    public int getFiches() {
        return fiches;
    }

    public void setFiches(int fiches) {
        this.fiches = fiches;
    }

    public void incrementaFiches(int fiches)
    {
        this.fiches += fiches;
    }
}
