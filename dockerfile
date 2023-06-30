# SMTP-SERVER DOCKERFILE

#Build stage
FROM maven:3.9.3-eclipse-temurin-11 as build-stage
COPY src /usr/app/src
COPY pom.xml /usr/app/pom.xml
RUN mvn -f /usr/app/pom.xml clean package


#Deploy stage
FROM eclipse-temurin:11 as deploy-stage
COPY --from=build-stage /usr/app/target/smtp-server.jar /usr/app/smtp-server.jar
  # jboss will send mail to 26 instead of 25
RUN mkdir /usr/app/incoming
VOLUME ["/usr/app/incoming"]
EXPOSE 26
ENTRYPOINT ["java", "-jar", "/usr/app/smtp-server.jar", "--smtpserver.port=26", "--smtpserver.directory=/usr/app/incoming"]
