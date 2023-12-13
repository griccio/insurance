CREATE TABLE IF NOT EXISTS holder
(
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    surname VARCHAR(100) NOT NULL,
    birth_date date NOT NULL,
    fiscal_code VARCHAR(25) NOT NULL UNIQUE,
    residence VARCHAR(250)NOT NULL,
    domicile VARCHAR (250) NOT NULL,
    tel VARCHAR(20) NOT NULL,
    email VARCHAR(50) NOT NULL UNIQUE,
    login VARCHAR(20),
    password VARCHAR(15)
    );
