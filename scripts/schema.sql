-- Schema for Relik (MySQL)
-- Ajusta el nombre de la base de datos si procede
CREATE DATABASE IF NOT EXISTS BRelik CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE BRelik;

-- Table: tarqueologo
CREATE TABLE IF NOT EXISTS tarqueologo (
  id_arqueologo INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(150) NOT NULL,
  apellidos VARCHAR(150) DEFAULT '',
  especialidad VARCHAR(150) DEFAULT 'Arqueología General',
  correo VARCHAR(150) NOT NULL,
  contrasena VARCHAR(150) NOT NULL,
  rol VARCHAR(50) NOT NULL DEFAULT 'ARQUEOLOGO'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Table: tyacimiento
CREATE TABLE IF NOT EXISTS tyacimiento (
  id_yacimiento INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(200) NOT NULL,
  ubicacion VARCHAR(255),
  coordenadas VARCHAR(255),
  epoca VARCHAR(100) DEFAULT 'General',
  fecha_descubrimiento DATE,
  fecha_inicio DATE,
  fecha_fin DATE,
  UNIQUE KEY ux_tyacimiento_nombre (nombre)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Table: tmuseo
CREATE TABLE IF NOT EXISTS tmuseo (
  id_museo INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(200) NOT NULL,
  ciudad VARCHAR(100),
  pais VARCHAR(100),
  epoca_especializada VARCHAR(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Table: tresto_material
CREATE TABLE IF NOT EXISTS tresto_material (
  id_resto INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(200) NOT NULL,
  epoca VARCHAR(100) NOT NULL,
  tipologia VARCHAR(100) NOT NULL,
  id_museo INT NOT NULL,
  CONSTRAINT fk_resto_museo FOREIGN KEY (id_museo) REFERENCES tmuseo(id_museo) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Table: thallazgo
CREATE TABLE IF NOT EXISTS thallazgo (
  id_hallazgo INT AUTO_INCREMENT PRIMARY KEY,
  fecha_hallazgo DATETIME NOT NULL,
  campana VARCHAR(100) DEFAULT 'Campaña 2026',
  cuadricula VARCHAR(50) DEFAULT 'S/C',
  coordenada_x VARCHAR(50) DEFAULT '0.0m',
  coordenada_y VARCHAR(50) DEFAULT '0.0m',
  cota_z VARCHAR(50) DEFAULT '0.0m',
  unidad_estratigrafica VARCHAR(50) DEFAULT 'UE-100',
  id_arqueologo INT NOT NULL,
  id_yacimiento INT NOT NULL,
  id_resto INT NOT NULL,
  CONSTRAINT fk_hallazgo_arqueologo FOREIGN KEY (id_arqueologo) REFERENCES tarqueologo(id_arqueologo) ON DELETE RESTRICT,
  CONSTRAINT fk_hallazgo_yacimiento FOREIGN KEY (id_yacimiento) REFERENCES tyacimiento(id_yacimiento) ON DELETE RESTRICT,
  CONSTRAINT fk_hallazgo_resto FOREIGN KEY (id_resto) REFERENCES tresto_material(id_resto) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

