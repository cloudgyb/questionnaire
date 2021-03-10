SET NAMES 'utf8mb4';
--
-- Dumping data for table `questionnaire_type`,调查问卷模板类型测试数据
--

INSERT INTO `questionnaire_type` VALUES (1,'大学生消费情况调查','在经济相对自由的学生时代，没有方向的随意消费似乎是当前大学生普遍的消费状况。只有充分进行调查了解，才能帮助建立健康的消费价值观'),(2,'大学生就业调查','如何了解当代大学生的就业选择、心理、形势等问题？在越来越丰富的工作形态下，个性独特的大学生会如何选择自己的工作？'),(3,'大学生调查问卷','为了更好的了解当代大学生的生活、行为及就业情况和发展趋势，小星整合了一系列大学生调查问卷'),(4,'市场调查','通过市场调查分析市场情况，了解市场现状和发展趋势，以避免企业在制定营销策略时发生错误，从而在不断变化的市场中谋取企业最大效益'),(5,'客户满意度调查','通过顾客满意度调查，可以挖掘出导致顾客不满意的关键因素，从而有针对性的改善企业服务质量、提升顾客重复购买率、企业竞争能力及盈利'),(6,'员工满意度调查','只有充分的了解了员工对薪酬、工作、制度、个人发展等方面的需求和期望，才能通过改善这些需求来促进员工的工作热情，提高企业绩效'),(7,'培训需求调查表','通过培训需求调查，可根据员工自身发展的实际需求分类培训，因人施教，使培训更有针对性和实效性'),(8,'教育调查','通过教育问卷调查，了解当前家长、学生、老师以及专业人士对家庭教育和学校教育的看法及建议，为更好的开展和组织各种教育做充分的准备'),(9,'家庭情况调查','家庭是一个小的社会组织群体，包含着各类家庭活动和健康教育，各大媒体和各类社会群体总是乐此不疲的挖掘并研究着各种家庭问题');


--
-- Dumping data for table `template`,调查问卷模板测试数据
--

INSERT INTO `template` VALUES (1,'大学生消费情况调查问卷','2020-11-08 12:13:26','2020-11-08 12:13:45',0,1,12),(2,'大学生“双十一”消费情况调查问卷','2020-11-08 12:13:30','2020-11-08 12:13:47',0,1,20),(3,'大学生双十一期间网上消费情况调查问卷','2020-11-08 12:13:33','2020-11-08 12:13:49',0,1,10),(4,'大学生校内消费情况调查','2020-11-08 12:13:35','2020-11-08 12:13:51',0,1,15),(5,'疫情后大学生就业情况与就业指导调查','2020-11-08 12:13:37','2020-11-08 12:13:53',0,2,12),(6,'大学生就业意向调查','2020-11-08 12:13:40','2020-11-08 12:13:56',0,2,12),(7,'大学生赴日就业调查','2020-11-08 12:13:42','2020-11-08 12:13:59',0,2,20),(8,'大学生“12.12”消费情况调查','2020-12-03 17:08:29','2020-12-03 17:08:35',0,1,0),(9,'关于大学生情侣之间的消费情况调查','2020-12-03 17:09:56','2020-12-03 17:10:00',0,1,0),(10,'关于大学生情侣之间的消费情况调查2','2020-12-03 17:10:20','2020-12-03 17:10:22',0,1,0),(11,'关于大学生情侣之间的消费情况调查3','2020-12-03 17:12:56','2020-12-03 17:12:59',0,1,0);


--
-- Dumping data for table `template_question`
--

INSERT INTO `template_question` VALUES (1,1,3,'您的性别?',1,1,NULL),(2,1,3,'你的年级？',2,2,NULL),(3,1,3,'在校期间的平均月消费?',3,3,NULL),(4,1,3,'生活费来源?',4,4,NULL),(5,1,3,'每月用于校内食堂就餐的费用约为',5,5,NULL),(6,1,3,' 购物的方式通常为?',6,6,NULL),(7,1,3,'每月购置生活用品及衣物的费用',7,7,NULL),(8,1,3,'每学期在化妆品或护肤品方面的花费',8,8,NULL),(9,1,3,'今年双11您打算花费多少钱在网购上?',9,9,NULL),(10,1,4,'您的月消费多用在哪些方面?',10,10,NULL),(11,1,4,'你会使用哪些预支方式付款',11,11,NULL),(12,1,4,'每月的生活费有余时，你会把它用于?',12,12,NULL),(13,1,2,'你对大学生超前消费的看法？',13,13,'请填写你对大学生超前消费的看法');



--
-- Dumping data for table `template_question_option`
--

INSERT INTO `template_question_option` VALUES (1,1,'男',2),(2,1,'女',1),(3,2,'大一',3),(4,2,'大二',4),(5,2,'大三',5),(6,2,'大四',6),(7,3,'600-1000',1),(8,3,'1000-1500',2),(9,3,'1500-2000',3),(10,3,'2000以上',4),(11,4,'全部来自家庭',1),(12,4,'部分来自家庭，部分靠自己赚取',2),(13,4,'全部靠自己赚取',3),(14,5,'300以下',1),(15,5,'300-600',2),(16,5,'600-1000',3),(17,5,'1000以上',4),(18,6,'实体店',1),(19,6,'网购',2),(20,7,'100以下',1),(21,7,'100-500',2),(22,7,'500-1000',3),(23,7,'1000以上',4),(24,8,'不网购',1),(25,8,'1-300',2),(26,8,'300-800',3),(27,8,'800-1500',4),(28,8,'1500以上',5),(29,9,'不花钱',1),(30,9,'10-100元',2),(31,9,'100-500元',3),(32,9,'500-1000元',4),(33,9,'1000以上',5),(34,10,'学习',1),(35,10,'游戏',2),(36,10,'买东西',3),(37,11,'从不',1),(38,11,'花呗',2),(39,11,'京东白条',3),(40,11,'信用卡',4),(41,11,'其他',5),(42,12,'吃喝',1),(43,12,'旅游',2),(44,12,'添置衣物',3),(45,12,'买书',4),(46,12,'存起来',5),(47,12,'其他',6);
