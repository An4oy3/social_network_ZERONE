FROM openjdk:latest
MAINTAINER Team JavaPRO <x.noreply@yzq.org>
ENTRYPOINT ["java", "-jar", "/javapro/social-network-0.1.jar"]
ARG JAR_FILE
ADD target/${JAR_FILE} /org/javapro/social-network-0.1.jar

