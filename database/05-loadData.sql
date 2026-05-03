-- 1. CATEGORIA
INSERT INTO categoria (nombre_categoria) VALUES
('Recurvo 18m Novatos'),
('Recurvo 70m Avanzado'),
('Compuesto 50m Libre');

-- 2. USUARIO
INSERT INTO usuario (rut, nombre, correo, contrasena, rol) VALUES
('1111111-1', 'Admin',   'admin@sistema.cl',  'admin123', 'ADMIN'),
('2222222-2', 'Jose',    'jose@usach.cl',     'arco123',  'ARQUERO'),
('3333333-3', 'Andrea',  'andrea@mail.cl',    'arco123',  'ARQUERO'),
('4444444-4', 'Carlos',  'carlos@mail.com',   'arco123',  'ARQUERO'),
('5555555-5', 'Camila',  'camila@mail.com',   'arco123',  'ARQUERO'),
('6666666-6', 'Roberto', 'roberto@mail.com',  'arco123',  'ARQUERO');

-- 3. TORNEO (ahora con IN_COURSE)
INSERT INTO torneo (id_categoria, nombre_torneo, estado_torneo, fecha_inicio, fecha_termino) VALUES
(1, 'Copa Apertura', 'COMPLETED', '2026-02-08', '2026-02-09'),
(2, 'Open Valle',    'COMPLETED', '2026-02-22', '2026-02-23'),
(3, 'Torneo Costa',  'COMPLETED', '2026-03-15', '2026-03-16'),
(1, 'Ranking Otoño', 'COMPLETED', '2026-04-12', '2026-04-13'),
(2, 'Gran Premio',   'COMPLETED', '2026-04-26', '2026-04-28'),
(3, 'Copa Invierno', 'NOT_STARTED','2026-06-04','2026-06-06'),

-- NUEVOS EN CURSO
(1, 'Liga Metropolitana', 'IN_COURSE', '2026-05-01', '2026-05-10'),
(2, 'Open Primavera',     'IN_COURSE', '2026-05-03', '2026-05-12');

-- 4. PARTICIPACION
INSERT INTO participacion (id_usuario, id_torneo, puntaje_final, posicion_final) VALUES
-- COMPLETED
(2,1,56,1),(3,1,53,2),(4,1,49,3),
(3,2,55,1),(5,2,55,2),(4,2,48,3),
(6,3,56,1),(2,3,53,2),(5,3,47,3),
(6,4,84,1),(2,4,83,2),(3,4,74,3),
(5,5,85,1),(3,5,83,2),(4,5,64,3),

-- IN_COURSE (sin puntaje final aún)
(2,7,NULL,NULL),
(3,7,NULL,NULL),
(4,7,NULL,NULL),

(5,8,NULL,NULL),
(6,8,NULL,NULL),
(2,8,NULL,NULL);

-- 5. RONDA
INSERT INTO ronda (id_torneo, numero_ronda) VALUES
-- COMPLETED
(1,1),(1,2),
(2,1),(2,2),
(3,1),(3,2),
(4,1),(4,2),(4,3),
(5,1),(5,2),(5,3),

-- IN COURSE
(7,1),(7,2),
(8,1),(8,2);

-- 6. PUNTAJE_RONDA (solo para completed)
INSERT INTO puntaje_ronda (id_ronda, id_participacion, puntaje_ronda) VALUES
(1,1,28),(1,2,25),(1,3,23),
(2,1,28),(2,2,28),(2,3,26),

(3,4,29),(3,5,23),(3,6,26),
(4,4,26),(4,5,25),(4,6,29);

-- 7. FLECHA
INSERT INTO flecha (id_puntaje_ronda, puntaje) VALUES
(1,10),(1,9),(1,9),
(2,9),(2,8),(2,8),
(3,7),(3,8),(3,8);

-- 8. LOGS
INSERT INTO logs (
    id_admin,
    id_afectado,
    fecha_editado,
    torneo_afectado,
    ronda_afectada,
    puntaje_anterior,
    puntaje_nuevo
) VALUES
(1,2,'2026-02-09 15:30:00',1,1,27,28),
(1,3,'2026-02-23 16:10:00',2,1,28,29);
