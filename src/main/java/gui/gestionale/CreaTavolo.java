package gui.gestionale;

import controller.gestionale.DipendenteWelcomeController;
import controller.gestionale.WelcomeController;
import database.implementazioneDAO.ImpDAOopd;
import model.gestionale.Gioco;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class CreaTavolo {
    private JComboBox giocoComboBox;
    private JSpinner numeroPostiSpinner;
    private JTextField codiceTavoloTextField;
    private JButton aggiungiTavolo;
    private JPanel creaTavolo;
    private JLabel numeroPostiLabel;
    private JLabel giocoLabel;
    private JLabel codiceTavoloLabel;
    private JButton indietroButton;

    public CreaTavolo(DipendenteWelcomeController controller, JFrame frameChiamante, DefaultListModel modelloListaTavoli)
    {
        JFrame thisFrame = new JFrame("CreaTavolo");
        thisFrame.setContentPane(creaTavolo);
        thisFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        thisFrame.pack();
        thisFrame.setVisible(true);

        visibilitaPulsantiPosti(false);

        giocoComboBox.addItem(Gioco.Blackjack);
        giocoComboBox.addItem(Gioco.Poker);
        giocoComboBox.addItem(Gioco.SlotMachine);

        //serve a lasciare la combo box bianca all'inizio del programma
        giocoComboBox.setSelectedIndex(-1);

        giocoComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(giocoComboBox.getSelectedItem() == null) return;

                SpinnerNumberModel modelloSpinnerNumeroPosti;

                if(giocoComboBox.getSelectedItem().equals(Gioco.Poker))
                {
                    modelloSpinnerNumeroPosti = new SpinnerNumberModel(2, 2, 5, 1);
                    numeroPostiSpinner.setModel(modelloSpinnerNumeroPosti);
                    visibilitaPulsantiPosti(true);
                }
                else if(giocoComboBox.getSelectedItem().equals(Gioco.Blackjack))
                {
                    modelloSpinnerNumeroPosti = new SpinnerNumberModel(1, 1, 5, 1);
                    numeroPostiSpinner.setModel(modelloSpinnerNumeroPosti);
                    visibilitaPulsantiPosti(true);
                }
                else
                {
                    visibilitaPulsantiPosti(false);
                }
            }
        });

        aggiungiTavolo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Gioco selezione = (Gioco) giocoComboBox.getSelectedItem();
                if(selezione == null) JOptionPane.showMessageDialog(null,
                        "nessun gioco selezionato", "errore", JOptionPane.ERROR_MESSAGE);
                else
                {
                    String id = codiceTavoloTextField.getText();
                    if(id.isEmpty())
                    {
                        JOptionPane.showMessageDialog(null, "campo codice tavolo vuoto",
                                "errore", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    int numero;

                    try {
                        numero = Integer.parseInt(id);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "solo numeri accettati per il codice tavolo",
                                "errore", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if(controller.idGiaPreso(numero))
                    {
                        JOptionPane.showMessageDialog(null, "codice assegnato già ad un altro" +
                                "tavolo", "errore", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (selezione.equals(Gioco.SlotMachine)) {
                        try {
                            controller.aggiungiTavolo(numero, selezione, 1);
                            JOptionPane.showMessageDialog(null, "tavolo aggiunto correttamente");
                            modelloListaTavoli.clear();
                            modelloListaTavoli.addAll(controller.getTavoliInLocale());
                            uscita(frameChiamante, thisFrame);
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(null, ex.getMessage(), "errore",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    else {
                        try {
                            controller.aggiungiTavolo(numero, selezione, (int) numeroPostiSpinner.getValue());
                            modelloListaTavoli.clear();
                            modelloListaTavoli.addAll(controller.getTavoliInLocale());
                            JOptionPane.showMessageDialog(null, "tavolo aggiunto correttamente");
                            uscita(frameChiamante, thisFrame);
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(null, ex.getMessage(), "errore",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });

        indietroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                uscita(frameChiamante, thisFrame);
            }
        });
    }

    public void visibilitaPulsantiPosti(boolean val)
    {
        numeroPostiLabel.setVisible(val);
        numeroPostiSpinner.setVisible(val);
    }

    public void uscita(JFrame frameChiamante, JFrame thisFrame)
    {
        frameChiamante.setVisible(true);
        thisFrame.dispose();
    }
}
