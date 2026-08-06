package model.giochi;

import model.gestionale.Gioco;

public class ManoPoker extends Mano
{
    private boolean folded;

    public ManoPoker(Gioco gioco)
    {
        super(gioco);

        folded = false;
    }

    public boolean isFolded() {
        return folded;
    }

    public void setFolded(boolean folded) {
        this.folded = folded;
    }

    public void aumentaPuntata(int valore)
    {
        puntata += valore;
    }
}
