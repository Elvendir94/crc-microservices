FROM redhat/ubi9

RUN yum -y upgrade \
    && yum list installed \
    && yum -y install fontconfig rsync tar procps jq \
    && yum -y install java-21-openjdk \
    && yum -y remove python-urllib3 \
    && java --version \

RUN useradd -ms /bin/bash buildUser
USER buildUser

ARG JAR_FILE=build/libs/service-c.jar
COPY ${JAR_FILE} /service-c.jar

ENTRYPOINT ["java","-Duser.timezone=UTC", "-XX:MaxRAMPercentage=80.0","-Xms128m","-XX:+CompactStrings","-XX:TieredStopAtLevel=1","-XX:+UseSerialGC","-Xlog:gc=info","-jar", "./service-c.jar"]

EXPOSE 8084

# docker run -e spring.profiles.active=local,kafka_binding --network host --user 1001 service-b