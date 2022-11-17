-- MySQL dump 10.13  Distrib 5.7.40, for Linux (x86_64)
--
-- Host: localhost    Database: Kookbob
-- ------------------------------------------------------
-- Server version	5.7.40-0ubuntu0.18.04.1

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
-- Table structure for table `menu`
--

DROP TABLE IF EXISTS `menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `menu` (
  `menu_Id` int(11) NOT NULL AUTO_INCREMENT,
  `restaurant_name` varchar(45) NOT NULL,
  `menu_name` varchar(45) NOT NULL,
  `count_review` int(11) DEFAULT NULL,
  `star_avg` float DEFAULT NULL,
  `total_like` int(11) DEFAULT NULL,
  PRIMARY KEY (`menu_Id`),
  KEY `restaurant_link_idx` (`restaurant_name`),
  CONSTRAINT `restaurant_link` FOREIGN KEY (`restaurant_name`) REFERENCES `restaurant` (`name`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `menu`
--

LOCK TABLES `menu` WRITE;
/*!40000 ALTER TABLE `menu` DISABLE KEYS */;
INSERT INTO `menu` VALUES (1,'학생식당','묵채비빔밥',0,NULL,0),(2,'학생식당','파채부대덮밥',0,NULL,0),(3,'학생식당','시래기뼈다귀해장국',0,NULL,0),(4,'학생식당','눈꽃치즈돈까스나베',0,NULL,0),(5,'학생식당','얼갈이두부된장국',0,NULL,0),(6,'학생식당','치즈닭갈비납작당면',0,NULL,0),(7,'학생식당','트윈에비동',0,NULL,0),(9,'한울식당','qq',0,0,0),(10,'한울식당','rr',0,0,0),(11,'한울식당','참치마요덮밥',0,0,0),(12,'한울식당','mac',0,0,0),(13,'한울식당','아이폰',0,0,0),(14,'한울식당','쇠고기무국',0,0,0),(15,'한울식당','삼계떡국',0,0,0),(16,'한울식당','오믈렛라이스',0,0,0),(17,'한울식당','qwe',0,0,0),(18,'학생식당','뚝배기쇠고기쌀국수',0,0,0),(19,'학생식당','얼큰장터국',0,0,0),(20,'한울식당','유부우동',0,0,0),(21,'교직원식당','계란탁순두부찌개',0,0,0),(22,'한울식당','킹소시지',0,0,0),(23,'학생식당','직화간짜장',0,0,0);
/*!40000 ALTER TABLE `menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `menu_appearance`
--

DROP TABLE IF EXISTS `menu_appearance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `menu_appearance` (
  `menu_appearance_id` int(11) NOT NULL AUTO_INCREMENT,
  `menu_Id` int(11) NOT NULL,
  `date` date NOT NULL,
  PRIMARY KEY (`menu_appearance_id`),
  KEY `fk_menu_appearance1` (`menu_Id`),
  CONSTRAINT `fk_menu_appearance1` FOREIGN KEY (`menu_Id`) REFERENCES `menu` (`menu_Id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `menu_appearance`
--

