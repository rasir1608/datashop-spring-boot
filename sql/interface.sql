use `datashop`;
CREATE TABLE IF NOT EXISTS `d_interface` (
  `id` int(11) NOT NULL AUTO_INCREMENT comment "接口ID",
  `name` varchar(255) NOT NULL comment "接口名称",
  `path` varchar(255) NOT NULL comment "接口url",
  `method` varchar(10) NOT NULL DEFAULT "GET" comment "请求方法",
  `content_type` varchar(255) DEFAULT "application/x-www-form-urlencoded; charset=UTF-8" comment "接口头部设置",
  `creator` int(11) NOT NULL comment "接口创建者",
  `modifier` int(11) NOT NULL comment "接口修改者",
  `request` text comment "接口请求信息",
  `response` text comment "接口返回信息",
  `project` int(11) NOT NULL comment "接口所属系统ID",
  `remark` varchar(255) comment "接口备注",
  `create_time` bigint(20) comment "接口创建时间",
  `update_time` bigint(20) comment "接口修改时间",
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8;