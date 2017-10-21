CREATE TABLE `resource_meta_data` (
  `id` varchar(40) NOT NULL,
  `regdate` datetime DEFAULT NULL,
  `regname` varchar(40) DEFAULT NULL,
  `regcode` varchar(100) DEFAULT NULL COMMENT '登记人的微信unionid',
  `filepath` varchar(400) DEFAULT NULL COMMENT '文件地址,其他转出来的类型默认都在同级目录下',
  `filesize` double(10,2) DEFAULT NULL COMMENT '文件大小',
  `videoaudio_posterpath` varchar(400) DEFAULT NULL COMMENT '如果是视频或者音频,那么存封面地址或者动图地址',
  `filetype` tinyint(2) DEFAULT NULL COMMENT '文件类型标识:视频,图片,音频',
  `videoaudio_duration` int(10) DEFAULT NULL COMMENT '视频或者音频时长',
  `width` double(4,2) DEFAULT NULL,
  `height` double(4,0) DEFAULT NULL,
  `clientinfo` text,
  `longitude` varchar(20) DEFAULT NULL,
  `latitude` varchar(20) DEFAULT NULL,
  `location_msg` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `metadata_code_index` (`regcode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
