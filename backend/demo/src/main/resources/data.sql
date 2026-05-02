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

INSERT INTO torneo (id_categoria, nombre_torneo, estado_torneo, fecha_inicio, fecha_termino, posicion_final) VALUES
(1, 'Copa Apertura USACH', 'COMPLETED', '2026-02-08', '2026-02-09', 1),
(2, 'Open Valle Central', 'COMPLETED', '2026-02-22', '2026-02-23', 1),
(3, 'Torneo Costa Norte', 'COMPLETED', '2026-03-15', '2026-03-16', 1),
(1, 'Ranking Otono Sur', 'COMPLETED', '2026-04-12', '2026-04-13', 1),
(2, 'Gran Premio Metropolitano', 'COMPLETED', '2026-04-26', '2026-04-28', 1),
(3, 'Copa Invierno 2026', 'NOT_STARTED', '2026-06-04', '2026-06-06', NULL),
(1, 'Abierto La Serena', 'NOT_STARTED', '2026-06-18', '2026-06-20', NULL),
(2, 'Clasificatorio Centro', 'NOT_STARTED', '2026-07-05', '2026-07-06', NULL),
(3, 'Copa Nacional Juvenil', 'NOT_STARTED', '2026-07-25', '2026-07-27', NULL),
(1, 'Festival Tiro al Aire', 'NOT_STARTED', '2026-08-15', '2026-08-16', NULL);

INSERT INTO ronda (id_torneo, numero_ronda) VALUES
(1, 1), (1, 2),
(2, 1), (2, 2),
(3, 1), (3, 2),
(4, 1), (4, 2), (4, 3),
(5, 1), (5, 2), (5, 3);

INSERT INTO participacion (id_usuario, id_torneo, puntaje_final) VALUES
(2, 1, 56),
(3, 1, 53),
(4, 1, 49),
(3, 2, 55),
(4, 2, 48),
(5, 2, 55),
(2, 3, 53),
(5, 3, 47),
(6, 3, 56),
(2, 4, 83),
(3, 4, 74),
(6, 4, 84),
(3, 5, 83),
(4, 5, 64),
(5, 5, 85);

INSERT INTO flecha (id_participacion, id_ronda, puntaje)
SELECT v.id_participacion, v.id_ronda, FLOOR(RANDOM() * 101)::int
FROM (
	VALUES
	(1, 1), (1, 1), (1, 1),
	(1, 2), (1, 2), (1, 2),
	(2, 1), (2, 1), (2, 1),
	(2, 2), (2, 2), (2, 2),
	(3, 1), (3, 1), (3, 1),
	(3, 2), (3, 2), (3, 2),
	(4, 3), (4, 3), (4, 3),
	(4, 4), (4, 4), (4, 4),
	(5, 3), (5, 3), (5, 3),
	(5, 4), (5, 4), (5, 4),
	(6, 3), (6, 3), (6, 3),
	(6, 4), (6, 4), (6, 4),
	(7, 5), (7, 5), (7, 5),
	(7, 6), (7, 6), (7, 6),
	(8, 5), (8, 5), (8, 5),
	(8, 6), (8, 6), (8, 6),
	(9, 5), (9, 5), (9, 5),
	(9, 6), (9, 6), (9, 6),
	(10, 7), (10, 7), (10, 7),
	(10, 8), (10, 8), (10, 8),
	(10, 9), (10, 9), (10, 9),
	(11, 7), (11, 7), (11, 7),
	(11, 8), (11, 8), (11, 8),
	(11, 9), (11, 9), (11, 9),
	(12, 7), (12, 7), (12, 7),
	(12, 8), (12, 8), (12, 8),
	(12, 9), (12, 9), (12, 9),
	(13, 10), (13, 10), (13, 10),
	(13, 11), (13, 11), (13, 11),
	(13, 12), (13, 12), (13, 12),
	(14, 10), (14, 10), (14, 10),
	(14, 11), (14, 11), (14, 11),
	(14, 12), (14, 12), (14, 12),
	(15, 10), (15, 10), (15, 10),
	(15, 11), (15, 11), (15, 11),
	(15, 12), (15, 12), (15, 12)
) AS v(id_participacion, id_ronda);

UPDATE participacion p
SET puntaje_final = COALESCE((
	SELECT SUM(f.puntaje)
	FROM flecha f
	WHERE f.id_participacion = p.id_participacion
), 0);
