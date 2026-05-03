-- 0. LIMPIEZA TOTAL
TRUNCATE TABLE flecha, puntaje_ronda, ronda, participacion, torneo, usuario, categoria RESTART IDENTITY CASCADE;;

-- 1. CATEGORÍAS
INSERT INTO categoria (nombre_categoria) VALUES
('Recurvo 18m Novatos'),
('Compuesto 50m Libre');;

-- 2. USUARIOS (1 Admin + 9 Arqueros)
INSERT INTO usuario (rut, nombre, correo, contrasena, rol) VALUES
('11.111.111-1', 'Admin Supremo', 'admin@usach.cl', 'admin123', 'ADMIN'),
-- Arqueros Torneo 1 (IDs 2, 3, 4)
('22.222.222-2', 'Jose Ceardi', 'jose@mail.com', 'arco123', 'ARQUERO'),
('33.333.333-3', 'Andrea Lopez', 'andrea@mail.com', 'arco123', 'ARQUERO'),
('44.444.444-4', 'Carlos Ruiz', 'carlos@mail.com', 'arco123', 'ARQUERO'),
-- Arqueros Torneo 2 (IDs 5, 6, 7)
('55.555.555-5', 'Luis Rojas', 'luis@mail.com', 'arco123', 'ARQUERO'),
('66.666.666-6', 'Maria Perez', 'maria@mail.com', 'arco123', 'ARQUERO'),
('77.777.777-7', 'Pedro Soto', 'pedro@mail.com', 'arco123', 'ARQUERO'),
-- Arqueros Torneo 3 (IDs 8, 9, 10)
('88.888.888-8', 'Sofia Vega', 'sofia@mail.com', 'arco123', 'ARQUERO'),
('99.999.999-9', 'Diego Tapia', 'diego@mail.com', 'arco123', 'ARQUERO'),
('10.000.000-0', 'Valentina Silva', 'valentina@mail.com', 'arco123', 'ARQUERO');;

-- 3. TORNEOS
INSERT INTO torneo (id_categoria, nombre_torneo, estado_torneo, fecha_inicio, fecha_termino) VALUES
(1, 'Copa Invierno 2025', 'COMPLETED', '2025-06-01', '2025-06-03'), -- ID 1
(2, 'Liga Nacional 2026', 'IN_COURSE', '2026-05-01', '2026-05-30'),   -- ID 2
(1, 'Torneo Novatos 2026', 'NOT_STARTED', '2026-08-10', '2026-08-12');; -- ID 3

-- 4. RONDAS (3 por cada torneo)
INSERT INTO ronda (id_torneo, numero_ronda) VALUES
(1, 1), (1, 2), (1, 3), -- Torneo 1 (IDs 1-3)
(2, 1), (2, 2), (2, 3), -- Torneo 2 (IDs 4-6)
(3, 1), (3, 2), (3, 3);; -- Torneo 3 (IDs 7-9)

-- 5. PARTICIPACIONES
INSERT INTO participacion (id_usuario, id_torneo, puntaje_final, posicion_final) VALUES
-- Torneo 1 (Finalizado)
(2, 1, 55, 1), (3, 1, 48, 2), (4, 1, 36, 3),
-- Torneo 2 (En curso)
(5, 2, 38, NULL), (6, 2, 30, NULL), (7, 2, 20, NULL),
-- Torneo 3 (Not started)
(8, 3, 0, NULL), (9, 3, 0, NULL), (10, 3, 0, NULL);;

-- 6. PUNTAJE_RONDA
INSERT INTO puntaje_ronda (id_ronda, id_participacion, puntaje_ronda) VALUES
-- Torneo 1 (Completo)
(1, 1, 19), (2, 1, 18), (3, 1, 18),
(1, 2, 16), (2, 2, 17), (3, 2, 15),
(1, 3, 12), (2, 3, 12), (3, 3, 12),
-- Torneo 2 (Incompleto: solo 2 rondas)
(4, 4, 20), (5, 4, 18),
(4, 5, 15), (5, 5, 15),
(4, 6, 10), (5, 6, 10);;

-- 7. FLECHAS (2 por cada ronda registrada)
INSERT INTO flecha (id_puntaje_ronda, puntaje) VALUES
-- Torneo 1
(1, 10), (1, 9), (2, 8), (2, 10), (3, 9), (3, 9),
(4, 8), (4, 8), (5, 9), (5, 8), (6, 7), (6, 8),
(7, 6), (7, 6), (8, 6), (8, 6), (9, 6), (9, 6),
-- Torneo 2
(10, 10), (10, 10), (11, 9), (11, 9),
(12, 8), (12, 7), (13, 7), (13, 8),
(14, 5), (14, 5), (15, 6), (15, 4);;