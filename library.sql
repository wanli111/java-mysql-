/*
 Navicat MySQL Data Transfer

 Source Server         : Library
 Source Server Type    : MySQL
 Source Server Version : 80200 (8.2.0)
 Source Host           : localhost:3306
 Source Schema         : library

 Target Server Type    : MySQL
 Target Server Version : 80200 (8.2.0)
 File Encoding         : 65001

 Date: 22/12/2023 09:41:59
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for book_category
-- ----------------------------
DROP TABLE IF EXISTS `book_category`;
CREATE TABLE `book_category`  (
  `book_ISBN` varchar(30) CHARACTER SET gb2312 COLLATE gb2312_bin NOT NULL,
  `book_name` varchar(30) CHARACTER SET gb2312 COLLATE gb2312_bin NOT NULL,
  `book_purchase` int NOT NULL,
  `book_inventory` int NOT NULL,
  PRIMARY KEY (`book_ISBN`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = gb2312 COLLATE = gb2312_chinese_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for book_each
-- ----------------------------
DROP TABLE IF EXISTS `book_each`;
CREATE TABLE `book_each`  (
  `book_id` int NOT NULL AUTO_INCREMENT,
  `book_ISBN` varchar(30) CHARACTER SET gb2312 COLLATE gb2312_bin NOT NULL,
  `book_position` int NOT NULL DEFAULT 1,
  PRIMARY KEY (`book_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1660 CHARACTER SET = gb2312 COLLATE = gb2312_chinese_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for borrow
-- ----------------------------
DROP TABLE IF EXISTS `borrow`;
CREATE TABLE `borrow`  (
  `borrow_num` int NOT NULL AUTO_INCREMENT,
  `book_id` int NOT NULL,
  `reader_num` int NOT NULL,
  `state` int NOT NULL DEFAULT 0,
  PRIMARY KEY (`borrow_num`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 27 CHARACTER SET = gb2312 COLLATE = gb2312_chinese_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for reader
-- ----------------------------
DROP TABLE IF EXISTS `reader`;
CREATE TABLE `reader`  (
  `reader_num` int NOT NULL AUTO_INCREMENT,
  `reader_name` varchar(30) CHARACTER SET gb2312 COLLATE gb2312_bin NOT NULL,
  `reader_password` varchar(30) CHARACTER SET gb2312 COLLATE gb2312_bin NOT NULL,
  `reader_count` int NOT NULL DEFAULT 0,
  `reader_idnum` varchar(30) CHARACTER SET gb2312 COLLATE gb2312_bin NOT NULL,
  `reader_phone` varchar(30) CHARACTER SET gb2312 COLLATE gb2312_bin NOT NULL,
  `reader_role` int NOT NULL DEFAULT 0,
  PRIMARY KEY (`reader_num`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = gb2312 COLLATE = gb2312_chinese_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
