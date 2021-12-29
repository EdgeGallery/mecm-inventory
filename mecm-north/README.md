[ch](README.md) | [en](README_en.md)

# mecm北向模块

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
![Jenkins](https://img.shields.io/jenkins/build?jobUrl=http%3A%2F%2Fjenkins.edgegallery.org%2Fview%2FMEC-PLATFORM-BUILD%2Fjob%2Fappstore-backend-docker-image-build-update-daily-master%2F)

mecm-north是mecm与外部系统对接的模块，将mecm内部inventory、apm、appo等模块接口抽离到north供外部模块调用


## 特性介绍

EdgeGallery的mecm平台提供给其他外部系统的调用模块

## 编译运行

mecm-north对外提供restful接口，基于开源的ServiceComb微服务框架进行开发，并且集成了Spring Boot框架。本地运行需要依赖ServiceCenter进行服务注册发现，通过postman进行接口测试。

- ### 环境准备（本地运行）
  
    | Name | Version | Link |
    | ---- | ------- | ---- |
    | JDK1.8 |1.8xxx or above | [download](https://www.oracle.com/java/technologies/javase-jdk8-downloads.html)
    | MavApache Maven |3.6.3 | [download](https://maven.apache.org/download.cgi)
    | IntelliJ IDEA |Community |[download](https://www.jetbrains.com/idea/download/)
    | Servicecomb Service-Center    | 1.3.0 | [download](https://servicecomb.apache.org/cn/release/service-center-downloads/)
    | Postgres  | 9.6.17 or above |   [download](https://www.enterprisedb.com/downloads/postgres-postgresql-downloads) |

- ### 修改配置文件/src/main/resources/application.properties

    - 1 修改postgres配置，本地安装默认IP是127.0.0.1，默认端口是5432，默认用户名和密码postgres/root，如下：
    ```
    postgres.ip=127.0.0.1
    postgres.database=postgres
    postgres.port=5432
    postgres.username=postgres
    postgres.password=root
    ```
    - 2 配置Service Center，本地安装IP是127.0.0.1，默认端口30100，servicecomb.name是注册到servicecenter上的服务名，可修改，配置如下：
    ```
    #### Service Center config ####
    # ip or service name in k8s
    servicecenter.ip=127.0.0.1
    servicecenter.port=30100
    servicecomb.name=mecm-north
    ```

- ### 编译打包
    从代码仓库拉取代码，默认master分支
    
    ```
    git clone https://gitee.com/edgegallery/mecm-inventory.git
    ```

    编译构建，需要依赖JDK1.8，首次编译会比较耗时，因为maven需要下载所有的依赖库。

    ```
    mvn clean install
    ```