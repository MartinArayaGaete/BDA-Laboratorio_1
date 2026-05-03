-- Leaderboard Historico -> Promedio de puntos por flecha de los 50 arqueros -> Actualizacion diaria
CREATE MATERIALIZED VIEW leaderboard_top_50 AS
SELECT u.id_usuario, u.nombre, COALESCE(AVG(f.puntaje), 0) AS promedio_puntos_flecha
FROM usuario u
JOIN participacion p ON p.id_usuario = u.id_usuario
JOIN puntaje_ronda pr ON pr.id_participacion = p.id_participacion
JOIN flecha f ON f.id_puntaje_ronda = pr.id_puntaje_ronda
WHERE p.posicion_final IS NOT NULL
GROUP BY u.id_usuario, u.nombre
ORDER BY promedio_puntos_flecha DESC
LIMIT 50;