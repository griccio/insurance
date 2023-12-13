
DELETE FROM holder;
INSERT INTO holder(id,name, surname, birth_date, fiscal_code, residence, domicile, tel, email, login, password)
 VALUES (100,'Giuseppe','Riccio','1968-07-08', 'GPPIRCCO456789A', 'Via Manzoni 23 Albisola (SV)', 'Via Manzoni 23 Albisola (SV)',
         '123456789', 'giu.riccio@gmail,com', 'Riccio','Riccio'),
        (101,'Mario','Rossi','2000-01-18', 'MRARSS07A01L788R', 'Via della Vittoria 14 int 4 ', 'Via della Vittoria 14 int 4',
        '994537215', 'm.rossi@gmail,com', 'mrossi','13QWER'),
        (102,'Agata','Reversi','2009-07-21', 'GTARRS09L61A001V', 'Piazza Garibaldi Genova ', 'Piazza Garibaldi Genova ',
                                                              '994537215', 'a.reversi@gmail,com', 'areversi','13QWER');
