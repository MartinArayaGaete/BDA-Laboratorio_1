DROP TABLE IF EXISTS flecha;
DROP TABLE IF EXISTS puntaje_ronda;
DROP TABLE IF EXISTS ronda;
DROP TABLE IF EXISTS participacion;
DROP TABLE IF EXISTS torneo;
DROP TABLE IF EXISTS logs;
DROP TABLE IF EXISTS categoria;
DROP TABLE IF EXISTS usuario;

CREATE TABLE IF NOT EXISTS usuario (
    id_usuario BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    rut VARCHAR(20) NOT NULL,
    nombre VARCHAR(80) NOT NULL,
    correo VARCHAR(80) NOT NULL,
    contrasena VARCHAR(80) NOT NULL,
    rol VARCHAR(80) NOT NULL
);

CREATE TABLE IF NOT EXISTS categoria (
    id_categoria BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre_categoria VARCHAR(80) NOT NULL
);

CREATE TABLE IF NOT EXISTS logs (
    id_logs BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_admin BIGINT NOT NULL,
    id_afectado BIGINT NOT NULL,
    fecha_editado TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    torneo_afectado BIGINT NOT NULL,
    ronda_afectada BIGINT NOT NULL,
    puntaje_anterior INT,
    puntaje_nuevo INT,
    FOREIGN KEY (id_admin) REFERENCES usuario(id_usuario),
    FOREIGN KEY (id_afectado) REFERENCES usuario(id_usuario)
);

CREATE TABLE IF NOT EXISTS torneo (
    id_torneo BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_categoria BIGINT NOT NULL,
    nombre_torneo VARCHAR(80) NOT NULL,
    estado_torneo VARCHAR(80) NOT NULL,
    fecha_inicio DATE NOT NULL,
    fecha_termino DATE NOT NULL,
    FOREIGN KEY (id_categoria) REFERENCES categoria(id_categoria)
);

CREATE TABLE IF NOT EXISTS participacion (
    id_participacion BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_usuario BIGINT NOT NULL,
    id_torneo BIGINT NOT NULL,
    puntaje_final INT,
    posicion_final INT,
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario) ON DELETE CASCADE,
    FOREIGN KEY (id_torneo) REFERENCES torneo(id_torneo) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS ronda (
    id_ronda BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_torneo BIGINT NOT NULL,
    numero_ronda INT NOT NULL,
    FOREIGN KEY (id_torneo) REFERENCES torneo(id_torneo) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS puntaje_ronda (
    id_puntaje_ronda BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_ronda BIGINT NOT NULL,
    id_participacion BIGINT NOT NULL,
    puntaje_ronda INT,

    CONSTRAINT unique_ronda_participacion
    UNIQUE (id_ronda, id_participacion),

    FOREIGN KEY (id_ronda) REFERENCES ronda(id_ronda) ON DELETE CASCADE,
    FOREIGN KEY (id_participacion) REFERENCES participacion(id_participacion) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS flecha (
    id_flecha BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_puntaje_ronda BIGINT NOT NULL,
    puntaje INT NOT NULL,
    FOREIGN KEY (id_puntaje_ronda) REFERENCES puntaje_ronda(id_puntaje_ronda) ON DELETE CASCADE
);


-- Creacion de indices
CREATE INDEX idx_torneo_categoria
ON torneo(id_categoria);
CREATE INDEX idx_participacion_usuario
ON participacion(id_usuario);
CREATE INDEX idx_puntaje_ronda_participacion
ON puntaje_ronda(id_participacion);