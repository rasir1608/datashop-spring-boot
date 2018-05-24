
use `datashop`;
CREATE TABLE IF NOT EXISTS `d_power_mapping` (
  `id` int(11) NOT NULL AUTO_INCREMENT comment '用户id',
--   `rid` varchar(8) DEFAULT NULL comment '用户rid',
  `user_id` char(50) NOT NULL comment '用户账号',
  `project_id` char(50) NOT NULL comment '用户姓名',
  `power` int(1) NOT NULL comment '0 申请项目权限，1 用户拥有项目使用权限，5 用户是项目管理员',
  `create_time` varchar(20) comment '创建时间',
  `update_time` varchar(20) comment '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;