package gui.gestionale;

import controller.DipendenteCorrente;
import controller.WelcomeController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenuAdmin {
    private JPanel AdminMainMenu;
    private JLabel usernameInMainMenu;
    private JButton gestisciUtenti;
    private JButton gestisciFondiButton;
    private JButton indietroButton;
    private JButton visualizzaUtentiButton;
    private JButton controllaUtenti;
    private JButton controllaPartite;

    private static JFrame thisFrame;
    private JFrame frameChiamante;
    private WelcomeController controller;
    private DipendenteCorrente currentWorker;


    public MainMenuAdmin(WelcomeController welcomeController, JFrame mainframe, DipendenteCorrente currentWorker) {
        this.controller = welcomeController;
        this.frameChiamante = mainframe;

        thisFrame = new JFrame("mainMenuAdmin");
        thisFrame.setContentPane(AdminMainMenu);
        thisFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        thisFrame.setVisible(true);
        thisFrame.pack();

        frameChiamante.setVisible(false);
        usernameInMainMenu.setText("User: " + currentWorker.getUsername());

        gestisciUtenti.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        indietroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameChiamante.setVisible(true);
                thisFrame.dispose();
            }
        });
        visualizzaUtentiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

}
