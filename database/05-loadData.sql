DO $$
DECLARE
    v_admin_id BIGINT;
    v_torneo_id BIGINT;
    v_ronda_id BIGINT;
    v_part_id BIGINT;
    v_ronda_ids BIGINT[];
    v_flechas DECIMAL[];
    v_fecha_inicio DATE;
    v_fecha_termino DATE;
    i INT;
    j INT;
    rec RECORD;
BEGIN
    -- 0. Limpieza profunda
    TRUNCATE TABLE flecha, puntaje_ronda, ronda, participacion, torneo, usuario, categoria, logs RESTART IDENTITY CASCADE;

    -- 1. Insertar Categorías
    INSERT INTO categoria (nombre_categoria) VALUES
    ('Recurvo 18m Novatos'),
    ('Recurvo 70m Avanzado'),
    ('Compuesto 50m Libre');

    -- 2. Insertar Administrador principal
    INSERT INTO usuario (rut, nombre, correo, contrasena, rol)
    VALUES ('1111111-1', 'Admin Supremo', 'admin@sistema.cl', 'admin123', 'ADMIN')
    RETURNING id_usuario INTO v_admin_id;

    -- 3. Generar 80 Arqueros de prueba (necesario para probar el límite del Top 50)
    FOR i IN 1..80 LOOP
        INSERT INTO usuario (rut, nombre, correo, contrasena, rol)
        VALUES ((10000000 + i) || '-K', 'Arquero ' || i, 'arquero' || i || '@mail.com', 'arco123', 'ARQUERO');
    END LOOP;

    ---------------------------------------------------------
    -- 4. CREAR 150 TORNEOS "COMPLETED" (50 Recientes, 100 Históricos)
    ---------------------------------------------------------
    FOR i IN 1..150 LOOP
        -- Distribuir fechas para probar Requisito 9 (Último mes) vs Requisito 7 (Histórico)
        IF i <= 50 THEN
            -- Torneos del ÚLTIMO MES (entre hace 1 y 28 días)
            v_fecha_termino := CURRENT_DATE - (floor(random() * 28) + 1)::INT;
        ELSE
            -- Torneos HISTÓRICOS (entre hace 35 y 200 días)
            v_fecha_termino := CURRENT_DATE - (floor(random() * 165) + 35)::INT;
        END IF;
        v_fecha_inicio := v_fecha_termino - 2;

        -- Crear torneo temporalmente EN CURSO
        INSERT INTO torneo (id_categoria, nombre_torneo, estado_torneo, fecha_inicio, fecha_termino)
        VALUES (1 + (i % 3), 'Copa Nacional ' || i, 'IN_COURSE', v_fecha_inicio, v_fecha_termino)
        RETURNING id_torneo INTO v_torneo_id;

        -- Crear 3 Rondas para este torneo
        v_ronda_ids := ARRAY[]::BIGINT[];
        FOR j IN 1..3 LOOP
            INSERT INTO ronda (id_torneo, numero_ronda) VALUES (v_torneo_id, j)
            RETURNING id_ronda INTO v_ronda_id;
            v_ronda_ids := v_ronda_ids || v_ronda_id;
        END LOOP;

        -- Seleccionar 8 arqueros al azar para este torneo
        FOR rec IN (SELECT id_usuario FROM usuario WHERE rol = 'ARQUERO' ORDER BY random() LIMIT 8) LOOP
            -- Inscribir
            INSERT INTO participacion (id_usuario, id_torneo) VALUES (rec.id_usuario, v_torneo_id)
            RETURNING id_participacion INTO v_part_id;

            -- Registrar puntajes en las 3 rondas
            FOR j IN 1..3 LOOP
                -- Factor de habilidad (los arqueros con ID múltiplo de 5 tendrán puntajes más altos)
                v_flechas := ARRAY[
                    LEAST(10, floor(random() * 4) + (rec.id_usuario % 6) + 3),
                    LEAST(10, floor(random() * 4) + (rec.id_usuario % 6) + 3),
                    LEAST(10, floor(random() * 4) + (rec.id_usuario % 6) + 3)
                ]::DECIMAL[];

                CALL registrar_puntaje_ronda(v_ronda_ids[j], v_part_id, v_flechas, v_admin_id);
            END LOOP;
        END LOOP;

        -- Finalizar torneo y calcular ranking real
        UPDATE torneo SET estado_torneo = 'COMPLETED' WHERE id_torneo = v_torneo_id;
        CALL actualizar_posiciones(v_torneo_id);
    END LOOP;

    ---------------------------------------------------------
    -- 5. CREAR 30 TORNEOS "NOT_STARTED" (Disponibles para inscribirse)
    ---------------------------------------------------------
    FOR i IN 1..30 LOOP
        INSERT INTO torneo (id_categoria, nombre_torneo, estado_torneo, fecha_inicio, fecha_termino)
        VALUES (1 + (i % 3), 'Open Futuro ' || i, 'NOT_STARTED', CURRENT_DATE + i + 10, CURRENT_DATE + i + 12)
        RETURNING id_torneo INTO v_torneo_id;

        -- Pre-inscribir a 3 arqueros al azar (solo inscripción, sin flechas)
        FOR rec IN (SELECT id_usuario FROM usuario WHERE rol = 'ARQUERO' ORDER BY random() LIMIT 3) LOOP
            INSERT INTO participacion (id_usuario, id_torneo) VALUES (rec.id_usuario, v_torneo_id);
        END LOOP;

        FOR j IN 1..3 LOOP
            INSERT INTO ronda (id_torneo, numero_ronda) VALUES (v_torneo_id, j);
        END LOOP;
    END LOOP;

    ---------------------------------------------------------
    -- 6. CREAR 10 TORNEOS "IN_COURSE" (Para pruebas del Admin gestionando flechas)
    ---------------------------------------------------------
    FOR i IN 1..10 LOOP
        INSERT INTO torneo (id_categoria, nombre_torneo, estado_torneo, fecha_inicio, fecha_termino)
        VALUES (1 + (i % 3), 'Torneo Activo ' || i, 'IN_COURSE', CURRENT_DATE - 1, CURRENT_DATE + 2)
        RETURNING id_torneo INTO v_torneo_id;

        FOR rec IN (SELECT id_usuario FROM usuario WHERE rol = 'ARQUERO' ORDER BY random() LIMIT 5) LOOP
            INSERT INTO participacion (id_usuario, id_torneo) VALUES (rec.id_usuario, v_torneo_id);
        END LOOP;

        FOR j IN 1..3 LOOP
            INSERT INTO ronda (id_torneo, numero_ronda) VALUES (v_torneo_id, j);
        END LOOP;
    END LOOP;

END $$;

-- 7. Actualizar la vista materializada
REFRESH MATERIALIZED VIEW leaderboard_top_50;