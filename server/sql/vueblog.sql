/*
 Navicat Premium Data Transfer

 Source Server         : local
 Source Server Type    : MySQL
 Source Server Version : 80028
 Source Host           : localhost:3306
 Source Schema         : vueblog

 Target Server Type    : MySQL
 Target Server Version : 80028
 File Encoding         : 65001

 Date: 25/06/2022 15:22:17
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for m_blog
-- ----------------------------
DROP TABLE IF EXISTS `m_blog`;
CREATE TABLE `m_blog`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `sort_id` int NOT NULL,
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `content` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `created` datetime NOT NULL,
  `status` tinyint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 37 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of m_blog
-- ----------------------------

-- ----------------------------
-- Table structure for m_blog_statistics
-- ----------------------------
DROP TABLE IF EXISTS `m_blog_statistics`;
CREATE TABLE `m_blog_statistics`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `day` date NOT NULL,
  `view_count` bigint NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 59 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of m_blog_statistics
-- ----------------------------
INSERT INTO `m_blog_statistics` VALUES (1, '2022-06-08', 0);
INSERT INTO `m_blog_statistics` VALUES (2, '2022-06-09', 10);
INSERT INTO `m_blog_statistics` VALUES (3, '2022-06-10', 20);
INSERT INTO `m_blog_statistics` VALUES (4, '2022-06-11', 5);
INSERT INTO `m_blog_statistics` VALUES (5, '2022-06-12', 100);
INSERT INTO `m_blog_statistics` VALUES (56, '2022-06-21', 1);
INSERT INTO `m_blog_statistics` VALUES (57, '2022-06-21', 2);
INSERT INTO `m_blog_statistics` VALUES (58, '2022-06-21', 22222);

-- ----------------------------
-- Table structure for m_blogtag
-- ----------------------------
DROP TABLE IF EXISTS `m_blogtag`;
CREATE TABLE `m_blogtag`  (
  `blogId` bigint NOT NULL,
  `tagId` bigint NOT NULL,
  `sort` bigint NULL DEFAULT 0
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of m_blogtag
-- ----------------------------
INSERT INTO `m_blogtag` VALUES (15, 41, 1);
INSERT INTO `m_blogtag` VALUES (15, 39, 0);
INSERT INTO `m_blogtag` VALUES (15, 42, 2);

-- ----------------------------
-- Table structure for m_friend
-- ----------------------------
DROP TABLE IF EXISTS `m_friend`;
CREATE TABLE `m_friend`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '名称',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `href` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '链接地址',
  `avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '头像地址',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of m_friend
-- ----------------------------

-- ----------------------------
-- Table structure for m_other
-- ----------------------------
DROP TABLE IF EXISTS `m_other`;
CREATE TABLE `m_other`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order` tinyint NULL DEFAULT 0,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of m_other
-- ----------------------------
INSERT INTO `m_other` VALUES (1, 0, '关于我', '## 关于我');
INSERT INTO `m_other` VALUES (2, 1, '网页底部', '');
INSERT INTO `m_other` VALUES (3, 3, '备份设置', '{\"username\":\" \",\"password\":\" \",\"operator\":\" \",\"operator_password\":\" \",\"bucket_name\":\" \"}');

-- ----------------------------
-- Table structure for m_sort
-- ----------------------------
DROP TABLE IF EXISTS `m_sort`;
CREATE TABLE `m_sort`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `order` int NOT NULL DEFAULT 0,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of m_sort
-- ----------------------------

-- ----------------------------
-- Table structure for m_system
-- ----------------------------
DROP TABLE IF EXISTS `m_system`;
CREATE TABLE `m_system`  (
  `id` int NOT NULL,
  `welcome` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of m_system
-- ----------------------------
INSERT INTO `m_system` VALUES (1, '欢迎来到Smilex博客', 'Smile\' Blog', '一个神秘的博客');

-- ----------------------------
-- Table structure for m_tag
-- ----------------------------
DROP TABLE IF EXISTS `m_tag`;
CREATE TABLE `m_tag`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 45 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of m_tag
-- ----------------------------
INSERT INTO `m_tag` VALUES (41, 'A');
INSERT INTO `m_tag` VALUES (42, 'B');
INSERT INTO `m_tag` VALUES (43, 'C');
INSERT INTO `m_tag` VALUES (44, 'D');

-- ----------------------------
-- Table structure for m_user
-- ----------------------------
DROP TABLE IF EXISTS `m_user`;
CREATE TABLE `m_user`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `email` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `password` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `status` int NOT NULL,
  `created` datetime NOT NULL,
  `last_login` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of m_user
-- ----------------------------
INSERT INTO `m_user` VALUES (2, 'x', 'https://avatars.githubusercontent.com/u/28394742', 'msmliexx1@gmail.com', '123123', -1, '2022-03-29 21:23:55', '2022-03-29 21:24:00');

SET FOREIGN_KEY_CHECKS = 1;
