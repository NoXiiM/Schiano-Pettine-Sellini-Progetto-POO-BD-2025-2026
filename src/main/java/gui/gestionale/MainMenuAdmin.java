package gui.gestionale;

import controller.gestionale.DipendenteWelcomeController;
import controller.gestionale.WelcomeController;
import database.implementazioneDAO.ImpDAOopd;
import model.gestionale.Gioco;
import model.gestionale.Tavolo;
import model.gestionale.utenteEFigli.Cliente;
import model.gestionale.utenteEFigli.Dealer;
import model.gestionale.utenteEFigli.Dipendente;
import model.gestionale.utenteEFigli.Supervisore;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

//manca: parte di gestione tavoli e gestione account

public class MainMenuAdmin {
    private JPanel AdminPanel;
    private JTabbedPane tabbedPane1;
    private JList listaClienti;
    private JButton bannaButton;
    private JTextArea textAreaInfoFieldClienti;
    private JButton logoutDaClienti;
    private JLabel userFieldAdmin;
    private JButton cercaButton;
    private JTextField textFieldNome;
    private JTextField textFieldCognome;
    private JTextField textFieldUsername;
    private JCheckBox siCheckBoxSospetto;
    private JCheckBox noCheckBoxSospetto;
    private JCheckBox siCheckBoxBan;
    private JCheckBox noCheckBoxBan;
    private JScrollPane jScrollPaneJlist;
    private JScrollPane jScrollPaneInfoClienti;
    private JLabel nomeText;
    private JLabel cognomeText;
    private JPanel jpanelRicerca;
    private JLabel usernameText;
    private JLabel saldoTextMin;
    private JLabel percentualVincitaTextMin;
    private JLabel partiteGiocateTextMin;
    private JSpinner spinnerSaldoMin;
    private JSpinner spinnerSaldoMax;
    private JSpinner spinnerPercMin;
    private JSpinner spinnerPercMax;
    private JSpinner spinnerPartMin;
    private JSpinner spinnerPartMax;
    private JCheckBox controllaSaldoCheckBox;
    private JCheckBox controllaPercentualeCheckBox;
    private JCheckBox controllaPartiteCheckBox;
    private JButton aggiornaButton;
    private JPanel gestioneClienti;
    private JPanel gestioneDipendenti;
    private JButton aggiornaDipendenti;
    private JButton logoutDaDipendenti;
    private JButton licenziaDipendenti;
    private JButton cercaDipendenti;
    private JButton aggiungiDipendenti;
    private JList listaDipendenti;
    private JTextArea textAreaInfoDipendenti;
    private JList listaTavoli;
    private JTextArea textAreaInfoTavoli;
    private JButton aggiornaTavoli;
    private JButton logoutDaTavoli;
    private JButton assegnaTavoloButton;
    private JButton aggiungiGiocoButton;
    private JButton filtraButton;
    private JButton modificaGiochiButton;
    private JLabel saldoTextMax;
    private JLabel percentualVincitaTextMax;
    private JLabel partiteGiocateTextMax;
    private JCheckBox filtraPerRuoloCheckBox;
    private JRadioButton supervisoreRadioButton;
    private JRadioButton dealerRadioButton;
    private JCheckBox filtraPerSospettiCheckBox;
    private JCheckBox filtraPerBanCheckBox;
    private JRadioButton siSospettoRadio;
    private JRadioButton noSospettoRadio;
    private JRadioButton siBanRadio;
    private JRadioButton noBanRadio;
    private JCheckBox filtraPerGiocoCheckBox;
    private JRadioButton pokerRadio;
    private JRadioButton blackjackRadio;
    private JTextField nomeDipendenteField;
    private JTextField cognomeDipendenteField;
    private JTextField usernameDipendenteField;
    private JPanel gestioneTavoli;
    private JPanel gestioneAccountPanel;
    private JButton cambiaPasswordButton;
    private JButton cambiaUsernameButton;
    private JButton resettaPasswordButton;
    private JLabel userDipendenteLabel;
    private JLabel userClientiLabel;
    private JLabel userTavoliLabel;
    private JButton visualizzaSessioniCliente;
    private JButton aggiungiTavolo;
    private JButton rimuoviDipDaTavoloButton;
    private JButton rimuoviTavolo;

