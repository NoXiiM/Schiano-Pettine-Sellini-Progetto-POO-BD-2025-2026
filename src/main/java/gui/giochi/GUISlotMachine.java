package gui.giochi;

import controller.ClienteCorrente;
import controller.slotMachine.slotMachineController;

import model.giochi.NonCarte.Simboli;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

    private slotMachineController controller;
    private float saldoGiocatore;

    //comunicazione tra frame
    private static JFrame thisFrame;

    private JFrame frameChiamante;
    private ClienteCorrente clienteCorrente;

    public GUISlotMachine(JFrame frameChiamante, ClienteCorrente clienteCorrente) {
        //settaggio frame

        thisFrame = new JFrame("GUISlotMachine");
        thisFrame.setContentPane(slotMachinePanel);
        thisFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        thisFrame.pack();
        thisFrame.setVisible(true);
        //Setto una immagine icona della finestra
        ImageIcon iconaSlotMachine = new ImageIcon(getClass().getResource("/IconeSlotmachin/SfondoSlotMachine.png"));
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
        /// istanziazione controller
        this.clienteCorrente = clienteCorrente;
        controller = new slotMachineController(clienteCorrente);
        /// settaggio foto
        Image img = new ImageIcon(
                getClass().getResource(controller.getPathSette())
        ).getImage().getScaledInstance(75, 75, Image.SCALE_SMOOTH);
        simbolo1.setIcon(new ImageIcon(img));
        simbolo1.setText("");
        simbolo2.setIcon(new ImageIcon(img));
        simbolo2.setText("");
        simbolo3.setIcon(new ImageIcon(img));
        simbolo3.setText("");

        // recupero saldo giocatore
        saldoGiocatore=controller.getsaldoGiocatore();
        saldoGiocatoreNumber.setText("Il saldo del giocatore è: "+saldoGiocatore);

        //default di guadagno
        guadagnatoText.setText("");

        //Avvio timer
        controller.startTimer();

        spinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    //Cancella dal cliente e a schermo quanto puntato
                    controller.decrementa(Integer.parseInt(puntate.getSelection().getActionCommand()));
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
                            getClass().getResource(controller.getCollegamento(colonna1))
                    ).getImage().getScaledInstance(75, 75, Image.SCALE_SMOOTH);
                    Image img2 = new ImageIcon(
                            getClass().getResource(controller.getCollegamento(colonna2))
                    ).getImage().getScaledInstance(75, 75, Image.SCALE_SMOOTH);
                    Image img3 = new ImageIcon(
                            getClass().getResource(controller.getCollegamento(colonna3))
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
                        controller.incrementa(creditoRisultato);
                        controller.aggiornaVincitaPercentuale(true);
                        guadagnatoText.setText("Hai vinto: "+creditoRisultato+"!");
                        saldoGiocatoreNumber.setText("Il saldo del giocatore è: "+saldoGiocatore);



                    }
                    else{
                        guadagnatoText.setText("oh no hai perso! ");
                        controller.aggiornaVincitaPercentuale(false);
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
                controller.stopTimer();
                controller.aggiornaCliente();
                //Check della percentuale di vittoria o perdita
                System.out.println(controller.getCheckPercentualeVittoria());
                System.out.println(controller.getClienteCorrente().getClienteCorrente());
                thisFrame.dispose();
                frameChiamante.setVisible(true);
            }
        });
    }

}

