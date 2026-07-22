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