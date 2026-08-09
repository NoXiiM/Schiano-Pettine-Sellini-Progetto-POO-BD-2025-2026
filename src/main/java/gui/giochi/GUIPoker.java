package gui.giochi;

import controller.gestionale.ClientWelcomeController;
import controller.poker.ControllerPoker;
import model.gestionale.Gioco;
import model.gestionale.Tavolo;
import model.gestionale.utenteEFigli.Cliente;
import model.giochi.ManoPoker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
    private JLabel pot;
    private JButton rimischiaButton;
    private JButton okButton;

    //conta qual è la mano corrente
    private int currentHand = 0;

    private ControllerPoker controller;
    private ClientWelcomeController sessioneCorrente;

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
        rimischiaButton.setVisible(false);
        okButton.setVisible(false);

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

                int ante = (int) spinnerAnte.getValue();
                controller.setAnte(ante);

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
        for(int i = 0; i < sessioniCorrenti.size(); i++)
        {
            if(!decrementa(controller.getAnte(), i))
            {
                controller.setFolded(i);

                if(i == currentHand) currentHand++;
            }
        }
        //TODO uscita se rimane solo un giocatore che può giocare
        controller.resetPot();
        aggiornaPot(controller.getAnte()*sessioniCorrenti.size());
        rimuoviActionListener(vediCarteButton);
        rimuoviActionListener(puntaButton);
        rimuoviActionListener(confermaButton);
        rimuoviActionListener(checkButton);
        rimuoviActionListener(foldButton);

        startingButton(false);
        vediCarteButton.setVisible(true);
        usernameLabel.setText(sessioniCorrenti.get(currentHand).getClienteUsername());
        usernameLabel.setVisible(true);
        saldo.setVisible(false);

        controller.serviCarte();

        sessioneCorrente = sessioniCorrenti.get(currentHand);

        vediCarteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mano.removeAll();
                disegnaCarte();
                refreshPanel(mano);
                vediCarteButton.setVisible(false);
                azioniButton(true);
                saldo.setVisible(true);
                saldo.setText("saldo: " + sessioneCorrente.getSaldoGiocatore());

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
                aggiornaPot(input);
                controller.setPuntataAttuale(input);

                nextHand1();

                prossimoPescata();
            }
        });

        checkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int input = controller.getPuntataAttuale();

                if(!decrementa(input, currentHand)) return;

                controller.getMano(currentHand).setPuntata(input);
                aggiornaPot(input);
                pot.setText("pot: " + controller.getPot());

                nextHand1();

                prossimoPescata();
            }
        });

        foldButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                vittoriaPerFold(controller.setFolded(currentHand));

                nextHand1();

                prossimoPescata();
            }
        });
    }

    //[2]
    public void rimischiata()
    {
        //System.out.println("sono qui");
        rimuoviActionListener(vediCarteButton);
        rimuoviActionListener(rimischiaButton);
        rimuoviActionListener(okButton);

        resetCurrentHand();
        usernameLabel.setText(sessioniCorrenti.get(currentHand).getClienteUsername());
        sessioneCorrente = sessioniCorrenti.get(currentHand);

        vediCarteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mano.removeAll();
                disegnaCarteClickabili();
                refreshPanel(mano);

                vediCarteButton.setVisible(false);
                saldo.setVisible(true);
                saldo.setText("saldo: " + sessioneCorrente.getSaldoGiocatore());

                rimischiaButton.setVisible(true);
            }
        });
        rimischiaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.rimischiataMano(currentHand);

                mano.removeAll();
                disegnaCarte();
                refreshPanel(mano);

                rimischiaButton.setVisible(false);
                okButton.setVisible(true);
            }
        });
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                okButton.setVisible(false);
                vediCarteButton.setVisible(true);
                mano.removeAll();

                nextHand2();
            }
        });
    }

    //[3]
    public void puntata2()
    {

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
        //usernameLabel.setVisible(visibilità);
    }

    private void relativiRilancia(boolean visibilità)
    {
        spinnerPuntata.setVisible(visibilità);
        confermaButton.setVisible(visibilità);
    }

    //macro
    private void prossimoPescata()
    {
        azioniButton(false);
        confermaButton.setVisible(false);
        spinnerPuntata.setVisible(false);
        vediCarteButton.setVisible(true);
        saldo.setVisible(false);


        mano.removeAll();
        refreshPanel(mano);
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

    private void disegnaCarteClickabili()
    {
        String pathIm;
        JLabel temp;

        for(int i = 0; i < 5; i++)
        {
            pathIm = controller.displayCard(currentHand, i);
            temp = new JLabel(new ImageIcon(Objects.requireNonNull(getClass().getResource(pathIm))));

            temp.setCursor(new Cursor(Cursor.HAND_CURSOR));

            //per passare variabile esterna in un listener deve essere final
            final int indexCarta = i;

            //TODO vedi se c'è un modo più elegante a livello di pattern per creare questi listener
            temp.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    JLabel cartaCliccata = (JLabel) e.getSource(); //restituisce la Jlabel clickata

                    ManoPoker manoTemp = (ManoPoker) controller.getMano(currentHand);

                    ArrayList<Integer> indexList = manoTemp.getCarteSelezionate();

                    if(indexList.contains(indexCarta))
                    {
                        //set border per creare o togliere bordo intorno alla carta
                        cartaCliccata.setBorder(null);

                        indexList.remove((Object) indexCarta);
                    }
                    else
                    {
                        if(indexList.size() >= 4)
                        {
                            JOptionPane.showMessageDialog(null, "non puoi rimischiare tutta la mano",
                                    "errore", JOptionPane.ERROR_MESSAGE);

                            return;
                        }

                        cartaCliccata.setBorder(BorderFactory.createLineBorder(Color.RED, 3));

                        indexList.add(indexCarta);
                    }

                    manoTemp.setCarteSelezionate(indexList);
                }
            });

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

    //transazioni
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

    //aggiornamenti
    //gestione cambio player
    public void nextHand1()
    {
        do {
            currentHand++;
            if(currentHand >= sessioniCorrenti.size())
            {
                rimischiata();
                return;
            }
        }while(controller.getFolded(currentHand));

        if(controller.getPuntataAttuale() != 0)
        {
            checkButton.setText("call");
            puntaButton.setText("rilancia");
        }
        if(currentHand == sessioniCorrenti.size())
        {
            checkButton.setText("check");
            puntaButton.setText("punta");
        }

        usernameLabel.setText(sessioniCorrenti.get(currentHand).getClienteUsername());
        sessioneCorrente = sessioniCorrenti.get(currentHand);
    }

    public void nextHand2()
    {
        do {
            currentHand++;
            if(currentHand >= sessioniCorrenti.size())
            {
                puntata2();
                return;
            }
        }while(controller.getFolded(currentHand));

        usernameLabel.setText(sessioniCorrenti.get(currentHand).getClienteUsername());
        sessioneCorrente = sessioniCorrenti.get(currentHand);
    }

    //aggiorna pot
    public void aggiornaPot(int valore)
    {
        controller.incrementaPot(valore);
        pot.setText("pot: " + controller.getPot());
    }

    public void resetCurrentHand()
    {
        currentHand = 0;

        while(controller.getFolded(currentHand)) currentHand++;
    }

    public void vittoriaPerFold(Integer indexVincitore)
    {
        if(indexVincitore == null) return;

        sessioneCorrente = sessioniCorrenti.get(indexVincitore);

        sessioneCorrente.incrementaSaldoGiocatore(controller.getPot());

        JOptionPane.showMessageDialog(null, "il giocatore " + sessioneCorrente.getClienteUsername() +
                " vince perchè gli altri hanno foldato");

        pescataIniziale();
    }
}
