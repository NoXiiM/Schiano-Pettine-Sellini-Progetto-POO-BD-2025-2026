CREATE or REPLACE FUNCTION nonCompatibilitaTavoloDealer() --per quanto riguarda i giochi
RETURNS TRIGGER
LANGUAGE 'plpgsql' as $$
BEGIN
	if(new.idDealer is not null and new.gioco not in(
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