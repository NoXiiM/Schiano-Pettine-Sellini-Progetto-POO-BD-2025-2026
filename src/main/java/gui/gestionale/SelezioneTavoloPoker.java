package gui.gestionale;

import controller.TavoloController;
import controller.gestionale.ClientWelcomeController;
import gui.giochi.GUIBlackJack;
import gui.giochi.GUIPoker;

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
 * Interfaccia di selezione per tavoli da poker.
 */

public class SelezioneTavoloPoker {
    private JList<String> listaTavoli;
    private JTextArea informazioniTavolo;
    private JButton giocaButton;
    private JButton indietroButton;
    private JPanel selezionaTavoloPanel;

    private TavoloController controller;

    private static DefaultListModel<String> modellolistaTavoli;

    /**
     * Costruttore di SelezioneTavoloPoker, popola e rende visibile la lista dei tavoli disponibili per il Poker.
     *
     * @param frameChiamante    interfaccia principale per clienti: {@link TabbedMenuPlayer}, resa nuovamente visibile alla fine della sessione di gioco
     * @param clienteController controller contenente le info necessarie per la gestione del cliente: {@link ClientWelcomeController}
     * @param mainMenu          riferimento al menu principale, usato per aggiornare il saldo del giocatore al ritorno: {@link TabbedMenuPlayer}
     */
    public SelezioneTavoloPoker(JFrame frameChiamante, ClientWelcomeController clienteController, TabbedMenuPlayer mainMenu)
    {
        JFrame thisFrame = new JFrame("SelezioneTavoloPoker");
        thisFrame.setContentPane(selezionaTavoloPanel);
        thisFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        thisFrame.pack();
        thisFrame.setVisible(true);

        Dimension minDim = new Dimension(700, 200);
        thisFrame.setMinimumSize(minDim);

        informazioniTavolo.setEditable(false);
        informazioniTavolo.setFocusable(false);

        controller = new TavoloController();
        try {
            controller.popolaPoker();
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
                    int sommaDaPagare = controller.pagaTavoloPoker(idTavolo, clienteController.getScontoCliente());

                    int input = JOptionPane.showConfirmDialog(null, "per usufruire di questo tavolo" +
                            " e del servizio del dealer devi pagare " + sommaDaPagare);

                    if(input == JOptionPane.YES_OPTION)
                    {
                        try {
                            clienteController.decrementaSaldoCliente(sommaDaPagare);
                            clienteController.creaNuovaSessioneDiGioco(controller.getTavoloWithId(idTavolo));
                            new GUIPoker(thisFrame, clienteController, sommaDaPagare);
                            thisFrame.setVisible(false);
                        } catch (RuntimeException ex) {
                            JOptionPane.showMessageDialog(null, ex.getMessage(), "errore", JOptionPane.ERROR_MESSAGE);
                        }
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
