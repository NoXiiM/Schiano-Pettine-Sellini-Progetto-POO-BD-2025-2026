package gui.giochi;

import controller.gestionale.ClientWelcomeController;
import controller.poker.ControllerPoker;
import model.gestionale.Gioco;
import model.gestionale.Tavolo;
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
    private JButton puntaButton;
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
    private JLabel usernameLabel;
    private JLabel saldo;

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
                            sessioniCorrenti.add(new ClientWelcomeController(cliente));
                            //TODO da modificare con comunicazione con altre interfacce
                            sessioniCorrenti.getLast().creaNuovaSessioneDiGioco(new Tavolo(0, Gioco.Blackjack,
                                    0));
                            JOptionPane.showMessageDialog(null,
                                    "registrazione avvenuta con successo");
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
        usernameLabel.setText(sessioniCorrenti.get(currentHand).getClienteUsername());
        usernameLabel.setVisible(true);

        controller.serviCarte();

        ClientWelcomeController sessioneCorrente = sessioniCorrenti.get(0);

        vediCarteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                disegnaCarte();
                vediCarteButton.setVisible(false);
                azioniButton(true);

                SpinnerNumberModel modelloSpinnerPuntata = new SpinnerNumberModel(controller.getPuntataAttuale(),
                        controller.getPuntataAttuale(), sessioneCorrente.getSaldoGiocatore(), 1);
                spinnerPuntata.setModel(modelloSpinnerPuntata);
            }
        });
        puntaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(confermaButton.isVisible()) relativiRilancia(false);
                else relativiRilancia(true);
            }
        });
        confermaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int input = (int) spinnerPuntata.getValue();
                if(!decrementa(input, currentHand)) return;

                controller.getMano(currentHand).setPuntata(input);

                currentHand++;
                pescataIniziale();
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
        puntaButton.setVisible(visibilità);
        checkButton.setVisible(visibilità);
        foldButton.setVisible(visibilità);
        usernameLabel.setVisible(visibilità);
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

    //pulisci action listener
    public void rimuoviActionListener(JButton pulsante)
    {
        for (ActionListener i : pulsante.getActionListeners()) {
            pulsante.removeActionListener(i);
        }
    }

    public boolean decrementa(int input, int indice)
    {
        ClientWelcomeController sessioneCorrente = sessioniCorrenti.get(indice);
        try {
            sessioneCorrente.decrementaSaldoGiocatore(input);
            saldo.setText("saldo: " + sessioneCorrente.getSaldoGiocatore());

            return true;
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(),
                    "errore", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}
