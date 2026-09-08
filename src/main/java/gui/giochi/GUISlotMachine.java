package gui.giochi;

import controller.gestionale.ClientWelcomeController;
import controller.slotMachine.SlotMachineController;

import model.giochi.NonCarte.Simboli;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.Objects;

/**
 *  Gui slot machine emula il funzionamento di una classica slot con diamanti e frutta come simboli.
 *  In alto a sinistra sono presenti i simboli restituiti dalla slot (Al primo avvio saranno sempre 3 sette);
 *  Al di sotto dei simboli sono presenti le possibili puntate che si possono inserire nella slot;
 *  In alto a destra è presente il saldo del giocatore;
 *  Sotto il saldo è presente il guadagno / perdita del giocatore che si aggiorna a ogni spin;
 *  Il pulsante spin fa iniziare lo spin della macchina.
 */
public class GUISlotMachine {
    private JPanel slotMachinePanel;
    private JLabel simbolo1;
    private JLabel simbolo2;
    private JLabel simbolo3;
    private JButton spinButton;
    private JRadioButton a10RadioButton;
    private JRadioButton a20RadioButton;
    private JRadioButton a50RadioButton;
    private JRadioButton a100RadioButton;
    private JRadioButton a200RadioButton;
    private JRadioButton a500RadioButton;
    private JRadioButton a1000RadioButton;
    private JRadioButton a2500RadioButton;
    private JRadioButton a5000RadioButton;
    private JLabel saldoGiocatoreNumber;
    private JLabel guadagnatoText;
    private JButton tornaIndietroButton;

    private final SlotMachineController controller;
    //TODO saldo giocatore può essere potenzialmente eliminato
    private float saldoGiocatore;

