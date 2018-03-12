/*
Navicat MariaDB Data Transfer

Source Server         : 代理商数据库
Source Server Version : 100122
Source Host           : 119.23.107.149:3306
Source Database       : morphling

Target Server Type    : MariaDB
Target Server Version : 100122
File Encoding         : 65001

Date: 2018-01-08 14:30:18
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
  `admin_user` varchar(100) NOT NULL,
  `settings` text NOT NULL,
  `create_time` datetime NOT NULL,
  `create_userid` int(11) NOT NULL,
  `create_username` varchar(50) NOT NULL,
  `state` tinyint(4) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of app_instance
-- ----------------------------

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
  `state` tinyint(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `host_address_uk` (`host_address`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of client
-- ----------------------------

-- ----------------------------
-- Table structure for env
-- ----------------------------
DROP TABLE IF EXISTS `env`;
CREATE TABLE `env` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `key` varchar(20) NOT NULL,
  `name` varchar(50) NOT NULL,
  `is_prod` bit(1) NOT NULL,
  `settings` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of env
-- ----------------------------
INSERT INTO `env` VALUES ('1', 'dev', '开发环境', '\0', '{\"etcds\":[\"\"],\"redises\":{1:{\"address\":\"\",\"name\":\"默认cluster集群\",\"type\":\"CLUSTER\"}},\"configPortal\":\"\"}');
INSERT INTO `env` VALUES ('2', 'test', '测试环境', '\0', '{\"etcds\":[\"\"],\"redises\":{1:{\"address\":\"\",\"name\":\"默认cluster集群\",\"type\":\"CLUSTER\"}},\"configPortal\":\"\"}');
INSERT INTO `env` VALUES ('3', 'release', '仿真环境', '\0', '{\"etcds\":[]}');
INSERT INTO `env` VALUES ('4', 'product', '线上环境', '', '{\"etcds\":[\"\",\"\",\"\"],\"redises\":{1:{\"address\":\"\",\"name\":\"默认cluster集群\",\"type\":\"CLUSTER\"}},\"configPortal\":\"\"}');

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
  `parent` int(11) NOT NULL DEFAULT '0',
  `url_matches` varchar(1000) NOT NULL,
  `ordered` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of menu
-- ----------------------------
INSERT INTO `menu` VALUES ('1', '系统配置', '1', 'icon-settings text-danger', '', '', '', '0', '', '1000');
INSERT INTO `menu` VALUES ('2', '客户端管理', '1', ' icon-screen-desktop text-info', 'system.client', '', '', '1', '/client/**', '0');
INSERT INTO `menu` VALUES ('3', '用户管理', '1', 'fa fa-users text-success', 'system.users', '', '', '1', '/user/**,/role/**,/user/**', '0');
INSERT INTO `menu` VALUES ('4', '应用管理', '1', 'fa fa-cloud text-primary', 'system.app', '', '', '1', '/env,/app/**', '0');
INSERT INTO `menu` VALUES ('5', '应用发布', '1', ' icon-cloud-upload text-success', 'app.deploy', '', '', '0', '/app/preview/**,/deploy/**', '0');
INSERT INTO `menu` VALUES ('6', '业务降级', '1', 'fa  fa-level-down text-danger', 'app.degrade', '', '', '0', '/degrade/**', '0');
INSERT INTO `menu` VALUES ('7', '缓存管理', '1', 'fa fa-database text-info', 'app.cache', '', '', '0', '/cache/**', '0');
INSERT INTO `menu` VALUES ('8', '端点监控', '1', 'glyphicon glyphicon-fire text-primary', 'app.endpoints', '', '', '0', '', '0');
INSERT INTO `menu` VALUES ('9', '顶部分割线', '3', '', '', '', '', '0', '', '0');

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
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of menu_role
-- ----------------------------
INSERT INTO `menu_role` VALUES ('6', '2', '5', '1');
INSERT INTO `menu_role` VALUES ('7', '2', '6', '1');
INSERT INTO `menu_role` VALUES ('8', '2', '7', '1');
INSERT INTO `menu_role` VALUES ('9', '2', '8', '1');

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of operate_log
-- ----------------------------

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role` varchar(50) NOT NULL,
  `description` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES ('1', 'SUPERADMIN', '超级管理员');
INSERT INTO `role` VALUES ('2', 'RD', '开发');
INSERT INTO `role` VALUES ('3', 'QA', '测试');
INSERT INTO `role` VALUES ('5', 'OP', '运维');

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
-- Records of shell_log
-- ----------------------------

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
  `state` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username_uk` (`username`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', '管理员', 'admin', '18888888888', 'admin@qq.com', '$2a$10$yQdf.cLpD1XHK7KOfjH0rONz.M23gXVEwjldl.kptmZHiuJfpJcdK', 'https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=8232468,2916696848&fm=27&gp=0.jpg', '2018-01-08 14:29:56', '118.186.227.243', '2017-11-23 17:19:16', '1');

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
) ENGINE=InnoDB AUTO_INCREMENT=70 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_app
-- ----------------------------
INSERT INTO `user_app` VALUES ('23', '3', '3', '2017-12-06 12:34:33', '1');
INSERT INTO `user_app` VALUES ('27', '3', '4', '2017-12-18 13:57:52', '1');
INSERT INTO `user_app` VALUES ('28', '3', '5', '2017-12-18 13:57:52', '1');
INSERT INTO `user_app` VALUES ('29', '3', '6', '2017-12-18 13:57:52', '1');
INSERT INTO `user_app` VALUES ('30', '3', '7', '2017-12-21 14:46:00', '1');
INSERT INTO `user_app` VALUES ('31', '3', '8', '2017-12-21 14:46:00', '1');
INSERT INTO `user_app` VALUES ('32', '3', '9', '2017-12-21 14:46:00', '1');
INSERT INTO `user_app` VALUES ('33', '3', '10', '2017-12-21 14:46:00', '1');
INSERT INTO `user_app` VALUES ('34', '7', '7', '2017-12-22 10:45:52', '1');
INSERT INTO `user_app` VALUES ('35', '7', '8', '2017-12-22 10:45:52', '1');
INSERT INTO `user_app` VALUES ('36', '7', '9', '2017-12-22 10:45:52', '1');
INSERT INTO `user_app` VALUES ('37', '7', '10', '2017-12-22 10:45:52', '1');
INSERT INTO `user_app` VALUES ('38', '6', '7', '2017-12-22 10:46:09', '1');
INSERT INTO `user_app` VALUES ('39', '6', '8', '2017-12-22 10:46:09', '1');
INSERT INTO `user_app` VALUES ('40', '6', '9', '2017-12-22 10:46:09', '1');
INSERT INTO `user_app` VALUES ('41', '6', '10', '2017-12-22 10:46:09', '1');
INSERT INTO `user_app` VALUES ('42', '5', '3', '2017-12-22 10:46:19', '1');
INSERT INTO `user_app` VALUES ('43', '5', '4', '2017-12-22 10:46:19', '1');
INSERT INTO `user_app` VALUES ('44', '5', '5', '2017-12-22 10:46:19', '1');
INSERT INTO `user_app` VALUES ('45', '5', '6', '2017-12-22 10:46:19', '1');
INSERT INTO `user_app` VALUES ('46', '5', '7', '2017-12-22 10:46:19', '1');
INSERT INTO `user_app` VALUES ('47', '5', '8', '2017-12-22 10:46:19', '1');
INSERT INTO `user_app` VALUES ('48', '5', '9', '2017-12-22 10:46:19', '1');
INSERT INTO `user_app` VALUES ('49', '5', '10', '2017-12-22 10:46:19', '1');
INSERT INTO `user_app` VALUES ('50', '8', '7', '2017-12-22 10:59:39', '1');
INSERT INTO `user_app` VALUES ('51', '8', '8', '2017-12-22 10:59:39', '1');
INSERT INTO `user_app` VALUES ('52', '8', '9', '2017-12-22 10:59:39', '1');
INSERT INTO `user_app` VALUES ('53', '8', '10', '2017-12-22 10:59:39', '1');
INSERT INTO `user_app` VALUES ('54', '9', '7', '2017-12-22 11:00:57', '1');
INSERT INTO `user_app` VALUES ('55', '9', '8', '2017-12-22 11:00:57', '1');
INSERT INTO `user_app` VALUES ('56', '9', '9', '2017-12-22 11:00:57', '1');
INSERT INTO `user_app` VALUES ('57', '9', '10', '2017-12-22 11:00:57', '1');
INSERT INTO `user_app` VALUES ('58', '10', '7', '2017-12-22 11:01:45', '1');
INSERT INTO `user_app` VALUES ('59', '10', '8', '2017-12-22 11:01:45', '1');
INSERT INTO `user_app` VALUES ('60', '10', '9', '2017-12-22 11:01:45', '1');
INSERT INTO `user_app` VALUES ('61', '10', '10', '2017-12-22 11:01:46', '1');
INSERT INTO `user_app` VALUES ('62', '6', '3', '2017-12-22 11:14:05', '1');
INSERT INTO `user_app` VALUES ('63', '6', '4', '2017-12-22 11:14:05', '1');
INSERT INTO `user_app` VALUES ('64', '6', '5', '2017-12-22 11:14:05', '1');
INSERT INTO `user_app` VALUES ('65', '6', '6', '2017-12-22 11:14:05', '1');
INSERT INTO `user_app` VALUES ('66', '7', '3', '2017-12-22 11:14:10', '1');
INSERT INTO `user_app` VALUES ('67', '7', '4', '2017-12-22 11:14:10', '1');
INSERT INTO `user_app` VALUES ('68', '7', '5', '2017-12-22 11:14:10', '1');
INSERT INTO `user_app` VALUES ('69', '7', '6', '2017-12-22 11:14:10', '1');

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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` VALUES ('1', '1', '1', '2017-11-06 16:50:38', '1');
