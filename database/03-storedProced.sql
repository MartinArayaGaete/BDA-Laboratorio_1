-- Procedimiento almacenado 2
-- Procedimiento almacenado para actualizar las posiciones de un torneo después de que se hayan ingresado todos los puntajes de las rondas.
CREATE OR REPLACE PROCEDURE actualizar_posiciones(p_id_torneo BIGINT)
LANGUAGE plpgsql
AS $$
DECLARE
    v_estado_torneo torneo.estado_torneo%TYPE;
BEGIN
    -- Verificar que el torneo existe
    SELECT t.estado_torneo
    INTO v_estado_torneo
    FROM torneo t
    WHERE t.id_torneo = p_id_torneo;

    IF v_estado_torneo IS NULL THEN
        RAISE EXCEPTION 'El torneo no existe.';
    END IF;

    -- Verificar que el torneo ha finalizado
    IF v_estado_torneo <> 'COMPLETED' THEN
        RAISE EXCEPTION 'El torneo no ha finalizado.';
    END IF;

    -- Actualizar posiciones finales
    UPDATE participacion p
    SET posicion_final = ranking.posicion
    FROM (
        SELECT 
            id_participacion,
            DENSE_RANK() OVER (
                ORDER BY puntaje_final DESC
            ) AS posicion
        FROM participacion
        WHERE id_torneo = p_id_torneo
    ) ranking
    WHERE p.id_participacion = ranking.id_participacion;
END;
$$;