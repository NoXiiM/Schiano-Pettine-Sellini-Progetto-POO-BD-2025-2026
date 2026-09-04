package model.gestionale;

import java.time.LocalDate;

/**
 * The type Ban.
 */
public class Ban
{
    private LocalDate dataDiBan;
    private final String motivi;

    /**
     * Istanzia nuovo ban, infatti la data non viene passata come parametro perché si registra la data attuale, ban quando
     * è istanziato è sempre associato a un cliente poi
     *
     * @param motivi the motivi
     */
    public Ban(String motivi)
    {
        dataDiBan = LocalDate.now();
        this.motivi = motivi;
    }

    /**
     * Istanzia un ban che già è registrato nel db, la data viene passata
     *
     * @param dataDiBan the data di ban
     * @param motivi    the motivi
     */
    public Ban(LocalDate dataDiBan, String motivi)
    {
        this(motivi);
        this.dataDiBan = dataDiBan;
    }

    /**
     * Gets motivi.
     *
     * @return the motivi
     */
    public String getMotivi() {
        return motivi;
    }

    /**
     * Gets data di ban.
     *
     * @return the data di ban
     */
    public LocalDate getDataDiBan() {
        return dataDiBan;
    }
}
