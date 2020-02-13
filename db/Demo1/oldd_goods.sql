/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50726
 Source Host           : localhost:3306
 Source Schema         : xxh

 Target Server Type    : MySQL
 Target Server Version : 50726
 File Encoding         : 65001

 Date: 12/02/2020 22:16:47
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for oldd_goods
-- ----------------------------
DROP TABLE IF EXISTS `oldd_goods`;
CREATE TABLE `oldd_goods`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '商品id',
  `goodsName` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '商品名称',
  `goodsStock` int(10) UNSIGNED DEFAULT NULL COMMENT '库存',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'Java+MySQL数据库的乐观锁实现demo的测试表\r\n' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of oldd_goods
-- ----------------------------
INSERT INTO `oldd_goods` VALUES (1, '并发商品', 5);

SET FOREIGN_KEY_CHECKS = 1;
