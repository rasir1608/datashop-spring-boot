
use `datashop`;
CREATE TABLE IF NOT EXISTS `d_power_mapping` (
  `id` int(11) NOT NULL AUTO_INCREMENT comment '用户id',
--   `rid` varchar(8) DEFAULT NULL comment '用户rid',
  `user_id` int(11) NOT NULL comment '用户账号',
  `project_id` int(11) NOT NULL comment '用户姓名',
  `power` int(1) NOT NULL default 0 comment '0 申请被驳回,1 申请项目权限，2 用户拥有项目使用权限，5 用户是项目管理员',
  `create_time` bigint(20) comment '创建时间',
  `update_time` bigint(20) comment '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;