    private JFrame thisFrame;
    private JFrame frameChiamante;
    private DipendenteWelcomeController dipendenteController;

    private static DefaultListModel<Cliente> modelloListaClienti;
    private static DefaultListModel<Dipendente> modelloListaDipendente;
    private static DefaultListModel<Tavolo> modelloListaTavoli;

    public MainMenuAdmin(DipendenteWelcomeController controller, JFrame frameChiamante) {
        dipendenteController = controller;
        this.frameChiamante= frameChiamante;

        modelloListaClienti= new DefaultListModel<>();
        try {
            modelloListaClienti.addAll(dipendenteController.getListaClientiDB());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "errore", JOptionPane.ERROR_MESSAGE);
        }
        listaClienti.setModel(modelloListaClienti);

        modelloListaDipendente= new DefaultListModel<>();
        try {
            modelloListaDipendente.addAll(dipendenteController.getDipendentiDB());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "errore", JOptionPane.ERROR_MESSAGE);
        }
        listaDipendenti.setModel(modelloListaDipendente);

        modelloListaTavoli = new DefaultListModel<>();
        try {
            modelloListaTavoli.addAll(dipendenteController.getTavoliDB());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "errore", JOptionPane.ERROR_MESSAGE);
        }
        listaTavoli.setModel(modelloListaTavoli);

        String usr = dipendenteController.getUserUtente();
        userDipendenteLabel.setText(usr);
        userClientiLabel.setText(usr);
        userTavoliLabel.setText(usr);

        modificaGiochiButton.setVisible(false);
        assegnaTavoloButton.setVisible(false);
        rimuoviDipDaTavoloButton.setVisible(false);

        thisFrame = new JFrame("MainMenuAdmin");
        thisFrame.setContentPane(AdminPanel);
        thisFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        thisFrame.pack();
        thisFrame.setVisible(true);
        frameChiamante.setVisible(false);

        textAreaInfoFieldClienti.setEditable(false);
        textAreaInfoFieldClienti.setFocusable(false);
        textAreaInfoDipendenti.setEditable(false);
        textAreaInfoDipendenti.setFocusable(false);
        textAreaInfoTavoli.setEditable(false);
        textAreaInfoTavoli.setFocusable(false);

        inizializzaMenuAdmin();

        //[1] Clienti
        bannaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Cliente temp= (Cliente) listaClienti.getSelectedValue();

                if(temp != null) {
                    String input = JOptionPane.showInputDialog(null, "Inserisci motivo ban:", "Ban utente", JOptionPane.QUESTION_MESSAGE);

                    if (input != null && !input.isBlank()) {

                        temp.creaBan(input);

                        stampaClienteInfoField(temp);

                        ImpDAOopd db = new ImpDAOopd();

                        try {
                            db.salvataggioBan(temp.getCodiceTesseraGiocatore(), temp.getDataBan(), temp.getMotivoBan());
                            JOptionPane.showMessageDialog(null, "ban registrato con successo");
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(null, "errore salvataggio ban",
                                    "errore", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Non hai inserito un motivo di ban", "Errore", JOptionPane.ERROR_MESSAGE);
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "Cliente non selezionato", "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        visualizzaSessioniCliente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Cliente temp = (Cliente) listaClienti.getSelectedValue();

                if(temp != null)
                {
                    try {
                        new VisualizzatoreSessioni(dipendenteController.visualizzaSessioni(temp.getCodiceTesseraGiocatore()),
                                ((Cliente) listaClienti.getSelectedValue()).getUsername());
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage(), "errore", JOptionPane.ERROR_MESSAGE);
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "Cliente non selezionato", "errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        listaClienti.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {

                Cliente temp= (Cliente) listaClienti.getSelectedValue();
                if(temp != null){
                    stampaClienteInfoField(temp);
                }
                else
                {
                    textAreaInfoFieldClienti.setText(null);
                }
            }
        });

        logoutDaClienti.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logoutAdmin();
            }
        });

        cercaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String nomeRicerca = textFieldNome.getText();
                String cognomeRicerca = textFieldCognome.getText();
                String usernameRicerca = textFieldUsername.getText();

                int saldoMin= (int) spinnerSaldoMin.getValue();
                int saldoMax = (int) spinnerSaldoMax.getValue();

                int percMin= (int) spinnerPercMin.getValue();
                int percMax= (int) spinnerPercMax.getValue();

                int partiteMin= (int) spinnerPartMin.getValue();
                int partiteMax= (int) spinnerPartMax.getValue();

                boolean checkSaldo= controllaSaldoCheckBox.isSelected();
                boolean checkPercentuale= controllaPercentualeCheckBox.isSelected();
                boolean checkPartite= controllaPartiteCheckBox.isSelected();

                String sospettoRicerca = "indifferente";
                if(filtraPerSospettiCheckBox.isSelected()) {

                    if(siSospettoRadio.isSelected()){
                        sospettoRicerca= "si";

                    } else if(noSospettoRadio.isSelected()){
                        sospettoRicerca= "no";
                    }

                }
