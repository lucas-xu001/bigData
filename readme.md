## Docker images

可以用dockerfile编译(已经在项目中提供)，但是需要额外配置网络

已经打包了docker镜像

dockerhub地址：https://hub.docker.com/repository/docker/xufengyuan/bigdata/general

共三个镜像：hadoop102，hadoop103，hadoop104

直接使用docker push命令部署到本地

**项目最终展示接口：hadoop102:37799/webroot/decision/link/6W4N**

**最终演示结果参考视频：推荐部分.mkv，异常检测部分.mkv**

**相关数据存储地址：/opt/software/ad_mock/log**

**hadoop访问接口：hadoop102:9870**

相关shell命令已经打包在/bin目录中，**可以直接使用**

开启hadoop：hdp.sh start

关闭hadoop：hdp.sh stop

加载点击数据：ad_mock.sh

加载广告数据：add_mysql_to_hdfs.sh 

开启kafka：kf.sh start

开启zookeeper：zk.sh start

开启flume生产者：ad_f1.sh

开启flume消费者：ad_f2.sh



## 数据库概述

广告管理平台中核心的几张表结构如下：

（1）ads（广告表）

| id  (广告编号) | product_id  (商品id) | material_id  (素材id) | group_id  (广告组id) | ad_name  (广告名称) | material_url  (素材地址) |
| ------------------------------ | ------------------------------------ | ------------------------------------- | ------------------------------------ | ----------------------------------- | ---------------------------------------- |
| 0                          | 329375909941                         | 712337489641                          | 8                                    | XXX                                 | XXX                                      |
| 1                          | 130171159227                         | 519572879265                          | 7                                    | XXX                                 | XXX                                      |
| 2                          | 251005109937                         | 294923573889                          | 10                                   | XXX                                 | XXX                                      |

（2）platform_info（推广平台表）

| id  (平台id) | platform  (平台标识) | platform_alias_zh  (平台中文名称) |
| ---------------------------- | ------------------------------------ | ------------------------------------------------- |
| 1                        | tencent                              | 腾讯广告                                          |
| 2                        | baidu                                | 百度推广                                          |
| 3                        | juliang                              | 巨量                                              |

（3）product（商品表）

| id  (商品id) | name  (商品名称)                             | price  (商品价格) |
| ---------------------------- | ------------------------------------------------------------ | --------------------------------- |
| 659417                   | 【精选】葡萄柚台湾葡萄蜜柚皮薄平和甜蜜柚当季新鲜水果批发包邮 | 9.9                               |
| 1214894                  | 【精品】正宗赣南脐橙新鲜橙子江西甜橙孕妇水果冰糖果冻香橙包邮 | 14.8                              |
| 5307635                  | 特辣朝天椒小米椒微辣中辣干辣椒超辣特香散装干货500克          | 17.5                              |

（4）ads_platform（广告投放表）

| id  (编号) | ad_id  (广告id) | platform_id  (平台id) | create_time  (开始投放时间) | cancel_time  (取消投放时间) |
| -------------------------- | ------------------------------- | ------------------------------------- | ------------------------------------------- | ------------------------------------------- |
| 1                      | 0                               | 3                                     | XXX                                         | XXX                                         |
| 2                      | 0                               | 2                                     | XXX                                         | XXX                                         |
| 3                      | 1                               | 3                                     | XXX                                         | XXX                                         |

（5）server_host（日志服务器列表）

| id  (编号) | ipv4  (服务器ip) |
| -------------------------- | -------------------------------- |
| 1                      | 203.195.136.146                  |
| 2                      | 103.250.32.51                    |
| 3                      | 203.90.0.54                      |

## 数据曝光数据概述

| 字段           | 说明                                                     | 示例数据                                                 |
| ------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| \u0001         | 分隔符，Unicode中的一个不可见字符，用作分隔符，可避免与文本中的字符冲突，同时也是Hive中列分隔符的默认值 |                                                              |
| time_local     | 日志服务器收到监测数据上报请求的时间                         | 2023-01-07 00:00:10                                          |
| request_method | HTTP请求方法，常用的方法有GET、POST，GET请求通常用于向Web服务器请求数据，POST通常用于向Web服务器提交数据处理请求 | GET                                                          |
| request_uri    | 请求路径，这部分内容包括了媒体上报的监测数据的核心内容。     | "/ad/baidu/impression?id=89&t=1676394705587&ip=122.189.79.23&ua=Mozilla/5.0%20(Windows%20NT%2010.0)%20AppleWebKit/537.36%20(KHTML,%20like%20Gecko)%20Chrome/40.0.2214.93%20Safari/537.36&device_id= d4b8f3898515056278ccf78a7a2cca2d&os_type=Android" |
| status         | 日志服务器的响应状态码，200表示响应成功                      | 200                                                          |
| server_addr    | 日志服务器自身的IP地址                                       | 203.195.136.146                                              |

## hadoop 配置概述

|          | **hadoop102**      | **hadoop103**                | **hadoop104**               |
| -------- | ------------------ | ---------------------------- | --------------------------- |
| **HDFS** | NameNode  DataNode | DataNode                     | SecondaryNameNode  DataNode |
| **YARN** | NodeManager        | ResourceManager  NodeManager | NodeManager                 |

## zookeeper和kafka配置概述

| **hadoop102** | **hadoop103** | **hadoop104** |
| ------------- | ------------- | ------------- |
| **zk**        | zk            | zk            |
| **kafka**     | kafka         | kafka         |

## 数据采集通道示意

![采集]([\main\pic\采集.jpg](https://github.com/lucas-xu001/bigData/tree/main/pic)https://github.com/lucas-xu001/bigData/tree/main/pic)

## 
