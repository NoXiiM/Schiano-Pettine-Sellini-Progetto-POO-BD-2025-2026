package controller.blackjack;

public class DeckOut extends RuntimeException {
    public DeckOut(String message) {
        super(message);
    }
}
