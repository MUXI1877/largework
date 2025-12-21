-- 更新用户密码从MD5到BCrypt
-- 密码123456的BCrypt哈希值：$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy

SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

USE quanxianguanli;

-- 更新所有用户的密码为BCrypt加密
UPDATE sys_user SET password = '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy' WHERE password = 'e10adc3949ba59abbe56e057f20f883e';

