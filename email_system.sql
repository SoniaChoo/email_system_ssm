/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50730
Source Host           : 127.0.0.1:3306
Source Database       : email_system

Target Server Type    : MYSQL
Target Server Version : 50730
File Encoding         : 65001

Date: 2020-06-22 13:57:04
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
INSERT INTO `account` VALUES ('1111111', 'nucm1@wangpanhuiyuan.com', '656565', '5');
INSERT INTO `account` VALUES ('2222222', 'nucm2@wangpanhuiyuan.com', '656565', '9');
INSERT INTO `account` VALUES ('666666', 'nucm6@wangpanhuiyuan.com', '656565', '3');
INSERT INTO `account` VALUES ('66668888', 'nucm68@wangpanhuiyuan.com', '656565', '3');
INSERT INTO `account` VALUES ('8888', 'nucm8@wangpanhuiyuan.com', '656565', '5');

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
  `captcha_code` varchar(255) DEFAULT NULL,
  `captcha_html` text,
  `captcha_read` int(11) DEFAULT NULL COMMENT '是否已读',
  `captcha_receive_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '收件时间',
  `captcha_send_time` datetime DEFAULT NULL,
  PRIMARY KEY (`captcha_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of captcha
-- ----------------------------
INSERT INTO `captcha` VALUES ('05cb6384-2825-4f22-9d45-8d30d3ecc67c', 'soniachoo.zhu@gmail.com', 'random-nucm@wangpanhuiyuan.com', 'another test', 'hello你好カレンダー', null, null, null, '2020-06-20 22:55:56', '2020-06-20 16:03:02');
INSERT INTO `captcha` VALUES ('05cb6384-2825-4f22-9d45-8d30d3ecc67d', 'soniachoo.zhu@gmail.com', 'nucm68@wangpanhuiyuan.com', 'another test', 'hello000你好カレンダー', null, null, '1', '2020-06-22 12:45:47', '2020-06-22 12:13:02');
INSERT INTO `captcha` VALUES ('05cb6384-2825-4f22-9d45-8d30d3ecc67de', 'soniachoo.zhu@gmail.com', 'nucm68@wangpanhuiyuan.com', 'another test', 'hello999你好カレンダー', null, null, null, '2020-06-22 13:19:43', '2020-06-22 12:13:02');
INSERT INTO `captcha` VALUES ('05cb6384-2825-4f22-9d45-8d30d3ecc67df', 'soniachoo.zhu@gmail.com', 'nucm68@wangpanhuiyuan.com', 'another test', 'hello888你好カレンダー', null, null, '1', '2020-06-22 13:19:47', '2020-06-22 12:13:02');
INSERT INTO `captcha` VALUES ('05cb6384-2825-4f22-9d45-8d30d3ecc67dfg', 'soniachoo.zhu@gmail.com', 'nucm68@wangpanhuiyuan.com', 'another test', 'hello777你好カレンダー', null, null, null, '2020-06-22 13:19:50', '2020-06-22 12:13:02');
INSERT INTO `captcha` VALUES ('05cb6384-2825-4f22-9d45-8d30d3ecc67dfh', 'soniachoo.zhu@gmail.com', 'random-nucm@wangpanhuiyuan.com', 'another test', 'hello666你好カレンダー', null, null, null, '2020-06-22 13:19:54', '2020-06-22 12:13:02');
INSERT INTO `captcha` VALUES ('445fa954-865b-41da-a72d-a94a90298f29', 'soniachoo.zhu@gmail.com', 'random-nucm@wangpanhuiyuan.com', 'another test', 'hello你好カレンダー', null, null, null, '2020-06-20 22:56:41', '2020-06-20 16:03:02');
INSERT INTO `captcha` VALUES ('4913db88-e138-4189-b133-974aa18da839', 'soniachoo.zhu@gmail.com', 'random-nucm@wangpanhuiyuan.com', 'another test', 'hello你好カレンダー', null, null, null, '2020-06-20 22:58:00', '2020-06-20 16:03:02');

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
INSERT INTO `invitation` VALUES ('6666', '123456', '3', 'nucm68@wangpanhuiyuan.com', '2020-06-21 15:28:48', '8', '2020-06-22 13:02:41');
INSERT INTO `invitation` VALUES ('8888', '654321', '5', '', null, null, null);
