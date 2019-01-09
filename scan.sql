CREATE DATABASE `scan` DEFAULT CHARACTER SET utf8;

USE `scan`;

SET NAMES utf8;

CREATE TABLE `api` (
  `Key` char(32) NOT NULL,
  `Description` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`Key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `api` VALUES ('epDSMnxvEmGeHVn8QcjJsTW1PIFDFork','Web');

CREATE TABLE `image` (
  `Id` char(32) NOT NULL,
  `User` bigint(12) NOT NULL,
  `Name` varchar(255) NOT NULL,
  `Thumbnail` longblob NOT NULL,
  `Raw` longblob NOT NULL,
  `Created` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`Id`,`User`),
  KEY `FK_image_user_idx` (`User`),
  KEY `Name_Index` (`User`,`Name`),
  CONSTRAINT `FK_image_user` FOREIGN KEY (`User`) REFERENCES `user` (`phonenumber`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `option` (
  `key` varchar(255) NOT NULL,
  `value` text,
  PRIMARY KEY (`key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `project` (
  `Id` char(32) NOT NULL,
  `User` bigint(12) NOT NULL,
  `Tasks` text NOT NULL,
  `Thumbnail` longblob NOT NULL,
  `Raw` longblob NOT NULL,
  `Created` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`Id`,`User`),
  KEY `FK_project_user` (`User`),
  CONSTRAINT `FK_project_user` FOREIGN KEY (`User`) REFERENCES `user` (`phonenumber`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

CREATE TABLE `token` (
  `Key` char(32) NOT NULL,
  `API` char(32) NOT NULL,
  `User` bigint(12) NOT NULL,
  `Expires` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`Key`,`API`),
  KEY `FK_token_user_idx` (`User`),
  KEY `FK_token_api_idx` (`API`),
  CONSTRAINT `FK_token_api` FOREIGN KEY (`API`) REFERENCES `api` (`key`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_token_user` FOREIGN KEY (`User`) REFERENCES `user` (`phonenumber`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `user` (
  `PhoneNumber` bigint(12) NOT NULL,
  `Type` int(3) NOT NULL DEFAULT '0',
  `LoginCode` int(9) NOT NULL DEFAULT '0',
  `LoginCodeExpires` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`PhoneNumber`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
