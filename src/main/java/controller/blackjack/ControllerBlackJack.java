package controller.blackjack;

import controller.mazzo.ControllerMazzo;
import model.gestionale.Gioco;
import model.giochi.Carte.*;

import javax.swing.*;

/**
 * Controller realizzato per gestire la logica del black jack
 */
public class ControllerBlackJack extends ControllerMazzo
{
    private ManoBlackJack banco;
    private int indiceRiduzioneMano;

    /**
     * Instantiates a new Controller black jack.
     *
     * @param nmazzi quanti mazzi contiene il sabot
     * @param nmani  numero di giocatori/mani
     */
    public ControllerBlackJack(int nmazzi, int nmani) {
        super(nmazzi, nmani, Gioco.Blackjack);

        mazzo.inizializzaSabot();
        mazzo.mischiaMazzo();

        banco = new ManoBlackJack();
        indiceRiduzioneMano = 0;
    }

    @Override
    public void reinizializzaMazzo() {
        mazzo = new Sabot(mazzo.getNumeroDiMazzi(), mazzo.getGioco());
        mazzo.inizializzaSabot();
        mazzo.mischiaMazzo();
    }

    @Override
    public ManoBlackJack creaMano(Gioco gioco) {
        return new ManoBlackJack();
    }

    /**
     * Calcola il punteggio numerico di una mano di black jack
     *
     * @param mano mano
     * @return punteggio
     */
    public int getPoints(ManoBlackJack mano)
    {
        int acc = 0;
        int aceCounter = 0;
        for(Carta i : mano.getListaMano())
        {
            if(getValoreNumero(i) == 1) aceCounter += 1;
            else acc += getValoreNumero(i);
        }

        for(int i = 0; i < aceCounter; i++)
        {
            if(i == aceCounter - 1 && acc <= 10) acc += 11;
            else acc += 1;
        }

        return acc;
    }

    /**
     * Funzione di mapping numero:valore in blackJack
     *
     * @param carta carta
     * @return valore carta
     */
    public static int getValoreNumero(Carta carta) {
        int valore = ControllerMazzo.getValoreNumero(carta);

        if(valore >= 10) return 10;
        else return valore;
    }

