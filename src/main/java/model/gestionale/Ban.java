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
}
