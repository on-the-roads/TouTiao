DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL DEFAULT '',
  `password` varchar(128) NOT NULL DEFAULT '',
  `salt` varchar(32) NOT NULL DEFAULT '',
  `head_url` varchar(256) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `news`;
CREATE TABLE `news` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(128) NOT NULL DEFAULT '',
  `link` varchar(256) NOT NULL DEFAULT '',
  `image` varchar(256) NOT NULL DEFAULT '',
  `like_count` int NOT NULL,
  `comment_count` int NOT NULL,
  `created_date` datetime NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

drop table if exists comment;
create table comment(
  id int(11) unsigned not null auto_increment,
  content varchar(256) not null default '',
  user_id int(11) not null,
  create_date datetime not null,
  news_id int(11) not null,
  primary key(id)
)engine=InnoDB default CHARSET=utf8;

drop table if exists message;
create table message(
  id int(11) unsigned not null auto_increment,
  fromid int(11) not null,
  toid int(11) not null,
  content varchar(256) default '',
  conversion_is int(11) not null,
  create_id datetime not null,
  primary key(id)
  )engine=InnoDB default CHARSET=utf8;