DROP TABLE IF EXISTS flecha CASCADE;
DROP TABLE IF EXISTS ronda CASCADE;
DROP TABLE IF EXISTS participacion CASCADE;
DROP TABLE IF EXISTS torneo CASCADE;
DROP TABLE IF EXISTS logs CASCADE;
DROP TABLE IF EXISTS usuario CASCADE;
DROP TABLE IF EXISTS categoria CASCADE;

CREATE TABLE IF NOT EXISTS categoria (
    id_categoria BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre_categoria VARCHAR(80) NOT NULL
);

CREATE TABLE IF NOT EXISTS usuario (
    id_usuario BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    rut VARCHAR(25) UNIQUE NOT NULL,
    nombre VARCHAR(80) NOT NULL,
    correo VARCHAR(80) UNIQUE NOT NULL,
    contrasena VARCHAR(255) NOT NULL,
    rol VARCHAR(80) NOT NULL
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
    puntaje_final INT DEFAULT 0,
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario) ON DELETE CASCADE,
    FOREIGN KEY (id_torneo) REFERENCES torneo(id_torneo) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS ronda (
    id_ronda BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_torneo BIGINT NOT NULL,
    numero_ronda INT NOT NULL,
    FOREIGN KEY (id_torneo) REFERENCES torneo(id_torneo) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS flecha (
    id_flecha BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_participacion BIGINT NOT NULL,
    id_ronda BIGINT NOT NULL,
    puntaje INT NOT NULL,
    FOREIGN KEY (id_participacion) REFERENCES participacion(id_participacion) ON DELETE CASCADE,
    FOREIGN KEY (id_ronda) REFERENCES ronda(id_ronda) ON DELETE CASCADE
);