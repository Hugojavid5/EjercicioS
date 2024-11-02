DROP SCHEMA IF EXISTS `animales`;

CREATE SCHEMA IF NOT EXISTS `animales` DEFAULT CHARACTER SET latin1 COLLATE latin1_spanish_ci;
USE `animales`;

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;

START TRANSACTION;

DROP TABLE IF EXISTS `animales`;

CREATE TABLE IF NOT EXISTS `animales` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `nombre` varchar(50) NOT NULL,
    `especie` varchar(50) NOT NULL,
    `raza` varchar(50) NULL,
    `sexo` enum('Macho', 'Hembra') NOT NULL,
    `edad` int(3) NULL,
    `peso` decimal(5,2) NULL,
    `observaciones` text NULL,
    `fecha_primera_consulta` date NOT NULL,
    `foto` blob NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;