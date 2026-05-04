DO $$
DECLARE
    v_admin_id BIGINT;
    v_arqueros BIGINT[] := ARRAY[]::BIGINT[];
    v_torneo_id BIGINT;
    v_ronda_id BIGINT;
    v_part_id BIGINT;
    v_part_ids BIGINT[];
    v_ronda_ids BIGINT[];
    v_flechas DECIMAL[];
    i INT;
    j INT;
    p INT;
BEGIN
    -- 0. Limpieza profunda (TRUNCATE resetea las IDs a 1)
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

    -- 3. Generar 10 Arqueros de prueba
    FOR i IN 1..10 LOOP
        INSERT INTO usuario (rut, nombre, correo, contrasena, rol)
        VALUES ((10000000 + i) || '-K', 'Arquero ' || i, 'arquero' || i || '@mail.com', 'arco123', 'ARQUERO')
        RETURNING id_usuario INTO v_part_id;
        v_arqueros := v_arqueros || v_part_id;
    END LOOP;

    ---------------------------------------------------------
    -- 4. CREAR 100 TORNEOS "COMPLETED" (Pasados)
    ---------------------------------------------------------
    FOR i IN 1..100 LOOP
        -- a) Se crea temporalmente como IN_COURSE para que tu Procedimiento Almacenado lo acepte
        INSERT INTO torneo (id_categoria, nombre_torneo, estado_torneo, fecha_inicio, fecha_termino)
        VALUES (1 + (i % 3), 'Copa Histórica ' || i, 'IN_COURSE', CURRENT_DATE - i - 5, CURRENT_DATE - i - 3)
        RETURNING id_torneo INTO v_torneo_id;

        -- b) Inscribir a los arqueros 1, 2 y 3
        v_part_ids := ARRAY[]::BIGINT[];
        FOR p IN 1..3 LOOP
            INSERT INTO participacion (id_usuario, id_torneo) VALUES (v_arqueros[p], v_torneo_id)
            RETURNING id_participacion INTO v_part_id;
            v_part_ids := v_part_ids || v_part_id;
        END LOOP;

        -- c) Crear 3 Rondas para este torneo
        v_ronda_ids := ARRAY[]::BIGINT[];
        FOR j IN 1..3 LOOP
            INSERT INTO ronda (id_torneo, numero_ronda) VALUES (v_torneo_id, j)
            RETURNING id_ronda INTO v_ronda_id;
            v_ronda_ids := v_ronda_ids || v_ronda_id;
        END LOOP;

        -- d) Disparar flechas (3 flechas x ronda x arquero)
        FOR j IN 1..3 LOOP
            FOR p IN 1..3 LOOP
                -- Genera 3 puntajes aleatorios entre 0 y 10
                v_flechas := ARRAY[floor(random() * 11), floor(random() * 11), floor(random() * 11)]::DECIMAL[];

                -- ¡MAGIA! Llamamos a TU Procedimiento Almacenado para que haga los inserts, el trigger y actualice el puntaje_final
                CALL registrar_puntaje_ronda(v_ronda_ids[j], v_part_ids[p], v_flechas, v_admin_id);
            END LOOP;
        END LOOP;

        -- e) Finalizar torneo y calcular ranking llamando a tu segundo Procedimiento
        UPDATE torneo SET estado_torneo = 'COMPLETED' WHERE id_torneo = v_torneo_id;
        CALL actualizar_posiciones(v_torneo_id);
    END LOOP;

    ---------------------------------------------------------
    -- 5. CREAR 100 TORNEOS "NOT_STARTED" (Futuros)
    ---------------------------------------------------------
    FOR i IN 1..100 LOOP
        INSERT INTO torneo (id_categoria, nombre_torneo, estado_torneo, fecha_inicio, fecha_termino)
        VALUES (1 + (i % 3), 'Open Futuro ' || i, 'NOT_STARTED', CURRENT_DATE + i + 10, CURRENT_DATE + i + 12)
        RETURNING id_torneo INTO v_torneo_id;

        -- Inscribimos a los arqueros 8, 9 y 10, pero SIN flechas (están en espera)
        FOR p IN 8..10 LOOP
            INSERT INTO participacion (id_usuario, id_torneo) VALUES (v_arqueros[p], v_torneo_id);
        END LOOP;

        -- Generamos las 3 rondas en blanco
        FOR j IN 1..3 LOOP
            INSERT INTO ronda (id_torneo, numero_ronda) VALUES (v_torneo_id, j);
        END LOOP;
    END LOOP;

    ---------------------------------------------------------
    -- 6. CREAR 10 TORNEOS "IN_COURSE" (Para pruebas en vivo)
    ---------------------------------------------------------
    FOR i IN 1..10 LOOP
        INSERT INTO torneo (id_categoria, nombre_torneo, estado_torneo, fecha_inicio, fecha_termino)
        VALUES (1 + (i % 3), 'Torneo Activo ' || i, 'IN_COURSE', CURRENT_DATE - 1, CURRENT_DATE + 2)
        RETURNING id_torneo INTO v_torneo_id;

        -- Inscribimos a los arqueros 4, 5 y 6
        FOR p IN 4..6 LOOP
            INSERT INTO participacion (id_usuario, id_torneo) VALUES (v_arqueros[p], v_torneo_id);
        END LOOP;

        FOR j IN 1..3 LOOP
            INSERT INTO ronda (id_torneo, numero_ronda) VALUES (v_torneo_id, j);
        END LOOP;
    END LOOP;

END $$;

-- 7. Actualizar la vista materializada para que refleje los nuevos datos inmediatamente
REFRESH MATERIALIZED VIEW leaderboard_top_50;