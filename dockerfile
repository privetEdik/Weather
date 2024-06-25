FROM maven:3 AS build
WORKDIR /app
COPY . .
RUN mvn package
FROM tomcat:10-jdk17
COPY --from=build /app/target/*.war $CATALINA_HOME/webapps/app.war
EXPOSE 8080
CMD ["catalina.sh", "run"]