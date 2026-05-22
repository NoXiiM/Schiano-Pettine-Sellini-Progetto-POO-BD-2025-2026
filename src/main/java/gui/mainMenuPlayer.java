package gui;

import controller.WelcomeController;

import javax.swing.*;

public class mainMenuPlayer {
    private JPanel PlayerMainMenu;
    private JLabel usernameInMainMenu;
    private JButton iniziaPartitaButton;
    private JButton gestisciFondiButton;

    private JFrame frameChiamante;
    private WelcomeController controller;


    public mainMenuPlayer(WelcomeController welcomeController, JFrame mainframe, String username) {
        this.controller = welcomeController;
        this.frameChiamante = mainframe;

        JFrame frameChiamato= new JFrame("mainMenuPlayer");
        frameChiamato.setContentPane(PlayerMainMenu);
        frameChiamato.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameChiamato.setVisible(true);
        frameChiamato.pack();

        frameChiamante.setVisible(false);
        usernameInMainMenu.setText("User: " + username);
    }

}
