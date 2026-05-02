-- Poblado de tablas maestras (Categorías y Usuarios)
INSERT INTO categoria (nombre_categoria) VALUES
('Recurvo 18m Novatos'), ('Recurvo 70m Avanzado'), ('Compuesto 50m Libre');

INSERT INTO usuario (rut, nombre, correo, contrasena, rol) VALUES
('1111111-1', 'Admin', 'admin@sistema.cl', 'admin123', 'ADMIN'),
('2222222-2', 'Jose', 'jose@usach.cl', 'arco123', 'ARQUERO'),
('3333333-3', 'Andrea', 'andrea.m@clublacisterna.cl', 'arco123', 'ARQUERO'),
('4444444-4', 'Carlos', 'carlos.vargas@mail.com', 'arco123', 'ARQUERO'),
('5555555-5', 'Camila', 'camila.s@mail.com', 'arco123', 'ARQUERO'),
('6666666-6', 'Roberto', 'rsilva@mail.com', 'arco123', 'ARQUERO');

-- Torneos
INSERT INTO torneo (id_categoria, nombre_torneo, estado_torneo, fecha_inicio, fecha_termino) VALUES
(1, 'Copa Apertura USACH', 'COMPLETED', '2026-02-08', '2026-02-09'),
(2, 'Open Valle Central', 'COMPLETED', '2026-02-22', '2026-02-23'),
(3, 'Torneo Costa Norte', 'COMPLETED', '2026-03-15', '2026-03-16'),
(1, 'Ranking Otono Sur', 'COMPLETED', '2026-04-12', '2026-04-13'),
(2, 'Gran Premio Metropolitano', 'COMPLETED', '2026-04-26', '2026-04-28'),
(3, 'Copa Invierno 2026', 'NOT_STARTED', '2026-06-04', '2026-06-06'),
(1, 'Abierto La Serena', 'NOT_STARTED', '2026-06-18', '2026-06-20'),
(2, 'Clasificatorio Centro', 'NOT_STARTED', '2026-07-05', '2026-07-06'),
(3, 'Copa Nacional Juvenil', 'NOT_STARTED', '2026-07-25', '2026-07-27'),
(1, 'Festival Tiro al Aire', 'NOT_STARTED', '2026-08-15', '2026-08-16');

-- Definición de Rondas por Torneo
INSERT INTO ronda (id_torneo, numero_ronda) VALUES
(1, 1), (1, 2),
(2, 1), (2, 2),
(3, 1), (3, 2),
(4, 1), (4, 2), (4, 3),
(5, 1), (5, 2), (5, 3);

-- Inscripciones (Participaciones)
INSERT INTO participacion (id_usuario, id_torneo, puntaje_final) VALUES
(2, 1, 0), (3, 1, 0), (4, 1, 0),
(3, 2, 0), (4, 2, 0), (5, 2, 0),
(2, 3, 0), (5, 3, 0), (6, 3, 0),
(2, 4, 0), (3, 4, 0), (6, 4, 0),
(3, 5, 0), (4, 5, 0), (5, 5, 0);

-- Generación de turnos (Puntaje Ronda)
INSERT INTO puntaje_ronda (id_ronda, id_participacion)
SELECT r.id_ronda, p.id_participacion
FROM participacion p
JOIN ronda r ON p.id_torneo = r.id_torneo;

-- Inserción de Flechas Aleatorias (Respetando el rango 0-10 del Trigger)
INSERT INTO flecha (id_puntaje_ronda, puntaje)
SELECT
    pr.id_puntaje_ronda,
    CASE
        WHEN (ROW_NUMBER() OVER ()) % 17 = 0 THEN 0
        WHEN (ROW_NUMBER() OVER ()) % 19 = 0 THEN 10
        ELSE FLOOR(RANDOM() * 11)::int
    END
FROM puntaje_ronda pr
CROSS JOIN generate_series(1, 3); -- 3 flechas por cada turno de ronda

-- Sincronización de totales por Ronda
UPDATE puntaje_ronda pr
SET puntaje_ronda = (
    SELECT COALESCE(SUM(f.puntaje), 0)
    FROM flecha f
    WHERE f.id_puntaje_ronda = pr.id_puntaje_ronda
);

-- Sincronización de totales por Torneo (Puntaje Final)
UPDATE participacion p
SET puntaje_final = (
    SELECT COALESCE(SUM(pr.puntaje_ronda), 0)
    FROM puntaje_ronda pr
    WHERE pr.id_participacion = p.id_participacion
);

-- Cálculo de Posición Final (Ranking) dentro de cada Torneo
WITH RankingCalculado AS (
    SELECT
        id_participacion,
        RANK() OVER (PARTITION BY id_torneo ORDER BY puntaje_final DESC) as posicion
    FROM participacion
)
UPDATE participacion p
SET posicion_final = rc.posicion
FROM RankingCalculado rc
WHERE p.id_participacion = rc.id_participacion;