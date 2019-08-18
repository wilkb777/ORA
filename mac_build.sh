#!/bin/bash
mvn clean install
java -jar ${PACKR} \
     --platform mac \
     --jdk ${ORA_TARGET_JRE} \
     --executable ORA-$1 \
     --classpath target/ORA-$1.jar \
     --mainclass com.bwc.ora.Ora \
     --vmargs Xmx4G \
     --resources src/main/resources \
     --minimizejre soft \
     --icon logo.png.icns \
     --bundle com.bwc.ora \
     --output ORA.app
