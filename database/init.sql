SELECT 'CREATE DATABASE tienda'
    WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'tienda');

DROP TABLE IF EXISTS "funkos";
DROP SEQUENCE IF EXISTS funkos_id_seq;
DROP TABLE IF EXISTS "user_roles";
DROP TABLE IF EXISTS "usuarios";
DROP SEQUENCE IF EXISTS usuarios_id_seq;
DROP SEQUENCE IF EXISTS categorias_id_seq;
DROP TABLE IF EXISTS "categorias";

CREATE SEQUENCE funkos_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 8 CACHE 1;

CREATE TABLE "public"."funkos"
(
    "id"            bigint           DEFAULT nextval('funkos_id_seq') NOT NULL,
    "nombre"        character varying(255),
    "precio"        double precision DEFAULT '0.0',
    "cantidad"      integer          DEFAULT '0',
    "imagen"        text             DEFAULT 'https://via.placeholder.com/150',
    "fecha_cre"     timestamp        DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "fecha_act"     timestamp        DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "categoria_id"  bigint,
    CONSTRAINT "funkos_pkey" PRIMARY KEY ("id")
) WITH (oids = false);

INSERT INTO "funkos" ("id", "nombre", "precio", "cantidad", "imagen", "fecha_cre", "fecha_act", "categoria_id")
VALUES
    (1, 'Funko 1', '100', '1', 'https://via.placeholder.com/150', '2022-12-12', '2022-12-12', 1),
    (2, 'Funko 2', '200', '2', 'https://via.placeholder.com/150', '2022-12-12', '2022-12-12', 2),
    (3, 'Funko 3', '300', '3', 'https://via.placeholder.com/150', '2022-12-12', '2022-12-12', 3),
    (4, 'Funko 4', '400', '4', 'https://via.placeholder.com/150', '2022-12-12', '2022-12-12', 4),
    (5, 'Funko 5', '500', '5', 'https://via.placeholder.com/150', '2022-12-12', '2022-12-12', 1),
    (6, 'Funko 6', '600', '6', 'https://via.placeholder.com/150', '2022-12-12', '2022-12-12', 2),
    (7, 'Funko 7', '700', '7', 'https://via.placeholder.com/150', '2022-12-12', '2022-12-12', 3);

CREATE TABLE "public"."user_roles"
(
    "user_id" bigint NOT NULL,
    "roles"   character varying(255)
) WITH (oids = false);

INSERT INTO "user_roles" ("user_id", "roles")
VALUES (1, 'USER'),
       (1, 'ADMIN'),
       (2, 'USER'),
       (2, 'USER'),
       (3, 'USER');

CREATE SEQUENCE usuarios_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 5 CACHE 1;

CREATE TABLE "public"."usuarios"
(
    "is_deleted" boolean   DEFAULT false,
    "created_at" timestamp DEFAULT CURRENT_TIMESTAMP          NOT NULL,
    "id"         bigint    DEFAULT nextval('usuarios_id_seq') NOT NULL,
    "updated_at" timestamp DEFAULT CURRENT_TIMESTAMP          NOT NULL,
    "apellidos"  character varying(255)                       NOT NULL,
    "email"      character varying(255)                       NOT NULL,
    "nombre"     character varying(255)                       NOT NULL,
    "password"   character varying(255)                       NOT NULL,
    "username"   character varying(255)                       NOT NULL,
    CONSTRAINT "usuarios_email_key" UNIQUE ("email"),
    CONSTRAINT "usuarios_pkey" PRIMARY KEY ("id"),
    CONSTRAINT "usuarios_username_key" UNIQUE ("username")
) WITH (oids = false);

INSERT INTO "usuarios" ("is_deleted", "created_at", "id", "updated_at", "apellidos", "email", "nombre", "password",
                        "username")
VALUES ('f', '2023-11-02 11:43:24.724871', 1, '2023-11-02 11:43:24.724871', 'Admin Admin', 'admin@prueba.net', 'Admin',
        '$2a$10$vPaqZvZkz6jhb7U7k/V/v.5vprfNdOnh4sxi/qpPRkYTzPmFlI9p2', 'admin'),
       ('f', '2023-11-02 11:43:24.730431', 2, '2023-11-02 11:43:24.730431', 'User User', 'user@prueba.net', 'User',
        '$2a$12$RUq2ScW1Kiizu5K4gKoK4OTz80.DWaruhdyfi2lZCB.KeuXTBh0S.', 'user'),
       ('f', '2023-11-02 11:43:24.733552', 3, '2023-11-02 11:43:24.733552', 'Test Test', 'test@prueba.net', 'Test',
        '$2a$10$Pd1yyq2NowcsDf4Cpf/ZXObYFkcycswqHAqBndE1wWJvYwRxlb.Pu', 'test'),
       ('f', '2023-11-02 11:43:24.736674', 4, '2023-11-02 11:43:24.736674', 'Otro Otro', 'otro@prueba.net', 'otro',
        '$2a$12$3Q4.UZbvBMBEvIwwjGEjae/zrIr6S50NusUlBcCNmBd2382eyU0bS', 'otro');

CREATE SEQUENCE categorias_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 5 CACHE 1;

CREATE TABLE "public"."categorias"
(
    "id"        bigint           DEFAULT nextval('categorias_id_seq') NOT NULL,
    "categoria" character varying(255),
    "fecha_cre" timestamp        DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "fecha_act" timestamp        DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT "categorias_pkey" PRIMARY KEY ("id")
) WITH (oids = false);
INSERT INTO "categorias" ("id", "categoria", "fecha_cre", "fecha_act")
VALUES
    (1, 'DISNEY', '2022-12-12', '2022-12-12'),
    (2, 'MARVEL', '2022-12-12', '2022-12-12'),
    (3, 'ANIME', '2022-12-12', '2022-12-12'),
    (4, 'OTROS', '2022-12-12', '2022-12-12');
ALTER TABLE ONLY "public"."funkos"
    ADD CONSTRAINT "fk2fwq10nwymfv7fumctxt9vpgb" FOREIGN KEY (categoria_id) REFERENCES categorias (id) NOT DEFERRABLE;

ALTER TABLE ONLY "public"."user_roles"
    ADD CONSTRAINT "fk2chxp26bnpqjibydrikgq4t9e" FOREIGN KEY (user_id) REFERENCES usuarios (id) NOT DEFERRABLE;



