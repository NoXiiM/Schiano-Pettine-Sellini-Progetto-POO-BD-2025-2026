package gui.gestionale;

import controller.gestionale.DipendenteWelcomeController;

import javax.swing.*;

public class DealerPanel {
    private JPanel dealer;
    private JTabbedPane dealerPanel;
    private JTextArea textAreaSessioni;
    private JList listaSessioni;
    private JButton cercaButton;
    private JButton logoutButton;
    private JButton aggiornaListaButton;
    private JButton attivaSospettoButton;
    private JPanel gestioneAccountPanel;
    private JButton cambiaPasswordButton;
    private JButton cambiaUsernameButton;
    private JButton resettaPasswordButton;
    private JLabel usernameSessionePanel;
    private JPanel ricercaPanel;

    private DipendenteWelcomeController controller;
    private JFrame frameChiamante;

    public DealerPanel(DipendenteWelcomeController controller, JFrame frameChiamante)
    {
        JFrame frameChiamato = new JFrame("dealerPanel");
        frameChiamato.setContentPane(dealer);
        frameChiamato.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameChiamato.pack();
        frameChiamato.setVisible(true);

        this.controller = controller;
        this.frameChiamante = frameChiamante;
    }
}
