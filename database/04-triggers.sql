------------TRIGGER 1-----------------------

CREATE OR REPLACE FUNCTION limites_puntaje_flecha_fn()
RETURNS TRIGGER AS $$
BEGIN

  IF NEW.puntaje < 0 OR NEW.puntaje > 10 THEN
    RAISE EXCEPTION 'Puntaje fuera de rango. Debe ser entre 0 y 10.';
  END IF;
  
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER limites_puntaje_flecha
BEFORE INSERT OR UPDATE ON flecha
FOR EACH ROW
EXECUTE FUNCTION limites_puntaje_flecha_fn();


------------TRIGGER 2-----------------------
CREATE OR REPLACE FUNCTION audit_puntaje_fn()
RETURNS TRIGGER AS $$
DECLARE
    admin_id BIGINT;
    ronda_id BIGINT;
BEGIN
  IF OLD.puntaje_final <> NEW.puntaje_final THEN

    admin_id := current_setting('my.user_id', true)::BIGINT;
    ronda_id := current_setting('my.ronda_id', true)::BIGINT;

    INSERT INTO logs (
        id_admin,
        id_afectado,
        torneo_afectado,
        ronda_afectada,
        puntaje_anterior,
        puntaje_nuevo,
        fecha_editado
    )
    VALUES (
        admin_id,
        OLD.id_usuario,
        OLD.id_torneo,
        ronda_id,
        OLD.puntaje_final,
        NEW.puntaje_final,
        CURRENT_TIMESTAMP
    );

  END IF;

  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER audit_puntaje
AFTER UPDATE ON participacion
FOR EACH ROW
EXECUTE FUNCTION audit_puntaje_fn();



------------TRIGGER 3-----------------------
-- Ejecutar procedimiento para calcular posiciones al completar ronda
CREATE OR REPLACE FUNCTION actualizar_posiciones_al_completar_torneo()
RETURNS TRIGGER AS $$
BEGIN
  IF OLD.estado_torneo = 'IN_COURSE'AND NEW.estado_torneo = 'COMPLETED' THEN
    CALL actualizar_posiciones(NEW.id_torneo);
  END IF;
  
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;


CREATE TRIGGER trg_actualizar_posiciones_al_completar
BEFORE UPDATE OF estado_torneo ON torneo
FOR EACH ROW
WHEN (
  OLD.estado_torneo = 'IN_COURSE'
  AND NEW.estado_torneo = 'COMPLETED'
)
EXECUTE FUNCTION actualizar_posiciones_al_completar_torneo();