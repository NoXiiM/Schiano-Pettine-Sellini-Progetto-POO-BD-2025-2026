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

//TODO 1) chiarire la questione: non mostrare dati nella text area quando nessuno è selezionato
//manca: parte di gestione tavoli e gestione account

public class MainMenuAdmin {
    private JPanel AdminPanel;
    private JTabbedPane tabbedPane1;
    private JList listaClienti;
    private JButton bannaButton;
    private JTextArea infoField;
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
    private JTextArea textArea1;
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
    private JLabel usernameClientiLabel;
    private JLabel usernameTavoliPanel;

    JFrame thisFrame;
    JFrame frameChiamante;
    WelcomeController controller;

    private static DefaultListModel<Cliente> modelloListaClienti;
    private static DefaultListModel<Dipendente> modelloListaDipendente;
    private static DefaultListModel<Tavolo> modelloListaTavoli;

    public MainMenuAdmin(DipendenteWelcomeController controller, JFrame frameChiamante) {
        this.controller = controller;
        this.frameChiamante= frameChiamante;

        modelloListaClienti= new DefaultListModel<>();
        try {
            modelloListaClienti.addAll(controller.getListaClientiDB());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "errore", JOptionPane.ERROR_MESSAGE);
        }
        listaClienti.setModel(modelloListaClienti);

