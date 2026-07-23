-- Crea tabella Cliente
CREATE TABLE Cliente (
    idCliente VARCHAR(20) PRIMARY KEY,
    --Attributi del giocatore
    saldo INT NOT NULL CHECK(saldo >= 0) DEFAULT 50,
    tempoDiGioco TIME NOT NULL DEFAULT '00:00:00',
    fichesGiocate INT NOT NULL DEFAULT 0 CHECK(fichesGiocate >= 0),
    vincitaPercentualeTot float8 NOT NULL CHECK(vincitaPercentualeTot >= 0 and vincitaPercentualeTot <= 100) DEFAULT 0,
    partiteGiocate INT NOT NULL CHECK(partiteGiocate >= 0) DEFAULT 0,
    tipo VARCHAR(7) NOT NULL check(tipo in('Base', 'Premium')) DEFAULT 'Base', --Base/Premium
    scontoPokerPercentuale float8 
	CHECK(scontoPokerPercentuale >= 0 and scontoPokerPercentuale <= 1) DEFAULT 0,
    --Eventuale Ban e indicie sospetto
    sospetto BOOLEAN NOT NULL DEFAULT false,
    dataDiBan DATE DEFAULT null,
    motiviBan VARCHAR(100) DEFAULT null,
    --Attributi del cliente anagrafico
    nome VARCHAR(20) NOT NULL,
    cognome VARCHAR(20) NOT NULL,
    codiceFiscale VARCHAR(16) NOT NULL unique,
    dataDiNascita DATE NOT NULL,
    --Dati d'accesso
    username VARCHAR(20) NOT NULL UNIQUE,
    password VARCHAR(20) NOT NULL
);

-- Crea Dipendente
CREATE TABLE Dipendente (
    idDipendente VARCHAR(20) PRIMARY KEY, 
    --Attributi del dipendente anagrafico
    nome VARCHAR(20) NOT NULL,
    cognome VARCHAR(20) NOT NULL,
    dataDiNascita DATE NOT NULL,
    codiceFiscale VARCHAR(16) NOT NULL unique,
    --Dati d'accesso
    username VARCHAR(20) NOT NULL UNIQUE,
    password VARCHAR(20) NOT NULL,
    --Attributi del dipendente lavorativo
    ruolo VARCHAR(11) NOT NULL check(ruolo in('Supervisore', 'Dealer'))
);

--Crea Gioco
CREATE TABLE Gioco (
    nomeGioco VARCHAR(11) PRIMARY KEY check(nomeGioco in('Poker', 'SlotMachine', 'Blackjack')) --Poker/SlotMachine/Blackjack
);

--Crea GiochiDealer
CREATE TABLE GiochiDealer (
    idDealer VARCHAR(5) NOT NULL,
    idGioco VARCHAR(11) NOT NULL check(idGioco in('Poker', 'SlotMachine', 'Blackjack')),
    PRIMARY KEY (idDealer, idGioco),
    FOREIGN KEY (idDealer) REFERENCES Dipendente(IdDipendente) ON DELETE CASCADE,
    FOREIGN KEY (idGioco) REFERENCES Gioco(nomeGioco) ON DELETE CASCADE
);

--Crea Tavolo
CREATE TABLE Tavolo (
    numero INT PRIMARY KEY,
    gioco VARCHAR(11) NOT NULL,
    numeroPosti INT NOT NULL,
    idDealer VARCHAR(5) UNIQUE,
    FOREIGN KEY (idDealer) REFERENCES Dipendente(IdDipendente) ON DELETE CASCADE,
    CHECK ((gioco = 'SlotMachine' AND idDealer IS NULL) OR (gioco IN ('Poker', 'Blackjack') AND idDealer IS NOT NULL)),
	FOREIGN KEY (gioco) REFERENCES Gioco(nomeGioco)
);

--Crea Supervisore
CREATE TABLE SupervisoreTavolo (
    idSupervisore VARCHAR(5) NOT NULL,
    idTavolo INT NOT NULL,
    PRIMARY KEY (idSupervisore, idTavolo),
    FOREIGN KEY (idSupervisore) REFERENCES Dipendente(IdDipendente) ON DELETE CASCADE,
    FOREIGN KEY (idTavolo) REFERENCES Tavolo(Numero) ON DELETE CASCADE
);

--Crea Sessione
CREATE TABLE Sessione(
    idSessione SERIAL PRIMARY KEY,
    idCliente VARCHAR(5) NOT NULL,
    idTavolo INT NOT NULL,
    durata TIME NOT NULL DEFAULT '00:00:00',
    vincitaPercentuale float8 NOT NULL check(vincitaPercentuale >= 0 and vincitaPercentuale <= 100),
    partiteSvolte INT NOT NULL check(partiteSvolte >= 0),
    FOREIGN KEY (idCliente) REFERENCES Cliente(IdCliente) ON DELETE CASCADE,
    FOREIGN KEY (idTavolo) REFERENCES Tavolo(Numero) ON DELETE CASCADE
);