# ORA
OCT Reflectivity Analytics

This project is being developed by Brandon Wilk Consulting

## PACKR installation
This requires `Git` and `maven` to be installed on your machine. PACKR is a commandline
tool that makes packaging of Java applications contained in Uber JARs 
easy to manage for multiple platforms. It has it's own built in launcher 
to make application launching easy.
```bash
mkdir -p ~/workspace/packr
cd ~/workspace/packr
git clone https://github.com/libgdx/packr.git
mvn clean install
```

## Packaging for Mac OS X
Set the following environment variable:
```bash
export PACKR=~/workspace/packr/target/packr-2.1-SNAPSHOT-jar-with-dependencies.jar
export ORA_TARGET_JRE=/Users/brandon/workspace/java_jre/mac_java8/jdk8u222-b10/
```
then run the mac build script specifying the version of the JAR to be packaged from the `target` directory
after a `mvn clean install` has been ran
```
./mac_build.sh 1.2.0
```
where `1.2.0` is the version of the JAR in the `target` directory