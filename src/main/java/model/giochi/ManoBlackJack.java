package model.giochi;

import model.gestionale.Gioco;

public class ManoBlackJack extends Mano implements Cloneable
{
    private HandStateBJ flag;
    private int sideBet;

    public ManoBlackJack(Gioco gioco){
        super(gioco);
        this.flag = HandStateBJ.normale;
    }

    public HandStateBJ getFlag() {
        return flag;
    }

    public void setFlag(HandStateBJ flag) {
        this.flag = flag;
    }

    public void setSideBet(int sideBet) {
        this.sideBet = sideBet;
    }

    public void raddoppio()
    {
        puntata *= 2;
    }

    public Carta traslaCarta()
    {
        Carta temp = listaMano.getLast();
        listaMano.removeLast();
        listaMano.trimToSize();

        return temp;
    }
}
