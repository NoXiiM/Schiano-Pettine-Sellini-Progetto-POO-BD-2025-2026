package test;

import controller.mazzo.ControllerMazzo;
import model.giochi.Sabot;

public class TestMazzo
{
    public static void main(String[] args)
    {
        Sabot mazzo = new Sabot(2);
        mazzo.stampaCarte();
    }
}
