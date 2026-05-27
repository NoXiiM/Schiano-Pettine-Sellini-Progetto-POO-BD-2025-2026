package model.gestionale;

import model.gestionale.utenteEFigli.Cliente;

public class Giocatore
{
    private int saldo;

    //attributi da associazioni
    private Cliente clienteAssociato;
    private int fichesGiocate;

    public Giocatore(Cliente clienteAssociato, int saldo)
    {
        //magari stesso nel costruttore possiamo applicare il tasso di conversione
        this.saldo = saldo;
        this.clienteAssociato = clienteAssociato;
        fichesGiocate = 0;
    }

    public int getFiches() {
        return saldo;
    }

    public void setFiches(int fiches) {
        this.saldo = fiches;
    }

    public void incrementaFiches(int fiches)
    {
        this.saldo += fiches;
    }

    public void chiudiSessione()
    {
        clienteAssociato.setSaldo(saldo);
        clienteAssociato.setFichesGiocate(fichesGiocate);
    }

    public Cliente getClienteAssociato() {
        return clienteAssociato;
    }

    public void incrementaFichesGiocate(int val)
    {
        fichesGiocate += val;
    }
}
