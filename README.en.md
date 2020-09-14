# mecm-inventory

#### Description
MECM-Inventory modules provides common inventory of all system and host level resources of MEC system.

#### Compile and build
The Inventory project is containerized based on docker, and it is divided into two steps during compilation and construction.

#### Compile
Inventory is a Java program written based on jdk1.8 and maven. To compile, you only need to execute mvn install to compile and generate jar package

#### Build image
The Inventory project provides a dockerfile file for mirroring. You can use the following commands when making a mirror

docker build -t mecm-inventory:latest -f docker/Dockerfile. 