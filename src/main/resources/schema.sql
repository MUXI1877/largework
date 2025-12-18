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
  UNIQUE INDEX `UK1rubn8s14r1patkll2r074pb3`(`role_id`, `module_id`) USING BTREE, 
  INDEX `module_id`(`module_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;