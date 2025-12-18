-- ---------------------------- 
-- Table structure for sys_permission 
-- ---------------------------- 
DROP TABLE IF EXISTS `sys_permission`; 
CREATE TABLE `sys_permission`  ( 
  `id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL, 
  `create_time` datetime(0) NOT NULL, 
  `update_time` datetime(0) NOT NULL, 
  `role_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL, 
  `module_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL, 
  `can_read` tinyint(1) NULL DEFAULT 0, 
  `can_add` tinyint(1) NULL DEFAULT 0, 
  `can_update` tinyint(1) NULL DEFAULT 0, 
  `can_see` tinyint(1) NULL DEFAULT 0, 
  PRIMARY KEY (`id`) USING BTREE, 
  UNIQUE INDEX `uk_role_module`(`role_id`, `module_id`) USING BTREE, 
  INDEX `module_id`(`module_id`) USING BTREE, 
  CONSTRAINT `sys_permission_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT, 
  CONSTRAINT `sys_permission_ibfk_2` FOREIGN KEY (`module_id`) REFERENCES `sys_module` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT 
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic; 

-- ---------------------------- 
-- Records of sys_permission 
-- ---------------------------- 
INSERT INTO `sys_permission` VALUES ('f8f4dce3-669e-4b47-a7e2-9b043557f18b', '2025-12-04 16:25:26', '2025-12-04 16:25:26', 'r001', 'e1544bde-02b8-4de8-9841-e59ff6c936d2', 1, 1, 1, 1); 
INSERT INTO `sys_permission` VALUES ('p001', '2025-12-04 11:48:14', '2025-12-04 11:48:14', 'r001', 'm001', 1, 1, 1, 1); 
INSERT INTO `sys_permission` VALUES ('p002', '2025-12-04 11:48:14', '2025-12-04 11:48:14', 'r001', 'm002', 1, 1, 1, 1); 
INSERT INTO `sys_permission` VALUES ('p003', '2025-12-04 11:48:14', '2025-12-04 11:48:14', 'r001', 'm003', 1, 1, 1, 1); 
INSERT INTO `sys_permission` VALUES ('p004', '2025-12-04 11:48:14', '2025-12-04 11:48:14', 'r001', 'm004', 1, 1, 1, 1); 
INSERT INTO `sys_permission` VALUES ('p005', '2025-12-04 11:48:14', '2025-12-04 11:48:14', 'r001', 'm005', 1, 1, 1, 1); 
INSERT INTO `sys_permission` VALUES ('p006', '2025-12-04 11:48:14', '2025-12-04 11:48:14', 'r001', 'm006', 1, 1, 1, 1); 
INSERT INTO `sys_permission` VALUES ('p007', '2025-12-04 11:48:14', '2025-12-04 11:48:14', 'r001', 'm007', 1, 1, 1, 1); 
INSERT INTO `sys_permission` VALUES ('p008', '2025-12-04 11:48:14', '2025-12-04 11:48:14', 'r002', 'm001', 1, 1, 1, 1); 
INSERT INTO `sys_permission` VALUES ('p009', '2025-12-04 11:48:14', '2025-12-04 11:48:14', 'r002', 'm002', 1, 1, 1, 1); 
INSERT INTO `sys_permission` VALUES ('p010', '2025-12-04 11:48:14', '2025-12-04 11:48:14', 'r002', 'm003', 1, 1, 1, 1); 
INSERT INTO `sys_permission` VALUES ('p011', '2025-12-04 11:48:14', '2025-12-04 11:48:14', 'r002', 'm006', 1, 1, 1, 1); 
INSERT INTO `sys_permission` VALUES ('p012', '2025-12-04 11:48:14', '2025-12-04 11:48:14', 'r002', 'm007', 1, 1, 1, 1); 
INSERT INTO `sys_permission` VALUES ('p013', '2025-12-04 11:48:14', '2025-12-04 11:48:14', 'r003', 'm002', 1, 0, 0, 1); 
INSERT INTO `sys_permission` VALUES ('p014', '2025-12-04 11:48:14', '2025-12-04 11:48:14', 'r003', 'm006', 1, 0, 0, 1); 

-- ---------------------------- 
-- Records of sys_user 
-- ---------------------------- 
INSERT INTO `sys_user` VALUES ('u001', '2025-12-04 12:12:02', '2025-12-04 12:12:02', '超级管理员', '110101199001010001', '13800138001', '1990-01-01', '男', 'o001', 'o101', 'admin', '$2a$10$ztlRNVIfMYy7cfLJEh6ZAu/dzqSjiuWtGIfkgZj7tHYGlnitjrCt6', 'r001', 1); 
INSERT INTO `sys_user` VALUES ('u002', '2025-12-04 12:12:02', '2025-12-04 12:12:02', '系统管理员', '110101199002020002', '13800138002', '1990-02-02', '女', 'o002', 'o102', 'sysadmin', '$2a$10$ztlRNVIfMYy7cfLJEh6ZAu/dzqSjiuWtGIfkgZj7tHYGlnitjrCt6', 'r002', 1); 
INSERT INTO `sys_user` VALUES ('u003', '2025-12-04 12:12:02', '2025-12-04 12:12:02', '张三', '110101199003030003', '13800138003', '1990-03-03', '男', 'o004', 'o103', 'zhangsan', '$2a$10$ztlRNVIfMYy7cfLJEh6ZAu/dzqSjiuWtGIfkgZj7tHYGlnitjrCt6', 'r003', 1); 
INSERT INTO `sys_user` VALUES ('u004', '2025-12-04 12:12:02', '2025-12-04 12:12:02', '李四', '110101199004040004', '13800138004', '1990-04-04', '女', 'o005', 'o104', 'lisi', '$2a$10$ztlRNVIfMYy7cfLJEh6ZAu/dzqSjiuWtGIfkgZj7tHYGlnitjrCt6', 'r003', 1); 
INSERT INTO `sys_user` VALUES ('u005', '2025-12-04 12:12:02', '2025-12-04 12:12:02', '王五', '110101199005050005', '13800138005', '1990-05-05', '男', 'o006', 'o105', 'wangwu', '$2a$10$ztlRNVIfMYy7cfLJEh6ZAu/dzqSjiuWtGIfkgZj7tHYGlnitjrCt6', 'r003', 1); 
INSERT INTO `sys_user` VALUES ('u006', '2025-12-04 12:12:02', '2025-12-04 12:53:18', '赵六', '110101199006060006', '13800138006', '1990-06-06', '女', 'o004', 'o106', '13800138006', '$2a$10$xH7HxJCs1AYmn50nRkF80uXvS3YVzdZ89EABc6pN//Me4Ot1q0Jt6', 'r003', 1); 
INSERT INTO `sys_user` VALUES ('u007', '2025-12-04 12:12:02', '2025-12-04 12:12:02', '钱七', '110101199007070007', '13800138007', '1990-07-07', '男', 'o005', 'o101', 'qianqi', '$2a$10$ztlRNVIfMYy7cfLJEh6ZAu/dzqSjiuWtGIfkgZj7tHYGlnitjrCt6', 'r003', 0); 

SET FOREIGN_KEY_CHECKS = 1;