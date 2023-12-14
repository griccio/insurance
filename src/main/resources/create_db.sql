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

CREATE TABLE IF NOT EXISTS quote
(
    id SERIAL PRIMARY KEY,
    holder_id INT NOT NULL,
    registration_mark VARCHAR(10) NOT NULL,/*car's plate*/
    registration_date_car date NOT NULL,
    worth NUMERIC(9,2) NOT NULL, /*1000000,00 car's value*/
    policy_type VARCHAR(5)NOT NULL, /*RCA6, RCA12, RCA50*/
    cost NUMERIC(9,2) NOT NULL, /*1000000,00 policy's cost*/
    quote_number VARCHAR(25), /*client_id-registration_mark-quote_id*/
    FOREIGN KEY (holder_id) REFERENCES holder(id),
    date TIMESTAMP NOT NULL
    );