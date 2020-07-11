/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50730
Source Host           : 127.0.0.1:3306
Source Database       : email_system

Target Server Type    : MYSQL
Target Server Version : 50730
File Encoding         : 65001

Date: 2020-06-26 00:00:35
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for account
-- ----------------------------
DROP TABLE IF EXISTS `account`;
CREATE TABLE `account` (
   `account_id` varchar(255) NOT NULL COMMENT '账户ID',
   `account_email` varchar(255) NOT NULL COMMENT '邮箱账号',
   `account_password` varchar(255) NOT NULL COMMENT '邮箱密码',
   `account_using_count` int(11) unsigned zerofill DEFAULT '00000000000' COMMENT '使用次数',
   PRIMARY KEY (`account_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of account
-- ----------------------------
INSERT INTO `account` VALUES ('1', 'xxxwangpan@sina.com', '123456qq', '00000000001');
INSERT INTO `account` VALUES ('66668888', 'nucm68@wangpanhuiyuan.com', '656565', '00000000002');
INSERT INTO `account` VALUES ('8888', 'nucm88@wangpanhuiyuan.com', 'nucm88_pw', '00000000001');

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
   `captcha_read` int(11) unsigned zerofill DEFAULT '00000000000' COMMENT '是否已读',
   `captcha_receive_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '收件时间',
   `captcha_send_time` datetime DEFAULT NULL,
   PRIMARY KEY (`captcha_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of captcha
-- ----------------------------
INSERT INTO `captcha` VALUES ('05cb6384-2825-4f22-9d45-8d30d3ecc67d', 'soniachoo.zhu@baidu.com', 'nucm68@wangpanhuiyuan.com', 'another test', 'hello000你好カレンダー', '223353', null, '00000000001', '2020-06-25 23:45:47', '2020-06-22 12:13:02');
INSERT INTO `captcha` VALUES ('05cb6384-2825-4f22-9d45-8d30d3ecc67dfh', 'soniachoo.zhu@baidu.com', 'xxxwangpan@sina.com', 'another test', 'hello666你好カレンダー', '196546', null, '00000000001', '2020-06-25 23:19:54', '2020-06-22 12:13:02');
INSERT INTO `captcha` VALUES ('445fa954-865b-41da-a72d-a94a90298f29', 'soniachoo.zhu@baidu.com', 'random-nucm@wangpanhuiyuan.com', 'another test', 'hello你好カレンダー', null, null, '00000000000', '2020-06-20 22:56:41', '2020-06-20 16:03:02');
INSERT INTO `captcha` VALUES ('4913db88-e138-4189-b133-974aa18da839', 'soniachoo.zhu@baidu.com', 'random-nucm@wangpanhuiyuan.com', 'another test', 'hello你好カレンダー', null, null, '00000000000', '2020-06-20 22:58:00', '2020-06-20 16:03:02');
INSERT INTO `captcha` VALUES ('b724f3f6-f4f7-46f3-8583-0dce5c425191', 'passport@baidu.com', 'nucm88@wangpanhuiyuan.com', '百度帐号--邮箱安全验证', null, '223353', '<html style=\"background:#f7f7f7\">\r\n<head>\r\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\r\n<title>百度帐号—邮箱安全验证</title>\r\n</head>\r\n<body>\r\n<div style=\"background:#f7f7f7;overflow:hidden\">\r\n<div>\r\n<img src=\"https://passport.baidu.com/img/logo.gif\" alt=\"\" class=\"logo\" ellpadding=\"0\" cellspacing=\"0\" style=\"margin:40px 0 0 60px\" />\r\n</div>\r\n<div style=\"background:#fff;border:1px solid #ccc;margin:2%;padding:0 30px\">\r\n<div style=\"line-height:40px;height:40px\">&nbsp;</div>\r\n<p style=\"margin:0;padding:0;font-size:14px;line-height:30px;color:#333;font-family:arial,sans-serif;font-weight:bold\">亲爱的用户：</p>\r\n<div style=\"line-height:20px;height:20px\">&nbsp;</div>\r\n<p style=\"margin:0;padding:0;line-height:30px;font-size:14px;color:#333;font-family:\'宋体\',arial,sans-serif\">您好！感谢您使用百度服务，您的账号（XX***会员）正在进行邮箱验证，本次请求的验证码为：</p>\r\n<p style=\"margin:0;padding:0;line-height:30px;font-size:14px;color:#333;font-family:\'宋体\',arial,sans-serif\"><b style=\"font-size:18px;color:#f90\">223353</b><span style=\"margin:0;padding:0;margin-left:10px;line-height:30px;font-size:14px;color:#979797;font-family:\'宋体\',arial,sans-serif\">(为了保障您帐号的安全性，请在1小时内完成验证。)</span></p>\r\n<div style=\"line-height:80px;height:80px\">&nbsp;</div>\r\n<p style=\"margin:0;padding:0;line-height:30px;font-size:14px;color:#333;font-family:\'宋体\',arial,sans-serif\">百度帐号团队</p>\r\n<p style=\"margin:0;padding:0;line-height:30px;font-size:14px;color:#333;font-family:\'宋体\',arial,sans-serif\">2020年06月21日</p>\r\n<div style=\"line-height:20px;height:20px\">&nbsp;</div>\r\n<div style=\"border-top:1px dashed #dfdfdf;padding:30px 0;overflow:hidden\">\r\n<div style=\"float:left;width:110px\">\r\n<img src=\"https://passport.baidu.com/export/app/img/qrcode_android.png\" style=\"border:1px solid #dfdfdf;padding:5px\" />\r\n</div>\r\n<div style=\"overflow:hidden\">\r\n<p style=\"text-indent:2em;color:#666;font-size:14px\">绑定<a href=\"https://passport.baidu.com/export/app/index.html\" style=\"font-size:16px;color:#36c;text-decoration:none;font-weight:bold\" target=\"_blank\">百度帐号管家</a>，收取验证码不再等待！</p>\r\n<p style=\"text-indent:2em;color:#666;font-size:14px\">赶紧扫描下载吧</p>\r\n</div>\r\n</div>\r\n</div>\r\n</div>\r\n</body>\r\n</html>', '00000000000', '2020-06-25 23:44:43', '2020-06-21 19:37:29');

-- ----------------------------
-- Table structure for invitation
-- ----------------------------
DROP TABLE IF EXISTS `invitation`;
CREATE TABLE `invitation` (
  `invitation_id` varchar(255) NOT NULL COMMENT '邀请码ID',
  `invitation_code` varchar(255) NOT NULL COMMENT '邀请码',
  `invitation_lifetime` int(11) NOT NULL COMMENT '有效天数',
  `invitation_email` varchar(255) DEFAULT NULL COMMENT '绑定的邮箱',
  `invitation_activate_time` datetime DEFAULT NULL COMMENT '激活时间',
  `invitation_captcha_count` int(11) unsigned zerofill DEFAULT '00000000000' COMMENT '验证次数',
  `invitation_first_captcha_time` datetime DEFAULT NULL COMMENT '初次验证时间',
  PRIMARY KEY (`invitation_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of invitation
-- ----------------------------
INSERT INTO `invitation` VALUES ('1', 'xxxwangpanhuiyuan', '1000', 'nucm88@wangpanhuiyuan.com', '2020-06-25 23:44:17', '00000000005', '2020-06-25 23:44:31');
INSERT INTO `invitation` VALUES ('2', 'ceshi', '100', 'nucm68@wangpanhuiyuan.com', '2020-06-25 22:44:31', '00000000001', '2020-06-26 00:00:18');
INSERT INTO `invitation` VALUES ('3', 'test', '199', 'nucm68@wangpanhuiyuan.com', '2020-06-25 22:53:52', '00000000004', '2020-06-25 23:09:33');
INSERT INTO `invitation` VALUES ('6666', 'test2', '3', 'xxxwangpan@sina.com', '2020-06-25 23:40:44', '00000000003', '2020-06-25 23:41:50');
