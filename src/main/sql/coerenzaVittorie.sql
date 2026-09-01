--idea di trigger che però non serve e non sarebbe utile

CREATE or REPLACE FUNCTION coerenzaAggiornamentoWinRate()
RETURNS TRIGGER
LANGUAGE 'plpgsql' as $$
DECLARE vittorie, partiteTotali float;
BEGIN
	SELECT sum(vincitaPercentuale * partiteSvolte) into vittorie, sum(partiteSvolte) into partiteTotali
	from sessione
	where idCliente = new.idCliente;

	if(partiteTotali <> 0 and vittorie/partiteTotali <> new.vincitaPercentualeTot) then
		raise exception 'percentuali vittorie non coerenti';
	end if;

	return new;
end;
$$;

create TRIGGER percentualiVittorie
before UPDATE of vincitaPercentualeTotale
on Cliente
for each row
execute function coerenzaAggiornamentoWinRate();