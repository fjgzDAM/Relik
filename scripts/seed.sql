USE BRelik;

-- 1. Arqueólogos y Administradores de prueba
INSERT IGNORE INTO tarqueologo (id_arqueologo, nombre, apellidos, especialidad, correo, contrasena, rol) VALUES
(1, 'Administrador', 'General', 'Sistemas y Dirección', 'admin@relik.com', 'admin', 'ADMIN'),
(2, 'Elena', 'Ramos', 'Estratigrafía y Cerámica', 'elena.ramos@relik.com', '1234', 'ARQUEOLOGO'),
(3, 'Carlos', 'Mendoza', 'Antropología Física', 'carlos.mendoza@relik.com', '1234', 'ARQUEOLOGO'),
(4, 'Usuario', 'Prueba', 'Prospección de Campo', 'prueba@example.com', '1234', 'ARQUEOLOGO');

-- 2. Museos asignados por época prehistórica e histórica
INSERT IGNORE INTO tmuseo (id_museo, nombre, ciudad, pais, epoca_especializada) VALUES
(1, 'Museo Arqueológico Nacional', 'Madrid', 'España', 'Paleolitico'),
(2, 'Museo Nacional de Altamira', 'Santillana del Mar', 'España', 'Paleolitico'),
(3, 'Museo Arqueológico de Sevilla', 'Sevilla', 'España', 'Romana'),
(4, 'Museo de la Prehistoria de Valencia', 'Valencia', 'España', 'Neolitico'),
(5, 'Museo Monográfico de Atapuerca', 'Ibeas de Juarros', 'España', 'Calcolitico');

-- 3. Yacimientos Arqueológicos de Campo con ubicación, coordenadas, época y fecha
INSERT IGNORE INTO tyacimiento (id_yacimiento, nombre, ubicacion, coordenadas, epoca, fecha_descubrimiento, fecha_inicio) VALUES
(1, 'Gran Dolina (Atapuerca)', 'Burgos, Castilla y León', '42.3514 N, -3.5182 W', 'Paleolitico', '1978-07-10', '1978-07-10'),
(2, 'Cueva de Altamira', 'Santillana del Mar, Cantabria', '43.3772 N, -4.1225 W', 'Paleolitico', '1879-11-20', '1879-11-20'),
(3, 'Conjunto Arqueológico de Itálica', 'Santiponce, Sevilla', '37.4442 N, -6.0441 W', 'Romana', '1781-04-12', '1781-04-12'),
(4, 'Yacimiento de Los Millares', 'Santa Fe de Mondújar, Almería', '36.9664 N, -2.5273 W', 'Calcolitico', '1891-09-15', '1891-09-15'),
(5, 'Cova de l\'Or', 'Beniarrés, Alicante', '38.8153 N, -0.3781 W', 'Neolitico', '1955-05-18', '1955-05-18');

-- 4. Restos Materiales hallados y vinculados a su museo correspondiente
INSERT IGNORE INTO tresto_material (id_resto, nombre, epoca, tipologia, id_museo) VALUES
(1, 'Bifaz Acheliense "Excalibur"', 'Paleolitico', 'Herramienta de piedra cuarcita tallada', 1),
(2, 'Pigmento Rupestre de Bizonte', 'Paleolitico', 'Arte parietal con óxido de hierro', 2),
(3, 'Mosaico Polícromo de Neptuno', 'Romana', 'Pavimento de tesserae romanas', 3),
(4, 'Vasija Cerámica Impresa Cardial', 'Neolitico', 'Recipiente cerámico con impresiones', 4),
(5, 'Cuchillo Metalúrgico de Cobre', 'Calcolitico', 'Artefacto de cobre fundido primario', 5);

-- 5. Registros de Hallazgos a pie de campo (Arqueólogo + Yacimiento + Resto Material + Campaña + Micro-localización 3D + UE)
INSERT IGNORE INTO thallazgo (id_hallazgo, fecha_hallazgo, campana, cuadricula, coordenada_x, coordenada_y, cota_z, unidad_estratigrafica, id_arqueologo, id_yacimiento, id_resto) VALUES
(1, '2026-06-10 10:30:00', 'Campaña Anual 2026', 'Cuadrícula A1', '0.45m', '1.20m', '-1.85m', 'UE-102', 2, 1, 1),
(2, '2026-06-15 11:45:00', 'Campaña Verano 2026', 'Cuadrícula B3', '0.90m', '0.65m', '-2.40m', 'UE-105', 3, 2, 2),
(3, '2026-07-01 09:15:00', 'Campaña Anual 2026', 'Sector C2', '1.15m', '2.05m', '-0.90m', 'UE-201', 2, 3, 3),
(4, '2026-07-05 16:20:00', 'Campaña Urgencia 2026', 'Cuadrícula D4', '0.30m', '1.80m', '-3.10m', 'UE-304', 3, 4, 4),
(5, '2026-07-12 12:00:00', 'Campaña Verano 2026', 'Cuadrícula A2', '1.50m', '0.40m', '-0.75m', 'UE-108', 4, 5, 5);
