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