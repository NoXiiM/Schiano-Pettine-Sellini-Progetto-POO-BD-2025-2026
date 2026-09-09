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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Interfaccia di selezione per le slot machines.
 */
public class SelezioneTavoloSlotMachine {
    private static JFrame thisFrame;
    private JPanel selezioneTavoloPanel;
    private JButton tornaIndietroButton;
    private JButton entraTavoloButton;
    private JList<String> listaTavoli;
    private JLabel selezioneListaLabel;

    private final TavoloController controller;

    public static DefaultListModel<String> modellolistaTavoli;

    /**
     * Costruttore di SelezioneTavoloSlotMachine, popola e rende visibile la lista dei tavoli disponibili per le Slot Machines,
     * per accedere a un tavolo e giocare bisogna selezionare il tavolo dalla lista e premere il pulsante: 'entra tavolo'.
     * Nella text area a destra vengono mostrate ulteriori informazioni sul tavolo selezionato.
     *
     * @param frameChiamante    interfaccia principale per clienti: {@link TabbedMenuPlayer}, resa nuovamente visibile alla fine della sessione di gioco
     * @param clienteController controller contenente le info necessarie per la gestione del cliente: {@link ClientWelcomeController}
     * @param mainMenu          riferimento al menu principale, usato per aggiornare il saldo del giocatore al ritorno: {@link TabbedMenuPlayer}
     */
    public SelezioneTavoloSlotMachine(JFrame frameChiamante, ClientWelcomeController clienteController, TabbedMenuPlayer mainMenu)
    {
        thisFrame= new JFrame("SelezioneTavoloSlotMachine");
        thisFrame.setContentPane(selezioneTavoloPanel);
        thisFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        thisFrame.pack();
        thisFrame.setVisible(true);
        Dimension dimensioniMinime = new Dimension(700,200);
        thisFrame.setMinimumSize(dimensioniMinime);
        thisFrame.setLocationRelativeTo(null);
        //Setting icon
        ImageIcon iconaFrame = new ImageIcon(Objects.requireNonNull(getClass().getResource("/icone/LogoCasinò.png")));
        thisFrame.setIconImage(iconaFrame.getImage().getScaledInstance(1783, 1113, Image.SCALE_SMOOTH));

        controller = new TavoloController();

        try {
            controller.popolaSlotMachine();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "errore", JOptionPane.ERROR_MESSAGE);
        }

        modellolistaTavoli = new DefaultListModel<>();

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
                    selezione = listaTavoli.getSelectedValue();
                    int idTavolo = controller.getIdFromList(selezione);
                    thisFrame.setVisible(false);
                    try {
                        clienteController.creaNuovaSessioneDiGioco(controller.getTavoloWithId(idTavolo));
                        new GUISlotMachine(thisFrame, clienteController);
                    } catch (RuntimeException ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
                    }
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
                String tavoloSelezionato = listaTavoli.getSelectedValue();
                int idTavolo = controller.getIdFromList(tavoloSelezionato);
                selezioneListaLabel.setText(controller.getTavoloWithId(idTavolo).toString());
                selezioneListaLabel.setVisible(true);
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
