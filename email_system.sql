/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50730
Source Host           : 127.0.0.1:3306
Source Database       : email_system

Target Server Type    : MYSQL
Target Server Version : 50730
File Encoding         : 65001

Date: 2020-06-20 16:25:27
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for account
-- ----------------------------
DROP TABLE IF EXISTS `account`;
CREATE TABLE `account` (
  `account_id` varchar(255) NOT NULL COMMENT '账户ID',
  `account_email` varchar(255) DEFAULT NULL COMMENT '邮箱账号',
  `account_password` varchar(255) DEFAULT NULL COMMENT '邮箱密码',
  `account_using_count` int(11) DEFAULT NULL COMMENT '使用次数',
  PRIMARY KEY (`account_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of account
-- ----------------------------

-- ----------------------------
-- Table structure for captcha
-- ----------------------------
DROP TABLE IF EXISTS `captcha`;
CREATE TABLE `captcha` (
  `captcha_id` varchar(255) NOT NULL COMMENT '验证码ID',
  `captcha_from` varchar(255) DEFAULT NULL COMMENT '发件人',
  `captcha_to` varchar(255) DEFAULT NULL COMMENT '邮箱账号',
  `captcha_subject` varchar(255) DEFAULT NULL COMMENT '邮件主题',
  `captcha_content` text COMMENT '邮件内容',
  `captcha_read` int(11) DEFAULT NULL COMMENT '是否已读',
  `captcha_send_time` datetime DEFAULT NULL,
  `captcha_receive_time` datetime DEFAULT NULL COMMENT '收件时间',
  PRIMARY KEY (`captcha_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of captcha
-- ----------------------------
INSERT INTO `captcha` VALUES ('888888888888', '982709437@qq.com', '602232939@qq.com', 'test2', 'test2', '1', null, '2020-06-19 11:24:11');
INSERT INTO `captcha` VALUES ('9999999999', '982709437@qq.com', '602232939@qq.com', 'test3', 'test3', '0', null, null);

-- ----------------------------
-- Table structure for invitation
-- ----------------------------
DROP TABLE IF EXISTS `invitation`;
CREATE TABLE `invitation` (
  `invitation_id` varchar(255) NOT NULL COMMENT '邀请码ID',
  `invitation_code` varchar(255) DEFAULT NULL COMMENT '邀请码',
  `invitation_lifetime` int(11) DEFAULT NULL COMMENT '有效天数',
  `invitation_email` varchar(255) DEFAULT NULL COMMENT '绑定的邮箱',
  `invitation_activate_time` datetime DEFAULT NULL COMMENT '激活时间',
  `invitation_captcha_count` int(11) DEFAULT NULL COMMENT '验证次数',
  `invitation_first_captcha_time` datetime DEFAULT NULL COMMENT '初次验证时间',
  PRIMARY KEY (`invitation_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of invitation
-- ----------------------------
