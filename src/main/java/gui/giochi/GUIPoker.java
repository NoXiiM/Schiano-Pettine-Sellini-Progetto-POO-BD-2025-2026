package gui.giochi;

import controller.gestionale.ClientWelcomeController;
import controller.gestionale.WelcomeController;
import controller.poker.ControllerPoker;
import database.implementazioneDAO.ImpDAOop;
import model.gestionale.utenteEFigli.Cliente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class GUIPoker {
    private JPanel mano;
    private JLabel indicatoreMani;
    private JPanel pokerPanel;
    private JLabel mazzo;
    private JButton rilanciaButton;
    private JButton checkButton;
    private JButton foldButton;
    private JSpinner spinnerPuntata;
    private JButton confermaButton;
    private JButton indietroButton;
    private JButton giocaButton;
    private JSpinner spinnerNplayer;
    private JLabel labelNplayer;
    private JButton vediCarteButton;
    private JSpinner spinnerAnte;
    private JLabel labelAnte;

    //conta qual è la mano corrente
    private int currentHand = 0;

    private ControllerPoker controller;

    private ArrayList<ClientWelcomeController> sessioniCorrenti;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Poker");
        frame.setContentPane(new GUIPoker().pokerPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    //[0]
    public GUIPoker()
    {
        sessioniCorrenti = new ArrayList<>();

        //caricamento immagine deck
        Image img = new ImageIcon(
                getClass().getResource("/carte2/42_kerenel_Cards.png")
        ).getImage();
        mazzo.setIcon(new ImageIcon(img));

        azioniButton(false);
        relativiRilancia(false);
        vediCarteButton.setVisible(false);

        //TODO da modificare max con numero di posti
        SpinnerNumberModel modelloSpinnerNplayer = new SpinnerNumberModel(2, 2, 5, 1);
        spinnerNplayer.setModel(modelloSpinnerNplayer);
        ((JSpinner.DefaultEditor) spinnerNplayer.getEditor()).getTextField().setEditable(false);

        SpinnerNumberModel modelloSpinnerAnte = new SpinnerNumberModel(10, 0, 1000, 5);
        spinnerAnte.setModel(modelloSpinnerAnte);
        ((JSpinner.DefaultEditor) spinnerAnte.getEditor()).getTextField().setEditable(false);

        indietroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        giocaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int nmani = (int) spinnerNplayer.getValue();
                controller = new ControllerPoker(nmani);

                while(sessioniCorrenti.size() < nmani)
                {
                    String username = JOptionPane.showInputDialog(null,
                            "inserisci il tuo username per aggiungerti alla partita");
                    String password = JOptionPane.showInputDialog(null, "inserisci la password");

                    try {
                        Cliente cliente = controller.caricaPlayer(username, password);

                        if(cliente == null) JOptionPane.showMessageDialog(null, "credenziali sbagliate",
                                "errore", JOptionPane.ERROR_MESSAGE);
                        else
                        {
                            //sessioniCorrenti.add(new ClientWelcomeController());
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage(),
                                "errore", JOptionPane.ERROR_MESSAGE);
                    }
                }

                pescataIniziale();
            }
        });
    }

    //[1]
    public void pescataIniziale()
    {
        startingButton(false);
        vediCarteButton.setVisible(true);

        controller.serviCarte();

        vediCarteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                disegnaCarte();
                vediCarteButton.setVisible(false);
                azioniButton(true);


            }
        });
    }



    //funzioni di utility
    //gestione visibilità componenti
    private void startingButton(boolean visibilità)
    {
        indietroButton.setVisible(visibilità);
        labelNplayer.setVisible(visibilità);
        spinnerNplayer.setVisible(visibilità);
        giocaButton.setVisible(visibilità);
        spinnerAnte.setVisible(visibilità);
        labelAnte.setVisible(visibilità);
    }

    private void azioniButton(boolean visibilità)
    {
        rilanciaButton.setVisible(visibilità);
        checkButton.setVisible(visibilità);
        foldButton.setVisible(visibilità);
    }

    private void relativiRilancia(boolean visibilità)
    {
        spinnerPuntata.setVisible(visibilità);
        confermaButton.setVisible(visibilità);
    }

    //funzioni che disegnano le carte
    private void disegnaCarte()
    {
        String pathIm;
        JLabel temp;

        for(int i = 0; i < 5; i++)
        {
            pathIm = controller.displayCard(currentHand, i);
            temp = new JLabel(new ImageIcon(Objects.requireNonNull(getClass().getResource(pathIm))));
            //System.out.println(temp + " " + pathIm);
            mano.add(temp);
        }
    }

    public void refreshPanel(JPanel pannello)
    {
        //ricalcola la posizione delle componenti nel pannello
        pannello.revalidate();
        //renderizza i nuovi widget in maniera che possono essere visti
        pannello.repaint();
    }
}