LOCK TABLES `menu_appearance` WRITE;
/*!40000 ALTER TABLE `menu_appearance` DISABLE KEYS */;
INSERT INTO `menu_appearance` VALUES (1,1,'2022-11-11'),(2,2,'2022-11-11'),(3,3,'2022-11-11'),(4,4,'2022-11-11'),(5,5,'2022-11-11'),(6,6,'2022-11-11');
/*!40000 ALTER TABLE `menu_appearance` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `menu_like`
--

DROP TABLE IF EXISTS `menu_like`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `menu_like` (
  `menu_like_id` int(11) NOT NULL AUTO_INCREMENT,
  `Mliked_user_id` varchar(45) NOT NULL,
  `Mliked_menu_id` int(11) NOT NULL,
  PRIMARY KEY (`menu_like_id`),
  KEY `fk_like_user1_idx` (`Mliked_user_id`),
  KEY `fk_like_menu1_idx` (`Mliked_menu_id`),
  CONSTRAINT `fk_like_menu1` FOREIGN KEY (`Mliked_menu_id`) REFERENCES `menu` (`menu_Id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_like_menu2` FOREIGN KEY (`Mliked_user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `menu_like`
--

LOCK TABLES `menu_like` WRITE;
/*!40000 ALTER TABLE `menu_like` DISABLE KEYS */;
INSERT INTO `menu_like` VALUES (15,'jongbin',1),(16,'jongbin',2),(17,'jongbin',3),(18,'jongbin',4),(24,'minjae',1),(25,'jihun',3),(26,'minseok',5),(27,'hyunmin',7);
/*!40000 ALTER TABLE `menu_like` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `restaurant`
--

DROP TABLE IF EXISTS `restaurant`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `restaurant` (
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `restaurant`
--

LOCK TABLES `restaurant` WRITE;
/*!40000 ALTER TABLE `restaurant` DISABLE KEYS */;
INSERT INTO `restaurant` VALUES ('K-BOB'),('교직원식당'),('생활관식당 정기식'),('청향 양식당'),('청향 한식당'),('학생식당'),('한울식당');
/*!40000 ALTER TABLE `restaurant` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `review`
--

DROP TABLE IF EXISTS `review`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `review` (
  `review_number` int(11) NOT NULL AUTO_INCREMENT,
  `review_user_id` varchar(45) NOT NULL,
  `review_menu_id_reviewd` int(11) NOT NULL,
  `menu_name` varchar(45) DEFAULT NULL,
  `write_date` datetime DEFAULT NULL,
  `star` float DEFAULT NULL,
  `review_like` int(11) DEFAULT NULL,
  `description` text,
  `image` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`review_number`),
  KEY `fk_review_user_idx` (`review_user_id`),
  KEY `fk_review_menu1_idx` (`review_menu_id_reviewd`),
  CONSTRAINT `fk_review_menu1` FOREIGN KEY (`review_menu_id_reviewd`) REFERENCES `menu` (`menu_Id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_review_menu2` FOREIGN KEY (`review_user_id`) REFERENCES `user` (`user_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=124 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `review`
--

LOCK TABLES `review` WRITE;
/*!40000 ALTER TABLE `review` DISABLE KEYS */;
INSERT INTO `review` VALUES (99,'jongbin',1,'묵채비빔밥','2022-11-11 00:00:00',5,0,'맛있어요!',NULL),(100,'jongbin',2,'파채부대덮밥','2022-11-11 00:00:00',4,0,'맛있나요?',NULL),(101,'jihun',9,'qq','2022-11-14 06:52:50',2.5,0,'ddd','qq_jihun_2022-11-14 06:52:50.png'),(102,'jihun',10,'rr','2022-11-14 06:57:26',3.5,0,'aaa','rr_jihun_2022-11-14 06:57:26.png'),(103,'jihun',11,'참치마요덮밥','2022-11-14 07:05:07',3.5,0,'맛있다','참치마요덮밥_jihun_2022-11-14 07:05:07.png'),(104,'jihun',12,'mac','2022-11-14 14:00:54',3,0,'Good','mac_jihun_2022-11-14 14:00:54.png'),(105,'jihun',13,'아이폰','2022-11-14 15:24:36',2.5,0,'맜있어용','아이폰_jihun_2022-11-14 15:24:36.png'),(106,'jihun',14,'쇠고기무국','2022-11-14 15:27:33',2.5,0,'맛있다','쇠고기무국_jihun_2022-11-14 15:27:33.png'),(107,'jihun',14,'쇠고기무국','2022-11-14 15:27:48',2.5,0,'비ㅣ싸다','쇠고기무국_jihun_2022-11-14 15:27:48.png'),(108,'jihun',14,'쇠고기무국','2022-11-14 15:28:00',1.5,0,'갖고싶다','쇠고기무국_jihun_2022-11-14 15:28:00.png'),(109,'jihun',15,'삼계떡국','2022-11-15 01:26:48',3,0,'굳','삼계떡국_jihun_2022-11-15 01:26:48.png'),(110,'jihun',16,'오믈렛라이스','2022-11-15 03:33:43',4.5,0,'맛있어요~~~','오믈렛라이스_jihun_2022-11-15 03:33:43.png'),(111,'jihun',16,'오믈렛라이스','2022-11-15 04:14:01',2.5,0,'ㅂㅂㅂㅂㅂ','오믈렛라이스_jihun_2022-11-15 04:14:01.png'),(112,'jihun',17,'qwe','2022-11-16 02:29:12',3,0,'ronaldo','qwe_jihun_2022-11-16 02:29:12.png'),(113,'jihun',18,'뚝배기쇠고기쌀국수','2022-11-16 02:32:58',4,0,'messi gimochi','뚝배기쇠고기쌀국수_jihun_2022-11-16 02:32:58.png'),(114,'jihun',19,'얼큰장터국','2022-11-16 02:35:27',3,0,'Ronaldo is good','얼큰장터국_jihun_2022-11-16 02:35:27.png'),(115,'jihun',20,'유부우동','2022-11-16 02:36:00',0,0,'w','유부우동_jihun_2022-11-16 02:36:00.png'),(116,'jihun',21,'계란탁순두부찌개','2022-11-16 02:47:50',2,0,'[[','계란탁순두부찌개_jihun_2022-11-16 02:47:50.png'),(117,'jihun',21,'계란탁순두부찌개','2022-11-16 03:02:39',5,0,'GOOD','계란탁순두부찌개_jihun_2022-11-16 03:02:39.png'),(118,'jihun',20,'유부우동','2022-11-16 03:07:46',3,0,'messi gimochi','유부우동_jihun_2022-11-16 03:07:46.png'),(119,'jihun',21,'계란탁순두부찌개','2022-11-16 03:26:18',4,0,'맛이 있는지 없는지는 모르지만 맛이 잇을수도잇고 맛이없을수도잇고 맛이무엇인지모르지만 맛을 알수도잇고 모를수도잇습니다 맛이란무엇인가에대해생강ㄱ해볼수이스비낟','계란탁순두부찌개_jihun_2022-11-16 03:26:18.png'),(120,'jihun',20,'유부우동','2022-11-16 05:20:03',0,0,'ffff\n','유부우동_jihun_2022-11-16 05:20:03.png'),(121,'jihun',22,'킹소시지','2022-11-16 07:24:22',4,0,'맛나다','킹소시지_jihun_2022-11-16 07:24:22.png'),(122,'jihun',19,'얼큰장터국','2022-11-16 13:41:41',4.5,0,'아이폰','얼큰장터국_jihun_2022-11-16 13:41:41.png'),(123,'jihun',23,'직화간짜장','2022-11-17 03:38:32',4.5,0,'달달하고 맛있어요~~~','직화간짜장_jihun_2022-11-17 03:38:32.png');
/*!40000 ALTER TABLE `review` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `review_like`
--

DROP TABLE IF EXISTS `review_like`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `review_like` (
  `review_like_id` int(11) NOT NULL AUTO_INCREMENT,
  `Rlike_user_id` varchar(45) NOT NULL,
  `Rlike_review_no` int(11) NOT NULL,
  PRIMARY KEY (`review_like_id`),
  KEY `fk_reviewLike_user1_idx` (`Rlike_user_id`),
  KEY `fk_reviewLike_review1_idx` (`Rlike_review_no`),
  CONSTRAINT `fk_review_like_review_id` FOREIGN KEY (`Rlike_review_no`) REFERENCES `review` (`review_number`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_reviewlike_id` FOREIGN KEY (`Rlike_user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `review_like`
--

LOCK TABLES `review_like` WRITE;
/*!40000 ALTER TABLE `review_like` DISABLE KEYS */;
INSERT INTO `review_like` VALUES (11,'jihun',100),(12,'minjae',99),(13,'jihun',99),(14,'hyunmin',100);
/*!40000 ALTER TABLE `review_like` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `review_tag`
--

DROP TABLE IF EXISTS `review_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `review_tag` (
  `review_tag_id` int(11) NOT NULL AUTO_INCREMENT,
  `review_number` int(11) NOT NULL,
  `tag` varchar(45) NOT NULL,
  PRIMARY KEY (`review_tag_id`),
  KEY `fk_review_tag_tag_idx` (`tag`),
  KEY `fk_review_tag_review_id_idx` (`review_number`),
  CONSTRAINT `fk_review_tag_menu_id` FOREIGN KEY (`review_number`) REFERENCES `review` (`review_number`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_review_tag_tag` FOREIGN KEY (`tag`) REFERENCES `tag` (`tag`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `review_tag`
--

LOCK TABLES `review_tag` WRITE;
/*!40000 ALTER TABLE `review_tag` DISABLE KEYS */;
INSERT INTO `review_tag` VALUES (1,99,'매운'),(2,99,'차가운'),(3,99,'짠'),(4,100,'짠'),(5,100,'느끼한'),(6,100,'뜨거운');
/*!40000 ALTER TABLE `review_tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tag`
--

DROP TABLE IF EXISTS `tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tag` (
  `tag` varchar(45) NOT NULL,
  PRIMARY KEY (`tag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tag`
--

LOCK TABLES `tag` WRITE;
/*!40000 ALTER TABLE `tag` DISABLE KEYS */;
INSERT INTO `tag` VALUES ('고소한'),('느끼한'),('단'),('뜨거운'),('매운'),('짠'),('차가운');
/*!40000 ALTER TABLE `tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `user_id` varchar(45) NOT NULL,
  `nickname` varchar(45) DEFAULT NULL,
  `name` varchar(45) DEFAULT NULL,
  `writing_count` int(11) DEFAULT NULL,
  `user_num` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `user_num_UNIQUE` (`user_num`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES ('hyunmin','hyunmin','조현민',0,1),('jihun','jihun','최지훈',0,2),('jongbin','jongbin','노종빈',0,3),('minjae','minjae','김민제',0,4),('minseok','minseok','유민석',0,5);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-11-17  4:23:16
