package gui.gestionale;

import controller.ClienteCorrente;
import controller.TavoloController;


import gui.giochi.GUISlotMachine;
import model.gestionale.Sessione;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SelezioneTavoloSlotMachine {



    private static JFrame thisFrame;
    private JPanel selezioneTavoloPanel;
    private JRadioButton tavolo1RadioButton;
    private JRadioButton tavolo2RadioButton;
    private JRadioButton tavolo3RadioButton;
    private JLabel slotMachine1;
    private JLabel slotMachine2;
    private JLabel slotMachine3;
    private JButton tornaIndietroButton;
    private JButton entraTavoloButton;

    private JFrame frameChiamante;
    private ClienteCorrente currentClient;
    private TavoloController controller;

    public SelezioneTavoloSlotMachine(JFrame frameChiamante, ClienteCorrente currentClient)
    {
        thisFrame= new JFrame("SelezioneTavoloSlotMachine");
        thisFrame.setContentPane(selezioneTavoloPanel);
        thisFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        thisFrame.pack();
        thisFrame.setVisible(true);



        controller = new TavoloController();
        controller.popolaSlotMachine();

        ButtonGroup selezionaTavolo = new ButtonGroup();

        selezionaTavolo.add(tavolo1RadioButton);
        selezionaTavolo.add(tavolo2RadioButton);
        selezionaTavolo.add(tavolo3RadioButton);
        tavolo1RadioButton.setActionCommand("0");
        tavolo2RadioButton.setActionCommand("1");
        tavolo3RadioButton.setActionCommand("2");

        if(controller.getNumeroPosti(0)==1){slotMachine1.setText("la slotmachine 1 è libera ");}
        else{slotMachine2.setText("la slotmachine 1 è occupata ");}
        if(controller.getNumeroPosti(1)==1){slotMachine2.setText("la slotmachine 2 è libera ");}
        else{slotMachine2.setText("la slotmachine 2 è occupata ");}
        if(controller.getNumeroPosti(2)==1){slotMachine3.setText("la slotmachine 3 è libera ");}
        else{slotMachine3.setText("la slotmachine 3 è occupata ");}

        tornaIndietroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                thisFrame.dispose();
                frameChiamante.setVisible(true);

            }
        });
        entraTavoloButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(selezionaTavolo.getSelection() != null)
                {

                    if(controller.getNumeroPosti(Integer.parseInt(selezionaTavolo.getSelection().getActionCommand()))==1) {
                        thisFrame.setVisible(false);
                        Sessione nuovaSessione;
                        try {
                            if (slotMachine1.isEnabled())
                                nuovaSessione = currentClient.creaNuovaSessioneDiGioco(controller.getTavolo(0));
                            if (slotMachine2.isEnabled())
                                nuovaSessione = currentClient.creaNuovaSessioneDiGioco(controller.getTavolo(1));
                            if (slotMachine3.isEnabled())
                                nuovaSessione = currentClient.creaNuovaSessioneDiGioco(controller.getTavolo(2));
                            else throw new RuntimeException("è successo qualcosa di brutto coi tavoli");
                            new GUISlotMachine(thisFrame, nuovaSessione);
                        } catch (RuntimeException ex) {
                            ex.getMessage();
                        }
                    }
                    else{
                        JOptionPane.showMessageDialog(null, "Il tavolo selezionato è occupato ",
                                "errore", JOptionPane.ERROR_MESSAGE);
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "nessun tavolo è stato selezionato",
                            "errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
