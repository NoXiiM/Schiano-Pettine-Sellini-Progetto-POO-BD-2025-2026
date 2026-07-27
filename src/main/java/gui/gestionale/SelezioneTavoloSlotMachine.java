package gui.gestionale;

import controller.TavoloController;


import controller.gestionale.ClientWelcomeController;
import gui.giochi.GUISlotMachine;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

public class SelezioneTavoloSlotMachine {



    private static JFrame thisFrame;
    private JPanel selezioneTavoloPanel;
    private JButton tornaIndietroButton;
    private JButton entraTavoloButton;
    private JList listaTavoli;
    private JLabel selezioneListaLabel;

    private TavoloController controller;

    public static DefaultListModel<String> modellolistaTavoli;

    public SelezioneTavoloSlotMachine(JFrame frameChiamante, ClientWelcomeController clienteController, TabbedMenuPlayer mainMenu)
    {
        thisFrame= new JFrame("SelezioneTavoloSlotMachine");
        thisFrame.setContentPane(selezioneTavoloPanel);
        thisFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        thisFrame.pack();
        thisFrame.setVisible(true);
        Dimension dimensioniMinime = new Dimension(700,200);
        thisFrame.setMinimumSize(dimensioniMinime);
        thisFrame.setLocationRelativeTo(null);




        controller = new TavoloController();

        try {
            controller.popolaSlotMachine();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "errore", JOptionPane.ERROR_MESSAGE);
        }

        modellolistaTavoli = new DefaultListModel<String>();

        ArrayList<String> tavoliDaMostrare = controller.getTavoliId();

        modellolistaTavoli.addAll(tavoliDaMostrare);

        listaTavoli.setModel(modellolistaTavoli);

        selezioneListaLabel.setVisible(false);

        tornaIndietroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                thisFrame.dispose();
                mainMenu.aggiornaSaldo();
                frameChiamante.setVisible(true);

            }
        });
        entraTavoloButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selezione;
                if(listaTavoli.getSelectedValue() != null)
                {
                    //TODO ricordare di poter inserire solo tavoli con un posto per la slot
//                  if(controller.getNumeroPosti(Integer.parseInt(selezione.replaceAll("\\D+", ""))-1) ==1) {
                    selezione = (String)listaTavoli.getSelectedValue();
                    int idTavolo = controller.getIdFromList(selezione);
                    thisFrame.setVisible(false);
                    try {
                        clienteController.creaNuovaSessioneDiGioco(controller.getTavoloWithId(idTavolo));
                        new GUISlotMachine(thisFrame, clienteController);
                    } catch (RuntimeException ex) {
                        ex.getMessage();
                    }
//                    else{
//                        JOptionPane.showMessageDialog(null, "Il tavolo selezionato è occupato ",
//                                "errore", JOptionPane.ERROR_MESSAGE);
//                    }
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "nessun tavolo è stato selezionato",
                            "errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        listaTavoli.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {


                String tavoloSelezionato = (String)listaTavoli.getSelectedValue();
                int idTavolo = controller.getIdFromList(tavoloSelezionato);
                selezioneListaLabel.setText(controller.getTavoloWithId(idTavolo).toString());
                selezioneListaLabel.setVisible(true);
            }
        });
    }
}
