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
import java.sql.SQLException;
import java.util.ArrayList;

public class SelezioneTavoloPoker {
    private JList<String> listaTavoli;
    private JTextArea informazioniTavolo;
    private JButton giocaButton;
    private JButton indietroButton;
    private JPanel selezionaTavoloPanel;

    private TavoloController controller;

    private static DefaultListModel<String> modellolistaTavoli;

    public SelezioneTavoloPoker(JFrame frameChiamante, ClientWelcomeController clienteController, TabbedMenuPlayer mainMenu)
    {
        JFrame thisFrame = new JFrame("SelezioneTavoloPoker");
        thisFrame.setContentPane(selezionaTavoloPanel);
        thisFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
                            new GUIPoker(thisFrame, clienteController);
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
    }
}
