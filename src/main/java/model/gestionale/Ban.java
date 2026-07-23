package model.gestionale;

import java.time.LocalDate;

public class Ban
{
    private LocalDate dataDiBan;
    private String motivi;

    public Ban(String motivi)
    {
        dataDiBan = null;
        this.motivi = motivi;
    }

    public Ban(LocalDate dataDiBan, String motivi)
    {
        this(motivi);
        this.dataDiBan = dataDiBan;
    }

    public String getMotivi() {
        return motivi;
    }

    public LocalDate getDataDiBan() {
        return dataDiBan;
    }
}
