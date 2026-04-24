
CREATE TABLE IF NOT EXISTS usuario (
    id_usuario BIGINT PRIMARY KEY,
    rut VARCHAR(20) NOT NULL,
    nombre VARCHAR(80) NOT NULL,
    correo VARCHAR(80) NOT NULL,
    contrasena VARCHAR(80) NOT NULL,
    rol VARCHAR(80) NOT NULL
);

CREATE TABLE IF NOT EXISTS categoria (
    id_categoria BIGINT PRIMARY KEY,
    nombre_categoria VARCHAR(80) NOT NULL
);

CREATE TABLE IF NOT EXISTS logs (
    id_logs BIGINT PRIMARY KEY,
    id_usuario BIGINT NOT NULL,
    fecha DATE NOT NULL,
    tipo_movimiento VARCHAR(80) NOT NULL,
    descripcion TEXT NOT NULL,
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
);

CREATE TABLE IF NOT EXISTS torneo (
    id_torneo BIGINT PRIMARY KEY,
    id_categoria BIGINT NOT NULL,
    nombre_torneo VARCHAR(80) NOT NULL,
    estado_torneo VARCHAR(80) NOT NULL,
    fecha_inicio DATE NOT NULL,
    fecha_termino DATE NOT NULL,
    FOREIGN KEY (id_categoria) REFERENCES categoria(id_categoria)
);

CREATE TABLE IF NOT EXISTS participacion (
    id_participacion BIGINT PRIMARY KEY,
    id_usuario BIGINT NOT NULL,
    id_torneo BIGINT NOT NULL,
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario),
    FOREIGN KEY (id_torneo) REFERENCES torneo(id_torneo)
);

CREATE TABLE IF NOT EXISTS ronda (
    id_ronda BIGINT PRIMARY KEY,
    id_participacion BIGINT NOT NULL,
    numero_ronda INT NOT NULL,
    FOREIGN KEY (id_participacion) REFERENCES participacion(id_participacion)
);

CREATE TABLE IF NOT EXISTS flecha (
    id_flecha BIGINT PRIMARY KEY,
    id_ronda BIGINT NOT NULL,
    puntaje DECIMAL,
    FOREIGN KEY (id_ronda) REFERENCES ronda(id_ronda)
);