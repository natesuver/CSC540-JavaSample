use csc540_sample;

CREATE TABLE charger (
    id integer NOT NULL,
    dateopened date,
    locationid integer NOT NULL
);

CREATE TABLE vehicle (
    vin character(17) NOT NULL,
    id integer,
    make character varying(25) NOT NULL,
    model character varying(30) NOT NULL,
    year integer NOT NULL,
    mileage integer NOT NULL,
    color int NOT NULL
);

CREATE TABLE owner (
    id integer NOT NULL,
    lastname character varying(50) NOT NULL,
    firstname character varying(50) NOT NULL,
    middlename character varying(50),
    birthdate date,
    address1 character varying(100),
    city character varying(50) NOT NULL,
    state character(2) NOT NULL,
    postalcode character varying(9),
    country character varying(25),
    address2 character varying(100),
    username character varying(50)
);

CREATE TABLE state (
    name character varying(40) NOT NULL
);

CREATE TABLE vehiclemake (
    name character varying(50) NOT NULL
);

CREATE TABLE vehiclemodel (
    name character varying(30) NOT NULL
);
