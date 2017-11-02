/*
Navicat MySQL Data Transfer

Source Server         : local
Source Server Version : 50605
Source Host           : localhost:3306
Source Database       : resourcemetadata

Target Server Type    : MYSQL
Target Server Version : 50605
File Encoding         : 65001

Date: 2017-11-02 18:31:09
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` varchar(50) NOT NULL,
  `account` varchar(200) DEFAULT NULL,
  `pwd` varchar(600) DEFAULT NULL,
  `fullname` varchar(200) DEFAULT NULL,
  `sex` bit(1) DEFAULT NULL,
  `city` varchar(100) DEFAULT NULL,
  `province` varchar(100) DEFAULT NULL,
  `country` varchar(100) DEFAULT NULL,
  `email` varchar(200) DEFAULT NULL,
  `phone` varchar(200) DEFAULT NULL,
  `state` tinyint(2) DEFAULT NULL,
  `union_id` varchar(50) DEFAULT NULL,
  `regdate` datetime DEFAULT NULL,
  `last_login_time` datetime DEFAULT NULL,
  `open_id` varchar(50) DEFAULT NULL,
  `last_Password_Reset_Date` datetime DEFAULT NULL,
  `roles` varchar(255) DEFAULT NULL COMMENT '由于角色屈指可数,直接记录到人员里面',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