//                else {
//                    sospettoRicerca= "indifferente";
//                }

                String banRicerca= "indifferente";
                if(filtraPerBanCheckBox.isSelected()) {

                    if(siBanRadio.isSelected()){
                        banRicerca= "si";

                    } else if (noBanRadio.isSelected()){
                        banRicerca= "no";
                    }

                }
//                else {
//                    banRicerca= "indifferente";
//                }

                modelloListaClienti.clear();

                modelloListaClienti.addAll(dipendenteController.ricercaClienti(nomeRicerca, cognomeRicerca, usernameRicerca,
                        saldoMin, saldoMax, percMin, percMax, partiteMin, partiteMax, sospettoRicerca, banRicerca,
                        checkSaldo, checkPartite, checkPercentuale));
            }
        });

        //il pulsante aggiorna carica nuovamente i clienti dal database nell'ipotetica eventualità che 2 supervisori
        //stiano modificando i clienti in contemporanea (cosa che in realtà non può succedere per come è concepito il sistema
        //attualmente)
        aggiornaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modelloListaClienti.clear();
                try {
                    modelloListaClienti.addAll(dipendenteController.getListaClientiDB());
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        controllaSaldoCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(controllaSaldoCheckBox.isSelected()){
                    saldoTextMin.setVisible(true);
                    saldoTextMax.setVisible(true);
                    spinnerSaldoMin.setVisible(true);
                    spinnerSaldoMax.setVisible(true);

                } else {
                    saldoTextMin.setVisible(false);
                    saldoTextMax.setVisible(false);
                    spinnerSaldoMin.setVisible(false);
                    spinnerSaldoMax.setVisible(false);
                }
            }
        });

        controllaPercentualeCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(controllaPercentualeCheckBox.isSelected()){
                        percentualVincitaTextMax.setVisible(true);
                        percentualVincitaTextMin.setVisible(true);
                        spinnerPercMin.setVisible(true);
                        spinnerPercMax.setVisible(true);

                } else {
                        percentualVincitaTextMax.setVisible(false);
                        percentualVincitaTextMin.setVisible(false);
                        spinnerPercMin.setVisible(false);
                        spinnerPercMax.setVisible(false);
                }
            }
        });
        controllaPartiteCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(controllaPartiteCheckBox.isSelected()){

                    partiteGiocateTextMin.setVisible(true);
                    partiteGiocateTextMax.setVisible(true);
                    spinnerPartMin.setVisible(true);
                    spinnerPartMax.setVisible(true);
                } else {
                    partiteGiocateTextMin.setVisible(false);
                    partiteGiocateTextMax.setVisible(false);
                    spinnerPartMin.setVisible(false);
                    spinnerPartMax.setVisible(false);
                }
            }
        });
        filtraPerSospettiCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(filtraPerSospettiCheckBox.isSelected()){
                    siSospettoRadio.setVisible(true);
                    noSospettoRadio.setVisible(true);

                } else {
                    siSospettoRadio.setVisible(false);
                    noSospettoRadio.setVisible(false);
                }
            }
        });
        filtraPerBanCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(filtraPerBanCheckBox.isSelected()){
                    siBanRadio.setVisible(true);
                    noBanRadio.setVisible(true);

                } else {
                    siBanRadio.setVisible(false);
                    noBanRadio.setVisible(false);
                }
            }
        });

        //[2] Dipendenti
        filtraPerRuoloCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(filtraPerRuoloCheckBox.isSelected()){
                    supervisoreRadioButton.setVisible(true);
                    dealerRadioButton.setVisible(true);

                } else {
                    supervisoreRadioButton.setVisible(false);
                    dealerRadioButton.setVisible(false);
                }
            }
        });

        logoutDaDipendenti.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logoutAdmin();
            }
        });

        aggiungiDipendenti.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RegistrationDipendente(thisFrame, dipendenteController);

            }
        });

        listaDipendenti.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {

                Dipendente temp= (Dipendente) listaDipendenti.getSelectedValue();

                if(temp != null){
                    stampaDipendenteInfoField(temp);
                }
                else
                {
                    textAreaInfoDipendenti.setText(null);
                }

                if(dipendenteController.isDealer(temp)) aggiungiGiocoButton.setVisible(true);
                else aggiungiGiocoButton.setVisible(false);
            }
        });

        //il pulsante aggiorna carica nuovamente i dipendenti dal database nell'ipotetica eventualità che 2 supervisori
        //stiano modificando i clienti in contemporanea
        aggiornaDipendenti.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {;
                modelloListaDipendente.clear();
                try {
                    modelloListaDipendente.addAll(dipendenteController.getDipendentiDB());
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        cercaDipendenti.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String nome = nomeDipendenteField.getText();
                String cognome = cognomeDipendenteField.getText();
                String username = usernameDipendenteField.getText();
                boolean checkRuolo = filtraPerRuoloCheckBox.isSelected();
                String ruoloSelezionato ="";
                if(dealerRadioButton.isSelected()||supervisoreRadioButton.isSelected()){
                    ruoloSelezionato =(dealerRadioButton.isSelected() ? "Dealer" : "Supervisore");
                }
                modelloListaDipendente.clear();
                modelloListaDipendente.addAll(dipendenteController.ricercaDipendente(nome,cognome, username, checkRuolo, ruoloSelezionato));
            }
        });

        licenziaDipendenti.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Dipendente temp = (Dipendente) listaDipendenti.getSelectedValue();

                if (temp != null) {
                    if(temp instanceof Dealer){
                        JPasswordField passwordField = new JPasswordField();

                        Object[] messaggio = {
                                "Inserisci la password per confermare il licenziamento:",
                                passwordField
                        };

                        int risposta = JOptionPane.showConfirmDialog(
                                null,
                                messaggio,
                                "Conferma licenziamento",
                                JOptionPane.OK_CANCEL_OPTION,
                                JOptionPane.WARNING_MESSAGE
                        );

                        if (risposta == JOptionPane.OK_OPTION) {
                            String password = new String(passwordField.getPassword());

                            // Controlla la password
                            if(password.equals(dipendenteController.getCurrentUser().getPassword())) {
                                try {
                                    dipendenteController.licenziaDipendente(temp);
                                } catch (SQLException ex) {
                                    JOptionPane.showMessageDialog(null, ex.getMessage(),
                                            "errore", JOptionPane.ERROR_MESSAGE);
                                }
                            } else {
                                JOptionPane.showMessageDialog(
                                        null,
                                        "Password non corretta!",
                                        "Errore",
                                        JOptionPane.ERROR_MESSAGE
                                );
                            }
                        }
                    }else{
                        if(dipendenteController.getCurrentUser().getUsername().equals("root")){
                            if(!temp.getUsername().equals("root")) {
                                JPasswordField passwordField = new JPasswordField();

                                Object[] messaggio = {
                                        "Inserisci la password per confermare il licenziamento:",
                                        passwordField
                                };

                                int risposta = JOptionPane.showConfirmDialog(
                                        null,
                                        messaggio,
                                        "Conferma licenziamento",
                                        JOptionPane.OK_CANCEL_OPTION,
                                        JOptionPane.WARNING_MESSAGE
                                );

                                if (risposta == JOptionPane.OK_OPTION) {
                                    String password = new String(passwordField.getPassword());

                                    // Controlla la password
                                    if (password.equals(dipendenteController.getCurrentUser().getPassword())) {
                                        try {
                                            dipendenteController.licenziaDipendente(temp);
                                        } catch (SQLException ex) {
                                            JOptionPane.showMessageDialog(null, ex.getMessage(),
                                                    "errore", JOptionPane.ERROR_MESSAGE);
                                        }
                                    } else {
                                        JOptionPane.showMessageDialog(
                                                null,
                                                "Password non corretta!",
                                                "Errore",
                                                JOptionPane.ERROR_MESSAGE
                                        );
                                    }
                                }
                            }else{
                                JOptionPane.showMessageDialog(null,"Non puoi cancellare te stesso","Errore",JOptionPane.ERROR_MESSAGE);
                            }
                        }else{
                            JOptionPane.showMessageDialog(null,"Non hai i permessi per cancellare un Supervisore","Errore",JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    modelloListaDipendente.clear();
                    modelloListaDipendente.addAll(dipendenteController.getDipendentiInLocale());
                } else {
                    JOptionPane.showMessageDialog(null,"Nessun dipendente selezionato","Errore",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        aggiungiGiocoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AggiungiGiocoDealer(dipendenteController, thisFrame, (Dealer) listaDipendenti.getSelectedValue());
            }
        });

        //[3] Tavoli
        logoutDaTavoli.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logoutAdmin();
            }
        });

        filtraPerGiocoCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(filtraPerGiocoCheckBox.isSelected()){
                    pokerRadio.setVisible(true);
                    blackjackRadio.setVisible(true);

                } else {
                    pokerRadio.setVisible(false);
                    blackjackRadio.setVisible(false);
                }
            }
        });

        aggiornaTavoli.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modelloListaTavoli.clear();
                try {
                    modelloListaTavoli.addAll(dipendenteController.getTavoliDB());
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        filtraButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Gioco selezione = null;

                if(filtraPerGiocoCheckBox.isSelected())
                {
                    if(pokerRadio.isSelected()) selezione = Gioco.Poker;
                    if(blackjackRadio.isSelected()) selezione = Gioco.Blackjack;
                }

                modelloListaTavoli.clear();
                modelloListaTavoli.addAll(dipendenteController.ricercaTavolo(selezione));
            }
        });

        listaTavoli.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                Tavolo temp = (Tavolo) listaTavoli.getSelectedValue();

                if(temp != null)
                {
                    if(!temp.getGioco().equals(Gioco.SlotMachine))
                    {
                        modificaGiochiButton.setVisible(true);
                        assegnaTavoloButton.setVisible(true);
                        rimuoviDipDaTavoloButton.setVisible(true);
                    }
                    else
                    {
                        modificaGiochiButton.setVisible(false);
                        assegnaTavoloButton.setVisible(false);
                        rimuoviDipDaTavoloButton.setVisible(false);
                    }
                    stampaTavoloInfoField(temp);
                }
                else
                {
                    textAreaInfoTavoli.setText(null);
                }
            }
        });

        modificaGiochiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Tavolo temp = (Tavolo) listaTavoli.getSelectedValue();

                if(temp != null && temp.getGioco().equals(Gioco.Blackjack))
                {
                    int input = JOptionPane.showConfirmDialog(null,
                            "vuoi cambiare il gioco da black jack a poker?");
                    if(input == JOptionPane.YES_OPTION)
                    {
                        try {
                            dipendenteController.modficaGiocoTavolo(temp.getIdTavolo(), Gioco.Poker);
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(null, ex.getMessage(),
                                    "errore", JOptionPane.ERROR_MESSAGE);
                        }

                        modelloListaTavoli.clear();
                        try {
                            modelloListaTavoli.addAll(dipendenteController.getTavoliDB());
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(null, ex.getMessage(), "errore", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    return;
                }
                if(temp != null && temp.getGioco().equals(Gioco.Poker))
                {
                    int input = JOptionPane.showConfirmDialog(null,
                            "vuoi cambiare il gioco da poker a black jack?");
                    if(input == JOptionPane.YES_OPTION)
                    {
                        try {
                            dipendenteController.modficaGiocoTavolo(temp.getIdTavolo(), Gioco.Blackjack);
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(null, ex.getMessage(),
                                    "errore", JOptionPane.ERROR_MESSAGE);
                        }

                        modelloListaTavoli.clear();
                        try {
                            modelloListaTavoli.addAll(dipendenteController.getTavoliDB());
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(null, ex.getMessage(), "errore", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });

        assegnaTavoloButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(listaTavoli.getSelectedValue() != null)
                {
                    thisFrame.setVisible(false);
                    new AssegnaDipendentiTavolo(dipendenteController, thisFrame,
                            dipendenteController.getIndexOfTavolo((Tavolo) listaTavoli.getSelectedValue()), false);
                }
                else JOptionPane.showMessageDialog(null, "nessun tavolo selezionato",
                        "errore", JOptionPane.ERROR_MESSAGE);

                listaTavoli.clearSelection();
            }
        });
        rimuoviDipDaTavoloButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(listaTavoli.getSelectedValue() != null)
                {
                    thisFrame.setVisible(false);
                    new AssegnaDipendentiTavolo(dipendenteController, thisFrame,
                            dipendenteController.getIndexOfTavolo((Tavolo) listaTavoli.getSelectedValue()), true);
                }
                else JOptionPane.showMessageDialog(null, "nessun tavolo selezionato",
                        "errore", JOptionPane.ERROR_MESSAGE);

                listaTavoli.clearSelection();
            }
        });

        aggiungiTavolo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                thisFrame.setVisible(false);
                new CreaTavolo(dipendenteController, thisFrame, modelloListaTavoli);
            }
        });

        rimuoviTavolo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Tavolo temp = (Tavolo) listaTavoli.getSelectedValue();

                if(temp != null)
                {
                    int input = JOptionPane.showConfirmDialog(null, "sei sicuro di voler cancellare questo tavolo?");

                    if(input == JOptionPane.NO_OPTION || input == JOptionPane.CANCEL_OPTION) return;

                    try {
                        dipendenteController.cancellaTavolo(temp);
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage(),
                                "errore", JOptionPane.ERROR_MESSAGE);
                    }

                    modelloListaTavoli.clear();
                    modelloListaTavoli.addAll(dipendenteController.getTavoliInLocale());
                }
                else JOptionPane.showMessageDialog(null, "nessun tavolo selezionato",
                        "errore", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void inizializzaMenuAdmin(){

        ButtonGroup sospettiButtons= new ButtonGroup();
        sospettiButtons.add(siSospettoRadio);
        sospettiButtons.add(noSospettoRadio);

        ButtonGroup banButtons= new ButtonGroup();
        banButtons.add(siBanRadio);
        banButtons.add(noBanRadio);

        ButtonGroup giochiButtons= new ButtonGroup();
        giochiButtons.add(pokerRadio);
        giochiButtons.add(blackjackRadio);

        ButtonGroup ruoloButtons = new ButtonGroup();
        ruoloButtons.add(dealerRadioButton);
        ruoloButtons.add(supervisoreRadioButton);

        spinnerPercMin.setModel(new SpinnerNumberModel(0, 0, 100, 1));
        spinnerPercMax.setModel(new SpinnerNumberModel(0, 0, 100, 1));

        spinnerSaldoMin.setModel(new SpinnerNumberModel(0, -10000, 10000, 1));
        spinnerSaldoMax.setModel(new SpinnerNumberModel(0, -10000, 10000, 1));

        spinnerPartMin.setModel(new SpinnerNumberModel(0, 0, 10000, 1));
        spinnerPartMax.setModel(new SpinnerNumberModel(0, 0, 10000, 1));

        aggiungiGiocoButton.setVisible(false);

        saldoTextMin.setVisible(false);
        saldoTextMax.setVisible(false);
        spinnerSaldoMin.setVisible(false);
        spinnerSaldoMax.setVisible(false);

        percentualVincitaTextMax.setVisible(false);
        percentualVincitaTextMin.setVisible(false);
        spinnerPercMin.setVisible(false);
        spinnerPercMax.setVisible(false);

        partiteGiocateTextMin.setVisible(false);
        partiteGiocateTextMax.setVisible(false);
        spinnerPartMin.setVisible(false);
        spinnerPartMax.setVisible(false);

        supervisoreRadioButton.setVisible(false);
        dealerRadioButton.setVisible(false);

        siBanRadio.setVisible(false);
        noBanRadio.setVisible(false);

        siSospettoRadio.setVisible(false);
        noSospettoRadio.setVisible(false);

        pokerRadio.setVisible(false);
        blackjackRadio.setVisible(false);
    }

    private void aggiornaUsername(){
        userFieldAdmin.setText(dipendenteController.getUserUtente() + "\t");
        userFieldAdmin.setText(dipendenteController.getUserUtente() + "\t");
    }

    private void logoutAdmin(){

        int risposta = JOptionPane.showConfirmDialog(
                null,
                "Sei sicuro di voler tornare alla schermata di login ?",
                "Conferma",
                JOptionPane.YES_NO_OPTION
        );

        if (risposta == JOptionPane.YES_OPTION) {
            thisFrame.setVisible(false);
            frameChiamante.setVisible(true);
            thisFrame.dispose();
        }
    }

    public static DefaultListModel<Dipendente> getModelloListaDipendente() {//Serve in registazione per aggiornare lista
        return modelloListaDipendente;
    }

    private void stampaClienteInfoField(Cliente temp){
        textAreaInfoFieldClienti.setText("Username: " + temp.getUsername() +
                "\n\nInformazioni anagrafiche" +
                "\nNome: " + temp.getNome() +
                "\nCognome: " + temp.getCognome() +
                "\nCodice Fiscale: " + temp.getCodiceFiscale() +
                "\nData di Nascita: " + temp.getDataDiNascita() +
                "\n\nInformazioni Giocatore" +
                "\nSaldo: " + temp.getSaldo() +
                "\nSaldo Giocate: " + temp.getFichesGiocate() +
                "\nTempo di gioco totale: " + temp.getTempoDiGioco().toHoursPart()+ ":" +
                String.format("%02d", temp.getTempoDiGioco().toMinutesPart()) +
                ":" + String.format("%02d", temp.getTempoDiGioco().toSecondsPart()) +
                "\nTasso vincita: " + temp.getVincitaPercentualeTot() +
                "\nPartite giocate: " + temp.getPartiteGiocate() +
                "\nTipo: " + (temp.isPremium() ? "Premium" : "Base") +
                "\nPercentuale Sconto " + temp.getSconto_premium() +
                (temp.getMotivoBan() == null ? "" : "\n\n Il giocatore è stato bannato\n" +
                        " Motivo di Ban: " + temp.getMotivoBan() + "\n In data: " + temp.getDataBan())
        );

    }
    private void stampaDipendenteInfoField(Dipendente temp){
        if(temp!=null){
            textAreaInfoDipendenti.setText("Username: " + temp.getUsername() +
                    "\n\nInformazioni anagrafiche" +
                    "\nNome: " + temp.getNome() +
                    "\nCognome: " + temp.getCognome() +
                    "\nCodice Fiscale: " + temp.getCodiceFiscale() +
                    "\nData di Nascita: " + temp.getDataDiNascita() +
                    (temp instanceof Supervisore ? "\n\nRuolo: Supervisore" :
                            "\n\nRuolo: Dealer\nGiochi a cui è abilitato: " + ((Dealer) temp).getGiochiDoveServeString()));
        }
    }

    private void stampaTavoloInfoField(Tavolo temp)
    {

        textAreaInfoTavoli.setText("idTavolo: " + temp.getIdTavolo() +
                "\ngioco: " + temp.getGioco() +
                "\nnumero posti: " + temp.getNumeroPosti());
        if(temp.getDealer()!=null || !temp.getSupervisori().isEmpty()){
            textAreaInfoTavoli.append("\n\nDipendenti:");
            if(temp.getDealer() != null)
            {
                textAreaInfoTavoli.append("\ndealer: " + temp.getDealer().getUsername());
            }
            if(!temp.getSupervisori().isEmpty()) {
                textAreaInfoTavoli.append("\nSupervisori: ");
                for (Supervisore i : temp.getSupervisori()) {
                    textAreaInfoTavoli.append(i.getUsername() + ", ");
                }
                String testoInfoTavoli = textAreaInfoTavoli.getText();  //Così da rimuovere ', ' dopo l'ultima iterazione
                textAreaInfoTavoli.setText(testoInfoTavoli.substring(0,testoInfoTavoli.length()-2));
            }
        }
    }
}
