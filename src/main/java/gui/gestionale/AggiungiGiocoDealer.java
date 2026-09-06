package gui.gestionale;

import controller.gestionale.DipendenteWelcomeController;
import model.gestionale.Gioco;
import model.gestionale.utenteEFigli.Dealer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * GUI che permette al supervisore di selezionare quali giochi aggiungere a un dealer
 */
public class AggiungiGiocoDealer {
    private JPanel aggiungiGiocoPanel;
    private JCheckBox pokerCheckBox;
    private JCheckBox blackJackCheckBox;
    private JButton confermaGiochi;
    private JButton tornaIndietroButton;

    /**
     * Se il dealer conosce già tutti i giochi la finestra si chiude e viene mostrato un messaggio di errore, se il dealer
     * non conosce almeno un gioco vengono mostrate le checkbox di ogni gioco non conosciuto, si può spuntare le checkbox
     * e poi premere conferma per aggiunger quei giochi al dealer
     *
     * @param controller        the controller
     * @param frameChiamante    the frame chiamante
     * @param dealerSelezionato the dealer selezionato
     */
    public AggiungiGiocoDealer(DipendenteWelcomeController controller, JFrame frameChiamante, Dealer dealerSelezionato) {

        ArrayList<Gioco> giochi = dealerSelezionato.getGiochiDealer();

        ArrayList<Gioco> giochiMancanti = new ArrayList<>();
        if (!giochi.contains(Gioco.Poker)) giochiMancanti.add(Gioco.Poker);
        if (!giochi.contains(Gioco.Blackjack)) giochiMancanti.add(Gioco.Blackjack);

        JFrame thisFrame = new JFrame("AggiungiGiocoDealer");
        thisFrame.setContentPane(aggiungiGiocoPanel);
        thisFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        thisFrame.pack();

        if (giochiMancanti.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Il dealer conosce già tutti i giochi", "Errore", JOptionPane.ERROR_MESSAGE);
            thisFrame.dispose();
            return;
        }

        pokerCheckBox.setVisible(giochiMancanti.contains(Gioco.Poker));
        blackJackCheckBox.setVisible(giochiMancanti.contains(Gioco.Blackjack));

        frameChiamante.setVisible(false);
        thisFrame.setVisible(true);

        confermaGiochi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (!(pokerCheckBox.isSelected() || blackJackCheckBox.isSelected())) {
                    JOptionPane.showMessageDialog(null, "Seleziona gioco", "Errore", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                ArrayList<Gioco> giochiSelezionati = new ArrayList<>();
                if (pokerCheckBox.isSelected()) giochiSelezionati.add(Gioco.Poker);
                if (blackJackCheckBox.isSelected()) giochiSelezionati.add(Gioco.Blackjack);

                try {
                    controller.aggiungiGiochi(dealerSelezionato, giochiSelezionati);
                    thisFrame.dispose();
                    frameChiamante.setVisible(true);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
                }

                MainMenuAdmin.getModelloListaDipendente().clear();
                MainMenuAdmin.getModelloListaDipendente().addAll(controller.getDipendentiInLocale());
            }
        });

        tornaIndietroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                thisFrame.dispose();
                frameChiamante.setVisible(true);
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