package model.giochi.carte;

public class Carta
{
    private Numero numero;
    private Seme seme;

    public Carta(Numero numero, Seme seme) {
        this.numero = numero;
        this.seme = seme;
    }

    public Numero getNumero() {
        return numero;
    }

    public Seme getSeme() {
        return seme;
    }
}
