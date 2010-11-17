-- phpMyAdmin SQL Dump
-- version 3.1.1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Nov 13, 2010 at 04:01 PM
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
-- Table structure for table `fom_cluster`
--

CREATE TABLE IF NOT EXISTS `fom_cluster` (
  `id_cluster` bigint(20) unsigned NOT NULL auto_increment,
  `meta` mediumtext COMMENT 'can be used to store cluster descriptors (such as centroids and std dev). suggested enc: json',
  `terms_meta` mediumtext COMMENT 'can be used to store information on related terms, and/or csv of terms',
  `posts_meta` mediumtext COMMENT 'can be used to store information on related posts, and/or csv of posts',
  `id_query` bigint(20) unsigned NOT NULL,
  PRIMARY KEY  (`id_cluster`),
  KEY `fk_fom_cluster_fom_query1` (`id_query`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='where all the clusters are stored' AUTO_INCREMENT=1 ;

--
-- Dumping data for table `fom_cluster`
--


-- --------------------------------------------------------

--
-- Table structure for table `fom_clusterpost`
--

CREATE TABLE IF NOT EXISTS `fom_clusterpost` (
  `id_clusterpost` bigint(20) unsigned NOT NULL auto_increment,
  `id_cluster` bigint(20) unsigned NOT NULL,
  `id_post` bigint(20) unsigned NOT NULL,
  PRIMARY KEY  (`id_clusterpost`),
  KEY `fk_fom_clusterpost_fom_cluster1` (`id_cluster`),
  KEY `fk_fom_clusterpost_fom_post1` (`id_post`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='posts related to clusters, used as fom_cluster.post_meta' AUTO_INCREMENT=1 ;

--
-- Dumping data for table `fom_clusterpost`
--


-- --------------------------------------------------------

--
-- Table structure for table `fom_clusterterm`
--

CREATE TABLE IF NOT EXISTS `fom_clusterterm` (
  `id_clusterterm` bigint(20) unsigned NOT NULL auto_increment,
  `id_cluster` bigint(20) unsigned NOT NULL,
  `id_term` bigint(20) unsigned NOT NULL,
  PRIMARY KEY  (`id_clusterterm`),
  KEY `fk_fom_clusterterm_fom_cluster1` (`id_cluster`),
  KEY `fk_fom_clusterterm_fom_term1` (`id_term`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='terms related to clusters, used as fom_cluster.term_meta' AUTO_INCREMENT=1 ;

--
-- Dumping data for table `fom_clusterterm`
--


-- --------------------------------------------------------

--
-- Table structure for table `fom_log`
--

CREATE TABLE IF NOT EXISTS `fom_log` (
  `id_log` bigint(20) unsigned NOT NULL auto_increment,
  `action` varchar(255) default NULL,
  `meta` varchar(255) default NULL,
  `created` timestamp NULL default NULL,
  `id_user` bigint(20) unsigned NOT NULL,
  PRIMARY KEY  (`id_log`),
  KEY `fk_fm_log_fom_user1` (`id_user`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='contains description of users action' AUTO_INCREMENT=1 ;

--
-- Dumping data for table `fom_log`
--


-- --------------------------------------------------------

--
-- Table structure for table `fom_media`
--

CREATE TABLE IF NOT EXISTS `fom_media` (
  `id_media` bigint(20) unsigned NOT NULL auto_increment,
  `relpath` varchar(255) default NULL,
  `filename` varchar(255) default NULL,
  `filetype` varchar(255) default NULL,
  `description` mediumtext,
  `created` timestamp NULL default NULL,
  `modified` timestamp NULL default NULL,
  PRIMARY KEY  (`id_media`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='media related to posts and referenced by table fom_postmedia' AUTO_INCREMENT=1 ;

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
  `granularity` varchar(255) default NULL,
  PRIMARY KEY  (`id_place`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='association between geo coords and user defined names' AUTO_INCREMENT=1 ;

--
-- Dumping data for table `fom_place`
--


-- --------------------------------------------------------

--
-- Table structure for table `fom_post`
--

CREATE TABLE IF NOT EXISTS `fom_post` (
  `id_post` bigint(20) unsigned NOT NULL auto_increment,
  `id_user` bigint(20) unsigned default NULL COMMENT 'not a FK anymore, simpler',
  `id_place` bigint(20) unsigned default NULL COMMENT 'can be null',
  `lat` float(10,7) default NULL,
  `lon` float(10,7) default NULL,
  `content` mediumtext,
  `created` timestamp NULL default NULL,
  `modified` timestamp NULL default NULL,
  `timezone` int(11) default NULL,
  `meta` mediumtext COMMENT 'contains: src, stauts, tw_statusid, tw_replyto',
  `src` varchar(255) default NULL,
  PRIMARY KEY  (`id_post`),
  KEY `fk_post_place` (`id_place`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='contains all sources indexed for search and clustering' AUTO_INCREMENT=1 ;

--
-- Dumping data for table `fom_post`
--


-- --------------------------------------------------------

--
-- Table structure for table `fom_postmedia`
--

CREATE TABLE IF NOT EXISTS `fom_postmedia` (
  `id_attachment` bigint(20) unsigned NOT NULL auto_increment,
  `id_media` bigint(20) unsigned NOT NULL,
  `id_post` bigint(20) unsigned NOT NULL,
  PRIMARY KEY  (`id_attachment`),
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
-- Table structure for table `fom_query`
--

CREATE TABLE IF NOT EXISTS `fom_query` (
  `id_query` bigint(20) unsigned NOT NULL auto_increment,
  `id_user` bigint(20) unsigned NOT NULL,
  `query` mediumtext,
  `t_start` datetime default NULL,
  `t_end` datetime default NULL,
  `t_granularity` varchar(255) default NULL,
  `lat` float(10,7) default NULL,
  `lon` float(10,7) default NULL,
  `geo_granularity` varchar(255) default NULL,
  `created` timestamp NULL default NULL,
  `timezone` int(11) default NULL,
  PRIMARY KEY  (`id_query`),
  KEY `fk_fom_query_fom_user1` (`id_user`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='queries history, uses fom_querytag to store terms' AUTO_INCREMENT=1 ;

--
-- Dumping data for table `fom_query`
--


-- --------------------------------------------------------

--
-- Table structure for table `fom_querytag`
--

CREATE TABLE IF NOT EXISTS `fom_querytag` (
  `id_querytag` bigint(20) unsigned NOT NULL auto_increment,
  `id_query` bigint(20) unsigned NOT NULL,
  `id_term` bigint(20) unsigned NOT NULL,
  PRIMARY KEY  (`id_querytag`),
  KEY `fk_fom_querytag_fom_query1` (`id_query`),
  KEY `fk_fom_querytag_fom_term1` (`id_term`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='stores terms associated to queries with e.r. to vocabularies' AUTO_INCREMENT=1 ;

--
-- Dumping data for table `fom_querytag`
--


-- --------------------------------------------------------

--
-- Table structure for table `fom_term`
--

CREATE TABLE IF NOT EXISTS `fom_term` (
  `id_term` bigint(20) unsigned NOT NULL auto_increment,
  `name` varchar(255) default NULL,
  `id_termsyn` bigint(20) unsigned default NULL COMMENT 'dont need a foreign key for a simple synonim relation',
  `id_termparent` bigint(20) unsigned default NULL COMMENT 'simple hyerarchy, always use fom_termrel for complex relationship',
  `id_vocabulary` bigint(20) unsigned NOT NULL,
  `uri` mediumtext COMMENT 'contains reference to rdf uri if present, e.g. from dbpedia',
  PRIMARY KEY  (`id_term`),
  KEY `fk_term_vocabulary1` (`id_vocabulary`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `fom_term`
--


-- --------------------------------------------------------

--
-- Table structure for table `fom_termrel`
--

CREATE TABLE IF NOT EXISTS `fom_termrel` (
  `id_termrel` bigint(20) unsigned NOT NULL auto_increment,
  `id_term1` bigint(20) unsigned NOT NULL,
  `id_termreltype` bigint(20) unsigned NOT NULL,
  `id_term2` bigint(20) unsigned NOT NULL,
  PRIMARY KEY  (`id_termrel`),
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

CREATE TABLE IF NOT EXISTS `fom_termreltype` (
  `id_termreltype` bigint(20) unsigned NOT NULL auto_increment,
  `name` varchar(255) default NULL,
  PRIMARY KEY  (`id_termreltype`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='possible relations between terms, e.g.: isA' AUTO_INCREMENT=1 ;

--
-- Dumping data for table `fom_termreltype`
--


-- --------------------------------------------------------

--
-- Table structure for table `fom_user`
--

CREATE TABLE IF NOT EXISTS `fom_user` (
  `id_user` bigint(20) unsigned NOT NULL auto_increment,
  `id_twitterid` varchar(255) default NULL,
  `id_oauth` varchar(255) default NULL,
  `oauth_token` varchar(255) default NULL,
  `oauth_secret` varchar(255) default NULL,
  `user_login` varchar(255) default NULL,
  `user_pass` varchar(255) default NULL,
  `user_email` varchar(255) default NULL,
  `user_status` varchar(255) default NULL,
  `user_authcode` varchar(255) default NULL,
  PRIMARY KEY  (`id_user`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='superuser or oauth users accessing from internet' AUTO_INCREMENT=1 ;

--
-- Dumping data for table `fom_user`
--


-- --------------------------------------------------------

--
-- Table structure for table `fom_vocabulary`
--

CREATE TABLE IF NOT EXISTS `fom_vocabulary` (
  `id_vocabulary` bigint(20) unsigned NOT NULL auto_increment,
  `name` varchar(255) default NULL,
  `owl` mediumtext COMMENT 'contains reference to related owl if present',
  PRIMARY KEY  (`id_vocabulary`)
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
  ADD CONSTRAINT `fk_fom_cluster_fom_query1` FOREIGN KEY (`id_query`) REFERENCES `fom_query` (`id_query`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `fom_clusterpost`
--
ALTER TABLE `fom_clusterpost`
  ADD CONSTRAINT `fk_fom_clusterpost_fom_cluster1` FOREIGN KEY (`id_cluster`) REFERENCES `fom_cluster` (`id_cluster`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_fom_clusterpost_fom_post1` FOREIGN KEY (`id_post`) REFERENCES `fom_post` (`id_post`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `fom_clusterterm`
--
ALTER TABLE `fom_clusterterm`
  ADD CONSTRAINT `fk_fom_clusterterm_fom_cluster1` FOREIGN KEY (`id_cluster`) REFERENCES `fom_cluster` (`id_cluster`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_fom_clusterterm_fom_term1` FOREIGN KEY (`id_term`) REFERENCES `fom_term` (`id_term`) ON DELETE NO ACTION ON UPDATE NO ACTION;

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
-- Constraints for table `fom_postmedia`
--
ALTER TABLE `fom_postmedia`
  ADD CONSTRAINT `fk_attachment_media1` FOREIGN KEY (`id_media`) REFERENCES `fom_media` (`id_media`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_attachment_post1` FOREIGN KEY (`id_post`) REFERENCES `fom_post` (`id_post`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `fom_posttag`
--
ALTER TABLE `fom_posttag`
  ADD CONSTRAINT `fk_posttag_term1` FOREIGN KEY (`id_term`) REFERENCES `fom_term` (`id_term`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_posttag_post1` FOREIGN KEY (`id_post`) REFERENCES `fom_post` (`id_post`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `fom_query`
--
ALTER TABLE `fom_query`
  ADD CONSTRAINT `fk_fom_query_fom_user1` FOREIGN KEY (`id_user`) REFERENCES `fom_user` (`id_user`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `fom_querytag`
--
ALTER TABLE `fom_querytag`
  ADD CONSTRAINT `fk_fom_querytag_fom_query1` FOREIGN KEY (`id_query`) REFERENCES `fom_query` (`id_query`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_fom_querytag_fom_term1` FOREIGN KEY (`id_term`) REFERENCES `fom_term` (`id_term`) ON DELETE NO ACTION ON UPDATE NO ACTION;

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
  ADD CONSTRAINT `fk_fom_termrel_fom_termreltype1` FOREIGN KEY (`id_termreltype`) REFERENCES `fom_termreltype` (`id_termreltype`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_fom_termrel_fom_term2` FOREIGN KEY (`id_term2`) REFERENCES `fom_term` (`id_term`) ON DELETE NO ACTION ON UPDATE NO ACTION;
