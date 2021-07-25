# docker compose 部署文档
> 注意：以下操作默认都是在项目的根目录，如果你没有，请先进入：`cd questionnaire` 

## 一键启动项目
> 首先确保你本地安装了docker engine和docker compose，并且**系统能够联网**。

首先使用Maven构建项目
```shell
./mvnw clean package -Dmaven.test.skip=true
```

使用docker compose启动项目和依赖服务
```shell
docker-compose up --build
```

如果你本地没有mysql:8.0.22,elasticsearch:7.6.2的镜像，docker会先pull镜像，可能会有些慢！
另外在elasticsearch容器启动时会访问github下载把并安装ik分词插件。
mysql容器启动时，会执行一些初始化sql用于创建表结构和导入初始化数据。<br>

如果没有任何问题，启动完成之后执行命令`docker ps`你将看到三个运行的容器，分别为
- questionnaire_web：我们的项目questionnaire
- questionnaire_db：mysql数据库
- questionnaire_es：elasticsearch



## 同步数据到elasticsearch
等 elasticsearch完全启动后，就可以执行下面的操作了。<br>
在启动elasticsearch时，已经暴露了端口9200,所以你可以直接在本地执行下面的命令操作elasticsearch
### 创建`template`索引
```shell
curl -XPUT localhost:9200/template?pretty
```

### 设置字段_mapping
```shell
curl -XPOST localhost:9200/template/_mapping?pretty -H 'content-type:application/json' -d'
{
    "properties":{
       "id":{"type":"long"},
       "name":{
         "type":"text",
         "analyzer":"ik_max_word",
         "search_analyzer":"ik_smart"
       },
       "createDate":{
          "type":"date",
          "format":"yyyy/MM/dd HH:mm:ss"
       },
       "publishDate":{
          "type":"date",
          "format":"yyyy/MM/dd HH:mm:ss"
       },
       "authorId":{"type":"long"},
       "authorName":{"type":"text"},
       "typeId":{"type":"integer"},
       "questionCount":{"type":"integer"},
       "status":{"type":"integer"},
       "questions":{
          "type":"nested",
          "properties":{
             "id":{"type":"long"},
             "templateId":{"type":"long"},
             "questionTitle":{
                "type":"text",
                "analyzer":"ik_max_word",
                "search_analyzer":"ik_smart"
             },
             "questionOrder":{"type":"integer"},
             "questionNum":{"type":"integer"},
             "inputPlaceholder":{"type":"text"}
          }
       }
    }
}'
```

### 查看设置的_mapping
```shell
curl -XGET localhost:9200/template/_mapping?pretty
{
  "template" : {
    "mappings" : {
      "properties" : {
        "authorId" : {
          "type" : "long"
        },
        "authorName" : {
          "type" : "text"
        },
        "createDate" : {
          "type" : "date",
          "format" : "yyyy/MM/dd HH:mm:ss"
        },
        "id" : {
          "type" : "long"
        },
        "name" : {
          "type" : "text",
          "analyzer" : "ik_max_word",
          "search_analyzer" : "ik_smart"
        },
        "publishDate" : {
          "type" : "date",
          "format" : "yyyy/MM/dd HH:mm:ss"
        },
        "questionCount" : {
          "type" : "integer"
        },
        "status" : {
          "type" : "integer"
        },
        "questions" : {
          "type" : "nested",
          "properties" : {
            "id" : {
              "type" : "long"
            },
            "inputPlaceholder" : {
              "type" : "text"
            },
            "questionNum" : {
              "type" : "integer"
            },
            "questionOrder" : {
              "type" : "integer"
            },
            "questionTitle" : {
              "type" : "text",
              "analyzer" : "ik_max_word",
              "search_analyzer" : "ik_smart"
            },
            "templateId" : {
              "type" : "long"
            }
          }
        },
        "typeId" : {
          "type" : "integer"
        }
      }
    }
  }
}
```

### 索引数据到es，data.json位于docker/inites/目录
```shell
curl -XPOST localhost:9200/template/_bulk?pretty \
-H 'content-type:application/json' \ 
--data-binary "@./docker/inites/data.json"
```

### 测试查询
```shell
 curl -XPOST localhost:9200/template/_search?pretty -H 'content-type:application/json' -d'
{
    "query" : { "match" : { "name" : "大学生" }},
    "highlight" : {
        "pre_tags" : ["<tag1>", "<tag2>"],
        "post_tags" : ["</tag1>", "</tag2>"],
        "fields" : {
            "name" : {}
        }
    }
}
'
```

## FAQ
### 1. 为什么启动这么慢？<br>
 - 如果你本地没有mysql:8.0.22,elasticsearch:7.6.2的镜像，docker会先pull镜像，可能会有些慢！建议使用国内`docker hub`。
 - 另外在elasticsearch容器启动时会访问github下载把并安装ik分词插件。
 - mysql容器启动时，会执行一些初始化sql用于创建表结构和导入初始化数据。
    
### 2. 为什么会启动失败？<br>
 - 确保你的系统能够联网，因为pull镜像，下载ik插件都需要联网。
 - 执行docker-compose 所处的路径是否正确，请在项目的根目录执行。
     
### 3. 为什么Elasticsearch启动失败？
 - 在容器启动时会从github下载并安装ik分词插件，有可能是下载ik插件失败，导致es启动失败，确保你能够访问github。
   如果你不能访问，请先下载到本地，启动es后再手动安装。