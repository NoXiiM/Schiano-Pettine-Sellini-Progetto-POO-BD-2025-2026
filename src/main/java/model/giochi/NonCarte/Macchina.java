package model.giochi.NonCarte;

public abstract class Macchina {
    //da completare?

    protected float creditoOriginario;
    /// Credito iniziale presente nella macchina all'inserimento da parte dello staff
    protected float creditoInterno;
    /// Credito interno totale della macchina d'uscita o entrata
    protected float creditoTotEntrato;
    /// Credito totale entrato nella macchina (per perdite giocatori)
    protected float creditoTotUscito;
    /// Credito totale uscito dalla macchina (per vincite giocatori)
    protected float resocontoGuadagni;
    /// Credito guadagnato dalla macchina (si ottiene sovrapponendo entrate e uscite)
    protected float resocontoPerdite;
    /// Credito perso dalla macchina (si ottiene sovrapponendo entrate e uscite)
    protected float creditoGiocatore;

    /// Credito inserito nella macchina dal giocatore, in base all'esito partita entrerà nella macchina

    public void CalcolaResocontoCredito() {/// Funzione per calcolare i guadagni macchina ma anche aggiornare il credito originario da cui far riferimento
        if ((creditoTotEntrato - creditoTotUscito) > 0) {/// Se la differenzaa è maggiore di 0 la maccchina sta guadagnando
            resocontoGuadagni = creditoTotEntrato - creditoTotUscito;

        } else {
            if ((creditoTotEntrato - creditoTotUscito) < 0) {/// esito positivo di questo controllo riconduce a una perdita
                resocontoPerdite = creditoTotUscito - creditoTotEntrato;

            } else {/// La differenza è uguale a zero quindi zero guadagni zGG (Zero Gain Game)
                resocontoGuadagni = 0;
            }
        }
    }
}
