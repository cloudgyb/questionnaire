# 项目运行与部署

## 1.准备数据库
1. 确保安装并启动了MySQL或者MariaDB数据库服务.
2. 创建数据库questionnaire
   ```mysql
    CREATE DATABASE IF NOT EXISTS `questionnaire` DEFAULT CHARACTER SET utf8mb4;
   ```
3. 导入questionnaire-schema.sql（表结构定义）。
   + 使用mysqlimport命令
   ```bash
   mysqlimport.exe -uroot -p questionnaire your_path/questionnaire-schema.sql
   ```
   + 或者进入mysql命令行
   ```mysql
    use questionnaire;
    source your_absoult_path/questionnaire-schema.sql
   ```
4. 导入questionnaire-data.sql（测试数据）
   + 使用mysqlimport命令
   ```bash
   mysqlimport.exe -uroot -p questionnaire your_path/questionnaire-data.sql
   ```
   + 或者进入mysql命令行
   ```mysql
    use questionnaire;
    source your_absoult_path/questionnaire-data.sql
   ```
## 2.准备ElasticSearch
 > ElasticSearch版本为7.6.2 
1. 安装ElasticSearch <br>
   + 本地安装 <br>
   选择合适的包，windows选zip或者msi，linux选tar.gz。<br>
   [ElasticSearch下载](https://repo.huaweicloud.com/elasticsearch/7.6.2/)
   + Docker安装<br>
     ```bash
     docker pull elasticsearch:7.6.2
     ```
2. 安装ElasticSearch分词插件IK <br>
   进入elasticsearch的bin目录，执行如下命令直接安装：
   ```bash
   ./elasticsearch-plugin install https://github.com/medcl/elasticsearch-analysis-ik/releases/download/v7.6.2/elasticsearch-analysis-ik-7.6.2.zip
   ```
   或者先下载到本地再安装：<br>
   [ik下载地址](https://github.com/medcl/elasticsearch-analysis-ik/releases)
   选择v7.6.2。然后执行命令：
   ```bash
   ./elasticsearch-plugin install file:///{your_path}/elasticsearch-analysis-ik-7.6.2.zip
   ```
3. 启动ElasticSearch <br>
   进入bin/目录，执行：
   ```bash
   ./elasticsearch
   ```
   验证是否启动成功：
   ```bash
   curl "http://{your_ip}:9200/_cat"
   ```

## 3. 配置项目
1. 首先导入项目到IDE，推荐IDEA
2. 修改配置 <br>
   根据你的环境和需要在application-<profile>.properties修改数据库连接配置、Elasticsearch连接配置、邮件配置等配置项。
3. 导入数据到ElasticSearch <br>
   直接执行src/test/java/com/gyb/questionnaire/ESTemplateSearchTest.java的indexTemplateToES测试方法，将数据库中的数据导入到Elasticsearch。
   
## 4. 启动项目
 进入项目根目录执行：
 ```bash
  ./mvnw spring-boot:run
 ```
 或者在IDEA中，右键执行QuestionnaireApplication <br>
 在浏览器中输入http://localhost:8080,进入主页面。