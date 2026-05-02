--  CATEGORÍAS (ID 1 y ID 2)
INSERT INTO categoria (nombre_categoria) VALUES
('Recurvo 18m Novatos'),
('Compuesto 50m Libre');;

--  USUARIOS (ID 1: Admin, ID 2, 3 y 4: Arqueros)
INSERT INTO usuario (rut, nombre, correo, contrasena, rol) VALUES
('11.111.111-1', 'Admin', 'admin@usach.cl', 'admin123', 'ADMIN'),
('22.222.222-2', 'Jose', 'jose@mail.com', 'arco123', 'ARQUERO'),
('33.333.333-3', 'Andrea', 'andrea@mail.com', 'arco123', 'ARQUERO'),
('44.444.444-4', 'Carlos', 'carlos@mail.com', 'arco123', 'ARQUERO');;

--  TORNEO (Estado 'CREADO' para indicar que no ha finalizado)
INSERT INTO torneo (id_categoria, nombre_torneo, estado_torneo, fecha_inicio, fecha_termino) VALUES
(2, 'Gran Torneo Manual 2026', 'CREADO', '2026-06-10', '2026-06-12');;

--  RONDAS (ID 1, 2 y 3 para el Torneo 1)
INSERT INTO ronda (id_torneo, numero_ronda) VALUES
(1, 1),
(1, 2),
(1, 3);;

--  PARTICIPACIÓN (Puntajes finales precalculados, posiciones en NULL)
INSERT INTO participacion (id_usuario, id_torneo, puntaje_final, posicion_final) VALUES
(2, 1, 55, NULL), -- Jose
(3, 1, 48, NULL), -- Andrea
(4, 1, 36, NULL);; -- Carlos

--  PUNTAJE RONDA (3 rondas por cada participante)
INSERT INTO puntaje_ronda (id_ronda, id_participacion, puntaje_ronda) VALUES
-- Jose (Participación 1)
(1, 1, 19), (2, 1, 18), (3, 1, 18),
-- Andrea (Participación 2)
(1, 2, 16), (2, 2, 17), (3, 2, 15),
-- Carlos (Participación 3)
(1, 3, 12), (2, 3, 12), (3, 3, 12);;

--  FLECHAS (2 flechas por cada ronda, total 6 por participante)
INSERT INTO flecha (id_puntaje_ronda, puntaje) VALUES
-- Jose: R1(10,9), R2(8,10), R3(9,9) = 55
(1, 10), (1, 9),
(2, 8),  (2, 10),
(3, 9),  (3, 9),
-- Andrea: R1(8,8), R2(9,8), R3(7,8) = 48
(4, 8),  (4, 8),
(5, 9),  (5, 8),
(6, 7),  (6, 8),
-- Carlos: R1(6,6), R2(6,6), R3(6,6) = 36
(7, 6),  (7, 6),
(8, 6),  (8, 6),
(9, 6),  (9, 6);;