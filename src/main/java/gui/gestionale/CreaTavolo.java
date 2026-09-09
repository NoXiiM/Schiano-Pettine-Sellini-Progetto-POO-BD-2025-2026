package gui.gestionale;

import controller.gestionale.DipendenteWelcomeController;
import model.gestionale.Gioco;
import model.gestionale.Tavolo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.Objects;

/**
 * GUI tramite cui i supervisori possono creare nuovi tavoli
 */
public class CreaTavolo {
    private JComboBox<Gioco> giocoComboBox;
    private JSpinner numeroPostiSpinner;
    private JTextField codiceTavoloTextField;
    private JButton aggiungiTavolo;
    private JPanel creaTavolo;
    private JLabel numeroPostiLabel;
    private JButton indietroButton;

    /**
     * Se si vuole creare un tavolo bisogna sceglierne il gioco e l'identificativo, se il tavolo è di blackjack puoi sceglierne
     * il numero di posti (da 1 a 5) se è di poker stessa cosa (numero di posti da 2 a 5)
     *
     * @param controller         the controller
     * @param frameChiamante     the frame chiamante
     * @param modelloListaTavoli per modificare la lista dei tavoli della schermata precedente
     */
    public CreaTavolo(DipendenteWelcomeController controller, JFrame frameChiamante, DefaultListModel<Tavolo> modelloListaTavoli)
    {
        JFrame thisFrame = new JFrame("CreaTavolo");
        thisFrame.setContentPane(creaTavolo);
        thisFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        thisFrame.pack();
        thisFrame.setVisible(true);
        //Setting icon
        ImageIcon iconaFrame = new ImageIcon(Objects.requireNonNull(getClass().getResource("/icone/LogoCasinò.png")));
        thisFrame.setIconImage(iconaFrame.getImage().getScaledInstance(1783, 1113, Image.SCALE_SMOOTH));

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

                    try {
                        if(controller.idGiaPreso(numero))
                        {
                            JOptionPane.showMessageDialog(null, "codice assegnato già ad un altro " +
                                    "tavolo", "errore", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    } catch (RuntimeException ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage(), "errore",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    int numeroPosti;
                    if(selezione.equals(Gioco.SlotMachine)) numeroPosti = 1;
                    else numeroPosti = (int) numeroPostiSpinner.getValue();

                    try {
                        controller.aggiungiTavolo(numero, selezione, numeroPosti);
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
        });

        indietroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                uscita(frameChiamante, thisFrame);
            }
        });

        thisFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                uscita(frameChiamante, thisFrame);
            }
        });
    }

    private void visibilitaPulsantiPosti(boolean val)
    {
        numeroPostiLabel.setVisible(val);
        numeroPostiSpinner.setVisible(val);
    }

    private void uscita(JFrame frameChiamante, JFrame thisFrame)
    {
        frameChiamante.setVisible(true);
        thisFrame.dispose();
    }
}
