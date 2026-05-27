package gui.gestionale;

import controller.ClienteCorrente;
import controller.TavoloController;
import gui.giochi.GUIBlackJack;
import model.gestionale.utenteEFigli.Cliente;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SelezioneTavoloBlackJack
{
    private JRadioButton radioButtonBJ1;
    private JRadioButton radioButtonBJ2;
    private JRadioButton radioButtonBJ3;
    private JPanel selezioneTavoloPanel;
    private JButton giocaButton;
    private JLabel infoTavolo1;
    private JLabel infoTavolo2;
    private JLabel infoTavolo3;
    private JButton indietroButton;

    private JFrame frameChiamante;
    private ClienteCorrente currentClient;
    private TavoloController controller;

    private static JFrame thisFrame;

    public SelezioneTavoloBlackJack(JFrame frameChiamante, ClienteCorrente currentClient)
    {
        thisFrame= new JFrame("SelezioneTavoloBlackJack");
        thisFrame.setContentPane(selezioneTavoloPanel);
        thisFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        thisFrame.pack();
        thisFrame.setVisible(true);

        controller = new TavoloController();
        controller.popolaBlackJack();

        ButtonGroup selezionaTavolo = new ButtonGroup();

        selezionaTavolo.add(radioButtonBJ1);
        selezionaTavolo.add(radioButtonBJ2);
        selezionaTavolo.add(radioButtonBJ3);

        infoTavolo1.setText("il tavolo 1 ha " + controller.getNumeroPosti(0) + " posti");
        infoTavolo2.setText("il tavolo 2 ha " + controller.getNumeroPosti(1) + " posti");
        infoTavolo3.setText("il tavolo 3 ha " + controller.getNumeroPosti(2) + " posti");

        giocaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(selezionaTavolo.getSelection() != null)
                {
                thisFrame.setVisible(false);
                    try {
                        if(radioButtonBJ1.isSelected()) currentClient.creaNuovaSessioneDiGioco(controller.getTavolo(0));
                        else if(radioButtonBJ2.isSelected()) currentClient.creaNuovaSessioneDiGioco(controller.getTavolo(1));
                        else if(radioButtonBJ3.isSelected()) currentClient.creaNuovaSessioneDiGioco(controller.getTavolo(2));
                        else throw new RuntimeException("è successo qualcosa di brutto coi tavoli");
                        new GUIBlackJack(thisFrame, currentClient);
                    } catch (RuntimeException ex) {
                        ex.getMessage();
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "nessun tavolo è stato selezionato",
                            "errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        indietroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                thisFrame.dispose();
                frameChiamante.setVisible(true);
            }
        });
    }
}
