-- MySQL dump 10.13  Distrib 8.0.19, for macos10.15 (x86_64)
--
-- Host: localhost    Database: db3
-- ------------------------------------------------------
-- Server version	8.0.19

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `document`
--

DROP TABLE IF EXISTS `document`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `document` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `date` date NOT NULL,
  `summary` varchar(128) NOT NULL,
  `type` varchar(45) NOT NULL,
  `parentid` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`),
  KEY `id_document` (`parentid`),
  CONSTRAINT `id_document` FOREIGN KEY (`parentid`) REFERENCES `subfolder` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `document`
--

LOCK TABLES `document` WRITE;
/*!40000 ALTER TABLE `document` DISABLE KEYS */;
INSERT INTO `document` VALUES (11,'Document 1','2020-12-12','first document','pdf',1),(12,'Document 2','2020-12-12','second doc','ppt',1),(13,'Document AA','2020-10-10','AA document','pdf',2),(14,'Document BB','2020-10-10','BB document','txt',2),(15,'Document 99','1999-01-01','old document \'99','docx',3),(16,'Document 00','1998-01-01','old document \'98','pages',3),(17,'Document topsecret','1996-09-02','CLASSIFIED','txt',4),(19,'RICETTA4','2030-01-01','Partiamo cuocendo gli spinaci per il ripieno, strizziamo per bene, amalgamiamo con la ricotta, l’uovo','RICETTA .pdf',2),(20,'RICETTA5','2030-01-01','Partiamo cuocendo gli spinaci per il ripieno, strizziamo per bene, amalgamiamo con la ricotta, l’uovo','RICETTA .pdf',2),(21,'Video summer \'20','2020-07-02','Riprese video 2020','video .mp4',5),(22,'Video summer \'19','2020-07-02','Riprese video 2019','video .mp4',5),(23,'Video summer \'18','2020-07-02','Riprese video 2018','video .mp4',5),(24,'Confortably numb','1987-01-06','Pink floyd','song .mp3',6),(25,'Whole lotta love','1987-01-06','Led zeppelin','song .mp3',6),(26,'Higway to hell','1987-01-06','AC/DC','song .mp3',6),(27,'Paradise city','1987-01-06','Guns \'n\' roses','song .mp3',6),(28,'GitaMontagna1','2020-01-02','foto di una giornata in montagna','photo .jpeg',7),(29,'GitaMontagna2','2020-01-02','foto di una giornata in montagna','photo .jpeg',7),(30,'GitaMare1','2020-01-02','foto di una giornata al mare','photo .jpeg',8),(31,'GitaMare2','2020-01-02','foto di una giornata in montagna','photo .jpeg',8);
/*!40000 ALTER TABLE `document` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-07-13 20:11:59
