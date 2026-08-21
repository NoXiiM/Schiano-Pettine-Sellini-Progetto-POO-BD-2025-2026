package gui.gestionale;

import model.gestionale.Sessione;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class VisualizzatoreSessioni
{
    private JList listaSessioni;
    private JPanel visualizzatoreSessioniPanel;
    private JButton indietroButton;
    private JLabel clienteSessioni;

    private static DefaultListModel<Sessione> modelloListaSessioni;

    public VisualizzatoreSessioni(ArrayList<Sessione> sessioni, String user)
    {
        JFrame thisFrame = new JFrame("VisualizzatoreSessioni");
        thisFrame.setContentPane(visualizzatoreSessioniPanel);
        thisFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        thisFrame.pack();
        thisFrame.setVisible(true);

        clienteSessioni.setText("Sessioni svolte da " + user + ":");

        modelloListaSessioni = new DefaultListModel<>();
        modelloListaSessioni.addAll(sessioni);
        listaSessioni.setModel(modelloListaSessioni);

        indietroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                thisFrame.dispose();
            }
        });
    }
}
