-- 修复价格本管理模块级别：将其从销售管理的子模块改为顶级模块
-- ============================================

-- 1. 更新价格本管理模块配置，使其成为顶级模块（与销售管理同级）
UPDATE `sys_module` 
SET 
  `menu_level` = 1,           -- 改为一级菜单
  `parent_id` = NULL,         -- 移除父模块关联
  `is_parent` = 0,            -- 不是父模块（没有子模块）
  `is_expand` = 0,             -- 不展开
  `sort` = 5,                  -- 排序在销售管理（sort=4）之后
  `group_name` = 'price',     -- 修改分组名称
  `path` = '/price-book-management',  -- 确保路径正确
  `update_time` = NOW()
WHERE `id` = 'm019';

-- 2. 验证更新结果
SELECT 
  `id`, 
  `cn_name`, 
  `menu_level`, 
  `parent_id`, 
  `is_parent`, 
  `sort`,
  `group_name`,
  `path`
FROM `sys_module` 
WHERE `id` = 'm019';

SELECT '✅ 价格本管理模块已更新为顶级模块！' AS message;

