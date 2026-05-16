USE BArquealia;

-- Insert some initial museums and yacimientos (id auto)
INSERT IGNORE INTO tmuseo (nombre, ciudad, pais, epoca_especializada) VALUES
('Museo de Paleolitico', 'Ciudad A', 'España', 'Paleolitico'),
('Museo de Neolitico', 'Ciudad B', 'España', 'Neolitico'),
('Museo de Calcolitico', 'Ciudad C', 'España', 'Calcolitico');

INSERT IGNORE INTO tyacimiento (nombre, ubicacion) VALUES
('La Postiga','Provincia X'),
('El Pozo','Provincia Y'),
('Paleta','Provincia Z');

-- Sample arqueólogos
INSERT IGNORE INTO tarqueologo (nombre, correo, contrasena) VALUES
('Usuario Prueba','prueba@example.com','1234');