        modelloListaDipendente= new DefaultListModel<>();
        try {
            modelloListaDipendente.addAll(controller.getDipendentiDB());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "errore", JOptionPane.ERROR_MESSAGE);
        }
        listaDipendenti.setModel(modelloListaDipendente);

        modelloListaTavoli = new DefaultListModel<>();
        try {
            modelloListaTavoli.addAll(controller.getTavoliDB());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "errore", JOptionPane.ERROR_MESSAGE);
        }
        listaTavoli.setModel(modelloListaTavoli);

        thisFrame = new JFrame("MainMenuAdmin");
        thisFrame.setContentPane(AdminPanel);
        thisFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        thisFrame.pack();
        thisFrame.setVisible(true);
        frameChiamante.setVisible(false);

        inizializzaMenuAdmin();

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

        listaClienti.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {

                Cliente temp= (Cliente) listaClienti.getSelectedValue();
                if(temp != null){
                    stampaClienteInfoField(temp);
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

                String sospettoRicerca= "indifferente";
                if(filtraPerSospettiCheckBox.isSelected()) {

                    if(siSospettoRadio.isSelected()){
                        sospettoRicerca= "si";

                    } else if(noSospettoRadio.isSelected()){
                        sospettoRicerca= "no";
                    }

                } else {
                    sospettoRicerca= "indifferente";
                }

                String banRicerca= "indifferente";
                if(filtraPerBanCheckBox.isSelected()) {

                    if(siBanRadio.isSelected()){
                        banRicerca= "si";

                    } else if (noBanRadio.isSelected()){
                        banRicerca= "no";
                    }

                } else {
                    banRicerca= "indifferente";
                }

                modelloListaClienti.clear();

                modelloListaClienti.addAll(controller.ricercaClienti(nomeRicerca, cognomeRicerca, usernameRicerca,
                        saldoMin, saldoMax, percMin, percMax, partiteMin, partiteMax, sospettoRicerca, banRicerca,
                        checkSaldo, checkPartite, checkPercentuale));

                infoField.setText("");

            }
        });

        //il pulsante aggiorna carica nuovamente i clienti dal database nell'ipotetica eventualità che 2 supervisori
        //stiano modificando i clienti in contemporanea
        aggiornaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modelloListaClienti.clear();
                try {
                    modelloListaClienti.addAll(controller.getListaClientiDB());
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

        logoutDaTavoli.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logoutAdmin();
            }
        });

        logoutDaDipendenti.addActionListener(new ActionListener() {
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

        aggiungiDipendenti.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RegistrationDipendente(thisFrame, controller);

            }
        });
        listaDipendenti.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {

                Dipendente temp= (Dipendente) listaDipendenti.getSelectedValue();

                if(temp != null){
                    stampaDipendenteInfoField(temp);
                }

                if(controller.isDealer(temp)) aggiungiGiocoButton.setVisible(true);
                else aggiungiGiocoButton.setVisible(false);
            }
        });
        //il pulsante aggiorna carica nuovamente i dipendenti dal database nell'ipotetica eventualità che 2 supervisori
        //stiano modificando i clienti in contemporanea
        aggiornaDipendenti.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modelloListaDipendente.clear();
                try {
                    modelloListaDipendente.addAll(controller.getDipendentiDB());
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
                modelloListaDipendente.addAll(controller.ricercaDipendente(nome,cognome, username, checkRuolo, ruoloSelezionato));
            }
        });

        licenziaDipendenti.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Dipendente temp = (Dipendente) listaDipendenti.getSelectedValue();

                textAreaInfoDipendenti.setText("");

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
                            if(password.equals(controller.getCurrentUser().getPassword())) {
                                try {
                                    controller.licenziaDipendente(temp);
                                } catch (SQLException ex) {
                                    JOptionPane.showMessageDialog(null, ex.getMessage(),
                                            "errore", JOptionPane.ERROR_MESSAGE);
                                }
                                textAreaInfoDipendenti.setText("");
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
                        if(controller.getCurrentUser().getUsername().equals("root")){
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
                                if(password.equals(controller.getCurrentUser().getPassword())) {
                                    try {
                                        controller.licenziaDipendente(temp);
                                    } catch (SQLException ex) {
                                        JOptionPane.showMessageDialog(null, ex.getMessage(),
                                                "errore", JOptionPane.ERROR_MESSAGE);
                                    }
                                    textAreaInfoDipendenti.setText("");
                                }
                                else {
                                    JOptionPane.showMessageDialog(
                                            null,
                                            "Password non corretta!",
                                            "Errore",
                                            JOptionPane.ERROR_MESSAGE
                                    );
                                }
                            }
                        }
                        JOptionPane.showMessageDialog(null,"Non hai i permessi per cancellare un Supervisore","Errore",JOptionPane.ERROR_MESSAGE);
                    }
                    modelloListaDipendente.clear();
                    modelloListaDipendente.addAll(controller.getDipendentiInLocale());
                } else {
                    JOptionPane.showMessageDialog(null,"Nessun dipendente selezionato","Errore",JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        aggiungiGiocoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AggiungiGiocoDealer(controller, thisFrame, (Dealer) listaDipendenti.getSelectedValue());
            }
        });

        aggiornaTavoli.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modelloListaTavoli.clear();
                try {
                    modelloListaTavoli.addAll(controller.getTavoliDB());
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
                modelloListaTavoli.addAll(controller.ricercaTavolo(selezione));
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
        userFieldAdmin.setText(controller.getUserUtente() + "\t");
        userFieldAdmin.setText(controller.getUserUtente() + "\t");
    }

    private void logoutAdmin(){

        int risposta = JOptionPane.showConfirmDialog(
                null,
                "Sei sicuro di voler tornare alla schermata di login ?",
                "Conferma",
                JOptionPane.YES_NO_OPTION
        );

        if (risposta == JOptionPane.YES_OPTION) {
            controller.setCurrentUserNull();
            thisFrame.setVisible(false);
            frameChiamante.setVisible(true);
            thisFrame.dispose();
        }
    }

    public static DefaultListModel<Dipendente> getModelloListaDipendente() {//Serve in registazione per aggiornare lista
        return modelloListaDipendente;
    }

    private void stampaClienteInfoField(Cliente temp){
        infoField.setText("Username: " + temp.getUsername() +
                "\n\nInformazioni anagrafiche" +
                "\nNome: " + temp.getNome() +
                "\nCognome: " + temp.getCognome() +
                "\nCodice Fiscale: " + temp.getCodiceFiscale() +
                "\nData di Nascita: " + temp.getDataDiNascita() +
                "\n\nInformazioni Giocatore" +
                "\nSaldo: " + temp.getSaldo() +
                "\nSaldo Giocate: " + temp.getFichesGiocate() +
                "\nTempo di gioco totale: " + temp.getTempoDiGioco().toHoursPart()+ ":" +temp.getTempoDiGioco().toMinutesPart() + ":" + temp.getTempoDiGioco().toSecondsPart() +
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
}
