SELECT constraint_name 
FROM information_schema.table_constraints 
WHERE table_name = 'tavolo' 
  AND constraint_type = 'CHECK';

SELECT constraint_name 
FROM information_schema.table_constraints 
WHERE table_name = 'tavolo' 
  AND constraint_type = 'FOREIGN KEY';

ALTER TABLE Tavolo 
ADD CONSTRAINT tavolo_check CHECK ((gioco = 'SlotMachine' AND idDealer IS NULL AND numeroPosti = 1) OR (gioco IN ('Poker', 'Blackjack')));

ALTER TABLE Tavolo
  DROP CONSTRAINT tavolo_iddealer_fkey,
  ADD CONSTRAINT tavolo_iddealer_fkey 
      FOREIGN KEY (idDealer) 
      REFERENCES Dipendente(IdDipendente) 
      ON DELETE SET NULL;