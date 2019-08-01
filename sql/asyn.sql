/*
 Navicat Premium Data Transfer

 Source Server         : 127.0.0.1
 Source Server Type    : MySQL
 Source Server Version : 50726
 Source Host           : 127.0.0.1
 Source Database       : beauty_ssm

 Target Server Type    : MySQL
 Target Server Version : 50726
 File Encoding         : utf-8

 Date: 08/01/2019 19:40:24 PM
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `asyn_cmd00`
-- ----------------------------
DROP TABLE IF EXISTS `asyn_cmd00`;
CREATE TABLE `asyn_cmd00` (
  `cmd_id` bigint(64) NOT NULL AUTO_INCREMENT,
  `cmd_type` varchar(255) DEFAULT NULL,
  `biz_id` varchar(255) DEFAULT NULL,
  `content` varchar(1024) DEFAULT NULL,
  `gmt_create` datetime DEFAULT NULL,
  `gmt_modify` datetime DEFAULT NULL,
  `execute_num` int(11) DEFAULT '0',
  `next_time` datetime DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `create_host_name` varchar(255) DEFAULT NULL,
  `create_ip` varchar(255) DEFAULT NULL,
  `create_name` varchar(255) DEFAULT NULL,
  `update_host_name` varchar(255) DEFAULT NULL,
  `update_ip` varchar(255) DEFAULT NULL,
  `success_executers` varchar(255) DEFAULT NULL,
  `env` varchar(255) DEFAULT NULL COMMENT '所在环境',
  PRIMARY KEY (`cmd_id`),
  UNIQUE KEY `biz` (`biz_id`) USING BTREE,
  KEY `gmt_create` (`gmt_create`) USING BTREE,
  KEY `status_next_time` (`status`,`next_time`,`env`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=98832 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `asyn_cmd01`
-- ----------------------------
DROP TABLE IF EXISTS `asyn_cmd01`;
CREATE TABLE `asyn_cmd01` (
  `cmd_id` bigint(64) NOT NULL AUTO_INCREMENT,
  `cmd_type` varchar(255) DEFAULT NULL,
  `biz_id` varchar(255) DEFAULT NULL,
  `content` varchar(1024) DEFAULT NULL,
  `gmt_create` datetime DEFAULT NULL,
  `gmt_modify` datetime DEFAULT NULL,
  `execute_num` int(11) DEFAULT '0',
  `next_time` datetime DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `create_host_name` varchar(255) DEFAULT NULL,
  `create_ip` varchar(255) DEFAULT NULL,
  `create_name` varchar(255) DEFAULT NULL,
  `update_host_name` varchar(255) DEFAULT NULL,
  `update_ip` varchar(255) DEFAULT NULL,
  `success_executers` varchar(255) DEFAULT NULL,
  `env` varchar(255) DEFAULT NULL COMMENT '所在环境',
  PRIMARY KEY (`cmd_id`),
  UNIQUE KEY `biz` (`biz_id`) USING BTREE,
  KEY `gmt_create` (`gmt_create`) USING BTREE,
  KEY `status_next_time` (`status`,`next_time`,`env`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `asyn_cmd02`
-- ----------------------------
DROP TABLE IF EXISTS `asyn_cmd02`;
CREATE TABLE `asyn_cmd02` (
  `cmd_id` bigint(64) NOT NULL AUTO_INCREMENT,
  `cmd_type` varchar(255) DEFAULT NULL,
  `biz_id` varchar(255) DEFAULT NULL,
  `content` varchar(1024) DEFAULT NULL,
  `gmt_create` datetime DEFAULT NULL,
  `gmt_modify` datetime DEFAULT NULL,
  `execute_num` int(11) DEFAULT '0',
  `next_time` datetime DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `create_host_name` varchar(255) DEFAULT NULL,
  `create_ip` varchar(255) DEFAULT NULL,
  `create_name` varchar(255) DEFAULT NULL,
  `update_host_name` varchar(255) DEFAULT NULL,
  `update_ip` varchar(255) DEFAULT NULL,
  `success_executers` varchar(255) DEFAULT NULL,
  `env` varchar(255) DEFAULT NULL COMMENT '所在环境',
  PRIMARY KEY (`cmd_id`),
  UNIQUE KEY `biz` (`biz_id`) USING BTREE,
  KEY `gmt_create` (`gmt_create`) USING BTREE,
  KEY `status_next_time` (`status`,`next_time`,`env`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `asyn_cmd03`
-- ----------------------------
DROP TABLE IF EXISTS `asyn_cmd03`;
CREATE TABLE `asyn_cmd03` (
  `cmd_id` bigint(64) NOT NULL AUTO_INCREMENT,
  `cmd_type` varchar(255) DEFAULT NULL,
  `biz_id` varchar(255) DEFAULT NULL,
  `content` varchar(1024) DEFAULT NULL,
  `gmt_create` datetime DEFAULT NULL,
  `gmt_modify` datetime DEFAULT NULL,
  `execute_num` int(11) DEFAULT '0',
  `next_time` datetime DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `create_host_name` varchar(255) DEFAULT NULL,
  `create_ip` varchar(255) DEFAULT NULL,
  `create_name` varchar(255) DEFAULT NULL,
  `update_host_name` varchar(255) DEFAULT NULL,
  `update_ip` varchar(255) DEFAULT NULL,
  `success_executers` varchar(255) DEFAULT NULL,
  `env` varchar(255) DEFAULT NULL COMMENT '所在环境',
  PRIMARY KEY (`cmd_id`),
  UNIQUE KEY `biz` (`biz_id`) USING BTREE,
  KEY `gmt_create` (`gmt_create`) USING BTREE,
  KEY `status_next_time` (`status`,`next_time`,`env`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `asyn_cmd_history00`
-- ----------------------------
DROP TABLE IF EXISTS `asyn_cmd_history00`;
CREATE TABLE `asyn_cmd_history00` (
  `cmd_id` bigint(64) NOT NULL AUTO_INCREMENT,
  `cmd_type` varchar(255) DEFAULT NULL,
  `biz_id` varchar(255) DEFAULT NULL,
  `content` varchar(1024) DEFAULT NULL,
  `gmt_create` datetime DEFAULT NULL,
  `gmt_modify` datetime DEFAULT NULL,
  `execute_num` int(11) DEFAULT '0',
  `next_time` datetime DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `create_host_name` varchar(255) DEFAULT NULL,
  `create_ip` varchar(255) DEFAULT NULL,
  `create_name` varchar(255) DEFAULT NULL,
  `update_host_name` varchar(255) DEFAULT NULL,
  `update_ip` varchar(255) DEFAULT NULL,
  `success_executers` varchar(255) DEFAULT NULL,
  `env` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`cmd_id`),
  UNIQUE KEY `biz` (`biz_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=59 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `asyn_cmd_history01`
-- ----------------------------
DROP TABLE IF EXISTS `asyn_cmd_history01`;
CREATE TABLE `asyn_cmd_history01` (
  `cmd_id` bigint(64) NOT NULL AUTO_INCREMENT,
  `cmd_type` varchar(255) DEFAULT NULL,
  `biz_id` varchar(255) DEFAULT NULL,
  `content` varchar(1024) DEFAULT NULL,
  `gmt_create` datetime DEFAULT NULL,
  `gmt_modify` datetime DEFAULT NULL,
  `execute_num` int(11) DEFAULT '0',
  `next_time` datetime DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `create_host_name` varchar(255) DEFAULT NULL,
  `create_ip` varchar(255) DEFAULT NULL,
  `create_name` varchar(255) DEFAULT NULL,
  `update_host_name` varchar(255) DEFAULT NULL,
  `update_ip` varchar(255) DEFAULT NULL,
  `success_executers` varchar(255) DEFAULT NULL,
  `env` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`cmd_id`),
  UNIQUE KEY `biz` (`biz_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `asyn_cmd_history02`
-- ----------------------------
DROP TABLE IF EXISTS `asyn_cmd_history02`;
CREATE TABLE `asyn_cmd_history02` (
  `cmd_id` bigint(64) NOT NULL AUTO_INCREMENT,
  `cmd_type` varchar(255) DEFAULT NULL,
  `biz_id` varchar(255) DEFAULT NULL,
  `content` varchar(1024) DEFAULT NULL,
  `gmt_create` datetime DEFAULT NULL,
  `gmt_modify` datetime DEFAULT NULL,
  `execute_num` int(11) DEFAULT '0',
  `next_time` datetime DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `create_host_name` varchar(255) DEFAULT NULL,
  `create_ip` varchar(255) DEFAULT NULL,
  `create_name` varchar(255) DEFAULT NULL,
  `update_host_name` varchar(255) DEFAULT NULL,
  `update_ip` varchar(255) DEFAULT NULL,
  `success_executers` varchar(255) DEFAULT NULL,
  `env` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`cmd_id`),
  UNIQUE KEY `biz` (`biz_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `asyn_cmd_history03`
-- ----------------------------
DROP TABLE IF EXISTS `asyn_cmd_history03`;
CREATE TABLE `asyn_cmd_history03` (
  `cmd_id` bigint(64) NOT NULL AUTO_INCREMENT,
  `cmd_type` varchar(255) DEFAULT NULL,
  `biz_id` varchar(255) DEFAULT NULL,
  `content` varchar(1024) DEFAULT NULL,
  `gmt_create` datetime DEFAULT NULL,
  `gmt_modify` datetime DEFAULT NULL,
  `execute_num` int(11) DEFAULT '0',
  `next_time` datetime DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `create_host_name` varchar(255) DEFAULT NULL,
  `create_ip` varchar(255) DEFAULT NULL,
  `create_name` varchar(255) DEFAULT NULL,
  `update_host_name` varchar(255) DEFAULT NULL,
  `update_ip` varchar(255) DEFAULT NULL,
  `success_executers` varchar(255) DEFAULT NULL,
  `env` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`cmd_id`),
  UNIQUE KEY `biz` (`biz_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;
