package controller;

import model.gestionale.utenteEFigli.Dipendente;

public class DipendenteCorrente
{
    public Dipendente dipendente;

    public DipendenteCorrente(Dipendente dipendente)
    {
        this.dipendente = dipendente;
    }

    public String getUsername()
    {
        return dipendente.getUsername();
    }
}
