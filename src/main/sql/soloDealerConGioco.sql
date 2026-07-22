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