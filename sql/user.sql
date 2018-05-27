
use `datashop`;
CREATE TABLE IF NOT EXISTS `d_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT comment '用户id',
--   `rid` varchar(8) DEFAULT NULL comment '用户rid',
  `account` char(50) NOT NULL comment '用户账号',
  `name` char(50) NOT NULL comment '用户姓名',
  `password` char(128) NOT NULL comment '密码',
  `headerurl` varchar(255) DEFAULT NULL comment '头像URL地址',
  `role` int(1) DEFAULT 1 comment '用户权限 1普通用户，5最高管理员',
  `create_time` bigint(20) comment '创建时间',
  `update_time` bigint(20) comment '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8;