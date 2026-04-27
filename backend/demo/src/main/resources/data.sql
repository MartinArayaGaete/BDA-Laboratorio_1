INSERT INTO usuario (rut, nombre, correo, contrasena, rol)
VALUES ('11111111-1', 'Juan Perez', 'juan@mail.com', 'hola123', 'ADMIN')
ON CONFLICT (rut) DO NOTHING;