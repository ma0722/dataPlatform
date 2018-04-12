# 基于组件的文本数据处理工具

项目使用Spring Boot，在前端页面基于组件定义工作流，将工作流发送后台。后台将工作流将maven打包，通过spark-submit发送到Spark集群。


## 本地环境
* 安装maven [Apache Maven](http://maven.apache.org/) ： 用于项目自动打包，已将项目的依赖自动添加，无需集群额外配置
* 安装Spark [Spark download](http://spark.apache.org/downloads.html) ： 使用Spark的spark-submit向Spark集群提交jar包


## 项目配置信息

* application.properties： SpringBoot配置文件
* cluster.properties：Hadoop与Spark的IP、端口配置文件
* command.properties：本地spark-submit路径
* log4j.properties：日志配置文件


## 项目部署
* 可参见Spring Boot部署




