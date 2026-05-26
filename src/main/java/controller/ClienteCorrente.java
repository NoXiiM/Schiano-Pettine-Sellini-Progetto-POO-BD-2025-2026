package controller;

import model.gestionale.Giocatore;
import model.gestionale.Sessione;
import model.gestionale.Tavolo;
import model.gestionale.utenteEFigli.Cliente;

public class ClienteCorrente
{
    Cliente clienteCorrente;

    public ClienteCorrente(Cliente clienteCorrente) {
        this.clienteCorrente = clienteCorrente;
    }

    public Cliente getClienteCorrente() {
        return clienteCorrente;
    }

    public void setClienteCorrente(Cliente clienteCorrente) {
        this.clienteCorrente = clienteCorrente;
    }

    public int getSaldoCorrente()
    {
        return clienteCorrente.getSaldo();
    }

    public String getCurrentUsername()
    {
        return clienteCorrente.getUsername();
    }

    public Sessione creaNuovaSessioneDiGioco(Tavolo tavoloSelezionato)
    {
        Giocatore giocatoreCorrente = new Giocatore(clienteCorrente, clienteCorrente.getSaldo());

        return new Sessione(giocatoreCorrente, tavoloSelezionato);
    }
}
