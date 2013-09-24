CREATE DATABASE  IF NOT EXISTS `ponencias` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `ponencias`;
-- MySQL dump 10.13  Distrib 5.6.11, for Win32 (x86)
--
-- Host: localhost    Database: ponencias
-- ------------------------------------------------------
-- Server version	5.6.11-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `apoyo`
--

DROP TABLE IF EXISTS `apoyo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `apoyo` (
  `idapoyo` int(11) NOT NULL AUTO_INCREMENT,
  `apoyo` varchar(45) NOT NULL,
  `estado` varchar(10) NOT NULL,
  `descripcion` varchar(600) DEFAULT NULL,
  PRIMARY KEY (`idapoyo`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `apoyo`
--

LOCK TABLES `apoyo` WRITE;
/*!40000 ALTER TABLE `apoyo` DISABLE KEYS */;
INSERT INTO `apoyo` VALUES (1,'Entidad de apoyo CIFE','No_Activo','Al momento de un participante desvincular su cuenta y volver a crear una cuenta utilizando la cedula de un participante que esta desactiva. Permite que este se active nuevamente con la implicación de que le proporciona privilegios de administrador, lo que afecta la integridad de los participantes de la plataforma de ponencias. (SAC MAYOR- priori 5)\r\nAl momento de un participante desvincular su cuenta y volver a crear una cuenta utilizando la cedula de un participante que esta desactiva. Permite que este se active nuevamente con la implicación de que le proporciona privilegios de ad'),(2,'2 Entidad de apoyo CIFE','Activo','Descrip'),(3,'apo3','Activo','sin'),(4,'apo4','No_Activo','sin'),(5,'apo5','Activo','sin'),(6,'apo6','Activo','sin'),(7,'apo7','No_Activo','sin'),(8,'apo8','No_Activo','sin'),(9,'apo9','Activo','sin'),(10,'apo10','No_Activo','sin'),(11,'apo11','Activo','sin'),(12,'apo12','No_Activo','sin');
/*!40000 ALTER TABLE `apoyo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ciudad`
--

DROP TABLE IF EXISTS `ciudad`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ciudad` (
  `idCiudad` int(11) NOT NULL AUTO_INCREMENT,
  `Nombre` varchar(45) NOT NULL,
  PRIMARY KEY (`idCiudad`)
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ciudad`
--

LOCK TABLES `ciudad` WRITE;
/*!40000 ALTER TABLE `ciudad` DISABLE KEYS */;
INSERT INTO `ciudad` VALUES (44,'Medellin');
/*!40000 ALTER TABLE `ciudad` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `departamento`
--

DROP TABLE IF EXISTS `departamento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `departamento` (
  `idDepartamento` int(11) NOT NULL AUTO_INCREMENT,
  `Nombre` varchar(45) NOT NULL,
  `Pais_idPais` int(11) NOT NULL,
  PRIMARY KEY (`idDepartamento`),
  KEY `fk_Departamento_Pais1` (`Pais_idPais`),
  CONSTRAINT `fk_Departamento_Pais1` FOREIGN KEY (`Pais_idPais`) REFERENCES `pais` (`idPais`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=75 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `departamento`
--

LOCK TABLES `departamento` WRITE;
/*!40000 ALTER TABLE `departamento` DISABLE KEYS */;
INSERT INTO `departamento` VALUES (1,'Arequipa',2),(2,'Lima',2),(3,'Quito',3),(4,'Guayaquil',3),(5,'Santiago',4),(6,'Concepcion',4),(7,'Ciudad de Mexico',11),(8,'San Luis Potosi',11),(9,'Guanajuato',11),(10,'Chiguagua',11),(11,'Bajo California',11),(12,'Morelos',11),(13,'Puebla',11),(14,'Madrid',5),(15,'Barcelona',5),(16,'Alicante',5),(17,'Granada',5),(18,'Valencia',5),(19,'Sevilla',5),(20,'Buenos Aires',6),(21,'Cordoba',6),(22,'Sao Paulo',7),(23,'Rio de Janeiro',7),(24,'Santa Catalina',7),(25,'Rio Grande del Sur',7),(26,'Ceara',7),(27,'Maranhao',7),(28,'Florida',8),(29,'New York',8),(30,'California',8),(31,'Illinois',8),(32,'Atlanta',8),(33,'Texas',8),(34,'Washington',8),(35,'Massachusetts',8),(36,'Vancouver',9),(37,'Ottawa',9),(38,'Quebec',9),(39,'Cargary',9),(40,'Lisboa',10),(41,'Coimbra',10),(42,'Porto',10),(43,'Amazonas',1),(44,'Antioquia',1),(45,'Arauca',1),(46,'Atlantico',1),(47,'Bolivar',1),(48,'Boyaca',1),(49,'Caldas',1),(50,'Caqueta',1),(51,'Casanare',1),(52,'Cauca',1),(53,'Cesar',1),(54,'Choco',1),(55,'Cordoba',1),(56,'Cundinamarca',1),(57,'Guainia',1),(58,'Guaviare',1),(59,'Huila',1),(60,'La Guajira',1),(61,'Magdalena',1),(62,'Meta',1),(63,'Nariño',1),(64,'Norte de Santander',1),(65,'Putumayo',1),(66,'Quindio',1),(67,'Risaralda',1),(68,'San Andres',1),(69,'Santander',1),(70,'Sucre',1),(71,'Tolima',1),(72,'Valle del Cauca',1),(73,'Vaupes',1),(74,'Vichada',1);
/*!40000 ALTER TABLE `departamento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `grupoinvestigacion`
--

DROP TABLE IF EXISTS `grupoinvestigacion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `grupoinvestigacion` (
  `idGrupoInvestigacion` int(11) NOT NULL AUTO_INCREMENT,
  `Nombre` varchar(45) NOT NULL,
  `Descripcion` varchar(600) DEFAULT NULL,
  `Clasificacion` varchar(45) NOT NULL,
  `estado` varchar(10) NOT NULL,
  PRIMARY KEY (`idGrupoInvestigacion`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `grupoinvestigacion`
--

LOCK TABLES `grupoinvestigacion` WRITE;
/*!40000 ALTER TABLE `grupoinvestigacion` DISABLE KEYS */;
INSERT INTO `grupoinvestigacion` VALUES (1,'Enfermeria en el contexto social','Sin descripcion','A','Activo'),(2,'Politicas Sociales y servicios de salud','Sin descripcion','A','Activo'),(3,'Emergencias y desastes','Sin descripcion','C','Activo'),(4,'Salud de las mujeres','Sin descripcion','C','Activo'),(5,'Promocion de la Salud','Sin descripcion','D','Activo'),(6,'Administrador','Sin descripcion','???','Activo'),(7,'grupo7','sin','B','Activo'),(8,'grup8','sin','A','Activo'),(9,'grup9','sin','B','Activo'),(10,'grup10','sin','C','Activo'),(11,'grup11','sin','C','Activo'),(12,'para borrar','sin','C','No_Activo');
/*!40000 ALTER TABLE `grupoinvestigacion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pais`
--

DROP TABLE IF EXISTS `pais`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pais` (
  `idPais` int(11) NOT NULL AUTO_INCREMENT,
  `Nombre` varchar(45) NOT NULL,
  PRIMARY KEY (`idPais`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pais`
--

LOCK TABLES `pais` WRITE;
/*!40000 ALTER TABLE `pais` DISABLE KEYS */;
INSERT INTO `pais` VALUES (1,'Colombia'),(2,'Peru'),(3,'Ecuador'),(4,'Chile'),(5,'España'),(6,'Argentina'),(7,'Brazil'),(8,'USA'),(9,'Canada'),(10,'Portugal'),(11,'Mexico');
/*!40000 ALTER TABLE `pais` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `participantes`
--

DROP TABLE IF EXISTS `participantes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `participantes` (
  `cedula` int(11) NOT NULL,
  `nombre` varchar(45) NOT NULL,
  `vinculacion` int(11) NOT NULL,
  `rol` int(11) NOT NULL,
  `email` varchar(60) NOT NULL,
  `contraseña` varchar(45) NOT NULL,
  `contraseñav` varchar(45) NOT NULL,
  `GrupoInvestigacion_idGrupoInvestigacion` int(11) NOT NULL,
  `apellido` varchar(45) NOT NULL,
  `estado` varchar(10) NOT NULL,
  PRIMARY KEY (`cedula`),
  KEY `fk_Participantes_vinculacion1` (`vinculacion`),
  KEY `fk_Participantes_rol1` (`rol`),
  KEY `fk_participantes_GrupoInvestigacion1` (`GrupoInvestigacion_idGrupoInvestigacion`),
  CONSTRAINT `fk_participantes_GrupoInvestigacion1` FOREIGN KEY (`GrupoInvestigacion_idGrupoInvestigacion`) REFERENCES `grupoinvestigacion` (`idGrupoInvestigacion`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Participantes_rol1` FOREIGN KEY (`rol`) REFERENCES `rol` (`idrol`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Participantes_vinculacion1` FOREIGN KEY (`vinculacion`) REFERENCES `vinculacion` (`idvinculacion`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `participantes`
--

LOCK TABLES `participantes` WRITE;
/*!40000 ALTER TABLE `participantes` DISABLE KEYS */;
INSERT INTO `participantes` VALUES (0,'Adm',3,1,'nusuga@gmail.com','1234567890','1234567890',1,'Adm','Activo'),(789,'yef',1,3,'yef@hotmail.co','123456789','123456789',2,'mar','Activo'),(1128,'yef',3,2,'yef@hotmail.co','1234qwer','1234qwer',1,'mar','Activo'),(1129,'yef2',2,1,'yef2@hotmail.co','qwer1234','qwer1234',3,'mar2','Activo'),(1130,'admCife',3,3,'udea@udea.edu.co','123456789','123456789',3,'Cife','Activo'),(1131,'1partici',2,2,'part1@gmail.com','qwertyui','qwertyui',1,'pante','Activo'),(1132,'2partici',2,2,'part2@gmail.com','1q2w3e4r','1q2w3e4r',3,'pante','Activo'),(1133,'3partici',1,2,'part3@gmail.com','123','123',2,'pante','Activo'),(1134,'4partici',1,2,'part4@gmail.com','123','123',4,'pante','Activo'),(1135,'5partici',3,2,'part5@gmail.com','qwe','qwe',6,'pante','Activo'),(1136,'6partici',3,2,'part6@gmail.com','qwe','qwe',6,'pante','Activo'),(1137,'7partici',2,2,'part7@gmail.com','123','123',3,'pante','Activo'),(1138,'8partici',1,2,'part8@gmail.com','123','123',3,'pante','No_Activo'),(1139,'9partici',2,2,'part9@gmail.com','123','123',1,'pante','Activo'),(1140,'10partici',3,2,'part10@gmail.com','123','123',12,'pante','No_Activo'),(2218,'nose',1,2,'nose@gmasil.com','1234qwer','1234qwer',1,'nose','Activo'),(1128391,'yef',1,2,'yef@hot.com','123','123',1,'mar','Activo');
/*!40000 ALTER TABLE `participantes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ponencia`
--

DROP TABLE IF EXISTS `ponencia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ponencia` (
  `idponencia` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(45) NOT NULL,
  `descripcion` varchar(600) DEFAULT NULL,
  `fechaInicial` date NOT NULL,
  `tipo_idtipo` int(11) NOT NULL,
  `participantes_cedula` int(11) NOT NULL,
  `proyecto_idproyecto` int(11) NOT NULL,
  `Ciudad_idCiudad` int(11) NOT NULL,
  `evento` varchar(45) NOT NULL,
  `apoyo_idapoyo` int(11) NOT NULL,
  PRIMARY KEY (`idponencia`),
  KEY `fk_ponencia_tipo1` (`tipo_idtipo`),
  KEY `fk_ponencia_participantes1` (`participantes_cedula`),
  KEY `fk_ponencia_proyecto1` (`proyecto_idproyecto`),
  KEY `fk_ponencia_Ciudad1` (`Ciudad_idCiudad`),
  KEY `fk_ponencia_apoyo1` (`apoyo_idapoyo`),
  CONSTRAINT `fk_ponencia_apoyo1` FOREIGN KEY (`apoyo_idapoyo`) REFERENCES `apoyo` (`idapoyo`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_ponencia_idciudad1` FOREIGN KEY (`Ciudad_idCiudad`) REFERENCES `ciudad` (`idCiudad`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_ponencia_participantes1` FOREIGN KEY (`participantes_cedula`) REFERENCES `participantes` (`cedula`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_ponencia_proyecto1` FOREIGN KEY (`proyecto_idproyecto`) REFERENCES `proyecto` (`idproyecto`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_ponencia_tipo1` FOREIGN KEY (`tipo_idtipo`) REFERENCES `tipoponencia` (`idtipo`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ponencia`
--

LOCK TABLES `ponencia` WRITE;
/*!40000 ALTER TABLE `ponencia` DISABLE KEYS */;
INSERT INTO `ponencia` VALUES (1,'cuidado al adulto','sin','2012-08-08',2,1128,4,44,'Jornadas de investigación',2),(2,'cuidado al adulto','descr','2012-08-07',2,1128,1,44,'Jornadas de investigación',2),(3,'cuidado al adulto','','2012-08-06',2,1128,2,44,'Jornadas de investigación',1),(4,'cuidado al adulto','adminis','2012-08-02',1,0,2,44,'Jornadas de investigación',1),(7,'pon5','sin','2012-09-05',1,1128,2,44,'pon5',1),(8,'name1','sin','2012-11-07',1,1128,1,44,'even2',1),(9,'p1','d1','2012-10-09',1,789,1,44,'e1',1),(10,'p2','d2','2012-09-10',1,789,2,44,'e2',2),(11,'p3','d3','2012-10-25',2,1128,3,44,'e3',2),(12,'p4','d4','2012-11-01',2,1128391,2,44,'e4',1),(13,'p5','d5','2012-11-06',2,1128,3,44,'e5',2),(14,'p6','d6','2012-11-21',1,1129,2,44,'e6',1),(15,'p7','d7','2012-11-28',1,1128,2,44,'e7',1),(16,'p8','sin','2012-11-07',1,1128,2,44,'e8',1),(17,'p9','d9','2012-11-05',1,1128,2,44,'e9',1),(18,'p10','d10','2012-11-08',1,1128,2,44,'e10',1),(19,'p11','d11','2012-11-06',1,1128391,1,44,'e11',2),(20,'p12','d12','2012-11-06',2,1128391,1,44,'e12',1),(21,'p13','d13','2012-11-06',1,1128391,1,44,'e13',1),(22,'p14','d14','2012-08-14',1,789,1,44,'e14',1),(23,'Ponencia23','sin','2013-04-01',1,1128,5,44,'evento23',2),(24,'ponencia24','sin','2013-03-11',1,789,5,44,'even24',2),(25,'ponencia25','sin','2013-04-03',2,1128,5,44,'evento25',2),(26,'ponencia26','sin','2013-03-13',2,1128,5,44,'evento26',1),(29,'p28','sin','2013-03-05',1,789,5,44,'e28',1),(30,'ponenciaeliminar','sin','2013-04-01',1,1138,2,44,'ev',12);
/*!40000 ALTER TABLE `ponencia` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `proyecto`
--

DROP TABLE IF EXISTS `proyecto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `proyecto` (
  `idproyecto` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(45) NOT NULL,
  `estado` varchar(10) NOT NULL,
  `descripcion` varchar(600) DEFAULT NULL,
  PRIMARY KEY (`idproyecto`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `proyecto`
--

LOCK TABLES `proyecto` WRITE;
/*!40000 ALTER TABLE `proyecto` DISABLE KEYS */;
INSERT INTO `proyecto` VALUES (1,'Proyecto1','No_Activo','lo que yo quiera'),(2,'Proyecto2','No_Activo',''),(3,'Proyecto3','No_Activo',''),(4,'Proyecto4','Activo',''),(5,'Proyecto5','Activo','sin'),(6,'Proyecto6','No_Activo','sin'),(7,'proy7','Activo','sin'),(8,'proy8','Activo','sin'),(9,'proy9','Activo','sin'),(10,'proy10','No_Activo','sin'),(11,'proy11','Activo','sin'),(12,'proy12','No_Activo','sin'),(13,'proy13','No_Activo','sin'),(14,'proy14','No_Activo','sin');
/*!40000 ALTER TABLE `proyecto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rol`
--

DROP TABLE IF EXISTS `rol`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rol` (
  `idrol` int(11) NOT NULL AUTO_INCREMENT,
  `rol` varchar(45) NOT NULL,
  PRIMARY KEY (`idrol`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rol`
--

LOCK TABLES `rol` WRITE;
/*!40000 ALTER TABLE `rol` DISABLE KEYS */;
INSERT INTO `rol` VALUES (1,'Administrador'),(2,'Usuario'),(3,'Administrador CIFE');
/*!40000 ALTER TABLE `rol` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tipoponencia`
--

DROP TABLE IF EXISTS `tipoponencia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tipoponencia` (
  `idtipo` int(11) NOT NULL AUTO_INCREMENT,
  `tipo` varchar(45) NOT NULL,
  PRIMARY KEY (`idtipo`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tipoponencia`
--

LOCK TABLES `tipoponencia` WRITE;
/*!40000 ALTER TABLE `tipoponencia` DISABLE KEYS */;
INSERT INTO `tipoponencia` VALUES (1,'Oral'),(2,'Poster');
/*!40000 ALTER TABLE `tipoponencia` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vinculacion`
--

DROP TABLE IF EXISTS `vinculacion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `vinculacion` (
  `idvinculacion` int(11) NOT NULL AUTO_INCREMENT,
  `vinculacion` varchar(45) NOT NULL,
  PRIMARY KEY (`idvinculacion`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1 COMMENT='	';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vinculacion`
--

LOCK TABLES `vinculacion` WRITE;
/*!40000 ALTER TABLE `vinculacion` DISABLE KEYS */;
INSERT INTO `vinculacion` VALUES (1,'Profesor'),(2,'Estudiante'),(3,'Administrativo');
/*!40000 ALTER TABLE `vinculacion` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-08-01 14:51:43
