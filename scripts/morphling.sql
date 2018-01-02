/*
Navicat MariaDB Data Transfer

Source Server         : 192.168.100.20
Source Server Version : 100208
Source Host           : 192.168.100.20:3306
Source Database       : morphling

Target Server Type    : MariaDB
Target Server Version : 100208
File Encoding         : 65001

Date: 2017-12-21 14:36:15
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for app
-- ----------------------------
DROP TABLE IF EXISTS `app`;
CREATE TABLE `app` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `description` varchar(255) NOT NULL,
  `context_path` varchar(50) NOT NULL,
  `port` int(11) NOT NULL,
  `git_url` varchar(255) NOT NULL,
  `git_branch` varchar(50) NOT NULL DEFAULT '',
  `mvn_module` varchar(50) NOT NULL,
  `env` varchar(20) NOT NULL,
  `service_type` tinyint(4) NOT NULL,
  `deploy_type` tinyint(4) NOT NULL,
  `current_pack_version` varchar(50) NOT NULL,
  `apollo_id` varchar(50) NOT NULL DEFAULT '',
  `admin_user` varchar(100) NOT NULL,
  `settings` text NOT NULL,
  `create_time` datetime NOT NULL,
  `create_userid` int(11) NOT NULL,
  `create_username` varchar(50) NOT NULL,
  `state` tinyint(4) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of app
-- ----------------------------


-- ----------------------------
-- Table structure for app_instance
-- ----------------------------
DROP TABLE IF EXISTS `app_instance`;
CREATE TABLE `app_instance` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `app_id` int(11) NOT NULL,
  `client_id` int(11) NOT NULL,
  `current_version` varchar(50) NOT NULL,
  `status` tinyint(4) NOT NULL,
  `create_time` datetime NOT NULL,
  `state` tinyint(4) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for client
-- ----------------------------
DROP TABLE IF EXISTS `client`;
CREATE TABLE `client` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL,
  `host_address` varchar(20) NOT NULL,
  `port` int(11) NOT NULL,
  `env` varchar(20) NOT NULL,
  `remark` varchar(20) NOT NULL,
  `pack_version` varchar(20) NOT NULL,
  `create_userid` int(11) NOT NULL,
  `create_username` varchar(50) NOT NULL,
  `create_time` datetime NOT NULL,
  `state` tinyint(4) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`),
  UNIQUE KEY `host_address_uk` (`host_address`)
) ENGINE=InnoDB AUTO_INCREMENT=90 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for env
-- ----------------------------
DROP TABLE IF EXISTS `env`;
CREATE TABLE `env` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `key` varchar(20) NOT NULL,
  `name` varchar(50) NOT NULL,
  `is_prod` bit(1) NOT NULL,
  `settings` text DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of env
-- ----------------------------
INSERT INTO `env` VALUES ('1', 'dev', '开发环境', '\0', '');
INSERT INTO `env` VALUES ('2', 'test', '测试环境', '\0', '');
INSERT INTO `env` VALUES ('3', 'release', '仿真环境', '\0', '{\"etcds\":[]}');
INSERT INTO `env` VALUES ('4', 'product', '线上环境', '',  '');

-- ----------------------------
-- Table structure for env_role
-- ----------------------------
DROP TABLE IF EXISTS `env_role`;
CREATE TABLE `env_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NOT NULL,
  `env_key` varchar(20) NOT NULL,
  `state` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of env_role
-- ----------------------------
INSERT INTO `env_role` VALUES ('1', '2', 'dev', '1');
INSERT INTO `env_role` VALUES ('2', '2', 'test', '1');
INSERT INTO `env_role` VALUES ('3', '2', 'product', '1');

-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `text` varchar(100) NOT NULL DEFAULT '',
  `type` int(11) NOT NULL,
  `icon` varchar(255) NOT NULL DEFAULT '',
  `route` varchar(255) NOT NULL DEFAULT '',
  `html` varchar(5000) NOT NULL DEFAULT '',
  `translate` varchar(255) NOT NULL DEFAULT '',
  `parent` int(11) NOT NULL DEFAULT 0,
  `url_matches` varchar(1000) NOT NULL,
  `ordered` int(11) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of menu
-- ----------------------------
INSERT INTO `menu` VALUES ('0', '顶部分割线', '3', '', '', '', '', '0', '', '0');
INSERT INTO `menu` VALUES ('1', '系统配置', '1', 'icon-settings text-danger', '', '', '', '0', '', '1000');
INSERT INTO `menu` VALUES ('2', '客户端管理', '1', ' icon-screen-desktop text-info', 'system.client', '', '', '1', '/client/**', '0');
INSERT INTO `menu` VALUES ('3', '用户管理', '1', 'fa fa-users text-success', 'system.users', '', '', '1', '/user/**,/role/**,/user/**', '0');
INSERT INTO `menu` VALUES ('4', '应用管理', '1', 'fa fa-cloud text-primary', 'system.app', '', '', '1', '/env,/app/**', '0');
INSERT INTO `menu` VALUES ('5', '应用发布', '1', ' icon-cloud-upload text-success', 'app.deploy', '', '', '0', '/app/preview/**,/deploy/**', '0');
INSERT INTO `menu` VALUES ('6', '业务降级', '1', 'fa  fa-level-down text-danger', 'app.degrade', '', '', '0', '', '0');
INSERT INTO `menu` VALUES ('7', '缓存管理', '1', 'fa fa-database text-info', 'app.cache', '', '', '0', '/cache/**', '0');

-- ----------------------------
-- Table structure for menu_role
-- ----------------------------
DROP TABLE IF EXISTS `menu_role`;
CREATE TABLE `menu_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NOT NULL,
  `menu_id` int(11) NOT NULL,
  `state` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of menu_role
-- ----------------------------
INSERT INTO `menu_role` VALUES ('6', '2', '5', '1');

-- ----------------------------
-- Table structure for operate_log
-- ----------------------------
DROP TABLE IF EXISTS `operate_log`;
CREATE TABLE `operate_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` tinyint(4) NOT NULL,
  `service_id` int(11) NOT NULL,
  `service_name` varchar(255) NOT NULL,
  `log_id` varchar(20) NOT NULL,
  `create_userid` int(11) NOT NULL,
  `create_username` varchar(20) NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role` varchar(50) NOT NULL,
  `description` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES ('1', 'SUPERADMIN', '超级管理员');
INSERT INTO `role` VALUES ('2', 'DEVELOP', '开发');
INSERT INTO `role` VALUES ('3', 'QA', '测试');

-- ----------------------------
-- Table structure for shell_log
-- ----------------------------
DROP TABLE IF EXISTS `shell_log`;
CREATE TABLE `shell_log` (
  `id` bigint(20) NOT NULL,
  `status` varchar(11) NOT NULL,
  `content` text NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL DEFAULT '',
  `username` varchar(20) NOT NULL DEFAULT '',
  `phone` varchar(20) NOT NULL DEFAULT '',
  `email` varchar(50) NOT NULL DEFAULT '',
  `password` varchar(100) NOT NULL,
  `head_img` varchar(255) NOT NULL DEFAULT '',
  `last_login_time` datetime NOT NULL,
  `last_login_ip` varchar(50) NOT NULL,
  `create_time` datetime NOT NULL,
  `state` tinyint(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username_uk` (`username`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', '管理员', 'admin', '15652222294', '55375829@qq.com', '$2a$10$yQdf.cLpD1XHK7KOfjH0rONz.M23gXVEwjldl.kptmZHiuJfpJcdK', 'https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2611252081,3790047861&fm=27&gp=0.jpg', '2017-12-21 14:17:59', '127.0.0.1', '2017-11-23 17:19:16', '1');


-- ----------------------------
-- Table structure for user_app
-- ----------------------------
DROP TABLE IF EXISTS `user_app`;
CREATE TABLE `user_app` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `app_id` int(11) NOT NULL,
  `create_time` datetime NOT NULL,
  `state` tinyint(4) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  `create_time` datetime NOT NULL,
  `state` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

