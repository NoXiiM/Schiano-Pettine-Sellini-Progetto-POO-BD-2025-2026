package gui;

import controller.WelcomeController;

import javax.swing.*;

public class mainMenuAdmin {
    private JPanel AdminMainMenu;
    private JLabel usernameInMainMenu;
    private JButton gestisciUtenti;
    private JButton gestisciFondiButton;
    private JButton controllaUtenti;
    private JButton controllaPartite;

    private JFrame frameChiamante;
    private WelcomeController controller;


    public mainMenuAdmin(WelcomeController welcomeController, JFrame mainframe, String username) {
        this.controller = welcomeController;
        this.frameChiamante = mainframe;

        JFrame frameChiamato= new JFrame("mainMenuAdmin");
        frameChiamato.setContentPane(AdminMainMenu);
        frameChiamato.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameChiamato.setVisible(true);
        frameChiamato.pack();

        frameChiamante.setVisible(false);
        usernameInMainMenu.setText("User: " + username);
    }

}
