package model.giochi.Carte;

/**
 * Carta francese, in un mazzo di carte francesi escludendo il jolly ci sono 52 carte, 4 semi per 13 numeri
 */
public class Carta
{
    private final Numero numero;
    private final Seme seme;

    /**
     * Istanzia una nuova Carta
     *
     * @param numero the numero
     * @param seme   the seme
     */
    public Carta(Numero numero, Seme seme) {
        this.numero = numero;
        this.seme = seme;
    }

    /**
     * Gets numero.
     *
     * @return the numero
     */
    public Numero getNumero() {
        return numero;
    }

    /**
     * Gets seme.
     *
     * @return the seme
     */
    public Seme getSeme() {
        return seme;
    }
}
