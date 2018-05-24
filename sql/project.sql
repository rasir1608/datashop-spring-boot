use `datashop`;
CREATE TABLE IF NOT EXISTS `d_project` (
  `id` int(11) NOT NULL AUTO_INCREMENT comment "系统ID",
  `name` varchar(255) NOT NULL comment "系统名称",
  `creator` int(11) NOT NULL comment "系统管理员ID",
  `modifier` varchar(255) comment "系统操作人员,有操作权限的人",
  `remarks` varchar(255) comment "系统备注",
  `model` varchar(255) comment "原型地址",
  `ui` varchar(255) comment "ui地址",
  `web` varchar(255) comment "页面地址",
  `create_time` varchar(20) comment "创建时间",
  `update_time` varchar(20) comment "修改时间",
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8;