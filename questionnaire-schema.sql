-- MySQL dump 10.16  Distrib 10.1.37-MariaDB, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: questionnaire
-- ------------------------------------------------------
-- Server version	10.1.37-MariaDB-0+deb9u1

--
-- Table structure for table `log_login`
--
SET NAMES 'utf8mb4';

DROP TABLE IF EXISTS `log_login`;
CREATE TABLE `log_login`
(
    `id`          bigint(20) unsigned NOT NULL AUTO_INCREMENT,
    `user_id`     bigint(20) unsigned NOT NULL COMMENT '用户id',
    `client_name` varchar(20) DEFAULT NULL COMMENT '客户端名称',
    `system_name` varchar(20) DEFAULT NULL COMMENT '登录系统名称',
    `ip`          varchar(15) DEFAULT NULL COMMENT '登录ip',
    `position`    varchar(50) DEFAULT NULL COMMENT '登录地点',
    `create_time` datetime    DEFAULT NULL COMMENT '登录时间',
    PRIMARY KEY (`id`),
    KEY `login_log_user_id_IDX` (`user_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='登录日志存储表';


--
-- Table structure for table `paper`
--

DROP TABLE IF EXISTS `paper`;
CREATE TABLE `paper`
(
    `id`               bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
    `questionnaire_id` bigint unsigned NOT NULL COMMENT '问卷id',
    `submit_time`      datetime     DEFAULT NULL COMMENT '答卷提交时间',
    `elapsed_time`     int unsigned DEFAULT '0' COMMENT '答卷所耗时间(单位：s)',
    `source`           tinyint      DEFAULT NULL COMMENT '答卷来源，是微信分享还是QQ分享，0-网站链接，1-QQ，2-WX，3-QQ空间，4-微博,5-二维码扫码，6-其他',
    `ip`               varchar(15)  DEFAULT NULL COMMENT '答卷提交的IP地址',
    `address`          varchar(30)  DEFAULT NULL COMMENT '答卷提交的地点，粗略精确到城市',
    PRIMARY KEY (`id`),
    KEY `questionnaire_paper_questionnaire_id_IDX` (`questionnaire_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
   COMMENT ='问卷答卷存储表';


--
-- Table structure for table `paper_answer`
--

DROP TABLE IF EXISTS `paper_answer`;
CREATE TABLE `paper_answer`
(
    `id`          bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
    `paper_id`    bigint unsigned DEFAULT NULL COMMENT '答卷id',
    `question_id` bigint unsigned DEFAULT NULL COMMENT '问题id，对应于questionnaire_question表的id字段',
    `answer`      varchar(255)    DEFAULT NULL COMMENT '问题答案',
    PRIMARY KEY (`id`),
    KEY `paper_answer_paper_id_IDX` (`paper_id`) USING BTREE,
    KEY `paper_answer_question_id_IDX` (`question_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
   COMMENT ='答卷的问题答案存储表';

--
-- Table structure for table `questionnaire`
--

DROP TABLE IF EXISTS `questionnaire`;
CREATE TABLE `questionnaire`
(
    `id`             bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
    `name`           varchar(100)    NOT NULL COMMENT '调查问卷名称',
    `greeting`       varchar(200) DEFAULT '' COMMENT '该问卷展示时的问候语',
    `create_date`    datetime     DEFAULT NULL COMMENT '问卷创建日期',
    `publish_date`   datetime     DEFAULT NULL COMMENT '问卷发布时间',
    `user_id`        bigint       DEFAULT '0' COMMENT '问卷所属用户',
    `type_id`        bigint       DEFAULT NULL COMMENT '问卷类型id，参考questionnaire_type表的id',
    `question_count` int unsigned DEFAULT '0' COMMENT '该问卷问题总数',
    `template_id`    bigint       DEFAULT '0' COMMENT '该问卷的模板id',
    `invoke_count`   int          DEFAULT '0' COMMENT '问卷参与者计数',
    `status`         tinyint      DEFAULT '0' COMMENT '该问卷的状态，0设计中，1问卷已发布信息收集中，2问卷结束',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
   COMMENT ='调查问卷存储表';
--
-- Table structure for table `questionnaire_question`
--

DROP TABLE IF EXISTS `questionnaire_question`;
CREATE TABLE `questionnaire_question`
(
    `id`                bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
    `questionnaire_id`  bigint          NOT NULL COMMENT '所属问卷id',
    `question_type`     tinyint      DEFAULT NULL COMMENT '问题类型',
    `question_title`    varchar(255) DEFAULT NULL COMMENT '问题题目',
    `question_order`    tinyint      DEFAULT NULL COMMENT '问题在问卷中的顺序',
    `question_num`      tinyint      DEFAULT NULL COMMENT '问题在问卷中的题号',
    `input_placeholder` varchar(255) DEFAULT NULL COMMENT '对于填空题input的placeholder',
    PRIMARY KEY (`id`),
    KEY `questionnaire_id` (`questionnaire_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
   COMMENT ='调查问卷问题表，用于存储问卷中的问题';

--
-- Table structure for table `questionnaire_question_option`
--

DROP TABLE IF EXISTS `questionnaire_question_option`;
CREATE TABLE `questionnaire_question_option`
(
    `id`           bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
    `question_id`  bigint unsigned NOT NULL COMMENT '问题id',
    `option_text`  varchar(50)      DEFAULT '' COMMENT '问题选项文本',
    `option_order` tinyint unsigned DEFAULT '0' COMMENT '问题选项排序',
    PRIMARY KEY (`id`),
    KEY `question_id` (`question_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
   COMMENT ='调查问卷问题选项存储表';

--
-- Table structure for table `questionnaire_type`
--

DROP TABLE IF EXISTS `questionnaire_type`;
CREATE TABLE `questionnaire_type`
(
    `id`        bigint(20) unsigned NOT NULL AUTO_INCREMENT,
    `type_name` varchar(100)        NOT NULL COMMENT '调查问卷模板类型名称',
    `type_desc` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='调查问卷模板类型表';



--
-- Table structure for table `template`
--

DROP TABLE IF EXISTS `template`;
CREATE TABLE `template`
(
    `id`             bigint(20)   NOT NULL AUTO_INCREMENT,
    `name`           varchar(100) NOT NULL COMMENT '调查问卷模板名称',
    `create_date`    datetime         DEFAULT NULL COMMENT '模板创建日期',
    `publish_date`   datetime         DEFAULT NULL COMMENT '发布时间',
    `author_id`      bigint(20)       DEFAULT '0' COMMENT '模板制作人id,默认为0代表是系统发布的模板',
    `type_id`        bigint(20)       DEFAULT NULL COMMENT '模板类型id，参考questionnaire_type表的id',
    `question_count` int(10) unsigned DEFAULT '0' COMMENT '该模板问题总数',
    `status`         tinyint          DEFAULT '0' COMMENT '模板状态，0未发布1已发布2下线',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='调查问卷模板表';



--
-- Table structure for table `template_question`
--

DROP TABLE IF EXISTS `template_question`;
CREATE TABLE `template_question`
(
    `id`                bigint(20) unsigned NOT NULL AUTO_INCREMENT,
    `template_id`       bigint(20) unsigned DEFAULT NULL COMMENT '模板id',
    `question_type`     tinyint(4)          DEFAULT NULL COMMENT '问题类型',
    `question_title`    varchar(255)        DEFAULT NULL COMMENT '问题题目',
    `question_order`    tinyint(4)          DEFAULT NULL COMMENT '问题在模板中的顺序',
    `question_num`      tinyint(4)          DEFAULT NULL COMMENT '问题在模板中的题号',
    `input_placeholder` varchar(255)        DEFAULT NULL COMMENT '对于填空题input的placeholder',
    PRIMARY KEY (`id`),
    KEY `template_id` (`template_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='调查问卷模板问题表，用于存储模板中的问题';



--
-- Table structure for table `template_question_option`
--

DROP TABLE IF EXISTS `template_question_option`;
CREATE TABLE `template_question_option`
(
    `id`           bigint(20) unsigned NOT NULL AUTO_INCREMENT,
    `question_id`  bigint(20) unsigned NOT NULL COMMENT '问题id',
    `option_text`  varchar(50)         DEFAULT NULL COMMENT '问题选项文本',
    `option_order` tinyint(3) unsigned DEFAULT NULL COMMENT '问题选项排序',
    PRIMARY KEY (`id`),
    KEY `question_id` (`question_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='调查问卷模板问题选项存储表';


--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`
(
    `id`            bigint(20)   NOT NULL AUTO_INCREMENT,
    `username`      varchar(100) NOT NULL COMMENT '用户名',
    `password`      varchar(50)  DEFAULT NULL COMMENT '密码',
    `password_salt` varchar(50)  DEFAULT NULL COMMENT '密码盐',
    `real_name`     varchar(100) DEFAULT NULL COMMENT '真实姓名',
    `phone`         varchar(20)  DEFAULT NULL COMMENT '电话号码',
    `email`         varchar(50)  DEFAULT NULL COMMENT '电子邮件',
    `age`           tinyint(4)   DEFAULT NULL COMMENT '年龄',
    `sex`           tinyint(4)   DEFAULT '0' COMMENT '性别，0男1女',
    `is_vip`        tinyint(4)   DEFAULT '0' COMMENT '是否是vip用户，0否1是',
    `create_date`   datetime     DEFAULT NULL COMMENT '用户注册时间',
    `status`        tinyint      DEFAULT '1' COMMENT  '用户状态，0已被注销，1正常，2已被锁定',
    PRIMARY KEY (`id`),
    UNIQUE KEY `user_UN` (`username`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户表';
