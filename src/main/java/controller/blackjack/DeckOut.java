package controller.blackjack;

/**
 * Errore che viene sollevato in casi estremi nel blackjack, se le carte nel mazzo finiscono prima ancora che sia finito
 * il round, è più probabile che si ottenga se si gioca a blackjack con un solo mazzo ma è comunque difficile da ottenere
 */
public class DeckOut extends RuntimeException {
    /**
     * Instantiates a new Deck out.
     *
     * @param message è sempre qualcosa del genere: rimischiata di emergenza
     */
    public DeckOut(String message) {
        super(message);
    }
}
