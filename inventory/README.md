# mecm-inventory

#### 描述
MECM-Inventory 模块提供 MEC 系统的所有系统和主机级资源的公共清单。

#### 编译和构建
Inventory项目基于docker容器化，在编译和构建过程中分为两个步骤。

#### 编译
Inventory是一个基于jdk1.8和maven编写的Java程序。 编译只需执行 mvn install 即可编译生成jar包

#### 构建镜像
Inventory 项目提供了一个用于镜像的 dockerfile 文件。 制作镜像时可以使用以下命令

```shell
docker build -t edgegallery/mecm-inventory:latest -f docker/Dockerfile .
```