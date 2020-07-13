-- MySQL dump 10.13  Distrib 8.0.19, for macos10.15 (x86_64)
--
-- Host: localhost    Database: dbRIA
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
  `ownerid` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`),
  KEY `id_document` (`parentid`),
  KEY `id_owner_doc_idx` (`ownerid`),
  CONSTRAINT `id_document` FOREIGN KEY (`parentid`) REFERENCES `subfolder` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `id_owner_doc` FOREIGN KEY (`ownerid`) REFERENCES `user` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=88 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `document`
--

LOCK TABLES `document` WRITE;
/*!40000 ALTER TABLE `document` DISABLE KEYS */;
INSERT INTO `document` VALUES (11,'Document 1','2020-12-12','first document','pdf',7,2),(12,'Document 2','2020-12-12','second doc','ppt',3,2),(13,'Document AA','2020-10-10','AA document','pdf',5,2),(14,'Document BB','2020-10-10','BB document','txt',5,2),(15,'Document 99','1999-01-01','old document \'99','docx',3,2),(16,'Document 00','1998-01-01','old document \'98','pages',5,2),(17,'Document topsecret','1996-09-02','CLASSIFIED','txt',4,2),(22,'DOCUMENTO 103','1200-09-09','prova di documento, testo manoscritto','undefined',7,2),(33,'TESI1','1111-11-11','Lorem ipsum dolor sit amet, consectetur adipisci elit, sed eiusmod tempor incidunt ut labore et dolore magna aliqua.','latin document',1,2),(34,'TESI2','1111-11-11','Lorem ipsum dolor sit amet, consectetur adipisci elit, sed eiusmod tempor incidunt ut labore et dolore magna aliqua.','LATIN DOCUMENT',1,2),(35,'TESI3','1111-11-11','Lorem ipsum dolor sit amet, consectetur adipisci elit, sed eiusmod tempor incidunt ut labore et dolore magna aliqua.','LATIN DOCUMENT',1,2),(39,'TESI','1111-11-11','Lorem ipsum dolor sit amet, consectetur adipisci elit, sed eiusmod tempor incidunt ut labore et dolore magna aliqua.','LATIN DOCUMENT',1,2),(40,'RICETTA1','2030-01-01','Partiamo cuocendo gli spinaci per il ripieno, strizziamo per bene, amalgamiamo con la ricotta, l’uovo','RICETTA .pdf',2,2),(42,'RICETTA3','2030-01-01','Partiamo cuocendo gli spinaci per il ripieno, strizziamo per bene, amalgamiamo con la ricotta, l’uovo','RICETTA .pdf',2,2),(43,'RICETTA4','2030-01-01','Partiamo cuocendo gli spinaci per il ripieno, strizziamo per bene, amalgamiamo con la ricotta, l’uovo','RICETTA .pdf',2,2),(44,'RICETTA5','2030-01-01','Partiamo cuocendo gli spinaci per il ripieno, strizziamo per bene, amalgamiamo con la ricotta, l’uovo','RICETTA .pdf',2,2),(47,'Video summer \'20','2020-07-02','Riprese video 2020','video .mp4',11,1),(48,'Video summer \'19','2020-07-02','Riprese video 2019','video .mp4',11,1),(49,'Video summer \'18','2020-07-02','Riprese video 2018','video .mp4',11,1),(50,'Video summer \'17','2020-07-02','Riprese video 2017','video .mp4',11,1),(51,'Video summer \'16','2020-07-02','Riprese video 2016','video .mp4',11,1),(52,'Confortably numb','1987-01-06','Pink floyd','song .mp3',12,1),(53,'Whole lotta love','1987-01-06','Led zeppelin','song .mp3',12,1),(54,'Higway to hell','1987-01-06','AC/DC','song .mp3',12,1),(55,'Paradise city','1987-01-06','Guns \'n\' roses','song .mp3',12,1),(56,'GitaMontagna1','2020-01-02','foto di una giornata in montagna','photo .jpeg',13,1),(57,'GitaMontagna2','2020-01-02','foto di una giornata in montagna','photo .jpeg',13,1),(58,'GitaMontagna3','2020-01-02','foto di una giornata in montagna','photo .jpeg',13,1),(59,'GitaMontagna4','2020-01-02','foto di una giornata in montagna','photo .jpeg',13,1),(60,'GitaMare1','2020-01-02','foto di una giornata al mare','photo .jpeg',13,1),(61,'GitaMare2','2020-01-02','foto di una giornata in montagna','photo .jpeg',13,1),(67,'DOCUMENTO 101','1200-09-09','prova di documento, testo manoscritto','.DOCX',7,2),(68,'DOCUMENTO 102','1200-09-09','prova di documento, testo manoscritto','.DOCX',7,2),(69,'DOCUMENTO 105','1200-09-09','prova di documento, testo manoscritto','.DOCX',7,2),(70,'DOCUMENTO 106','1200-09-09','prova di documento, testo manoscritto','.DOCX',7,2),(72,'DOCUMENTO 107','1200-09-09','prova di documento, testo manoscritto','.DOCX',7,2),(73,'FILM1','1888-06-06','The button number that was pressed when the mouse event was fired: Left button=0','film .MPEG',14,1),(74,'FILM2','1888-06-06','The button number that was pressed when the mouse event was fired: Left button=0','film .MPEG',14,1),(75,'FILM3','1888-06-06','The button number that was pressed when the mouse event was fired: Left button=0','film .MPEG',14,1),(76,'FILM4','1888-06-06','The button number that was pressed when the mouse event was fired: Left button=0','film .MPEG',14,1),(77,'FILM5','1888-06-06','The button number that was pressed when the mouse event was fired: Left button=0','film .MPEG',14,1),(78,'GitaMontagna10','2020-01-02','foto di una giornata in montagna','photo .jpeg',17,2),(79,'GitaMontagna20','2020-01-02','foto di una giornata in montagna','photo .jpeg',17,2),(82,'GitaLago1','2020-01-02','giornata al lago: album di famiglia','photo .png',18,2),(83,'GitaLago2','2020-01-02','giornata al lago: album di famiglia','photo .png',18,2),(84,'GitaLago3','2020-01-02','giornata al lago: album di famiglia','photo .png',16,2),(85,'GitaLago4','2020-01-02','giornata al lago: album di famiglia','photo .png',19,2),(86,'GitaLago5','2020-01-02','giornata al lago: album di famiglia','photo .png',16,2);
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

-- Dump completed on 2020-07-13 20:13:01
