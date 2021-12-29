[ch](README.md) | [en](README_en.md)

# mecm-north

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
![Jenkins](https://img.shields.io/jenkins/build?jobUrl=http%3A%2F%2Fjenkins.edgegallery.org%2Fview%2FMEC-PLATFORM-BUILD%2Fjob%2Fappstore-backend-docker-image-build-update-daily-master%2F)

Mecm-north is a module that connects mecm with external systems. The interfaces of mecm's internal inventory, APM, appo and other modules are pulled out to North for external modules to call


## Features Introduction

Mecm-north provides modules for other external systems to request internal module of mecm

## Compile and run

 Mecm-North provides a restful interface, develops based on the open source servicecomb microservice framework, and integrates the spring boot framework. Local operation needs to rely on servicecenter for service registration and discovery, and interface testing through postman.

- ### Environmental preparation（Run locally）
  
    | Name | Version | Link |
    | ---- | ------- | ---- |
    | JDK1.8 |1.8xxx or above | [download](https://www.oracle.com/java/technologies/javase-jdk8-downloads.html)
    | MavApache Maven |3.6.3 | [download](https://maven.apache.org/download.cgi)
    | IntelliJ IDEA |Community |[download](https://www.jetbrains.com/idea/download/)
    | Servicecomb Service-Center    | 1.3.0 | [download](https://servicecomb.apache.org/cn/release/service-center-downloads/)
    | Postgres  | 9.6.17 or above |   [download](https://www.enterprisedb.com/downloads/postgres-postgresql-downloads) |

- ### Modify the configuration file/src/main/resources/application.properties

    - 1 modify postgres Configuration，Local installation default IP: 127.0.0.1，default port: 5432，Default username and 
      password: postgres/root，as follows：
    ```
    postgres.ip=127.0.0.1
    postgres.database=postgres
    postgres.port=5432
    postgres.username=postgres
    postgres.password=root
    ```
    - 2 ConfigurationService Center，Local installationIPYes127.0.0.1，Default port: 30100，servicecomb.
      name is register to servicecenter Service name，as follows：
    ```
    #### Service Center config ####
    # ip or service name in k8s
    servicecenter.ip=127.0.0.1
    servicecenter.port=30100
    servicecomb.name=mecm-north
    ```

- ### Compile and package
    Pull code from the code repository，default master Branch
    
    ```
    git clone https://gitee.com/edgegallery/mecm-inventory.git
    ```

    Compile and build，Need to rely onJDK1.8，Compiling for the first time will be time-consuming，becausemavenNeed to download all dependent libraries。

    ```
    mvn clean install
    ```