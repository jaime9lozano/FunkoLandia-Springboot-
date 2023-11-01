INSERT INTO categorias (`categoria`,`fecha_cre`,`fecha_act`)
VALUES ('DISNEY', '2022-12-12', '2022-12-12'),
       ('MARVEL', '2022-12-12', '2022-12-12'),
       ('ANIME', '2022-12-12', '2022-12-12'),
       ('OTROS', '2022-12-12', '2022-12-12');

INSERT INTO funkos ( `nombre`, `precio`, `cantidad`, `imagen`, `fecha_cre`, `fecha_act`, `categoria_id`)
VALUES ( 'Funko 1', '100', '1', 'funko1.jpg', '2022-12-12', '2022-12-12',1),
    ( 'Funko 2', '200', '2', 'funko2.jpg', '2022-12-12', '2022-12-12',2),
    ( 'Funko 3', '300', '3', 'funko3.jpg', '2022-12-12', '2022-12-12',3),
    ( 'Funko 4', '400', '4', 'funko4.jpg', '2022-12-12', '2022-12-12',4),
    ('Funko 5', '500', '5', 'funko5.jpg', '2022-12-12', '2022-12-12',1),
    ( 'Funko 6', '600', '6', 'funko6.jpg', '2022-12-12', '2022-12-12',2),
    ('Funko 7', '700', '7', 'funko7.jpg', '2022-12-12', '2022-12-12',3);