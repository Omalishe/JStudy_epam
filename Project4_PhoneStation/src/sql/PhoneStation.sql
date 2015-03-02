CREATE DATABASE  IF NOT EXISTS `PhoneStation` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci */;
USE `PhoneStation`;
-- MySQL dump 10.13  Distrib 5.5.41, for debian-linux-gnu (x86_64)
--
-- Host: 127.0.0.1    Database: PhoneStation
-- ------------------------------------------------------
-- Server version	5.5.41-0ubuntu0.14.04.1

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
-- Table structure for table `connected_services`
--

DROP TABLE IF EXISTS `connected_services`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `connected_services` (
  `users_id` int(11) NOT NULL,
  `services_id` int(11) NOT NULL,
  PRIMARY KEY (`users_id`,`services_id`),
  KEY `fk_users_has_services_services1_idx` (`services_id`),
  KEY `fk_users_has_services_users_idx` (`users_id`),
  CONSTRAINT `fk_users_has_services_services1` FOREIGN KEY (`services_id`) REFERENCES `services` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_users_has_services_users` FOREIGN KEY (`users_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `connected_services`
--

LOCK TABLES `connected_services` WRITE;
/*!40000 ALTER TABLE `connected_services` DISABLE KEYS */;
INSERT INTO `connected_services` (`users_id`, `services_id`) VALUES (1,1),(3,1),(3,3),(1,4);
/*!40000 ALTER TABLE `connected_services` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pay_bills`
--

DROP TABLE IF EXISTS `pay_bills`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pay_bills` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `users_id` int(11) NOT NULL,
  `date_issued` datetime NOT NULL,
  `pay_month` date NOT NULL,
  `amount` double NOT NULL DEFAULT '0',
  `is_payed` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`,`users_id`),
  KEY `fk_pay_bills_users1_idx` (`users_id`),
  CONSTRAINT `fk_pay_bills_users1` FOREIGN KEY (`users_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pay_bills`
--

LOCK TABLES `pay_bills` WRITE;
/*!40000 ALTER TABLE `pay_bills` DISABLE KEYS */;
INSERT INTO `pay_bills` (`id`, `users_id`, `date_issued`, `pay_month`, `amount`, `is_payed`) VALUES (4,1,'2015-02-17 01:17:37','2015-02-01',698.5,1),(5,1,'2015-02-17 01:17:49','2015-02-01',578.5,1),(6,1,'2015-02-17 01:17:51','2015-02-01',578.5,0),(7,1,'2015-02-17 01:45:03','2015-02-01',578.5,0);
/*!40000 ALTER TABLE `pay_bills` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `placed_calls`
--

DROP TABLE IF EXISTS `placed_calls`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `placed_calls` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `time_start` datetime NOT NULL,
  `duration` int(11) NOT NULL DEFAULT '0',
  `cost` double NOT NULL DEFAULT '0',
  `users_id` int(11) NOT NULL,
  PRIMARY KEY (`id`,`users_id`),
  KEY `fk_placed_calls_users1_idx` (`users_id`),
  CONSTRAINT `fk_placed_calls_users1` FOREIGN KEY (`users_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `placed_calls`
--

LOCK TABLES `placed_calls` WRITE;
/*!40000 ALTER TABLE `placed_calls` DISABLE KEYS */;
INSERT INTO `placed_calls` (`id`, `time_start`, `duration`, `cost`, `users_id`) VALUES (1,'2015-02-15 00:00:00',20,30.5,1),(2,'2015-02-15 00:00:00',10,60.5,1),(3,'2015-02-15 00:00:00',30,50.5,1),(4,'2015-02-15 00:00:00',23,432,1),(5,'2015-02-15 00:00:00',34,0,1),(6,'2015-02-15 00:00:00',4,5,1),(7,'2015-02-16 00:00:00',10,0,1);
/*!40000 ALTER TABLE `placed_calls` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `services`
--

DROP TABLE IF EXISTS `services`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `services` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `price` double NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `services`
--

LOCK TABLES `services` WRITE;
/*!40000 ALTER TABLE `services` DISABLE KEYS */;
INSERT INTO `services` (`id`, `price`) VALUES (1,100),(3,10),(4,20),(5,20);
/*!40000 ALTER TABLE `services` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `services_name_translations`
--

DROP TABLE IF EXISTS `services_name_translations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `services_name_translations` (
  `services_id` int(11) NOT NULL,
  `lang` varchar(2) COLLATE utf8_unicode_ci NOT NULL,
  `name` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`services_id`,`lang`),
  CONSTRAINT `fk_services_name_translations_1` FOREIGN KEY (`services_id`) REFERENCES `services` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `services_name_translations`
--

LOCK TABLES `services_name_translations` WRITE;
/*!40000 ALTER TABLE `services_name_translations` DISABLE KEYS */;
INSERT INTO `services_name_translations` (`services_id`, `lang`, `name`) VALUES (1,'en','internet'),(1,'ru','интернет'),(1,'uk','інтернет'),(3,'en','cute number'),(3,'ru','Красивый номер'),(3,'uk','Симпатичний номер'),(4,'en','over 15 minutes discount'),(4,'ru','Скидка за 15 минут бесед'),(4,'uk','Знижка за 15 хвилин розмов'),(5,'en','One year loyalty discount'),(5,'ru','Скидка за год лояльности'),(5,'uk','Знижка за рік лояльності');
/*!40000 ALTER TABLE `services_name_translations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `used_services`
--

DROP TABLE IF EXISTS `used_services`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `used_services` (
  `users_id` int(11) NOT NULL,
  `services_id` int(11) NOT NULL,
  `date_used` date NOT NULL,
  `cost` double NOT NULL,
  PRIMARY KEY (`users_id`,`services_id`),
  KEY `fk_users_has_services1_services1_idx` (`services_id`),
  KEY `fk_users_has_services1_users1_idx` (`users_id`),
  CONSTRAINT `fk_users_has_services1_services1` FOREIGN KEY (`services_id`) REFERENCES `services` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_users_has_services1_users1` FOREIGN KEY (`users_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `used_services`
--

LOCK TABLES `used_services` WRITE;
/*!40000 ALTER TABLE `used_services` DISABLE KEYS */;
INSERT INTO `used_services` (`users_id`, `services_id`, `date_used`, `cost`) VALUES (1,1,'2015-02-17',100),(1,4,'2015-02-17',20);
/*!40000 ALTER TABLE `used_services` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary table structure for view `user_calls`
--

DROP TABLE IF EXISTS `user_calls`;
/*!50001 DROP VIEW IF EXISTS `user_calls`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `user_calls` (
  `us_id` tinyint NOT NULL,
  `dur` tinyint NOT NULL
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Temporary table structure for view `user_services`
--

DROP TABLE IF EXISTS `user_services`;
/*!50001 DROP VIEW IF EXISTS `user_services`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `user_services` (
  `user_id` tinyint NOT NULL,
  `service_id` tinyint NOT NULL,
  `service_name` tinyint NOT NULL,
  `lang` tinyint NOT NULL,
  `service_price` tinyint NOT NULL
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(16) COLLATE utf8_unicode_ci NOT NULL,
  `password` varchar(32) COLLATE utf8_unicode_ci NOT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `phone_number` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `is_admin` tinyint(1) NOT NULL DEFAULT '0',
  `is_blocked` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username_UNIQUE` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` (`id`, `username`, `password`, `create_time`, `phone_number`, `is_admin`, `is_blocked`) VALUES (1,'alterne','1',NULL,'r23',0,0),(3,'july','22',NULL,'09309',1,0),(4,'vinsent','33','2015-02-15 22:00:00','30498',0,0),(6,'admin','1234','2015-02-15 22:00:00','',1,1),(9,'kucher','1','2015-03-01 22:00:00','333',1,0);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users_given_name_translations`
--

DROP TABLE IF EXISTS `users_given_name_translations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users_given_name_translations` (
  `users_id` int(11) NOT NULL,
  `lang` varchar(2) COLLATE utf8_unicode_ci NOT NULL,
  `given_name` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`users_id`,`lang`),
  CONSTRAINT `fk_users_given_name_translations_1` FOREIGN KEY (`users_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users_given_name_translations`
--

LOCK TABLES `users_given_name_translations` WRITE;
/*!40000 ALTER TABLE `users_given_name_translations` DISABLE KEYS */;
INSERT INTO `users_given_name_translations` (`users_id`, `lang`, `given_name`) VALUES (1,'en','Malishevskyi Oleksandr'),(1,'ru','Малишевский Александр'),(1,'uk','Малішевський Олександр'),(3,'en','Kozyuba Julia'),(3,'ru','Козюба Юлия'),(3,'uk','Козюба Юлія'),(4,'en','Vinsent Yorkshire'),(4,'ru','Винсент Ван Дог'),(4,'uk','Вінсент Собак'),(6,'en','Admin'),(6,'ru','Его Величество'),(6,'uk','Адміністратор'),(9,'en','Kucherenko Alex'),(9,'ru','Торговец Чёрным Золотом'),(9,'uk','Кучеренко Олександр');
/*!40000 ALTER TABLE `users_given_name_translations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'PhoneStation'
--

--
-- Dumping routines for database 'PhoneStation'
--
/*!50003 DROP PROCEDURE IF EXISTS `create_bill` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,STRICT_ALL_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ALLOW_INVALID_DATES,ERROR_FOR_DIVISION_BY_ZERO,TRADITIONAL,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `create_bill`(IN userId INT, IN startDate DATE, IN endDate DATE)
BEGIN
select userId, startDate, endDate;

drop table if exists sum_count;
create table if not exists sum_count 
select sum(placed_calls.cost) as cost,
placed_calls.users_id as user_id,
0 as service_id
from placed_calls as placed_calls
where users_id = userId
group by placed_calls.users_id, service_id;


insert into sum_count 
select sum(services.price) as cost,
users.id as user_id,
services.id as service_id
from users as users
inner join connected_services as connected_services 
on users.id = connected_services.users_id	
inner join services as services 
	on connected_services.services_id = services.id
left join used_services as used_services
	on used_services.users_id = userId
		and used_services.date_used between startDate and endDate

where users.id = userId 
and used_services.date_used is null
group by users.id, services.id;

insert into used_services
select user_id as users_id,
service_id as services_id,
CURRENT_TIMESTAMP as date_used, 
cost as cost 
from sum_count
where not service_id=0;

insert into pay_bills
select
null as id,
user_id as users_id,
CURRENT_TIMESTAMP as date_issued,
startDate as pay_month,
sum(cost) as amount,
false as is_payed
from sum_count
group by users_id, date_issued, pay_month
;


drop table sum_count;

END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Final view structure for view `user_calls`
--

/*!50001 DROP TABLE IF EXISTS `user_calls`*/;
/*!50001 DROP VIEW IF EXISTS `user_calls`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `user_calls` AS select `users`.`id` AS `us_id`,`placed_calls`.`duration` AS `dur` from (`users` left join `placed_calls` on((`users`.`id` = `placed_calls`.`users_id`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `user_services`
--

/*!50001 DROP TABLE IF EXISTS `user_services`*/;
/*!50001 DROP VIEW IF EXISTS `user_services`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `user_services` AS select `users`.`id` AS `user_id`,`services`.`id` AS `service_id`,`services_name_translations`.`name` AS `service_name`,`services_name_translations`.`lang` AS `lang`,`services`.`price` AS `service_price` from (((`users` join `connected_services` on((`users`.`id` = `connected_services`.`users_id`))) join `services` on((`connected_services`.`services_id` = `services`.`id`))) left join `services_name_translations` on((`services_name_translations`.`services_id` = `services`.`id`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-03-02 10:18:07
