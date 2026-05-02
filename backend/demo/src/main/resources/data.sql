--  CATEGORÍAS (ID 1 y ID 2)
INSERT INTO categoria (nombre_categoria) VALUES 
('Recurvo 18m Novatos'), 
('Compuesto 50m Libre');

--  USUARIOS (ID 1: Admin, ID 2: Arquero)
INSERT INTO usuario (rut, nombre, correo, contrasena, rol) VALUES 
('11.111.111-1', 'Admin', 'admin@usach.cl', 'admin123', 'ADMIN'),
('22.222.222-2', 'Arquero', 'arquero@mail.com', 'arco123', 'ARQUERO');

--  TORNEO (ID 1 asignado a la categoría 2)
INSERT INTO torneo (id_categoria, nombre_torneo, estado_torneo, fecha_inicio, fecha_termino) VALUES 
(2, 'Gran Torneo Manual 2026', 'CREADO', '2026-06-10', '2026-06-12');

--  RONDAS (IDs 1, 2 y 3 vinculadas al Torneo 1)
INSERT INTO ronda (id_torneo, numero_ronda) VALUES 
(1, 1), 
(1, 2), 
(1, 3);

--  PARTICIPACIÓN (ID 1 vinculando al Usuario 2 con el Torneo 1, puntaje final precalculado)
INSERT INTO participacion (id_usuario, id_torneo, puntaje_final) VALUES 
(2, 1, 55);

--  PUNTAJE RONDA (IDs 1, 2 y 3 vinculando las Rondas con la Participación 1)
INSERT INTO puntaje_ronda (id_ronda, id_participacion, puntaje_ronda) VALUES 
(1, 1, 19), 
(2, 1, 18), 
(3, 1, 18);

--  FLECHAS (Las 6 flechas exactas de tu JSON, vinculadas a su respectivo puntaje_ronda)
INSERT INTO flecha (id_puntaje_ronda, puntaje) VALUES 
(1, 10), (1, 9),  -- Pertenecen a la Ronda 1
(2, 8),  (2, 10), -- Pertenecen a la Ronda 2
(3, 9),  (3, 9);  -- Pertenecen a la Ronda 3