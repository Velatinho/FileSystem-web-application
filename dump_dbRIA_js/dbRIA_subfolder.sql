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
-- Table structure for table `subfolder`
--

DROP TABLE IF EXISTS `subfolder`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `subfolder` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `date` date NOT NULL,
  `parentid` int NOT NULL,
  `ownerid` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`),
  KEY `id_subfolder` (`parentid`),
  KEY `id_owner_idx` (`ownerid`),
  CONSTRAINT `id_owner_sub` FOREIGN KEY (`ownerid`) REFERENCES `user` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `id_subfolder` FOREIGN KEY (`parentid`) REFERENCES `folder` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subfolder`
--

LOCK TABLES `subfolder` WRITE;
/*!40000 ALTER TABLE `subfolder` DISABLE KEYS */;
INSERT INTO `subfolder` VALUES (1,'Subfolder 11','2020-02-01',1,2),(2,'Subfolder 12','2020-02-02',1,2),(3,'Subfolder 13','2020-02-03',1,2),(4,'Subfolder 21','2020-03-01',2,2),(5,'Subfolder 31','2020-04-01',3,2),(7,'Subfolder 22','1999-05-05',2,2),(11,'My videos','1000-01-01',5,1),(12,'Music','1000-02-02',5,1),(13,'Photos','2000-01-01',5,1),(14,'FILMS','1999-01-01',6,1),(15,'Subfolder 14','1999-05-05',1,2),(16,'Subfolder 32','1999-05-05',3,2),(17,'Subfolder 23','1999-05-05',2,2),(18,'Subfolder 41','1999-05-05',4,2),(19,'Subfolder 42','1999-05-05',4,2);
/*!40000 ALTER TABLE `subfolder` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-07-13 20:13:02
