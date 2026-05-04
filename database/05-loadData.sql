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
(1, 'Copa Apertura', 'IN_COURSE', '2026-02-08', '2026-02-09'),
(2, 'Open Valle',    'IN_COURSE', '2026-02-22', '2026-02-23'),
(3, 'Torneo Costa',  'IN_COURSE', '2026-03-15', '2026-03-16'),
(4, 'Liga Nacional 2026', 'NOT_STARTED', '2027-05-01', '2027-05-30'),
(5, 'Torneo Novatos 2026', 'NOT_STARTED', '2027-08-10', '2027-08-12');

-- 4. PARTICIPACION
INSERT INTO participacion (id_usuario, id_torneo, puntaje_final, posicion_final) VALUES

-- Torneos 1
(2,1,NULL,NULL),
(3,1,NULL,NULL),
(4,1,NULL,NULL),

-- Torneos 2
(5,2,NULL,NULL),
(6,2,NULL,NULL),
(2,2,NULL,NULL),

-- Torneos 3
(2,3,NULL,NULL),
(4,3,NULL,NULL),
(5,3,NULL,NULL),

-- Torneos 4
(5,4,NULL,NULL),

-- Torneos 5
(3,5,NULL,NULL);


-- 5. RONDA
INSERT INTO ronda (id_torneo, numero_ronda) VALUES
-- Torneo 1
(1,1),(1,2), (1,3),
-- Torneo 2
(2,1),(2,2), (2,3),
-- Torneo 3
(3,1),(3,2), (3,3),
-- Torneo 4
(4,1),(4,2), (4,3),
-- Torneo 5
(5,1),(5,2), (5,3);

-- 6. Procedimiento almacenado

-- Torneo 1 - Ronda 1
CALL registrar_puntaje_ronda(1,1,ARRAY[3,0,1,2,0,6],1);
CALL registrar_puntaje_ronda(1,2,ARRAY[0,5,1,2,3,10],1);
CALL registrar_puntaje_ronda(1,3,ARRAY[1,0,1,0,10,0],1);

-- Torneo 1 - Ronda 2
CALL registrar_puntaje_ronda(2,1,ARRAY[7,0,9,7,0,10],1);
CALL registrar_puntaje_ronda(2,2,ARRAY[9,0,5,7,2,0],1);
CALL registrar_puntaje_ronda(2,3,ARRAY[10,0,7,10,0,10],1);

-- Torneo 1 - Ronda 3 
CALL registrar_puntaje_ronda(3,1,ARRAY[0,6,8,10,0,2],1);
CALL registrar_puntaje_ronda(3,2,ARRAY[6,0,5,7,0,3],1);
CALL registrar_puntaje_ronda(3,3,ARRAY[0,4,6,1,0,3],1);

-- Torneo 2 - Ronda 1
CALL registrar_puntaje_ronda(1,4,ARRAY[7,0,2,0,5,6],1);
CALL registrar_puntaje_ronda(1,5,ARRAY[0,2,5,5,0,1],1);
CALL registrar_puntaje_ronda(1,6,ARRAY[3,0,5,6,0,5],1);

-- Torneo 2 - Ronda 2
CALL registrar_puntaje_ronda(2,4,ARRAY[6,0,2,5,10,0],1);
CALL registrar_puntaje_ronda(2,5,ARRAY[7,0,2,4,0,1],1);
CALL registrar_puntaje_ronda(2,6,ARRAY[0,5,6,0,10,4],1);

-- Torneo 2 - Ronda 3
CALL registrar_puntaje_ronda(3,4,ARRAY[6,0,2,2,0,6],1);
CALL registrar_puntaje_ronda(3,5,ARRAY[8,0,1,6,0,4],1);
CALL registrar_puntaje_ronda(3,6,ARRAY[4,0,7,5,0,10],1);

-- Torneo 3 - Ronda 1
CALL registrar_puntaje_ronda(1,7,ARRAY[0,3,0,2,3,0],1);
CALL registrar_puntaje_ronda(1,8,ARRAY[10,0,1,1,0,6],1);
CALL registrar_puntaje_ronda(1,9,ARRAY[6,0,2,5,0,5],1);

-- Torneo 3 - Ronda 2
CALL registrar_puntaje_ronda(2,7,ARRAY[6,0,3,4,0,1],1);
CALL registrar_puntaje_ronda(2,8,ARRAY[3,0,0,5,0,5],1);
CALL registrar_puntaje_ronda(2,9,ARRAY[7,10,0,5,0,1],1);

-- Torneo 3 - Ronda 3
CALL registrar_puntaje_ronda(3,7,ARRAY[7,0,2,7,0,2],1);
CALL registrar_puntaje_ronda(3,8,ARRAY[5,0,3,2,0,3],1);
CALL registrar_puntaje_ronda(3,9,ARRAY[10,10,0,3,0,5],1);

-- 7. Completacion de torneos
UPDATE torneo SET estado_torneo = 'COMPLETED' WHERE id_torneo = 1;

-- 8. Calcular posiciones finales (ejecutar procedimiento)
CALL actualizar_posiciones(1);
