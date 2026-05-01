INSERT INTO categoria (nombre_categoria) VALUES
('Recurvo 18m Novatos'),
('Recurvo 70m Avanzado'),
('Compuesto 50m Libre');

INSERT INTO usuario (rut, nombre, correo, contrasena, rol) VALUES
('1111111-1', 'Admin', 'admin@sistema.cl', 'admin123', 'ADMIN'),
('2222222-2', 'Jose', 'jose@usach.cl', 'arco123', 'ARQUERO'),
('3333333-3', 'Andrea', 'andrea.m@clublacisterna.cl', 'arco123', 'ARQUERO'),
('4444444-4', 'Carlos', 'carlos.vargas@mail.com', 'arco123', 'ARQUERO'),
('5555555-5', 'Camila', 'camila.s@mail.com', 'arco123', 'ARQUERO'),
('6666666-6', 'Roberto', 'rsilva@mail.com', 'arco123', 'ARQUERO');

INSERT INTO torneo (id_categoria, nombre_torneo, estado_torneo, fecha_inicio, fecha_termino) VALUES
(1, 'Copa Apertura USACH', 'FINALIZADO', '2026-03-01', '2026-03-05'),
(2, 'Torneo Metropolitano Sur', 'CREADO', '2026-05-20', '2026-05-25');

INSERT INTO ronda (id_torneo, numero_ronda) VALUES
(1, 1), (1, 2),
(2, 1), (2, 2), (2, 3);

INSERT INTO participacion (id_usuario, id_torneo) VALUES
(2, 1),
(3, 1),
(4, 1);

INSERT INTO participacion (id_usuario, id_torneo) VALUES
(3, 2),
(4, 2),
(5, 2),
(6, 2);

INSERT INTO flecha (id_participacion, id_ronda, puntaje) VALUES
(1, 1, 10), (1, 1, 9), (1, 1, 9),
(1, 2, 10), (1, 2, 10), (1, 2, 8);

INSERT INTO flecha (id_participacion, id_ronda, puntaje) VALUES
(2, 1, 9), (2, 1, 8), (2, 1, 8),
(2, 2, 10), (2, 2, 9), (2, 2, 9);

INSERT INTO flecha (id_participacion, id_ronda, puntaje) VALUES
(3, 1, 7), (3, 1, 0), (3, 1, 8),
(3, 2, 9), (3, 2, 8), (3, 2, 9);

INSERT INTO flecha (id_participacion, id_ronda, puntaje) VALUES
(4, 3, 10), (4, 3, 10), (4, 3, 9);

INSERT INTO flecha (id_participacion, id_ronda, puntaje) VALUES
(6, 3, 9), (6, 3, 9), (6, 3, 8);

INSERT INTO flecha (id_participacion, id_ronda, puntaje) VALUES
(7, 3, 10), (7, 3, 9), (7, 3, 9);
