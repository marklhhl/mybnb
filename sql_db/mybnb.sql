-- MySQL dump 10.13  Distrib 5.7.26, for Win64 (x86_64)
--
-- Host: localhost    Database: mybnb
-- ------------------------------------------------------
-- Server version	5.7.26

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
-- Table structure for table `calendar`
--

DROP TABLE IF EXISTS `calendar`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `calendar` (
  `Caid` int(11) NOT NULL AUTO_INCREMENT,
  `avaliable_from` date NOT NULL,
  `avaliable_till` date NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `listing_id` int(11) NOT NULL,
  PRIMARY KEY (`Caid`),
  KEY `Listing Id_idx` (`listing_id`),
  CONSTRAINT `Listing Id` FOREIGN KEY (`listing_id`) REFERENCES `listing` (`Lid`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=334 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `mybnb`.`calendar_BEFORE_INSERT` BEFORE INSERT ON `calendar` FOR EACH ROW
BEGIN
declare msg varchar(255);
if new.avaliable_from > new.avaliable_till then
set msg = 'Constraint violated: avaliable_from <= avaliable_till';
signal sqlstate '45000' set message_text = msg;
end if;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `history`
--

DROP TABLE IF EXISTS `history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `history` (
  `Hsid` int(11) NOT NULL AUTO_INCREMENT,
  `start` date NOT NULL,
  `end` date NOT NULL,
  `transaction_date` date NOT NULL,
  `cancelation_date` date DEFAULT NULL,
  `cost_per_day` decimal(10,2) NOT NULL,
  `total_cost` decimal(10,2) NOT NULL,
  `status` varchar(9) NOT NULL,
  `host_id` int(11) NOT NULL,
  `renter_id` int(11) NOT NULL,
  `list_id` int(11) NOT NULL,
  PRIMARY KEY (`Hsid`),
  KEY `renter_id_idx` (`renter_id`),
  KEY `list_idx` (`list_id`),
  KEY `host_idx` (`host_id`),
  CONSTRAINT `host` FOREIGN KEY (`host_id`) REFERENCES `user` (`Uid`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `list` FOREIGN KEY (`list_id`) REFERENCES `listing` (`Lid`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `renter` FOREIGN KEY (`renter_id`) REFERENCES `user` (`Uid`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=10000 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `mybnb`.`history_BEFORE_INSERT` BEFORE INSERT ON `history` FOR EACH ROW
BEGIN
declare msg varchar(255);
if new.status != 'Pending' AND new.status != 'Completed' AND new.status != 'Canceled' then
set msg = 'Constraint violated: status must be "Pending" or "Completed" or "Canceled"';
signal sqlstate '45000' set message_text = msg;
end if;
if new.start > new.end then
set msg = 'Constraint violated: start <= end';
signal sqlstate '45000' set message_text = msg;
end if;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `list_comment`
--

DROP TABLE IF EXISTS `list_comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `list_comment` (
  `Cid` int(11) NOT NULL AUTO_INCREMENT,
  `rating` decimal(2,1) NOT NULL,
  `date` date NOT NULL,
  `comment` varchar(500) NOT NULL,
  `comment_writer` int(11) NOT NULL,
  `comment_to_list` int(11) NOT NULL,
  PRIMARY KEY (`Cid`),
  KEY `writer_idx` (`comment_writer`),
  KEY `to_list_idx` (`comment_to_list`),
  CONSTRAINT `comment_writer2` FOREIGN KEY (`comment_writer`) REFERENCES `user` (`Uid`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `list_comment` FOREIGN KEY (`comment_to_list`) REFERENCES `listing` (`Lid`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=556 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `mybnb`.`comment_BEFORE_INSERT` BEFORE INSERT ON `list_comment` FOR EACH ROW
BEGIN
declare msg varchar(255);
if new.rating < 1 or new.rating > 5 then
set msg = 'Constraint violated: rating >= 1 AND <= 5';
signal sqlstate '45000' set message_text = msg;
end if;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `listing`
--

DROP TABLE IF EXISTS `listing`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `listing` (
  `Lid` int(11) NOT NULL AUTO_INCREMENT,
  `home_type` varchar(200) NOT NULL,
  `longitude` decimal(9,3) NOT NULL,
  `latitude` decimal(8,2) NOT NULL,
  `city` varchar(200) NOT NULL,
  `addr` varchar(200) NOT NULL,
  `postal_code` char(6) NOT NULL,
  `country` varchar(200) NOT NULL,
  `wifi` varchar(3) NOT NULL,
  `people` int(11) NOT NULL,
  `beds` int(11) NOT NULL,
  `bathrooms` int(11) NOT NULL,
  `kitchens` int(11) NOT NULL,
  `parking` int(11) NOT NULL,
  `other_accom` varchar(255) DEFAULT NULL,
  `addtional_comment` varchar(255) DEFAULT NULL,
  `host_id` int(11) NOT NULL,
  PRIMARY KEY (`Lid`),
  UNIQUE KEY `Lid_UNIQUE` (`Lid`),
  KEY `host_id_idx` (`host_id`),
  CONSTRAINT `host_id` FOREIGN KEY (`host_id`) REFERENCES `user` (`Uid`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `mybnb`.`listing_BEFORE_INSERT` BEFORE INSERT ON `listing` FOR EACH ROW
BEGIN
declare msg varchar(255);
if new.wifi != 'YES' AND new.wifi != 'NO' then
set msg = 'Constraint violated: wifi must be "YES" or "NO"';
signal sqlstate '45000' set message_text = msg;
end if;

if new.longitude < -180 or new.longitude > 180 or new.latitude < -90 or new.latitude > 90 then
set msg = 'Constraint violated: invalid Geo-Coordinates';
signal sqlstate '45000' set message_text = msg;
end if;

if new.home_type != 'house' AND new.home_type != 'condo' then
set msg = 'Constraint violated: wifi must be "house" or "apartment"';
signal sqlstate '45000' set message_text = msg;
end if;
if new.beds < 1 or new.kitchens < 0 or new.parking < 0 or new.people < 0 then
set msg = 'Constraint violated: bed >= 1, kitchen >= 0, parking >= 0, people >= 0';
signal sqlstate '45000' set message_text = msg;
end if;

-- IF new.postal_code not like '%[A-Z][0-9][A-Z][0-9][A-Z][0-9]%' then
-- set msg ='Constraint Violated: must enter valid postal_code';
--     signal sqlstate '45000' set message_text = msg;
-- END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `renter_comment`
--

DROP TABLE IF EXISTS `renter_comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `renter_comment` (
  `Cid` int(11) NOT NULL AUTO_INCREMENT,
  `rating` decimal(2,1) NOT NULL,
  `date` date NOT NULL,
  `comment` varchar(500) NOT NULL,
  `comment_writer` int(11) NOT NULL,
  `comment_to_renter` int(11) NOT NULL,
  PRIMARY KEY (`Cid`),
  KEY `to_renter_idx` (`comment_to_renter`),
  KEY `comment_writer_idx` (`comment_writer`),
  CONSTRAINT `comment_to` FOREIGN KEY (`comment_to_renter`) REFERENCES `user` (`Uid`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `comment_writer` FOREIGN KEY (`comment_writer`) REFERENCES `user` (`Uid`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=67 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `Uid` int(11) NOT NULL AUTO_INCREMENT,
  `addr` varchar(255) NOT NULL,
  `country` varchar(100) NOT NULL,
  `city` varchar(100) NOT NULL,
  `postal_code` char(6) NOT NULL,
  `birthday` date NOT NULL,
  `sin` int(9) NOT NULL,
  `first_name` varchar(100) NOT NULL,
  `last_name` varchar(100) NOT NULL,
  `middle_name` varchar(100) DEFAULT NULL,
  `occupation` varchar(100) NOT NULL,
  `payment` decimal(16,0) NOT NULL,
  `direct_deposit` decimal(15,0) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  PRIMARY KEY (`Uid`),
  UNIQUE KEY `email_UNIQUE` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `mybnb`.`renter_BEFORE_INSERT` BEFORE INSERT ON `user` FOR EACH ROW
BEGIN
declare msg varchar(255);
IF NEW.birthday > str_to_date('July 21 2001', '%M %d %Y')  then
	set msg ='Constraint Violated: age must be 18 or older';
	signal sqlstate '45000' set message_text = msg;
END IF;

IF length(new.sin) != 9 or length(new.payment) != 16 or length(new.direct_deposit) != 15 then
	set msg ='Constraint Violated: age must be 18 or older';
	signal sqlstate '45000' set message_text = msg;
END IF;

IF new.email not like '%_@__%.__%' then
	set msg ='Constraint Violated: must enter valid email';
    signal sqlstate '45000' set message_text = msg;
END IF;

-- IF new.postal_code not like '%[A-Z][0-9][A-Z][0-9][A-Z][0-9]%' then
-- set msg ='Constraint Violated: must enter valid postal_code';
--     signal sqlstate '45000' set message_text = msg;
-- END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Dumping routines for database 'mybnb'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-07-29  6:04:57
