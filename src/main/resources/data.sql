INSERT INTO categorias (`categoria`,`fecha_cre`,`fecha_act`)
VALUES ('DISNEY', '2022-12-12', '2022-12-12'),
       ('MARVEL', '2022-12-12', '2022-12-12'),
       ('ANIME', '2022-12-12', '2022-12-12'),
       ('OTROS', '2022-12-12', '2022-12-12');

INSERT INTO funkos ( `nombre`, `precio`, `cantidad`, `imagen`, `fecha_cre`, `fecha_act`, `categoria_id`)
VALUES ( 'Funko 1', '100', '1', 'https://via.placeholder.com/150', '2022-12-12', '2022-12-12',1),
    ( 'Funko 2', '200', '2', 'https://via.placeholder.com/150', '2022-12-12', '2022-12-12',2),
    ( 'Funko 3', '300', '3', 'https://via.placeholder.com/150', '2022-12-12', '2022-12-12',3),
    ( 'Funko 4', '400', '4', 'https://via.placeholder.com/150', '2022-12-12', '2022-12-12',4),
    ('Funko 5', '500', '5', 'https://via.placeholder.com/150', '2022-12-12', '2022-12-12',1),
    ( 'Funko 6', '600', '6', 'https://via.placeholder.com/150', '2022-12-12', '2022-12-12',2),
    ('Funko 7', '700', '7', 'https://via.placeholder.com/150', '2022-12-12', '2022-12-12',3);

insert into USUARIOS (nombre, apellidos, username, email, password)
values ('Admin', 'Admin Admin', 'admin', 'admin@prueba.net',
        '$2a$10$vPaqZvZkz6jhb7U7k/V/v.5vprfNdOnh4sxi/qpPRkYTzPmFlI9p2');

insert into USER_ROLES (user_id, roles)
values (1, 'USER');
insert into USER_ROLES (user_id, roles)
values (1, 'ADMIN');

-- Contraseña: User1
insert into USUARIOS (nombre, apellidos, username, email, password)
values ('User', 'User User', 'user', 'user@prueba.net',
        '$2a$12$RUq2ScW1Kiizu5K4gKoK4OTz80.DWaruhdyfi2lZCB.KeuXTBh0S.');
insert into USER_ROLES (user_id, roles)
values (2, 'USER');

-- Contraseña: Test1
insert into USUARIOS (nombre, apellidos, username, email, password)
values ('Test', 'Test Test', 'test', 'test@prueba.net',
        '$2a$10$Pd1yyq2NowcsDf4Cpf/ZXObYFkcycswqHAqBndE1wWJvYwRxlb.Pu');
insert into USER_ROLES (user_id, roles)
values (2, 'USER');

-- Contraseña: Otro1
insert into USUARIOS (nombre, apellidos, username, email, password)
values ('otro', 'Otro Otro', 'otro', 'otro@prueba.net',
        '$2a$12$3Q4.UZbvBMBEvIwwjGEjae/zrIr6S50NusUlBcCNmBd2382eyU0bS');
insert into USER_ROLES (user_id, roles)
values (3, 'USER');