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

ALTER TABLE giochidealer
  DROP CONSTRAINT giochidealer_iddealer_fkey,
  ADD CONSTRAINT giochidealer_iddealer_fkey
      FOREIGN KEY (idDealer) 
      REFERENCES Dipendente(IdDipendente) 
      ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tavolo
  DROP CONSTRAINT tavolo_iddealer_fkey,
  ADD CONSTRAINT tavolo_iddealer_fkey
      FOREIGN KEY (idDealer) 
      REFERENCES Dipendente(IdDipendente) 
      ON DELETE SET NULL ON UPDATE CASCADE;

ALTER TABLE SupervisoreTavolo
  DROP CONSTRAINT SupervisoreTavolo_idsupervisore_fkey,
  ADD CONSTRAINT SupervisoreTavolo_idsupervisore_fkey
      FOREIGN KEY (idsupervisore) 
      REFERENCES Dipendente(IdDipendente) 
      ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE Sessione
  DROP CONSTRAINT Sessione_idCliente_fkey,
  ADD CONSTRAINT Sessione_idCliente_fkey
      FOREIGN KEY (idCliente) 
      REFERENCES Cliente(idCliente) 
      ON DELETE CASCADE ON UPDATE CASCADE;
