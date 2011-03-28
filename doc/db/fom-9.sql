-- phpMyAdmin SQL Dump
-- version 3.3.9.2
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Mar 27, 2011 at 05:29 PM
-- Server version: 5.5.9
-- PHP Version: 5.3.5

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
-- Table structure for table `fom_cluster`
--

CREATE TABLE `fom_cluster` (
  `id_cluster` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `lat` float(10,7) DEFAULT NULL,
  `lon` float(10,7) DEFAULT NULL,
  `meta` mediumtext COMMENT 'can be used to store cluster descriptors (such as centroids and std dev). suggested enc: json',
  `terms_meta` mediumtext COMMENT 'can be used to store information on related terms, and/or csv of terms',
  `posts_meta` mediumtext COMMENT 'can be used to store information on related posts, and/or csv of posts',
  `id_query` bigint(20) unsigned NOT NULL,
  `id_parent` bigint(20) unsigned NOT NULL,
  `type` int(2) NOT NULL,
  PRIMARY KEY (`id_cluster`),
  KEY `fk_fom_cluster_fom_query1` (`id_query`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='where all the clusters are stored' AUTO_INCREMENT=1 ;

--
-- Dumping data for table `fom_cluster`
--


-- --------------------------------------------------------

--
-- Table structure for table `fom_link`
--

CREATE TABLE `fom_link` (
  `id_link` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `uri` varchar(400) NOT NULL,
  `text` longtext,
  PRIMARY KEY (`id_link`),
  UNIQUE KEY `uri` (`uri`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `fom_link`
--


-- --------------------------------------------------------

--
-- Table structure for table `fom_log`
--

CREATE TABLE `fom_log` (
  `id_log` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `action` varchar(255) DEFAULT NULL,
  `meta` varchar(255) DEFAULT NULL,
  `created` timestamp NULL DEFAULT NULL,
  `id_user` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`id_log`),
  KEY `fk_fm_log_fom_user1` (`id_user`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='contains description of users action' AUTO_INCREMENT=1 ;

--
-- Dumping data for table `fom_log`
--


-- --------------------------------------------------------

--
-- Table structure for table `fom_media`
--

CREATE TABLE `fom_media` (
  `id_media` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `relpath` varchar(255) DEFAULT NULL,
  `filename` varchar(255) DEFAULT NULL,
  `filetype` varchar(255) DEFAULT NULL,
  `description` mediumtext,
  `created` timestamp NULL DEFAULT NULL,
  `modified` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id_media`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='media related to posts and referenced by table fom_postmedia' AUTO_INCREMENT=1 ;

--
-- Dumping data for table `fom_media`
--


-- --------------------------------------------------------

--
-- Table structure for table `fom_place`
--

CREATE TABLE `fom_place` (
  `id_place` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `lat` float(10,7) DEFAULT NULL,
  `lon` float(10,7) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `id_user` bigint(20) unsigned DEFAULT NULL,
  `granularity` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_place`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='association between geo coords and user defined names' AUTO_INCREMENT=1 ;

--
-- Dumping data for table `fom_place`
--


-- --------------------------------------------------------

--
-- Table structure for table `fom_post`
--

CREATE TABLE `fom_post` (
  `id_post` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `id_user` bigint(20) unsigned DEFAULT NULL COMMENT 'not a FK anymore, simpler',
  `id_place` bigint(20) unsigned DEFAULT NULL COMMENT 'can be null',
  `lat` float(10,7) DEFAULT NULL,
  `lon` float(10,7) DEFAULT NULL,
  `content` mediumtext,
  `lang` varchar(20) NOT NULL,
  `created` timestamp NULL DEFAULT NULL,
  `modified` timestamp NULL DEFAULT NULL,
  `timezone` int(11) DEFAULT NULL,
  `meta` mediumtext COMMENT 'contains: src, stauts, tw_statusid, tw_replyto',
  `src` varchar(255) DEFAULT NULL,
  `src_id` bigint(20) unsigned DEFAULT NULL COMMENT 'reference to original id in source',
  `user_location` varchar(100) DEFAULT NULL,
  `coordinates_estimated` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id_post`),
  KEY `fk_post_place` (`id_place`),
  KEY `src_id` (`src_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='contains all sources indexed for search and clustering' AUTO_INCREMENT=1 ;

--
-- Dumping data for table `fom_post`
--


-- --------------------------------------------------------

--
-- Table structure for table `fom_postlink`
--

CREATE TABLE `fom_postlink` (
  `id_postlink` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `id_post` bigint(20) unsigned NOT NULL,
  `id_link` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`id_postlink`),
  KEY `id_post` (`id_post`),
  KEY `id_link` (`id_link`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `fom_postlink`
--


-- --------------------------------------------------------

--
-- Table structure for table `fom_postmedia`
--

CREATE TABLE `fom_postmedia` (
  `id_attachment` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `id_media` bigint(20) unsigned NOT NULL,
  `id_post` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`id_attachment`),
  KEY `fk_attachment_media1` (`id_media`),
  KEY `fk_attachment_post1` (`id_post`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='post attachments' AUTO_INCREMENT=1 ;

--
-- Dumping data for table `fom_postmedia`
--


-- --------------------------------------------------------

--
-- Table structure for table `fom_posttag`
--

CREATE TABLE `fom_posttag` (
  `id_posttag` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `id_term` bigint(20) unsigned NOT NULL,
  `id_post` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`id_posttag`),
  KEY `fk_posttag_term1` (`id_term`),
  KEY `fk_posttag_post1` (`id_post`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `fom_posttag`
--


-- --------------------------------------------------------

--
-- Table structure for table `fom_query`
--

CREATE TABLE `fom_query` (
  `id_query` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `id_user` bigint(20) unsigned NOT NULL,
  `query` mediumtext,
  `t_start` datetime DEFAULT NULL,
  `t_end` datetime DEFAULT NULL,
  `t_granularity` varchar(255) DEFAULT NULL,
  `lat` float(10,7) DEFAULT NULL,
  `lon` float(10,7) DEFAULT NULL,
  `geo_granularity` varchar(255) DEFAULT NULL,
  `created` timestamp NULL DEFAULT NULL,
  `timezone` int(11) DEFAULT NULL,
  `meta` mediumtext,
  PRIMARY KEY (`id_query`),
  KEY `fk_fom_query_fom_user1` (`id_user`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='queries history, uses fom_querytag to store terms' AUTO_INCREMENT=1 ;

--
-- Dumping data for table `fom_query`
--


-- --------------------------------------------------------

--
-- Table structure for table `fom_term`
--

CREATE TABLE `fom_term` (
  `id_term` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `id_termsyn` bigint(20) unsigned DEFAULT NULL COMMENT 'dont need a foreign key for a simple synonim relation',
  `id_termparent` bigint(20) unsigned DEFAULT NULL COMMENT 'simple hyerarchy, always use fom_termrel for complex relationship',
  `id_vocabulary` bigint(20) unsigned NOT NULL,
  `uri` mediumtext COMMENT 'contains reference to rdf uri if present, e.g. from dbpedia',
  PRIMARY KEY (`id_term`),
  KEY `fk_term_vocabulary1` (`id_vocabulary`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `fom_term`
--


-- --------------------------------------------------------

--
-- Table structure for table `fom_termrel`
--

CREATE TABLE `fom_termrel` (
  `id_termrel` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `id_term1` bigint(20) unsigned NOT NULL,
  `id_termreltype` bigint(20) unsigned NOT NULL,
  `id_term2` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`id_termrel`),
  KEY `fk_fom_termrel_fom_term1` (`id_term1`),
  KEY `fk_fom_termrel_fom_termreltype1` (`id_termreltype`),
  KEY `fk_fom_termrel_fom_term2` (`id_term2`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='the equivalent of an RDF triple, in a simpler way' AUTO_INCREMENT=1 ;

--
-- Dumping data for table `fom_termrel`
--


-- --------------------------------------------------------

--
-- Table structure for table `fom_termreltype`
--

CREATE TABLE `fom_termreltype` (
  `id_termreltype` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_termreltype`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='possible relations between terms, e.g.: isA' AUTO_INCREMENT=1 ;

--
-- Dumping data for table `fom_termreltype`
--


-- --------------------------------------------------------

--
-- Table structure for table `fom_user`
--

CREATE TABLE `fom_user` (
  `id_user` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `api_key` varchar(255) DEFAULT NULL,
  `user_login` varchar(255) DEFAULT NULL,
  `user_pass` varchar(255) DEFAULT NULL,
  `user_email` varchar(255) DEFAULT NULL,
  `meta` text NOT NULL,
  PRIMARY KEY (`id_user`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 COMMENT='superuser or oauth users accessing from internet' AUTO_INCREMENT=2 ;

--
-- Dumping data for table `fom_user`
--

INSERT INTO `fom_user` VALUES(1, NULL, 'default', NULL, NULL, '');

-- --------------------------------------------------------

--
-- Table structure for table `fom_vocabulary`
--

CREATE TABLE `fom_vocabulary` (
  `id_vocabulary` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `id_user` bigint(20) unsigned DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `owl` mediumtext COMMENT 'contains reference to related owl if present',
  `type` int(11) DEFAULT NULL COMMENT 'identifies if a vocabulary is UGC or system generated',
  PRIMARY KEY (`id_vocabulary`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `fom_vocabulary`
--


--
-- Constraints for dumped tables
--

--
-- Constraints for table `fom_cluster`
--
ALTER TABLE `fom_cluster`
  ADD CONSTRAINT `fom_cluster_ibfk_1` FOREIGN KEY (`id_query`) REFERENCES `fom_query` (`id_query`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `fom_log`
--
ALTER TABLE `fom_log`
  ADD CONSTRAINT `fk_fm_log_fom_user1` FOREIGN KEY (`id_user`) REFERENCES `fom_user` (`id_user`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `fom_post`
--
ALTER TABLE `fom_post`
  ADD CONSTRAINT `fk_post_place` FOREIGN KEY (`id_place`) REFERENCES `fom_place` (`id_place`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `fom_postlink`
--
ALTER TABLE `fom_postlink`
  ADD CONSTRAINT `fom_postlink_ibfk_1` FOREIGN KEY (`id_post`) REFERENCES `fom_post` (`id_post`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fom_postlink_ibfk_3` FOREIGN KEY (`id_link`) REFERENCES `fom_link` (`id_link`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `fom_postmedia`
--
ALTER TABLE `fom_postmedia`
  ADD CONSTRAINT `fk_attachment_media1` FOREIGN KEY (`id_media`) REFERENCES `fom_media` (`id_media`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_attachment_post1` FOREIGN KEY (`id_post`) REFERENCES `fom_post` (`id_post`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `fom_posttag`
--
ALTER TABLE `fom_posttag`
  ADD CONSTRAINT `fk_posttag_post1` FOREIGN KEY (`id_post`) REFERENCES `fom_post` (`id_post`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_posttag_term1` FOREIGN KEY (`id_term`) REFERENCES `fom_term` (`id_term`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `fom_query`
--
ALTER TABLE `fom_query`
  ADD CONSTRAINT `fk_fom_query_fom_user1` FOREIGN KEY (`id_user`) REFERENCES `fom_user` (`id_user`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `fom_term`
--
ALTER TABLE `fom_term`
  ADD CONSTRAINT `fk_term_vocabulary1` FOREIGN KEY (`id_vocabulary`) REFERENCES `fom_vocabulary` (`id_vocabulary`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `fom_termrel`
--
ALTER TABLE `fom_termrel`
  ADD CONSTRAINT `fk_fom_termrel_fom_term1` FOREIGN KEY (`id_term1`) REFERENCES `fom_term` (`id_term`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_fom_termrel_fom_term2` FOREIGN KEY (`id_term2`) REFERENCES `fom_term` (`id_term`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_fom_termrel_fom_termreltype1` FOREIGN KEY (`id_termreltype`) REFERENCES `fom_termreltype` (`id_termreltype`) ON DELETE NO ACTION ON UPDATE NO ACTION;
