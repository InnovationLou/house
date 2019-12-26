/*
 Navicat Premium Data Transfer

 Source Server         : nadev.xyz
 Source Server Type    : MySQL
 Source Server Version : 80017
 Source Host           : nadev.xyz:3306
 Source Schema         : fish_house

 Target Server Type    : MySQL
 Target Server Version : 80017
 File Encoding         : 65001

 Date: 26/12/2019 17:40:18
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for banner
-- ----------------------------
DROP TABLE IF EXISTS `banner`;
CREATE TABLE `banner` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `img_info` varchar(50) NOT NULL COMMENT '图片描述',
  `img_url` varchar(2083) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '图片url',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='banner图片';

-- ----------------------------
-- Records of banner
-- ----------------------------
BEGIN;
INSERT INTO `banner` VALUES (1, '1', 'https://fish-house-1252272257.cos.ap-chengdu.myqcloud.com/banner/1.png');
INSERT INTO `banner` VALUES (2, '2', 'https://fish-house-1252272257.cos.ap-chengdu.myqcloud.com/banner/1.png');
INSERT INTO `banner` VALUES (4, '3', 'https://fish-house-1252272257.cos.ap-chengdu.myqcloud.com/banner/1.png');
INSERT INTO `banner` VALUES (5, '4', 'https://fish-house-1252272257.cos.ap-chengdu.myqcloud.com/banner/1.png');
COMMIT;

-- ----------------------------
-- Table structure for bill
-- ----------------------------
DROP TABLE IF EXISTS `bill`;
CREATE TABLE `bill` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `house_id` int(11) DEFAULT NULL COMMENT '房子id',
  `money` double DEFAULT NULL COMMENT '付款金额',
  `pay_item` varchar(255) DEFAULT NULL COMMENT '支付项目类型  ',
  `is_paid` tinyint(4) DEFAULT NULL COMMENT '是否支付',
  `out_trade_no` varchar(255) DEFAULT NULL COMMENT '付款单号',
  `gmt_create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期 ',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='这个表用于记录租客的账单';

-- ----------------------------
-- Records of bill
-- ----------------------------
BEGIN;
INSERT INTO `bill` VALUES (1, 2, 2, 300, 'cash', NULL, '1905310', '2019-12-25 22:01:22', NULL);
INSERT INTO `bill` VALUES (2, 9, 3, 8888.88, 'deposit', 0, '2208200', '2019-12-25 22:09:00', NULL);
INSERT INTO `bill` VALUES (3, 9, 3, 888.88, 'cash', NULL, '2203470', '2019-12-25 22:32:02', NULL);
INSERT INTO `bill` VALUES (4, 1, 2, 300, 'utilities', 0, NULL, '2019-12-26 09:27:00', NULL);
INSERT INTO `bill` VALUES (5, 1, 2, 888, 'deposit', 0, NULL, '2019-12-26 09:27:25', NULL);
INSERT INTO `bill` VALUES (6, 1, 2, 2000, 'cash', 0, NULL, '2019-12-26 09:27:35', NULL);
INSERT INTO `bill` VALUES (7, 7, 2, 888, 'deposit', 0, '2203470', '2019-12-26 15:50:15', '押金');
INSERT INTO `bill` VALUES (8, 7, 3, 888.88, 'cash', 0, '2203470', '2019-12-26 15:50:29', '租金');
INSERT INTO `bill` VALUES (9, 7, 3, 777.77, 'utilities', 0, '2203470', '2019-12-26 15:50:55', '水电费');
COMMIT;

-- ----------------------------
-- Table structure for browse
-- ----------------------------
DROP TABLE IF EXISTS `browse`;
CREATE TABLE `browse` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(10) unsigned NOT NULL COMMENT '用户id',
  `house_id` int(10) unsigned NOT NULL COMMENT '房源id',
  `gmt_create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '浏览时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='房源浏览记录';

-- ----------------------------
-- Records of browse
-- ----------------------------
BEGIN;
INSERT INTO `browse` VALUES (2, 1, 2, '2019-12-25 16:41:46');
INSERT INTO `browse` VALUES (3, 1, 3, '2019-12-25 16:41:51');
INSERT INTO `browse` VALUES (4, 1, 4, '2019-12-25 17:05:40');
INSERT INTO `browse` VALUES (5, 1, 5, '2019-12-25 17:05:46');
INSERT INTO `browse` VALUES (6, 1, 2, '2019-12-26 10:18:18');
INSERT INTO `browse` VALUES (7, 7, 4, '2019-12-26 15:30:36');
INSERT INTO `browse` VALUES (8, 7, 3, '2019-12-26 15:30:45');
INSERT INTO `browse` VALUES (9, 6, 2, '2019-12-26 15:32:40');
INSERT INTO `browse` VALUES (10, 6, 2, '2019-12-26 15:35:40');
INSERT INTO `browse` VALUES (11, 6, 2, '2019-12-26 15:37:32');
INSERT INTO `browse` VALUES (12, 6, 2, '2019-12-26 15:49:08');
INSERT INTO `browse` VALUES (13, 6, 2, '2019-12-26 15:53:08');
INSERT INTO `browse` VALUES (14, 6, 2, '2019-12-26 15:59:13');
INSERT INTO `browse` VALUES (15, 6, 2, '2019-12-26 16:00:09');
INSERT INTO `browse` VALUES (16, 6, 2, '2019-12-26 16:01:16');
INSERT INTO `browse` VALUES (17, 6, 2, '2019-12-26 16:04:07');
INSERT INTO `browse` VALUES (18, 6, 2, '2019-12-26 16:06:34');
INSERT INTO `browse` VALUES (19, 6, 2, '2019-12-26 16:06:53');
INSERT INTO `browse` VALUES (20, 6, 3, '2019-12-26 16:07:01');
INSERT INTO `browse` VALUES (21, 6, 4, '2019-12-26 16:08:26');
INSERT INTO `browse` VALUES (22, 6, 2, '2019-12-26 16:08:29');
INSERT INTO `browse` VALUES (23, 6, 2, '2019-12-26 16:18:02');
INSERT INTO `browse` VALUES (24, 6, 4, '2019-12-26 16:18:29');
INSERT INTO `browse` VALUES (25, 6, 2, '2019-12-26 16:18:32');
INSERT INTO `browse` VALUES (26, 6, 2, '2019-12-26 16:43:26');
INSERT INTO `browse` VALUES (27, 6, 2, '2019-12-26 17:02:26');
INSERT INTO `browse` VALUES (28, 6, 2, '2019-12-26 17:03:30');
INSERT INTO `browse` VALUES (29, 6, 2, '2019-12-26 17:05:30');
INSERT INTO `browse` VALUES (30, 6, 2, '2019-12-26 17:07:03');
INSERT INTO `browse` VALUES (31, 6, 2, '2019-12-26 17:29:53');
COMMIT;

-- ----------------------------
-- Table structure for collection
-- ----------------------------
DROP TABLE IF EXISTS `collection`;
CREATE TABLE `collection` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(11) unsigned NOT NULL,
  `house_id` int(10) unsigned NOT NULL,
  `gmt_create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='房源收藏信息';

-- ----------------------------
-- Records of collection
-- ----------------------------
BEGIN;
INSERT INTO `collection` VALUES (2, 1, 1, '2019-12-25 14:28:00');
INSERT INTO `collection` VALUES (3, 1, 2, '2019-12-26 10:15:49');
COMMIT;

-- ----------------------------
-- Table structure for house
-- ----------------------------
DROP TABLE IF EXISTS `house`;
CREATE TABLE `house` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT '房东openid',
  `house_info` varchar(2000) NOT NULL COMMENT '房屋信息',
  `released` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否发布',
  `rented` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否出租',
  `tenant_id` int(11) unsigned NOT NULL COMMENT '房客,租房者id',
  `cash` int(10) unsigned DEFAULT NULL COMMENT '租金',
  `cash_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '租金付款类型 eg: 押一付三',
  `area` float unsigned DEFAULT NULL COMMENT '面积',
  `floor` tinyint(3) unsigned DEFAULT NULL COMMENT '所在楼层',
  `gmt_create` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modify` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `orientation` varchar(2) NOT NULL COMMENT '房屋朝向',
  `daylighting` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '采光',
  `textarea` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `has_elevator` tinyint(1) DEFAULT '0' COMMENT '是否有电梯',
  `has_televison` tinyint(1) DEFAULT '0' COMMENT '是否有电视',
  `has_refrigerator` tinyint(1) DEFAULT '0' COMMENT '是否有冰箱',
  `has_washer` tinyint(1) DEFAULT '0' COMMENT '是否有洗衣机',
  `has_air_conditioner` tinyint(1) DEFAULT '0' COMMENT '是否有空调',
  `has_heater` tinyint(1) DEFAULT '0' COMMENT '热水器',
  `has_bed` tinyint(1) DEFAULT '0' COMMENT '床',
  `has_heating` tinyint(1) DEFAULT '0' COMMENT '暖气',
  `has_boradband` tinyint(1) DEFAULT '0' COMMENT '宽带',
  `has_wardrobe` tinyint(1) DEFAULT '0' COMMENT '衣柜',
  `has_gas` tinyint(1) DEFAULT '0' COMMENT '天然气',
  `province` varchar(10) DEFAULT NULL COMMENT '省份',
  `city` varchar(10) DEFAULT NULL COMMENT '市',
  `district` varchar(10) DEFAULT NULL COMMENT '区县',
  `street` varchar(10) DEFAULT NULL COMMENT '街道',
  `street_number` varchar(10) DEFAULT NULL COMMENT '门牌号',
  `lat` double(9,6) DEFAULT NULL COMMENT '纬度',
  `lng` double(9,6) DEFAULT NULL COMMENT '经度',
  `house_type` varchar(10) DEFAULT NULL COMMENT '户型',
  `has_complete` tinyint(1) DEFAULT '0' COMMENT '周边配套是否齐全',
  `short_rent` tinyint(1) DEFAULT '0' COMMENT '可短租',
  `girl_shared` tinyint(1) DEFAULT NULL COMMENT '女生合租',
  `boy_shared` tinyint(1) DEFAULT NULL COMMENT '男生合租',
  `has_balcony` tinyint(1) unsigned zerofill DEFAULT NULL COMMENT '有独立阳台',
  `rent_type` tinyint(1) unsigned DEFAULT '0' COMMENT '1 整租 0 合租',
  `heading_img` varchar(2083) DEFAULT NULL COMMENT '标题图',
  `tags` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '房源标签列表 ["精选", "近地铁"]',
  `title` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '标题',
  `decoration` varchar(5) DEFAULT NULL COMMENT '装修',
  `check_in_date` datetime DEFAULT NULL COMMENT '入住日期',
  `inspection` varchar(10) DEFAULT NULL COMMENT '看房',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='房源信息';

-- ----------------------------
-- Records of house
-- ----------------------------
BEGIN;
INSERT INTO `house` VALUES (2, 1, '123456789', 0, 0, 10002, 2200, '押一付一', 95, 7, '2019-12-17 20:41:17', '2019-12-25 16:18:31', '南', '充足', NULL, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 1, '四川省', '雅安市', '雨城区', '东城街道', '新康路46号', 29.977093, 102.992122, '二室一厅一卫', 0, 0, 0, 0, 1, 0, 'https://fish-house-1252272257.cos.ap-chengdu.myqcloud.com/house/2-1.jpg', '{\"精选好房\",\"押一付一\",\"近地铁\",\"配套齐全\"}', '正对锦江 后看麓湖 户型方正', '精装', '2019-12-24 23:24:35', '随时可看');
INSERT INTO `house` VALUES (3, 2, '123456', 0, 0, 10003, 3100, '押一付三', 110, 8, '2019-12-20 19:29:39', '2019-12-25 16:19:13', '北', '充足', NULL, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, '四川省', '成都市', '武侯区', '不知名街道', '四不像路666号', 29.877093, 102.112334, '三室二厅二卫', 0, 0, 0, 0, 0, 0, 'https://fish-house-1252272257.cos.ap-chengdu.myqcloud.com/house/3-1.jpg', '{\"精选好房\",\"押一付三\",\"近地铁\"}', '精装套二 视野开阔 采光充足', '精装', '2019-12-05 23:24:38', '随时可看');
INSERT INTO `house` VALUES (4, 3, '3333', 0, 0, 10004, 12122, NULL, 120, NULL, '2019-12-25 22:31:33', '2019-12-25 22:31:33', '西', NULL, NULL, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 0, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for house_img
-- ----------------------------
DROP TABLE IF EXISTS `house_img`;
CREATE TABLE `house_img` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `house_id` int(10) unsigned DEFAULT NULL COMMENT '房屋ID',
  `img_url` varchar(2083) DEFAULT NULL COMMENT '图片链接',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='房源图片';

-- ----------------------------
-- Table structure for house_order
-- ----------------------------
DROP TABLE IF EXISTS `house_order`;
CREATE TABLE `house_order` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '这个表记录了用户预支付信息',
  `user_id` int(11) DEFAULT NULL,
  `house_id` int(11) DEFAULT NULL,
  `open_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户id',
  `out_trade_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '随机生成的订单号',
  `prepay_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '微信生成预支付订单号',
  `total_fee` decimal(10,2) unsigned DEFAULT '0.00' COMMENT '总金额',
  `gmt_create` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '订单生成时间',
  `gmt_modify` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '订单状态更改时间',
  `is_paid` tinyint(1) unsigned DEFAULT NULL COMMENT '是否已经支付,0未支付,1已支付',
  `is_finished` tinyint(1) unsigned DEFAULT NULL COMMENT '是否已完成，后台判断了密码后，才应该确认更改此字段',
  `addr_ip` varbinary(255) DEFAULT NULL COMMENT '用户付款IP',
  `pay_item` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '付款的项目,目前应该只有deposit,utilities,cash',
  `lease` int(4) DEFAULT NULL COMMENT '租期  单位以月计算',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='这个表记录了用户预支付信息';

-- ----------------------------
-- Records of house_order
-- ----------------------------
BEGIN;
INSERT INTO `house_order` VALUES (1, 2, 2, 'ovtRp5PhDlVNzlBaugrYi7hYzgXY', '1905310', NULL, 13.00, '2019-12-19 19:05:31', '2019-12-25 21:59:51', 0, NULL, NULL, NULL, NULL);
INSERT INTO `house_order` VALUES (2, 1, 2, 'ovtRp5G9EzgHdlIsD0zjZtbj8h0s', '1939520', NULL, 1.00, '2019-12-20 19:39:53', '2019-12-25 21:59:53', 0, NULL, NULL, NULL, NULL);
INSERT INTO `house_order` VALUES (3, 1, 3, 'ovtRp5G9EzgHdlIsD0zjZtbj8h0s', '1944340', NULL, 541.00, '2019-12-20 19:44:35', '2019-12-25 21:59:54', 0, NULL, NULL, NULL, NULL);
INSERT INTO `house_order` VALUES (4, 2, 2, 'ovtRp5PhDlVNzlBaugrYi7hYzgXY', '1151470', NULL, 113.00, '2019-12-22 11:51:47', '2019-12-25 21:59:54', 0, NULL, NULL, NULL, NULL);
INSERT INTO `house_order` VALUES (5, 2, 3, 'ovtRp5PhDlVNzlBaugrYi7hYzgXY', '1222260', NULL, 123.00, '2019-12-22 12:22:27', '2019-12-25 21:59:55', 0, NULL, NULL, NULL, NULL);
INSERT INTO `house_order` VALUES (6, 2, 2, 'ovtRp5PhDlVNzlBaugrYi7hYzgXY', '1228520', NULL, 123.00, '2019-12-22 12:28:52', '2019-12-25 21:59:57', 0, NULL, NULL, NULL, NULL);
INSERT INTO `house_order` VALUES (7, 9, 1, 'ovtRp5Hz14i84ZTrgafIV_GbDAq0', '2203470', NULL, 888.88, '2019-12-25 22:03:48', '2019-12-25 22:03:48', 0, NULL, NULL, 'cash', NULL);
INSERT INTO `house_order` VALUES (8, 9, 3, 'ovtRp5Hz14i84ZTrgafIV_GbDAq0', '2208200', NULL, 8888.88, '2019-12-25 22:08:21', '2019-12-25 22:08:21', 0, NULL, NULL, 'deposit', NULL);
INSERT INTO `house_order` VALUES (9, 7, 1, 'ovtRp5KEYcui5YvKFJ_mrG1IV074', '1434570', NULL, 304.14, '2019-12-26 14:34:58', '2019-12-26 14:34:58', 0, NULL, NULL, '\"水电\"', NULL);
INSERT INTO `house_order` VALUES (10, 7, 2, 'ovtRp5KEYcui5YvKFJ_mrG1IV074', '1435430', NULL, 304.14, '2019-12-26 14:35:43', '2019-12-26 14:35:43', 0, NULL, NULL, '\"水电\"', NULL);
INSERT INTO `house_order` VALUES (11, 7, 2, 'ovtRp5KEYcui5YvKFJ_mrG1IV074', '1440550', NULL, 304.14, '2019-12-26 14:40:55', '2019-12-26 14:40:55', 0, NULL, NULL, '\"水电\"', NULL);
INSERT INTO `house_order` VALUES (12, 7, 2, 'ovtRp5KEYcui5YvKFJ_mrG1IV074', '1441060', NULL, 304.14, '2019-12-26 14:41:06', '2019-12-26 14:41:06', 0, NULL, NULL, '\"水电\"', NULL);
INSERT INTO `house_order` VALUES (13, 7, 2, 'ovtRp5KEYcui5YvKFJ_mrG1IV074', '1541110', NULL, 304.14, '2019-12-26 15:41:11', '2019-12-26 15:41:11', 0, NULL, NULL, '\"水电\"', NULL);
COMMIT;

-- ----------------------------
-- Table structure for house_repair
-- ----------------------------
DROP TABLE IF EXISTS `house_repair`;
CREATE TABLE `house_repair` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `house_id` int(10) unsigned NOT NULL COMMENT '房源ID 关联house表',
  `user_id` int(10) unsigned DEFAULT NULL COMMENT '报修用户ID',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '联系电话',
  `content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '报修描述',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '报修状态 0 未处理 1  处理中 2 已关闭',
  `gmt_create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '报修创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='报修信息表';

-- ----------------------------
-- Table structure for house_repair_img
-- ----------------------------
DROP TABLE IF EXISTS `house_repair_img`;
CREATE TABLE `house_repair_img` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `house_id` int(10) unsigned DEFAULT NULL COMMENT '房屋ID 关联house表',
  `url` varchar(2083) DEFAULT NULL COMMENT '图片url',
  `gmt_create` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='报修图片表';

-- ----------------------------
-- Table structure for house_sign
-- ----------------------------
DROP TABLE IF EXISTS `house_sign`;
CREATE TABLE `house_sign` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '这个表用于记录用户签约信息',
  `user_id` int(11) DEFAULT NULL,
  `house_id` int(11) DEFAULT NULL,
  `gmt_create` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '签约时间',
  `is_fulfill` tinyint(1) unsigned DEFAULT NULL COMMENT '是否履行中 ',
  `end_create` timestamp NULL DEFAULT NULL COMMENT '到期时间',
  `exp_date` int(11) DEFAULT NULL COMMENT '有效期,单位为月',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户签约信息';

-- ----------------------------
-- Records of house_sign
-- ----------------------------
BEGIN;
INSERT INTO `house_sign` VALUES (1, 1, 2, '2019-12-26 13:15:15', 1, '2020-06-01 13:15:35', 6);
COMMIT;

-- ----------------------------
-- Table structure for im_msg
-- ----------------------------
DROP TABLE IF EXISTS `im_msg`;
CREATE TABLE `im_msg` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '该表是保存所有的聊天记录的,聊天记录有点多，上百万数据，需要定期清理',
  `sender_id` bigint(20) NOT NULL COMMENT '发消息人的id',
  `receiver_id` bigint(20) NOT NULL COMMENT '收消息人的id',
  `type` enum('product','message') NOT NULL DEFAULT 'message' COMMENT '消息类型,产品类型的话msg字段可以为空,但是返回给前台必须包含产品信息',
  `msg` varchar(255) DEFAULT NULL COMMENT '消息内容/如果类型是product可以为空',
  `is_read` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '消息是否被读过了',
  `gmt_send` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '消息生成时间,也就是发送时间',
  `gmt_read` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '消息被读取时间',
  `house_id` bigint(20) unsigned DEFAULT NULL COMMENT '如果type是product,该字段去查询相关house信息，然后返回前台',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of im_msg
-- ----------------------------
BEGIN;
INSERT INTO `im_msg` VALUES (5, 6, 9, 'message', '09090909090990', 1, '2019-12-26 13:48:42', '2019-12-26 16:33:49', NULL);
INSERT INTO `im_msg` VALUES (6, 6, 9, 'message', '的', 1, '2019-12-26 13:48:51', '2019-12-26 14:38:36', NULL);
INSERT INTO `im_msg` VALUES (7, 9, 6, 'message', '33', 1, '2019-12-26 13:48:59', '2019-12-26 14:38:36', NULL);
INSERT INTO `im_msg` VALUES (8, 6, 9, 'message', '33', 1, '2019-12-26 13:49:09', '2019-12-26 16:56:48', NULL);
INSERT INTO `im_msg` VALUES (9, 9, 6, 'message', '在吗？我来看房', 1, '2019-12-26 16:04:03', '2019-12-26 16:07:05', NULL);
INSERT INTO `im_msg` VALUES (10, 9, 6, 'message', '操你妈', 1, '2019-12-26 16:04:13', '2019-12-26 16:07:05', NULL);
INSERT INTO `im_msg` VALUES (11, 9, 6, 'product', '\"\"', 1, '2019-12-26 16:05:08', '2019-12-26 16:07:05', 3);
INSERT INTO `im_msg` VALUES (12, 6, 9, 'message', '看你二爷的火腿', 1, '2019-12-26 16:06:19', '2019-12-26 16:56:48', 3);
COMMIT;

-- ----------------------------
-- Table structure for message
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message` (
  `id` int(10) unsigned NOT NULL,
  `content` varchar(500) DEFAULT NULL COMMENT '内容',
  `img_url` varchar(2083) DEFAULT NULL COMMENT '图片链接',
  `gmt_create` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modify` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统消息表';

-- ----------------------------
-- Table structure for pay_owner
-- ----------------------------
DROP TABLE IF EXISTS `pay_owner`;
CREATE TABLE `pay_owner` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `open_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户id',
  `payment` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '订单号',
  `gmt_create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '订单生成时间',
  `gmt_modify` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '订单状态更改时间',
  `money` decimal(10,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '付款金额',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='记录平台给房东打款';

-- ----------------------------
-- Table structure for refund_user
-- ----------------------------
DROP TABLE IF EXISTS `refund_user`;
CREATE TABLE `refund_user` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '这个表用来给退款给用户',
  `open_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '微信提供的用户唯一ID(相对于本小程序)',
  `out_trade_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '付款的时候自己生成的订单号',
  `gmt_create` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '订单生成时间',
  `gmt_modify` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '订单状态更改时间',
  `money` decimal(10,2) unsigned DEFAULT '0.00' COMMENT '付款金额',
  `is_success` tinyint(4) DEFAULT NULL COMMENT '0为退款失败，1为退款成功',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='退款给用户';

-- ----------------------------
-- Records of refund_user
-- ----------------------------
BEGIN;
INSERT INTO `refund_user` VALUES (1, 'ovtRp5G9EzgHdlIsD0zjZtbj8h0s', '1905310', '2019-12-21 10:38:02', '2019-12-21 10:38:03', 13.00, 0);
INSERT INTO `refund_user` VALUES (2, 'ovtRp5PhDlVNzlBaugrYi7hYzgXY', '1905310', '2019-12-22 11:51:17', '2019-12-22 11:51:19', 13.00, 0);
INSERT INTO `refund_user` VALUES (3, 'ovtRp5PhDlVNzlBaugrYi7hYzgXY', '1905310', '2019-12-22 12:01:06', '2019-12-22 12:01:07', 13.00, 0);
COMMIT;

-- ----------------------------
-- Table structure for store
-- ----------------------------
DROP TABLE IF EXISTS `store`;
CREATE TABLE `store` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '商家名称',
  `head_img` varchar(2083) DEFAULT NULL COMMENT '标题图',
  `address` varchar(50) DEFAULT NULL COMMENT '商家地址',
  `opening_hours` varchar(20) DEFAULT NULL COMMENT '营业时间段',
  `phone` varchar(255) DEFAULT NULL COMMENT '联系电话',
  `type` varchar(4) DEFAULT NULL COMMENT '商家分类 零食便利 美容美发餐饮美食',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商家信息';

-- ----------------------------
-- Records of store
-- ----------------------------
BEGIN;
INSERT INTO `store` VALUES (1, '渔捞石锅河鲜火锅', 'https://fish-house-1252272257.cos.ap-chengdu.myqcloud.com/store/1.jpg', '四川省成都市龙泉驿区金桉路88号希尔顿酒店美食街11栋1-2号', '周一至周日 10:30-23:30', '15397623834', '餐饮美食');
INSERT INTO `store` VALUES (2, 'LIFAN丽梵日式沙龙', 'https://fish-house-1252272257.cos.ap-chengdu.myqcloud.com/store/2.jpg', '四川省成都市成华区府华二路68号4栋1楼附5号', '周一至周日 10:00-21:30', '13980473545', '美容美发');
INSERT INTO `store` VALUES (3, '全家711（金牛万达店）', 'https://fish-house-1252272257.cos.ap-chengdu.myqcloud.com/store/4.jpg', '四川省成都市金牛区肖家二巷89号附5号1层', '周一至周日 12:00-24:00', '02862608576', '零售便利');
COMMIT;

-- ----------------------------
-- Table structure for store_img
-- ----------------------------
DROP TABLE IF EXISTS `store_img`;
CREATE TABLE `store_img` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `store_id` int(10) unsigned DEFAULT NULL,
  `img_url` varchar(2083) DEFAULT NULL,
  `gmt_create` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商家图片';

-- ----------------------------
-- Records of store_img
-- ----------------------------
BEGIN;
INSERT INTO `store_img` VALUES (1, 1, 'https://fish-house-1252272257.cos.ap-chengdu.myqcloud.com/store/1.jpg', '2019-12-25 16:06:31');
INSERT INTO `store_img` VALUES (2, 2, 'https://fish-house-1252272257.cos.ap-chengdu.myqcloud.com/store/2.jpg', '2019-12-25 16:06:42');
INSERT INTO `store_img` VALUES (3, 2, 'https://fish-house-1252272257.cos.ap-chengdu.myqcloud.com/store/3.jpg', '2019-12-25 16:07:36');
INSERT INTO `store_img` VALUES (4, 1, 'https://fish-house-1252272257.cos.ap-chengdu.myqcloud.com/store/1.jpg', '2019-12-25 16:07:54');
INSERT INTO `store_img` VALUES (5, 3, 'https://fish-house-1252272257.cos.ap-chengdu.myqcloud.com/store/4.jpg', '2019-12-25 17:05:56');
INSERT INTO `store_img` VALUES (6, 3, 'https://fish-house-1252272257.cos.ap-chengdu.myqcloud.com/store/4.jpg', '2019-12-25 17:06:07');
COMMIT;

-- ----------------------------
-- Table structure for transfer_record
-- ----------------------------
DROP TABLE IF EXISTS `transfer_record`;
CREATE TABLE `transfer_record` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '这个表用于记录手动打款记录',
  `withdraw_ment` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '生成的提款订单号',
  `wx_id` varchar(255) DEFAULT NULL,
  `transfer_money` decimal(10,0) DEFAULT NULL COMMENT '付给用户的钱',
  `gmt_create` timestamp NULL DEFAULT NULL COMMENT '创建日期',
  `gmt_modify` timestamp NULL DEFAULT NULL COMMENT '修改日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='记录手动打款记录';

-- ----------------------------
-- Records of transfer_record
-- ----------------------------
BEGIN;
INSERT INTO `transfer_record` VALUES (1, '7aa0d303876f4214', '1791482634', 11, '2019-12-19 21:13:22', NULL);
INSERT INTO `transfer_record` VALUES (2, '7aa0d303876f4214', '1791482634', 11, '2019-12-19 21:14:09', NULL);
COMMIT;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `open_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '微信提供的用户唯一ID(相对于本小程序)',
  `nick_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '昵称',
  `city` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '所在城市',
  `province` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '省份',
  `country` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '国家',
  `language` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '语言',
  `gender` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '性别 0-无,1-男,2-女',
  `money` decimal(10,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '钱包余额',
  `gmt_create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modify` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `landlord` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否房东',
  `auth_img_url` varchar(2083) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '认证了房东 有相关的图片链接 身份证\r\n',
  `id_card_front_img` varchar(2083) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '证件正面',
  `id_card_back_img` varchar(2083) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '证件背面',
  `handwritten_signature` varchar(2083) DEFAULT NULL COMMENT '电子手签图',
  `is_auth` tinyint(1) unsigned DEFAULT '0' COMMENT '是否身份认证',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `open_id` (`open_id`) USING BTREE COMMENT '保证用户唯一'
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户信息表';

-- ----------------------------
-- Records of user
-- ----------------------------
BEGIN;
INSERT INTO `user` VALUES (1, 'ovtRp5G9EzgHdlIsD0zjZtbj8h0s', 'coder', '北京', NULL, NULL, NULL, 0, 111066.00, '2019-12-17 19:09:09', '2019-12-26 13:28:43', 0, NULL, NULL, NULL, NULL, 1);
INSERT INTO `user` VALUES (2, 'ovtRp5PhDlVNzlBaugrYi7hYzgXY', 'test1', 'YiBin', NULL, 'China', NULL, 1, 0.00, '2019-12-17 16:52:18', '2019-12-25 16:20:28', 1, NULL, NULL, NULL, NULL, 1);
INSERT INTO `user` VALUES (3, 'oX98M5D_n2lS0nsBAfYSDABoBmh0', 'nadev', '宜宾', '四川', '中国', 'zh-CN', 0, 0.00, '2019-12-15 20:56:43', '2019-12-26 13:34:08', 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `user` VALUES (6, 'ovtRp5ILPx_2FL8TYFVeCST8XLPM', 'ved', 'Chengdu', 'Sichuan', 'China', 'zh_CN', 1, 0.00, '2019-12-23 12:54:52', '2019-12-26 13:34:09', 0, 'https://wx.qlogo.cn/mmopen/vi_32/Lic2jKfvPRNSRo4n9CU8OiaXaPycsExhZwDLskJB8CIzP9UJfKAvYFxPeRic7RRbOHVgTo7V2Vo8np5M9ic0q2Q9ww/132', NULL, NULL, NULL, 0);
INSERT INTO `user` VALUES (7, 'ovtRp5KEYcui5YvKFJ_mrG1IV074', '次兔兔', '雅安', '四川', '中国', 'zh_CN', 1, 0.00, '2019-12-24 11:30:29', '2019-12-26 13:34:12', 0, 'https://wx.qlogo.cn/mmopen/vi_32/pRJKsyT6JnGDftWyyInk3kJRSP8VUVyUOiclzZnNza3sKjQMHLxlaG84yXWDbU7Uusg1IHiaF1UPibZbuIBP9zePQ/132', NULL, NULL, NULL, 0);
INSERT INTO `user` VALUES (8, 'ovtRp5Ka0kxfo0g-naPRDneopaxk', 'KrisLiu', '', '', '摩洛哥', 'zh_CN', 1, 0.00, '2019-12-24 21:41:24', '2019-12-26 13:34:13', 0, 'https://wx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTIEvkzKzKk0o7P5YS7yFSrBqjQ4v9F3rppicLQSNmJWK0bTZknXdLPhpVuTiakRvD7NqxvrPjGupMWg/132', NULL, NULL, NULL, 0);
INSERT INTO `user` VALUES (9, 'ovtRp5Hz14i84ZTrgafIV_GbDAq0', '阮中华', '丽江', '云南', '中国', 'zh_CN', 1, 0.00, '2019-12-24 21:43:43', '2019-12-26 13:34:14', 0, 'https://wx.qlogo.cn/mmopen/vi_32/DYAIOgq83eo0mGhWdZVZMjzckjb3UrjHx5f5tzFHC5VEQKMwkHbyaznUXKrXicLiaTVWcDajrgMiahiaNpRT9xyG9g/132', NULL, NULL, NULL, 0);
INSERT INTO `user` VALUES (10, 'ovtRp5ChOn4vz2rOuGau6KyjRQUA', 'Eriasan', '', '', '', 'zh_CN', 0, 0.00, '2019-12-25 16:30:32', '2019-12-26 13:34:16', 0, 'https://wx.qlogo.cn/mmopen/vi_32/DYAIOgq83eqVY5iaE7PcQzIibkEGiagIdSwGuDrwRLV2QNj1sSRBYkrwswJ463BYo2MpuVCY7kqIo1COp9UEEKUdQ/132', NULL, NULL, NULL, 0);
COMMIT;

-- ----------------------------
-- Table structure for withdraw
-- ----------------------------
DROP TABLE IF EXISTS `withdraw`;
CREATE TABLE `withdraw` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `withdraw_ment` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '生成的提款订单号',
  `open_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `wx_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '微信号，用于手动打款时使用',
  `gmt_create` timestamp NULL DEFAULT NULL COMMENT '创建日期',
  `gmt_modify` timestamp NULL DEFAULT NULL COMMENT '修改日期',
  `money` decimal(10,0) DEFAULT NULL COMMENT '提款金额',
  `withdraw_status` tinyint(4) DEFAULT NULL COMMENT '提现处理是否异常  0异常   1无异常',
  `is_finish` tinyint(4) DEFAULT NULL COMMENT '订单是否完成 0未完成 1 完成\n',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='提现记录表';

-- ----------------------------
-- Records of withdraw
-- ----------------------------
BEGIN;
INSERT INTO `withdraw` VALUES (1, '2', '1', 'xx1791482634', '2019-12-18 14:49:09', '2019-12-18 16:49:13', 1111, 1, 1, '');
INSERT INTO `withdraw` VALUES (2, 'ae1e9c9930a44f64', 'ovtRp5G9EzgHdlIsD0zjZtbj8h0s', '1791482634', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `withdraw` VALUES (3, '7aa0d303876f4214', 'ovtRp5G9EzgHdlIsD0zjZtbj8h0s', '1791482634', NULL, '2019-12-19 21:14:09', 11, NULL, 1, NULL);
INSERT INTO `withdraw` VALUES (4, 'c4dd3a882aeb48d1', 'ovtRp5G9EzgHdlIsD0zjZtbj8h0s', 'xiaocoder', '2019-12-20 19:55:24', '2019-12-20 19:55:24', 123, 0, 0, '管理员拒绝提现，已退回金额');
INSERT INTO `withdraw` VALUES (5, 'a09d1e3a10614f85', 'ovtRp5G9EzgHdlIsD0zjZtbj8h0s', '222', '2019-12-21 10:47:38', '2019-12-21 10:47:38', 12, 1, 0, NULL);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
