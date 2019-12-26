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

 Date: 25/12/2019 16:46:56
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
-- Table structure for browse
-- ----------------------------
DROP TABLE IF EXISTS `browse`;
CREATE TABLE `browse` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(10) unsigned NOT NULL COMMENT '用户id',
  `house_id` int(10) unsigned NOT NULL COMMENT '房源id',
  `gmt_create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '浏览时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='房源浏览记录';

-- ----------------------------
-- Records of browse
-- ----------------------------
BEGIN;
INSERT INTO `browse` VALUES (2, 1, 2, '2019-12-25 16:41:46');
INSERT INTO `browse` VALUES (3, 1, 3, '2019-12-25 16:41:51');
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='房源收藏信息';

-- ----------------------------
-- Records of collection
-- ----------------------------
BEGIN;
INSERT INTO `collection` VALUES (2, 1, 1, '2019-12-25 14:28:00');
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
  `has_bordband` tinyint(1) DEFAULT '0' COMMENT '宽带',
  `has_wardrobe` tinyint(1) DEFAULT '0' COMMENT '衣柜',
  `has_gas` tinyint(1) DEFAULT '0' COMMENT '煤气',
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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='房源信息';

-- ----------------------------
-- Records of house
-- ----------------------------
BEGIN;
INSERT INTO `house` VALUES (2, 1, '123456789', 0, 0, 10002, 2200, '押一付一', 95, 7, '2019-12-17 20:41:17', '2019-12-25 16:18:31', '南', '充足', NULL, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 1, '四川省', '雅安市', '雨城区', '东城街道', '新康路46号', 29.977093, 102.992122, '二室一厅一卫', 0, 0, 0, 0, 1, 0, 'https://fish-house-1252272257.cos.ap-chengdu.myqcloud.com/house/2-1.jpg', '{\"精选好房\",\"押一付一\",\"近地铁\",\"配套齐全\"}', '正对锦江 后看麓湖 户型方正', '精装', '2019-12-24 23:24:35', '随时可看');
INSERT INTO `house` VALUES (3, 2, '123456', 0, 0, 10003, 3100, '押一付三', 110, 8, '2019-12-20 19:29:39', '2019-12-25 16:19:13', '北', '充足', NULL, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, '四川省', '成都市', '武侯区', '不知名街道', '四不像路666号', 29.877093, 102.112334, '三室二厅二卫', 0, 0, 0, 0, 0, 0, 'https://fish-house-1252272257.cos.ap-chengdu.myqcloud.com/house/3-1.jpg', '{\"精选好房\",\"押一付三\",\"近地铁\"}', '精装套二 视野开阔 采光充足', '精装', '2019-12-05 23:24:38', '随时可看');
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
  `id` bigint(20) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `open_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户id',
  `out_trade_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '随机生成的订单号',
  `prepay_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '微信生成预支付订单号',
  `total_fee` decimal(10,2) unsigned DEFAULT '0.00' COMMENT '总金额',
  `gmt_create` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '订单生成时间',
  `gmt_modify` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '订单状态更改时间',
  `is_paid` tinyint(1) unsigned DEFAULT NULL COMMENT '是否已经支付,0未支付,1已支付',
  `is_finished` tinyint(1) unsigned DEFAULT NULL COMMENT '是否已完成，后台判断了密码后，才应该确认更改此字段',
  `addr_ip` varbinary(255) DEFAULT NULL COMMENT '用户付款IP',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注用户付的是租金还是水费啥的吧',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of house_order
