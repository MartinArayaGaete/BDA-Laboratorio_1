-- 1. Múltiples Categorías
INSERT INTO categoria (nombre_categoria) VALUES
('Recurvo 18m Novatos'),
('Recurvo 70m Avanzado'),
('Compuesto 50m Libre');

-- 2. Usuarios (1 Admin, 5 Arqueros de distintos clubes/sectores)
INSERT INTO usuario (rut, nombre, correo, contrasena, rol) VALUES
('1111111-1', 'Admin Principal', 'admin@sistema.cl', 'admin123', 'ADMIN'),
('2222222-2', 'José Ceardi', 'jose.ceardi@usach.cl', 'arco123', 'ARQUERO'),
('3333333-3', 'Andrea Morales', 'andrea.m@clublacisterna.cl', 'arco123', 'ARQUERO'),
('4444444-4', 'Carlos Vargas', 'carlos.vargas@mail.com', 'arco123', 'ARQUERO'),
('5555555-5', 'Camila Soto', 'camila.s@mail.com', 'arco123', 'ARQUERO'),
('6666666-6', 'Roberto Silva', 'rsilva@mail.com', 'arco123', 'ARQUERO');

-- 3. Torneos (Uno pasado y uno activo)
INSERT INTO torneo (id_categoria, nombre_torneo, estado_torneo, fecha_inicio, fecha_termino) VALUES
(1, 'Copa Apertura USACH', 'FINALIZADO', '2026-03-01', '2026-03-05'),
(2, 'Torneo Metropolitano Sur', 'CREADO', '2026-05-20', '2026-05-25');

-- 4. Rondas Globales (2 rondas para la Copa USACH, 3 rondas para el Metropolitano)
INSERT INTO ronda (id_torneo, numero_ronda) VALUES
(1, 1), (1, 2),        -- Rondas id 1 y 2 (Torneo 1)
(2, 1), (2, 2), (2, 3); -- Rondas id 3, 4 y 5 (Torneo 2)

-- 5. Inscripciones / Participaciones
-- En el Torneo 1 (Copa USACH) participan José, Andrea y Carlos (ids 2, 3, 4)
INSERT INTO participacion (id_usuario, id_torneo) VALUES
(2, 1), -- id_participacion = 1 (José)
(3, 1), -- id_participacion = 2 (Andrea)
(4, 1); -- id_participacion = 3 (Carlos)

-- En el Torneo 2 (Metropolitano Sur) participan Andrea, Carlos, Camila y Roberto (ids 3, 4, 5, 6)
INSERT INTO participacion (id_usuario, id_torneo) VALUES
(3, 2), -- id_participacion = 4 (Andrea)
(4, 2), -- id_participacion = 5 (Carlos)
(5, 2), -- id_participacion = 6 (Camila)
(6, 2); -- id_participacion = 7 (Roberto)

-- 6. Registro de Flechas
-- Torneo 1 (Finalizado):
-- José (Participación 1) tira en la Ronda 1 (Ronda ID 1) y Ronda 2 (Ronda ID 2)
INSERT INTO flecha (id_participacion, id_ronda, puntaje) VALUES
(1, 1, 10), (1, 1, 9), (1, 1, 9), -- José en Ronda 1 (28 pts)
(1, 2, 10), (1, 2, 10), (1, 2, 8); -- José en Ronda 2 (28 pts)

-- Andrea (Participación 2) tira en la Ronda 1 y Ronda 2
INSERT INTO flecha (id_participacion, id_ronda, puntaje) VALUES
(2, 1, 9), (2, 1, 8), (2, 1, 8),  -- Andrea en Ronda 1 (25 pts)
(2, 2, 10), (2, 2, 9), (2, 2, 9); -- Andrea en Ronda 2 (28 pts)

-- Carlos (Participación 3) tira en la Ronda 1 y Ronda 2
INSERT INTO flecha (id_participacion, id_ronda, puntaje) VALUES
(3, 1, 7), (3, 1, M), (3, 1, 8),  -- Carlos en Ronda 1 (M=0 en arquería, usar 0 en BD)
(3, 2, 9), (3, 2, 8), (3, 2, 9);

-- Corrección para Carlos (Reemplazando la 'M' por un 0 para mantener el tipo INT)
UPDATE flecha SET puntaje = 0 WHERE id_participacion = 3 AND id_ronda = 1 AND puntaje = 0;
-- (Nota: Para mantenerlo limpio en la inserción inicial, lo pondré directo abajo)

DELETE FROM flecha WHERE id_participacion = 3;
INSERT INTO flecha (id_participacion, id_ronda, puntaje) VALUES
(3, 1, 7), (3, 1, 0), (3, 1, 8),  -- Carlos en Ronda 1 (15 pts) (0 representa Miss/Fuera)
(3, 2, 9), (3, 2, 8), (3, 2, 9);  -- Carlos en Ronda 2 (26 pts)


-- Torneo 2 (En curso):
-- Solo han tirado la primera ronda (Ronda ID 3)
-- Andrea (Participación 4)
INSERT INTO flecha (id_participacion, id_ronda, puntaje) VALUES
(4, 3, 10), (4, 3, 10), (4, 3, 9);

-- Camila (Participación 6)
INSERT INTO flecha (id_participacion, id_ronda, puntaje) VALUES
(6, 3, 9), (6, 3, 9), (6, 3, 8);

-- Roberto (Participación 7)
INSERT INTO flecha (id_participacion, id_ronda, puntaje) VALUES
(7, 3, 10), (7, 3, 9), (7, 3, 9);