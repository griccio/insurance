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

CREATE TABLE IF NOT EXISTS car
(
    id SERIAL PRIMARY KEY,
    holder_id INT NOT NULL,
    registration_mark VARCHAR(10) NOT NULL UNIQUE,/*car's plate*/
    registration_date date NOT NULL,
    worth NUMERIC(9,2) NOT NULL /*1000000,00 car's value*/
    );


CREATE TABLE IF NOT EXISTS quote
(
    id SERIAL PRIMARY KEY,
    holder_id INT NOT NULL,
    car_id INT NOT NULL,
    policy_type VARCHAR(5)NOT NULL, /*RCA6, RCA12, RCA50*/
    cost NUMERIC(9,2) NOT NULL, /*1000000,00 policy's cost*/
    quote_number VARCHAR(25), /*client_id-registration_mark-quote_id*/
    FOREIGN KEY (holder_id) REFERENCES holder(id),
    FOREIGN KEY (car_id) REFERENCES car(id),
    date TIMESTAMP NOT NULL
    );


CREATE TABLE IF NOT EXISTS optional_extra
(
    id SERIAL PRIMARY KEY,
    code VARCHAR(2) NOT NULL UNIQUE,
    name  VARCHAR(100) NOT NULL
    );

insert into optional_extra(id,code,name)
VALUES(1,'C', 'POLIZZA CRISTALLI'),
    (2,'FI', 'FURTO INCENDIO'),
    (3,'AS', 'ASSISTENZA STRADALE'),
    (4,'TL', 'TUTELA LEGALE'),
    (5,'DC', 'DANNI CONDUCENTE'),
    (6,'K', 'KASKO');

CREATE TABLE IF NOT EXISTS quote_optional_extra
(
    quote_id INT NOT NULL,
    optional_extra_id INT NOT NULL,
    PRIMARY KEY (quote_id, optional_extra_id),
    FOREIGN KEY (quote_id) REFERENCES quote(id),
    FOREIGN KEY (optional_extra_id) REFERENCES optional_extra(id)
    );