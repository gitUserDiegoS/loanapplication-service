-- -----------------------------------------------------
-- Schema authentication
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `loanapplication-db`;

CREATE SCHEMA `loanapplication-db`;
USE `loanapplication-db`;

-- -----------------------------------------------------
-- Table `loanapplication-db`.`estado`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `loanapplication-db`.`estado` (
  `id_estado` BIGINT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(255) NOT NULL,
  `descripcion` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id_estado`)
  )
ENGINE=InnoDB
AUTO_INCREMENT = 1;

-- -----------------------------------------------------
-- Table `authentication-db`.`tipo-prestamo`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `loanapplication-db`.`tipo_prestamo` (
  `id_tipo_prestamo` BIGINT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(255) NOT NULL,
  `monto_minimo` DECIMAL(10,0) NULL DEFAULT NULL,
  `monto_maximo` DECIMAL(10,0)  NULL DEFAULT NULL,
  `tasa_interes` DECIMAL(5,2) NULL DEFAULT NULL,
  `validacion_automatica` BOOLEAN NULL DEFAULT NULL,
  PRIMARY KEY (`id_tipo_prestamo`)
  )
ENGINE=InnoDB
AUTO_INCREMENT = 1;

-- -----------------------------------------------------
-- Table `authentication-db`.`solicitud`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `loanapplication-db`.`solicitud` (
  `id_solicitud` BIGINT NOT NULL AUTO_INCREMENT,
  `monto` DECIMAL(10,0)  NOT NULL,
  `plazo` INTEGER NULL DEFAULT NULL,
  `email` VARCHAR(255) NULL DEFAULT NULL,
  `id_estado` BIGINT NOT NULL DEFAULT 1,
  `id_tipo_prestamo` BIGINT NOT NULL,
  PRIMARY KEY (`id_solicitud`),
  KEY `FK_id_estado` (`id_estado`),
  CONSTRAINT `FK_id_estado` FOREIGN KEY (`id_estado`) REFERENCES `estado` (`id_estado`),
  KEY `FK_id_tipo_prestamo` (`id_tipo_prestamo`),
  CONSTRAINT `FK_id_tipo_prestamo` FOREIGN KEY (`id_tipo_prestamo`) REFERENCES `tipo_prestamo` (`id_tipo_prestamo`)
  )
ENGINE=InnoDB
AUTO_INCREMENT = 1;


INSERT INTO tipo_prestamo values(1,"Personal",500000,50000000,20,true);
INSERT INTO tipo_prestamo values(2,"Hipotecario",500000,50000000,25,true);
INSERT INTO tipo_prestamo values(3,"Vehiculo",500000,50000000,30,true);
INSERT INTO tipo_prestamo values(4,"Libre Inversión",500000,50000000,35,true);
INSERT INTO tipo_prestamo values(5,"Microcredito",500000,50000000,15,true);



INSERT INTO estado values(1,"Pendiente de revisión","Solicitud pendiente de revisión");
INSERT INTO estado values(2,"Aprobado","Solicitud aprobada");
INSERT INTO estado values(3,"Rechazado","Solicitud rechazada");
INSERT INTO estado values(4,"Desembolsado","Prestamo desembolsado");
INSERT INTO estado values(5,"Cancelado","Prestamo cancelado");


commit;