
use `datashop`;
CREATE TABLE `d_edite` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `editor` int(11) NOT NULL COMMENT '编辑者账号',
  `target` int(11) NOT NULL COMMENT '目标ID',
  `kind` int(1) NOT NULL COMMENT '类型 0 project 1 interface',
  `islock` int(1) DEFAULT '0' COMMENT '是否锁定状态',
  `create_time` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `update_time` bigint(20) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8;