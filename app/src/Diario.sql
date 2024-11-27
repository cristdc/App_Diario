CREATE DATABASE IF NOT EXISTS Diario;
USE Diario;


CREATE TABLE `Dia` (
	`fecha` DATE,
	`calidad_sueño` INTEGER,
	`clima` VARCHAR(50),
	`siesta` BOOLEAN,
	`retos` TEXT,
	PRIMARY KEY(`fecha`)
);


CREATE TABLE `Estado_de_Animo` (
	`id_estado` INTEGER AUTO_INCREMENT,
	`emoji` MEDIUMTEXT,
	`paciencia` INTEGER,
	`fuerza_sentimiento` INTEGER,
	`grado_productividad` INTEGER,
	PRIMARY KEY(`id_estado`)
);


CREATE TABLE `Dia_EstadoAnimo_CR` (
	`fecha` DATE,
	`id_estado` INTEGER,
	`momento_dia` VARCHAR(50),
	`descripcion` TEXT,
	PRIMARY KEY(`fecha`, `momento_dia`)
);


ALTER TABLE `Dia_EstadoAnimo_CR`
ADD FOREIGN KEY(`fecha`) REFERENCES `Dia`(`fecha`)
ON UPDATE NO ACTION ON DELETE CASCADE;
ALTER TABLE `Dia_EstadoAnimo_CR`
ADD FOREIGN KEY(`id_estado`) REFERENCES `Estado_de_Animo`(`id_estado`)
ON UPDATE NO ACTION ON DELETE CASCADE;