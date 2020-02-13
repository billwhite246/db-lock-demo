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

 Date: 12/02/2020 22:16:53
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for oldd_goods_new
-- ----------------------------
DROP TABLE IF EXISTS `oldd_goods_new`;
CREATE TABLE `oldd_goods_new`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '商品id',
  `goodsName` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '商品名称',
  `goodsStock` int(10) UNSIGNED DEFAULT NULL COMMENT '库存',
  `version` int(10) UNSIGNED ZEROFILL DEFAULT NULL COMMENT '数据版本号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'Java+MySQL数据库的乐观锁实现demo的测试表\r\n' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of oldd_goods_new
-- ----------------------------
INSERT INTO `oldd_goods_new` VALUES (1, '并发商品', 5, 0000000000);

SET FOREIGN_KEY_CHECKS = 1;
