-- Crea tabella Cliente
CREATE TABLE Cliente (
    idCliente VARCHAR(20) PRIMARY KEY,
    --Attributi del giocatore
    saldo INT NOT NULL CHECK(saldo >= 0) DEFAULT 50,
    tempoDiGioco BIGINT NOT NULL DEFAULT 0,
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
    idDealer VARCHAR(20) NOT NULL,
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
    idDealer VARCHAR(20) UNIQUE,
    FOREIGN KEY (idDealer) REFERENCES Dipendente(IdDipendente) ON DELETE SET NULL,
    CHECK ((gioco = 'SlotMachine' AND idDealer IS NULL AND numeroPosti = 1) --vedi se fare la logica per occupato
	OR (gioco IN ('Poker', 'Blackjack'))),
	FOREIGN KEY (gioco) REFERENCES Gioco(nomeGioco)
);

--Crea Supervisore
CREATE TABLE SupervisoreTavolo (
    idSupervisore VARCHAR(20) NOT NULL,
    idTavolo INT NOT NULL,
    PRIMARY KEY (idSupervisore, idTavolo),
    FOREIGN KEY (idSupervisore) REFERENCES Dipendente(IdDipendente) ON DELETE CASCADE,
    FOREIGN KEY (idTavolo) REFERENCES Tavolo(Numero) ON DELETE CASCADE
);

--Crea Sessione
CREATE TABLE Sessione(
    idSessione SERIAL PRIMARY KEY,
    idCliente VARCHAR(20) NOT NULL,
    idTavolo INT NOT NULL,
    durata BIGINT NOT NULL DEFAULT 0,
    vincitaPercentuale float8 NOT NULL check(vincitaPercentuale >= 0 and vincitaPercentuale <= 100),
    partiteSvolte INT NOT NULL check(partiteSvolte >= 0),
    FOREIGN KEY (idCliente) REFERENCES Cliente(IdCliente) ON DELETE CASCADE,
    FOREIGN KEY (idTavolo) REFERENCES Tavolo(Numero) ON DELETE CASCADE
);

CREATE or REPLACE FUNCTION tavoloDealer()
RETURNS TRIGGER
LANGUAGE 'plpgsql' as $$
declare val BOOLEAN;
BEGIN
	val := new.idDealer in(
	select d.idDipendente
	from Dipendente as d
	where d.ruolo = 'Supervisore'
	);

	if(val) THEN
		raise exception 'non è un dealer';
	end IF;

	return new;
end;
$$;

CREATE or REPLACE FUNCTION tavoloSupervisore()
RETURNS TRIGGER
LANGUAGE 'plpgsql' as $$
declare val BOOLEAN;
BEGIN
	val := new.idSupervisore in(
	select d.idDipendente
	from Dipendente as d
	where d.ruolo = 'Dealer'
	);

	if(val) THEN
		raise exception 'non è un Supervisore';
	end IF;

	return new;
end;
$$;

create TRIGGER relazioneDealerTavolo
before insert or UPDATE of idDealer
on Tavolo
for each row
execute function tavoloDealer();

create TRIGGER relazioneSupervisoreTavolo
before insert or UPDATE of idSupervisore
on SupervisoreTavolo
for each row
execute function tavoloSupervisore();

CREATE or REPLACE FUNCTION nonCompatibilitaTavoloDealer() --per quanto riguarda i giochi
RETURNS TRIGGER
LANGUAGE 'plpgsql' as $$
BEGIN
	if(new.gioco not in(
		select idGioco
		from giochiDealer
		where idDealer = new.idDealer
	)) THEN
		new.idDealer := null;
	end if;

	RETURN new;
end;
$$;

create TRIGGER tavoloDealer
before UPDATE of gioco 
on tavolo
for each row
execute function nonCompatibilitaTavoloDealer();

CREATE or REPLACE FUNCTION noDuplicatiUsernameCliente()
RETURNS TRIGGER
LANGUAGE 'plpgsql' as $$
declare val BOOLEAN;
BEGIN
	val := EXISTS(
	select *
	from Dipendente as d
	where d.username = new.username
	);

	if(val) THEN
		raise exception 'username già preso';
	end IF;

	return new;
end;
$$;

CREATE or REPLACE FUNCTION noDuplicatiUsernameDipendente()
RETURNS TRIGGER
LANGUAGE 'plpgsql' as $$
declare val BOOLEAN;
BEGIN
	val := EXISTS(
	select *
	from Cliente as d
	where d.username = new.username
	);

	if(val) THEN
		raise exception 'username già preso';
	end IF;

	return new;
end;
$$;

CREATE or REPLACE FUNCTION noDuplicatiCFCliente()
RETURNS TRIGGER
LANGUAGE 'plpgsql' as $$
declare val BOOLEAN;
BEGIN
	val := EXISTS(
	select *
	from Dipendente as d
	where d.codiceFiscale = new.codiceFiscale
	);

	if(val) THEN
		raise exception 'codice fiscale già preso';
	end IF;

	return new;
end;
$$;

CREATE or REPLACE FUNCTION noDuplicatiCFDipendente()
RETURNS TRIGGER
LANGUAGE 'plpgsql' as $$
declare val BOOLEAN;
BEGIN
	val := EXISTS(
	select *
	from Cliente as d
	where d.codiceFiscale = new.codiceFiscale
	);

	if(val) THEN
		raise exception 'codice fiscale già preso';
	end IF;

	return new;
end;
$$;

create TRIGGER usernameCliente
before insert or UPDATE of username
on Cliente
for each row
execute function noDuplicatiUsernameCliente();

create TRIGGER usernameDipendente
before insert or UPDATE of username
on Dipendente
for each row
execute function noDuplicatiUsernameDipendente();

create TRIGGER CFCliente
before insert or UPDATE of codiceFiscale
on Cliente
for each row
execute function noDuplicatiCFCliente();

create TRIGGER CFDipendente
before insert or UPDATE of codiceFiscale
on Dipendente
for each row
execute function noDuplicatiCFDipendente();

CREATE or REPLACE FUNCTION dealerGioco()
RETURNS TRIGGER
LANGUAGE 'plpgsql' as $$
declare val BOOLEAN;
BEGIN
	val := new.idDealer in(
	select d.idDipendente
	from Dipendente as d
	where d.ruolo = 'Supervisore'
	);

	if(val) THEN
		raise exception 'non è un dealer';
	end IF;

	return new;
end;
$$;

create TRIGGER relazioneDipGioco
before insert or UPDATE 
on giochiDealer
for each row
execute function dealerGioco();

insert into gioco values('Blackjack'),('SlotMachine'),('Poker');

insert into dipendente values('root','franco','giordy','2000-9-9','HDHAHD','root','r','Supervisore');