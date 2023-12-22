
delete from quote_optional_extra;
delete from quote;
delete from car;
delete from holder;


INSERT INTO holder(id,name, surname, birth_date, fiscal_code, residence, domicile, tel, email, login, password)
VALUES (100,'Giuseppe','Riccio','1968-07-08', 'GPPIRCCO456789A', 'Via Manzoni 23 Albisola (SV)', 'Via Manzoni 23 Albisola (SV)',
        '123456789', 'giu.riccio@gmail.com', 'Riccio','Riccio'),
       (101,'Mario','Rossi','2000-01-18', 'MRARSS07A01L788R', 'Via della Vittoria 14 int 4 ', 'Via della Vittoria 14 int 4',
        '994537215', 'm.rossi@gmail.com', 'mrossi','13QWER'),
       (102,'Agata','Reversi','2009-07-21', 'GTARRS09L61A001V', 'Piazza Garibaldi Genova ', 'Piazza Garibaldi Genova ',
        '994537215', 'a.reversi@gmail.com', 'areversi','13QWER'),
       (103,'Filippo','Aneto','1998-04-11', 'NTFLPPAL61A001V', 'Via Mascherpa 22 int 7 Pavia ', 'Via Mascherpa 22 int 7 Pavia ',
             '994537215', 'f.aneto@gmail.com', 'aneto','13QQQ12');

INSERT INTO car(
    id, holder_id, registration_mark, registration_date, worth)
VALUES (200, 100, 'DR1234A', '2010-01-01',10000),
       (201, 101, 'CD2222A', '2022-01-01',30000),
       (202, 101, 'CD3344A', '2022-01-01',30000),
       (203, 103, 'EE1111EA', '2019-01-01',30000),
       (204, 103, 'EF2222WD', '2023-01-01',30000),
       (205, 102, 'AA333£WD', '2023-01-01',60000);

INSERT INTO quote(
    id, holder_id, car_id,  policy_type, cost, quote_number,date)
VALUES (300, 100, 200, 'RCA6', 10, '100-DR1234A-1','2023-10-01 13:16:00'),
       (301, 101, 201, 'RCA6', 30, '100-CD2222A-1','2023-12-22 13:16:00'),
       (302, 101, 202, 'RCA12', 50, '100-CD3344A-2','2023-09-16 13:16:00'),
       (303, 103, 203, 'RCA6', 20, '100-EE1111EA-1','2023-05-14 13:16:00'),
       (304, 103, 204, 'RCA50', 30, '100-EF2222WD-2','2023-07-30 13:16:00');

insert into quote_optional_extra values(300,1),(300,2),(300,3),(300,4);