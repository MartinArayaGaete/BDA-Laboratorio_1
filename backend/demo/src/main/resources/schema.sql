CREATE IF NOT EXISTS TABLE  categoria (
    id_categoria BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre_categoria VARCHAR(80) NOT NULL
);

CREATE IF NOT EXISTS TABLE usuario (
    id_usuario BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    rut VARCHAR(25) UNIQUE NOT NULL,
    nombre VARCHAR(80) NOT NULL,
    correo VARCHAR(80) UNIQUE NOT NULL,
    contrasena VARCHAR(255) NOT NULL,
    rol VARCHAR(80) NOT NULL
);

CREATE IF NOT EXISTS TABLE logs (
    id_logs BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_usuario BIGINT NOT NULL,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    tipo_movimiento VARCHAR(80) NOT NULL,
    descripcion TEXT NOT NULL,
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario) ON DELETE CASCADE
);

CREATE IF NOT EXISTS TABLE torneo (
    id_torneo BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_categoria BIGINT NOT NULL,
    nombre_torneo VARCHAR(80) NOT NULL,
    estado_torneo VARCHAR(80) NOT NULL,
    fecha_inicio DATE NOT NULL,
    fecha_termino DATE NOT NULL,
    FOREIGN KEY (id_categoria) REFERENCES categoria(id_categoria)
);

CREATE IF NOT EXISTS TABLE participacion (
    id_participacion BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_usuario BIGINT NOT NULL,
    id_torneo BIGINT NOT NULL,
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario) ON DELETE CASCADE,
    FOREIGN KEY (id_torneo) REFERENCES torneo(id_torneo) ON DELETE CASCADE
);

CREATE IF NOT EXISTS TABLE ronda (
    id_ronda BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_participacion BIGINT NOT NULL,
    numero_ronda INT NOT NULL,
    FOREIGN KEY (id_participacion) REFERENCES participacion(id_participacion) ON DELETE CASCADE
);

CREATE IF NOT EXISTS TABLE flecha (
    id_flecha BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_ronda BIGINT NOT NULL,
    puntaje INTEGER,
    FOREIGN KEY (id_ronda) REFERENCES ronda(id_ronda) ON DELETE CASCADE
);