    /**
     * Funzione che serve 2 carte a ogni giocatore e al banco
     */
    public void serviCarte()
    {
        for(Mano i: listaMani)
        {
            try {
                this.serviCarta(i);
            } catch (DeckOut e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "errore", JOptionPane.WARNING_MESSAGE);
                this.serviCarta(i);
            }
            try {
                this.serviCarta(i);
            } catch (DeckOut e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "errore", JOptionPane.WARNING_MESSAGE);
                this.serviCarta(i);
            }
        }

        try {
            this.serviCarta(banco);
        } catch (DeckOut e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "errore", JOptionPane.WARNING_MESSAGE);
        }
        try {
            this.serviCarta(banco);
        } catch (DeckOut e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "errore", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Restituisce la size della mano in listaMani di indice index
     *
     * @param index indice
     * @return size mano
     */
    public int getManoSize(int index)
    {
        return listaMani.get(index).getDimensioneMano();
    }

    /**
     * Funzione che calcola il path dell'immagine di una determinata carta del dealer in base al seme e al numero della
     * carta
     *
     * @param icarta indice carta
     * @return path immagine rispettiva
     */
//come la display card per il giocatore ma per il dealer
    public String displayCardDealer(int icarta)
    {
        String path = "/Carte2/";
        int num;

        Carta carta = banco.getCarta(icarta);
        Seme seme = banco.getCarta(icarta).getSeme();


        //suggerito da quick fixes di intellij
        num = switch (seme) {
            case Seme.cuore -> 0;
            case Seme.picche -> 14;
            case Seme.quadro -> 28;
            case Seme.fiore -> 42;
        };

        num += ControllerMazzo.getValoreNumero(carta);
        String numString = String.format("%02d", num);

        path += numString + "_kerenel_Cards.png";

        return path;
    }

    @Override
    public ManoBlackJack getMano(int index) {
        return (ManoBlackJack) super.getMano(index);
    }

    /**
     * Determina l'interazione tra la carta del dealer e la carta del giocatore corrente e ne restituisce un valore di
     * HandStateBJ, è strettamente interconnessa con la funzione pulsantiera in GUIBlackJack
     *
     * @param iManoGiocatore indice mano corrente
     * @return stato interazioni mani
     */
    public HandStateBJ statoPartitaIniziale(int iManoGiocatore)
    {
        ManoBlackJack corrente = (ManoBlackJack) listaMani.get(iManoGiocatore);

        if(bancoHaAsso() && getPoints(corrente) == 21) return HandStateBJ.evenmoney;
        if(bancoHaAsso()) return HandStateBJ.assicurazione;
        if(getPoints(corrente) == 21)
        {
            corrente.setFlag(HandStateBJ.bj);
            return HandStateBJ.bj;
        }

        return HandStateBJ.normale;
    }

    /**
     * Funzione che setta la flag bj o normale alla mano del banco
     */
    public void setStatoBanco()
    {
        if(getPoints(banco) == 21) banco.setFlag(HandStateBJ.bj);
        else banco.setFlag(HandStateBJ.normale);
    }

    /**
     * Funzione che ti dice se una mano è splittabile oppure no
     *
     * @param i indice mano
     * @return true: la mano è divisibile, false: la mano non è divisibile
     */
    public boolean isSplittable(int i)
    {
        Mano mano = getMano(i);
        return mano.getListaMano().size() == 2 &&
                mano.getCarta(0).getNumero().equals(mano.getCarta(1).getNumero());
    }

    /**
     * Gets mano banco size.
     *
     * @return size della mano del banco
     */
    public int getManoBancoSize()
    {
        return banco.getDimensioneMano();
    }

    /**
     * Funzione che regola l'algoritmo di pescata del banco, se il banco ha un punteggio minore di 17 prende un'altra
     * carta e il valore di ritorno fa in maniera tale che in GUIBlackJack non si passi alla fase successiva, altrimenti
     * il banco non pesca e si va alla fase successiva
     *
     * @return true: banco ha pescato, false: banco non può pescare
     */
//regola secondo cui il banco continua a pescare finchè non supera 17
    public boolean algoritmoPescataBanco()
    {
        if(getPoints(banco) < 17)
        {
            try {
                serviCarta(banco);
            } catch (DeckOut e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "errore", JOptionPane.WARNING_MESSAGE);
                serviCarta(banco);
            }
            return true;
        }
        else return false;
    }

    /**
     * Funzione che calcola la vincita in base a tutti i casi di gioco possibili
     *
     * @param indexMano the index mano
     * @return the int
     */
    public int calcolaVincita(int indexMano)
    {
        int punteggioBanco = getPoints(banco);
        ManoBlackJack manoCorrente = (ManoBlackJack) listaMani.get(indexMano);
        int punteggioGiocatore = getPoints(manoCorrente);

        if(manoCorrente.getFlag() == HandStateBJ.evenmoney) return manoCorrente.getPuntata()*2;
        if(manoCorrente.getFlag() == HandStateBJ.bj && banco.getFlag() == HandStateBJ.bj)
            return manoCorrente.getPuntata();
        if(manoCorrente.getFlag() == HandStateBJ.bj && banco.getFlag() != HandStateBJ.bj)
            return (int)(((float)(manoCorrente.getPuntata())) * (5f/2f));
        if(manoCorrente.getFlag() != HandStateBJ.bj && banco.getFlag() == HandStateBJ.bj)
            return 0;
        if(punteggioGiocatore > 21) return 0;
        if(punteggioBanco > 21) return manoCorrente.getPuntata() * 2;

        if(punteggioBanco > punteggioGiocatore) return 0;
        else if(punteggioGiocatore > punteggioBanco) return manoCorrente.getPuntata() * 2;
        else return manoCorrente.getPuntata();
    }

    /**
     * Funzione che controlla se l'insurance è stata vinta dalla carta di indice index
     *
     * @param index indice carta
     * @return true: insurance vinta, false: insurance non vinta
     */
    public boolean insuranceVinta(int index)
    {
        return getFlagMano(index).equals(HandStateBJ.assicurazione) && getFlagBanco().equals(HandStateBJ.bj);
    }

    /**
     * Funzione che resetta le mani dei giocatori e del banco nel blackjack
     */
    public void resettaMani()
    {
        super.resettaMani(Gioco.Blackjack);

        banco = new ManoBlackJack();
    }

    /**
     * Funzione che effettua la meccanica dello split: prende la mano di indice index, crea una nuova mano di BlackJack,
     * viene traslata una carta dalla vecchia mano alla nuova mano, entrambe le mani pescano una carta, la nuova mano è
     * inserita nell'Arraylist subito dopo la vecchia, viene incrementato l'attributo indiceRiduzione mano in maniera tale
     * che alla fine del match si possa ripristinare la corretta capienza dell'Arraylist di mani
     *
     * @param index indice mano su cui effettuare lo split
     */
    public void divisione(int index)
    {
        ManoBlackJack manoCorrente = getMano(index);

        ManoBlackJack nuovaMano = new ManoBlackJack();

        nuovaMano.setPuntata(manoCorrente.getPuntata());

        //eliminazione da mano corrente e return della carta
        Carta cartaTrasferita = manoCorrente.traslaCarta();
        nuovaMano.addCarta(cartaTrasferita);

        try {
            serviCarta(manoCorrente);
        } catch (DeckOut e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "errore", JOptionPane.WARNING_MESSAGE);
            serviCarta(manoCorrente);
        }
        try {
            serviCarta(nuovaMano);
        } catch (DeckOut e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "errore", JOptionPane.WARNING_MESSAGE);
            serviCarta(nuovaMano);
        }

        //fa inserimento con scorrimento, non sostituzione
        listaMani.add(index + 1, nuovaMano);

        indiceRiduzioneMano += 1;
    }

    /**
     * Verifica se banco ha un asso
     *
     * @return true: banco ha asso, false: banco non ha asso
     */
    private boolean bancoHaAsso()
    {
        return (banco.getCarta(0).getNumero() == Numero.uno);
    }

    /**
     * Funzione che restituisce tutte le puntate piazzate al giocatore in maniera tale da non smarrire i soldi del giocatore,
     * serve per quando il giocatore sceglie di uscire dal gioco pur avendo messo qualche puntata
     *
     * @return somma di tutte le puntate piazzate dal giocatore
     */
    public int restituisciPuntate()
    {
        int soldi = 0;

        for(Mano i: listaMani)
        {
            ManoBlackJack manoCorrente = (ManoBlackJack) i;

            soldi += manoCorrente.getPuntata() + manoCorrente.getSideBet();
        }

        return soldi;
    }

    /**
     * Gets flag mano.
     *
     * @param index index
     * @return flag mano
     */
    public HandStateBJ getFlagMano(int index)
    {
        ManoBlackJack mano = (ManoBlackJack) listaMani.get(index);

        return mano.getFlag();
    }

    /**
     * Gets flag banco.
     *
     * @return flag banco
     */
    public HandStateBJ getFlagBanco()
    {
        return banco.getFlag();
    }

    /**
     * Gets indice riduzione mano.
     *
     * @return indice riduzione mano
     */
    public int getIndiceRiduzioneMano() {
        return indiceRiduzioneMano;
    }

    /**
     * Sets indice riduzione mano.
     *
     * @param indiceRiduzioneMano indice riduzione mano
     */
    public void setIndiceRiduzioneMano(int indiceRiduzioneMano) {
        this.indiceRiduzioneMano = indiceRiduzioneMano;
    }
}
