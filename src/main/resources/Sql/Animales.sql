DROP SCHEMA IF EXISTS `animales`;
CREATE SCHEMA `animales`;
USE `animales`;
CREATE TABLE `Animales` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) DEFAULT NULL,
  `especie` varchar(50) DEFAULT NULL,
  `raza` varchar(50) DEFAULT NULL,
  `sexo` varchar(50) DEFAULT NULL,
  `edad` int(3) DEFAULT NULL,
  `peso` int(5) DEFAULT NULL,
  `observaciones` varchar(100) DEFAULT NULL,
  `fecha_primera_consulta` date DEFAULT NULL,
  `foto` blob DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;