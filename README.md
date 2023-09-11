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

## Installing on LINUX (Ubuntu)

The program is written in Java (Java 8) and needs to be build using maven. 

Building Packr as described above did not work on my system, because the Gradle script seems use deprecated features and does not work with the higher versions of Gradle. 

Therefore, download the latest PACKR release here: 

https://github.com/libgdx/packr/releases

and save it in the workspace directory. In my case, the file was called 
"packr-all-4.0.0.jar". 

```bash
apt update
apt install openjdk-8-jdk
apt install git

mkdir ~/workspace
cd ~/workspace
git clone https://github.com/huchzi/ORA.git

```

Next, set the following environment variable:

  * PACKR: Path of PACKR
  * ORA_TARGET_JRE: Path of the JAVA Runtime Environment

```bash
# use name of the release you downloaded:
export PACKR=~/workspace/packr-all-4.0.0.jar
# identify your jvm 
find / -iname "*java-8-openjdk*"
export ORA_TARGET_JRE=/usr/lib/jvm/java-8-openjdk-amd64
```

Lastly, run PACKR via the provided shell script. You need to specify the build version of ORA:

```
./linux_build.sh 1.2.0
```