    /**
     * Instantiates a new Gui slot machine.
     *
     * @param frameChiamante   the frame chiamante
     * @param sessioneCorrente the sessione corrente
     */
    public GUISlotMachine(JFrame frameChiamante, ClientWelcomeController sessioneCorrente) {
        //settaggio frame

        JFrame thisFrame = new JFrame("GUISlotMachine");
        thisFrame.setContentPane(slotMachinePanel);
        thisFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        thisFrame.pack();
        thisFrame.setVisible(true);
        //Personalizzazione finestra
        Dimension dimensioniMinime = new Dimension(600,300);
        thisFrame.setMinimumSize(dimensioniMinime);
        thisFrame.setLocationRelativeTo(null);

        //Setto una immagine icona della finestra
        ImageIcon iconaSlotMachine = new ImageIcon(Objects.requireNonNull(getClass().getResource("/IconeSlotmachin/SfondoSlotMachine.png")));
        thisFrame.setIconImage(iconaSlotMachine.getImage().getScaledInstance(75, 75, Image.SCALE_SMOOTH));
        // Settaggio delle box scelta
        ButtonGroup puntate = new ButtonGroup();
        puntate.add(a10RadioButton);
        puntate.add(a20RadioButton);
        puntate.add(a50RadioButton);
        a10RadioButton.setActionCommand("10");
        a20RadioButton.setActionCommand("20");
        a50RadioButton.setActionCommand("50");

        puntate.add(a100RadioButton);
        puntate.add(a200RadioButton);
        puntate.add(a500RadioButton);
        a100RadioButton.setActionCommand("100");
        a200RadioButton.setActionCommand("200");
        a500RadioButton.setActionCommand("500");


        puntate.add(a1000RadioButton);
        puntate.add(a2500RadioButton);
        puntate.add(a5000RadioButton);
        a1000RadioButton.setActionCommand("1000");
        a2500RadioButton.setActionCommand("2500");
        a5000RadioButton.setActionCommand("5000");


        a10RadioButton.setSelected(true);
        controller = new SlotMachineController();
        //Settaggio foto
        Image img = new ImageIcon(
                Objects.requireNonNull(getClass().getResource(controller.getPathSette()))
        ).getImage().getScaledInstance(75, 75, Image.SCALE_SMOOTH);
        simbolo1.setIcon(new ImageIcon(img));
        simbolo1.setText("");
        simbolo2.setIcon(new ImageIcon(img));
        simbolo2.setText("");
        simbolo3.setIcon(new ImageIcon(img));
        simbolo3.setText("");

        // recupero saldo giocatore
        saldoGiocatore= sessioneCorrente.getSaldoGiocatore();
        saldoGiocatoreNumber.setText("Il saldo del giocatore è: "+saldoGiocatore);

        //default di guadagno
        guadagnatoText.setText("");


        spinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    //Cancella dal cliente e a schermo quanto puntato
                    sessioneCorrente.decrementaSaldoGiocatore(Integer.parseInt(puntate.getSelection().getActionCommand()));
                    saldoGiocatore = saldoGiocatore - Integer.parseInt(puntate.getSelection().getActionCommand());

                    int creditoRisultato;
                    Simboli colonna1, colonna2, colonna3;

                    //otteniamo i simboli della partita
                    colonna1=controller.getSimboloCasuale();
                    colonna2=controller.getSimboloCasuale();
                    colonna3=controller.getSimboloCasuale();

                    //calcoliamo il risultato della partita
                    creditoRisultato = controller.getsaldopartita(colonna1,colonna2,colonna3,Integer.parseInt(puntate.getSelection().getActionCommand()));

                    //recuperiamo le foto per i simboli
                    Image img1 = new ImageIcon(
                            Objects.requireNonNull(getClass().getResource(controller.getCollegamento(colonna1)))
                    ).getImage().getScaledInstance(75, 75, Image.SCALE_SMOOTH);
                    Image img2 = new ImageIcon(
                            Objects.requireNonNull(getClass().getResource(controller.getCollegamento(colonna2)))
                    ).getImage().getScaledInstance(75, 75, Image.SCALE_SMOOTH);
                    Image img3 = new ImageIcon(
                            Objects.requireNonNull(getClass().getResource(controller.getCollegamento(colonna3)))
                    ).getImage().getScaledInstance(75, 75, Image.SCALE_SMOOTH);

                    //aggiorniamo i simboli
                    simbolo1.setIcon(new ImageIcon(img1));
                    simbolo1.setText("");
                    simbolo2.setIcon(new ImageIcon(img2));
                    simbolo2.setText("");
                    simbolo3.setIcon(new ImageIcon(img3));
                    simbolo3.setText("");

                    //Mostriamo a schermo l'esito della partita
                    //Aggiorniamo il saldo giocatore

                    if(creditoRisultato>0){
                        saldoGiocatore = saldoGiocatore+creditoRisultato;
                        sessioneCorrente.incrementaSaldoGiocatore(creditoRisultato);
                        sessioneCorrente.aggiornaVincitaPercentuale(true);
                        guadagnatoText.setText("Hai vinto: "+creditoRisultato+"!");
                        saldoGiocatoreNumber.setText("Il saldo del giocatore è: "+saldoGiocatore);
                    }
                    else{
                        guadagnatoText.setText("oh no hai perso! ");
                        sessioneCorrente.aggiornaVincitaPercentuale(false);
                        saldoGiocatoreNumber.setText("Il saldo del giocatore è: "+saldoGiocatore);
                    }


                }
                catch(RuntimeException ex){
                    JOptionPane.showMessageDialog(null,ex.getMessage(),"errore", JOptionPane.ERROR_MESSAGE);

                }

            }
        });
        tornaIndietroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    sessioneCorrente.terminaSessione();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
                }

                thisFrame.dispose();
                frameChiamante.setVisible(true);
            }
        });
        thisFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    sessioneCorrente.terminaSessione();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
                }

                thisFrame.dispose();
                frameChiamante.setVisible(true);
            }
        });
    }

}

