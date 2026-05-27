package model.gestionale;

import java.util.Date;

public class Ban
{
    private Date dataDiBan;
    private String motivi;

    public Ban(String motivi)
    {
        dataDiBan = new Date();
        this.motivi = motivi;
    }

    public String getMotivi() {
        return motivi;
    }

    public Date getDataDiBan() {
        return dataDiBan;
    }
}
