
SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema SistemaBiblioteca
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `SistemaBiblioteca` ;

-- -----------------------------------------------------
-- Schema SistemaBiblioteca
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `SistemaBiblioteca` ;
USE `SistemaBiblioteca` ;

-- -----------------------------------------------------
-- Table `SistemaBiblioteca`.`usuario`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `SistemaBiblioteca`.`usuario` ;

CREATE TABLE IF NOT EXISTS `SistemaBiblioteca`.`usuario` (
  `idUsuario` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(150) NULL,
  `dni` INT NULL,
  `email` VARCHAR(255) NULL,
  PRIMARY KEY (`idUsuario`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `SistemaBiblioteca`.`libro`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `SistemaBiblioteca`.`libro` ;

CREATE TABLE IF NOT EXISTS `SistemaBiblioteca`.`libro` (
  `idLibro` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(150) NULL,
  `autor` VARCHAR(150) NULL,
  `categoria` VARCHAR(150) NULL,
  `disponible` TINYINT NULL,
  PRIMARY KEY (`idLibro`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `SistemaBiblioteca`.`usuario_prestamo`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `SistemaBiblioteca`.`usuario_prestamo` ;

CREATE TABLE IF NOT EXISTS `SistemaBiblioteca`.`usuario_prestamo` (
  `idPrestamo` INT NOT NULL AUTO_INCREMENT,
  `idUsuario` INT NOT NULL,
  `idLibro` INT NOT NULL,
  `fechaPrestamo` DATE NULL,
  `fechaDevolucion` DATE NULL,
  PRIMARY KEY (`idPrestamo`, `idUsuario`, `idLibro`),
  INDEX `fk_usuario_has_libro_libro1_idx` (`idLibro` ASC) VISIBLE,
  INDEX `fk_usuario_has_libro_usuario_idx` (`idUsuario` ASC) VISIBLE,
  CONSTRAINT `fk_usuario_has_libro_usuario`
    FOREIGN KEY (`idUsuario`)
    REFERENCES `SistemaBiblioteca`.`usuario` (`idUsuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_usuario_has_libro_libro1`
    FOREIGN KEY (`idLibro`)
    REFERENCES `SistemaBiblioteca`.`libro` (`idLibro`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
