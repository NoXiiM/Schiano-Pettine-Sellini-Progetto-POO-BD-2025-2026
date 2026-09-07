package gui.gestionale;

import controller.TavoloController;
import controller.gestionale.ClientWelcomeController;
import gui.giochi.GUIBlackJack;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Interfaccia di selezione per tavoli da BlackJack.
 */

public class SelezioneTavoloBlackJack
{
    private JPanel selezioneTavoloPanel;
    private JButton giocaButton;
    private JButton indietroButton;
    private JList<String> listaTavoli;
    private JTextArea informazioniTavolo;

    private final TavoloController controller;

    private static DefaultListModel<String> modellolistaTavoli;

    /**
     * Costruttore di SelezioneTavoloBlackJack, popola e rende visibile la lista dei tavoli disponibili per il BlackJack,
     * per accedere a un tavolo e giocare bisogna selezionare il tavolo dalla lista e premere il pulsante: 'entra tavolo'.
     * Nella text area a destra vengono mostrate ulteriori informazioni sul tavolo selezionato.
     *
     * @param frameChiamante    interfaccia principale per clienti: {@link TabbedMenuPlayer}, resa nuovamente visibile alla fine della sessione di gioco
     * @param clienteController controller contenente le info necessarie per la gestione del cliente: {@link ClientWelcomeController}
     * @param mainMenu          riferimento al menu principale, usato per aggiornare il saldo del giocatore al ritorno: {@link TabbedMenuPlayer}
     */
    public SelezioneTavoloBlackJack(JFrame frameChiamante, ClientWelcomeController clienteController, TabbedMenuPlayer mainMenu)
    {
        JFrame thisFrame= new JFrame("SelezioneTavoloBlackJack");
        thisFrame.setContentPane(selezioneTavoloPanel);
        thisFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        thisFrame.pack();
        thisFrame.setVisible(true);
        Dimension minDim = new Dimension(700, 200);
        thisFrame.setMinimumSize(minDim);

        informazioniTavolo.setEditable(false);
        informazioniTavolo.setFocusable(false);

        controller = new TavoloController();
        try {
            controller.popolaBlackJack();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "errore", JOptionPane.ERROR_MESSAGE);
        }

        modellolistaTavoli = new DefaultListModel<>();

        ArrayList<String> tavoliDaMostrare = controller.getTavoliId();

        modellolistaTavoli.addAll(tavoliDaMostrare);

        listaTavoli.setModel(modellolistaTavoli);

        giocaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selezione;
                if(listaTavoli.getSelectedValue() != null)
                {
                    selezione = listaTavoli.getSelectedValue();
                    int idTavolo = controller.getIdFromList(selezione);
                    thisFrame.setVisible(false);
                    try {
                        clienteController.creaNuovaSessioneDiGioco(controller.getTavoloWithId(idTavolo));
                        new GUIBlackJack(thisFrame, clienteController);
                    } catch (RuntimeException ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage(),"errore", JOptionPane.ERROR_MESSAGE);
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
                mainMenu.aggiornaSaldo();
                frameChiamante.setVisible(true);
            }
        });
        listaTavoli.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                String selezione = listaTavoli.getSelectedValue();
                int idTavolo = controller.getIdFromList(selezione);

                informazioniTavolo.setText(controller.getTavoloWithId(idTavolo).toString());
            }
        });

        thisFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                thisFrame.dispose();
                frameChiamante.setVisible(true);
            }
        });
    }
}