-- ----------------------------
BEGIN;
INSERT INTO `house_order` VALUES (00000000000000000001, 'ovtRp5PhDlVNzlBaugrYi7hYzgXY', '1905310', NULL, 13.00, '2019-12-19 19:05:31', '2019-12-22 11:51:05', 0, NULL, NULL, NULL);
INSERT INTO `house_order` VALUES (00000000000000000002, 'ovtRp5G9EzgHdlIsD0zjZtbj8h0s', '1939520', NULL, 1.00, '2019-12-20 19:39:53', '2019-12-20 19:39:53', 0, NULL, NULL, NULL);
INSERT INTO `house_order` VALUES (00000000000000000003, 'ovtRp5G9EzgHdlIsD0zjZtbj8h0s', '1944340', NULL, 541.00, '2019-12-20 19:44:35', '2019-12-20 19:44:35', 0, NULL, NULL, NULL);
INSERT INTO `house_order` VALUES (00000000000000000004, 'ovtRp5PhDlVNzlBaugrYi7hYzgXY', '1151470', NULL, 113.00, '2019-12-22 11:51:47', '2019-12-22 11:51:47', 0, NULL, NULL, NULL);
INSERT INTO `house_order` VALUES (00000000000000000005, 'ovtRp5PhDlVNzlBaugrYi7hYzgXY', '1222260', NULL, 123.00, '2019-12-22 12:22:27', '2019-12-22 12:22:27', 0, NULL, NULL, NULL);
INSERT INTO `house_order` VALUES (00000000000000000006, 'ovtRp5PhDlVNzlBaugrYi7hYzgXY', '1228520', NULL, 123.00, '2019-12-22 12:28:52', '2019-12-22 12:28:52', 0, NULL, NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for house_repair
-- ----------------------------
DROP TABLE IF EXISTS `house_repair`;
CREATE TABLE `house_repair` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `house_id` int(10) unsigned NOT NULL COMMENT '房源ID 关联house表',
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
-- Table structure for message
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message` (
  `id` int(10) unsigned NOT NULL,
  `content` varchar(500) DEFAULT NULL COMMENT '内容',
  `img_url` varchar(2083) DEFAULT NULL COMMENT '图片链接',
  `gmt_create` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;

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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商家信息';

-- ----------------------------
-- Records of store
-- ----------------------------
BEGIN;
INSERT INTO `store` VALUES (1, '渔捞石锅河鲜火锅', 'https://fish-house-1252272257.cos.ap-chengdu.myqcloud.com/store/1.jpg', '四川省成都市龙泉驿区金桉路88号希尔顿酒店美食街11栋1-2号', '周一至周日 10:30-23:30', '15397623834', '餐饮美食');
INSERT INTO `store` VALUES (2, 'LIFAN丽梵日式沙龙', 'https://fish-house-1252272257.cos.ap-chengdu.myqcloud.com/store/2.jpg', '四川省成都市成华区府华二路68号4栋1楼附5号', '周一至周日 10:00-21:30', '13980473545', '美容美发');
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
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商家图片';

-- ----------------------------
-- Records of store_img
-- ----------------------------
BEGIN;
INSERT INTO `store_img` VALUES (1, 1, 'https://fish-house-1252272257.cos.ap-chengdu.myqcloud.com/store/1.jpg', '2019-12-25 16:06:31');
INSERT INTO `store_img` VALUES (2, 2, 'https://fish-house-1252272257.cos.ap-chengdu.myqcloud.com/store/2.jpg', '2019-12-25 16:06:42');
INSERT INTO `store_img` VALUES (3, 2, 'https://fish-house-1252272257.cos.ap-chengdu.myqcloud.com/store/3.jpg', '2019-12-25 16:07:36');
INSERT INTO `store_img` VALUES (4, 1, 'chengdu.myqcloud.com/store/1.jpg', '2019-12-25 16:07:54');
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

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
  `is_auth` tinyint(1) DEFAULT NULL COMMENT '是否身份认证',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `open_id` (`open_id`) USING BTREE COMMENT '保证用户唯一'
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户信息表';

-- ----------------------------
-- Records of user
-- ----------------------------
BEGIN;
INSERT INTO `user` VALUES (1, 'ovtRp5G9EzgHdlIsD0zjZtbj8h0s', 'coder', '北京', NULL, NULL, NULL, 0, 111066.00, '2019-12-17 19:09:09', '2019-12-25 16:20:24', 1, NULL, NULL, NULL, NULL, 1);
INSERT INTO `user` VALUES (2, 'ovtRp5PhDlVNzlBaugrYi7hYzgXY', 'test1', 'YiBin', NULL, 'China', NULL, 1, 0.00, '2019-12-17 16:52:18', '2019-12-25 16:20:28', 1, NULL, NULL, NULL, NULL, 1);
INSERT INTO `user` VALUES (3, 'oX98M5D_n2lS0nsBAfYSDABoBmh0', 'nadev', '宜宾', '四川', '中国', 'zh-CN', 0, 0.00, '2019-12-15 20:56:43', '2019-12-16 13:20:25', 0, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `user` VALUES (6, 'ovtRp5ILPx_2FL8TYFVeCST8XLPM', 'ved', 'Chengdu', 'Sichuan', 'China', 'zh_CN', 1, 0.00, '2019-12-23 12:54:52', '2019-12-23 12:54:52', 0, 'https://wx.qlogo.cn/mmopen/vi_32/Lic2jKfvPRNSRo4n9CU8OiaXaPycsExhZwDLskJB8CIzP9UJfKAvYFxPeRic7RRbOHVgTo7V2Vo8np5M9ic0q2Q9ww/132', NULL, NULL, NULL, NULL);
INSERT INTO `user` VALUES (7, 'ovtRp5KEYcui5YvKFJ_mrG1IV074', '次兔兔', '雅安', '四川', '中国', 'zh_CN', 1, 0.00, '2019-12-24 11:30:29', '2019-12-24 11:30:29', 0, 'https://wx.qlogo.cn/mmopen/vi_32/pRJKsyT6JnGDftWyyInk3kJRSP8VUVyUOiclzZnNza3sKjQMHLxlaG84yXWDbU7Uusg1IHiaF1UPibZbuIBP9zePQ/132', NULL, NULL, NULL, NULL);
INSERT INTO `user` VALUES (8, 'ovtRp5Ka0kxfo0g-naPRDneopaxk', 'KrisLiu', '', '', '摩洛哥', 'zh_CN', 1, 0.00, '2019-12-24 21:41:24', '2019-12-24 21:41:24', 0, 'https://wx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTIEvkzKzKk0o7P5YS7yFSrBqjQ4v9F3rppicLQSNmJWK0bTZknXdLPhpVuTiakRvD7NqxvrPjGupMWg/132', NULL, NULL, NULL, NULL);
INSERT INTO `user` VALUES (9, 'ovtRp5Hz14i84ZTrgafIV_GbDAq0', '阮中华', '丽江', '云南', '中国', 'zh_CN', 1, 0.00, '2019-12-24 21:43:43', '2019-12-24 21:43:43', 0, 'https://wx.qlogo.cn/mmopen/vi_32/DYAIOgq83eo0mGhWdZVZMjzckjb3UrjHx5f5tzFHC5VEQKMwkHbyaznUXKrXicLiaTVWcDajrgMiahiaNpRT9xyG9g/132', NULL, NULL, NULL, NULL);
INSERT INTO `user` VALUES (10, 'ovtRp5ChOn4vz2rOuGau6KyjRQUA', 'Eriasan', '', '', '', 'zh_CN', 0, 0.00, '2019-12-25 16:30:32', '2019-12-25 16:30:32', 0, 'https://wx.qlogo.cn/mmopen/vi_32/DYAIOgq83eqVY5iaE7PcQzIibkEGiagIdSwGuDrwRLV2QNj1sSRBYkrwswJ463BYo2MpuVCY7kqIo1COp9UEEKUdQ/132', NULL, NULL, NULL, NULL);
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
