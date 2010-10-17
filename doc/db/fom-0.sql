-- phpMyAdmin SQL Dump
-- version 3.1.1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Oct 17, 2010 at 02:51 PM
-- Server version: 5.0.41
-- PHP Version: 5.2.6

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `fom`
--

-- --------------------------------------------------------

--
-- Table structure for table `fom_attachment`
--

CREATE TABLE IF NOT EXISTS `fom_attachment` (
  `id_attachment` bigint(20) unsigned NOT NULL auto_increment,
  `id_media` bigint(20) unsigned NOT NULL,
  `id_post` bigint(20) unsigned NOT NULL,
  PRIMARY KEY  (`id_attachment`),
  KEY `fk_attachment_media1` (`id_media`),
  KEY `fk_attachment_post1` (`id_post`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `fom_attachment`
--


-- --------------------------------------------------------

--
-- Table structure for table `fom_media`
--

CREATE TABLE IF NOT EXISTS `fom_media` (
  `id_media` bigint(20) unsigned NOT NULL auto_increment,
  `relpath` varchar(255) default NULL,
  `filename` varchar(45) default NULL,
  `filetype` varchar(45) default NULL,
  `description` varchar(255) default NULL,
  `created` timestamp NULL default NULL,
  `modified` timestamp NULL default NULL,
  PRIMARY KEY  (`id_media`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `fom_media`
--


-- --------------------------------------------------------

--
-- Table structure for table `fom_place`
--

CREATE TABLE IF NOT EXISTS `fom_place` (
  `id_place` bigint(20) unsigned NOT NULL auto_increment,
  `lat` float(10,7) default NULL,
  `lon` float(10,7) default NULL,
  `description` varchar(255) default NULL,
  `id_user` bigint(20) unsigned default NULL,
  `granularity` varchar(45) default NULL,
  PRIMARY KEY  (`id_place`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `fom_place`
--


-- --------------------------------------------------------

--
-- Table structure for table `fom_post`
--

CREATE TABLE IF NOT EXISTS `fom_post` (
  `id_post` bigint(20) unsigned NOT NULL auto_increment,
  `id_statusid` bigint(20) unsigned default NULL,
  `id_inreplyto` bigint(20) unsigned default NULL,
  `id_user` bigint(20) unsigned NOT NULL,
  `id_place` bigint(20) unsigned NOT NULL,
  `lat` float(10,7) default NULL,
  `lon` float(10,7) default NULL,
  `content` mediumtext,
  `created` timestamp NULL default NULL,
  `modified` timestamp NULL default NULL,
  `timezone` int(11) default NULL,
  `src` varchar(45) default NULL,
  `status` varchar(45) default NULL,
  PRIMARY KEY  (`id_post`),
  KEY `fk_post_user` (`id_user`),
  KEY `fk_post_place` (`id_place`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `fom_post`
--


-- --------------------------------------------------------

--
-- Table structure for table `fom_posttag`
--

CREATE TABLE IF NOT EXISTS `fom_posttag` (
  `id_posttag` bigint(20) unsigned NOT NULL auto_increment,
  `id_term` bigint(20) unsigned NOT NULL,
  `id_post` bigint(20) unsigned NOT NULL,
  PRIMARY KEY  (`id_posttag`),
  KEY `fk_posttag_term1` (`id_term`),
  KEY `fk_posttag_post1` (`id_post`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `fom_posttag`
--


-- --------------------------------------------------------

--
-- Table structure for table `fom_term`
--

CREATE TABLE IF NOT EXISTS `fom_term` (
  `id_term` bigint(20) unsigned NOT NULL auto_increment,
  `name` varchar(45) default NULL,
  `id_termsyn` bigint(20) unsigned default NULL,
  `id_termparent` bigint(20) unsigned default NULL,
  `id_vocabulary` bigint(20) unsigned NOT NULL,
  PRIMARY KEY  (`id_term`),
  KEY `fk_term_vocabulary1` (`id_vocabulary`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `fom_term`
--


-- --------------------------------------------------------

--
-- Table structure for table `fom_user`
--

CREATE TABLE IF NOT EXISTS `fom_user` (
  `id_user` bigint(20) unsigned NOT NULL auto_increment,
  `id_twitterid` varchar(45) default NULL,
  `id_oauth` varchar(45) default NULL,
  `oauth_token` varchar(45) default NULL,
  `oauth_secret` varchar(45) default NULL,
  `user_login` varchar(45) default NULL,
  `user_pass` varchar(45) default NULL,
  `user_email` varchar(45) default NULL,
  `user_status` varchar(45) default NULL,
  `user_authcode` varchar(45) default NULL,
  PRIMARY KEY  (`id_user`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `fom_user`
--


-- --------------------------------------------------------

--
-- Table structure for table `fom_vocabulary`
--

CREATE TABLE IF NOT EXISTS `fom_vocabulary` (
  `id_vocabulary` bigint(20) unsigned NOT NULL auto_increment,
  `description` varchar(45) default NULL,
  PRIMARY KEY  (`id_vocabulary`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `fom_vocabulary`
--


--
-- Constraints for dumped tables
--

--
-- Constraints for table `fom_attachment`
--
ALTER TABLE `fom_attachment`
  ADD CONSTRAINT `fk_attachment_media1` FOREIGN KEY (`id_media`) REFERENCES `fom_media` (`id_media`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_attachment_post1` FOREIGN KEY (`id_post`) REFERENCES `fom_post` (`id_post`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `fom_post`
--
ALTER TABLE `fom_post`
  ADD CONSTRAINT `fk_post_user` FOREIGN KEY (`id_user`) REFERENCES `fom_user` (`id_user`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_post_place` FOREIGN KEY (`id_place`) REFERENCES `fom_place` (`id_place`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `fom_posttag`
--
ALTER TABLE `fom_posttag`
  ADD CONSTRAINT `fk_posttag_term1` FOREIGN KEY (`id_term`) REFERENCES `fom_term` (`id_term`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_posttag_post1` FOREIGN KEY (`id_post`) REFERENCES `fom_post` (`id_post`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `fom_term`
--
ALTER TABLE `fom_term`
  ADD CONSTRAINT `fk_term_vocabulary1` FOREIGN KEY (`id_vocabulary`) REFERENCES `fom_vocabulary` (`id_vocabulary`) ON DELETE NO ACTION ON UPDATE NO ACTION;
