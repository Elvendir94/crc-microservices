FROM redhat/ubi9

RUN yum -y upgrade \
    && yum list installed \
    && yum -y install fontconfig rsync tar procps jq \
    && yum -y install java-21-openjdk \
    && yum -y remove python-urllib3 \
    && java --version \

ARG JAR_FILE=build/libs/service-b.jar
COPY ${JAR_FILE} service-b.jar

ENTRYPOINT ["java","-Duser.timezone=UTC", "-XX:MaxRAMPercentage=80.0","-Xms128m","-XX:+CompactStrings","-XX:TieredStopAtLevel=1","-XX:+UseSerialGC","-Xlog:gc=info","-jar", "./service-b.jar"]

EXPOSE 8080